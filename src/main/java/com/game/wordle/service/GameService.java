package com.game.wordle.service;

import com.game.wordle.dto.GuessResponse;
import com.game.wordle.dto.GuessSummary;
import com.game.wordle.entity.GameSettings;
import com.game.wordle.entity.User;
import com.game.wordle.entity.UserAttempt;
import com.game.wordle.repository.GameSettingsRepository;
import com.game.wordle.repository.UserAttemptRepository;
import com.game.wordle.repository.UserRepository;
import com.game.wordle.repository.WordRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.stream.Collectors; 

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class GameService {

    @Autowired
    private WordRepository wordRepository;

    @Autowired
    private UserAttemptRepository userAttemptRepository;

    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private GameSettingsRepository gameSettingsRepository; 

    public GuessResponse processGuess(Long userId, String guess) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found with ID: " + userId));

        String dailyWord = getDailyWord(); // Retrieve or generate the daily word

        // Check if the user has already won today
        if (hasUserWonToday(user, dailyWord)) {
            return createResponse("You already won today!", null, user);
        }

        // Check the user's attempt count for today
        long attemptsToday = getAttemptsCountForToday(user);
        if (attemptsToday >= 6) {
            return createResponse("You lost! The word of the day is " + dailyWord, null, user);
        }

        String result = evaluateGuess(user, guess, dailyWord);

        // Check if the user has guessed correctly
        if (result.equals("GGGGG")) {
            return createResponse("You won!", result, user);
        }

        // If it’s the 6th attempt and the guess is wrong
        if (attemptsToday + 1 == 6) {
            return createResponse("You lost! The word of the day is " + dailyWord, result, user);
        }

        // Normal response for incorrect guesses before the 6th attempt
        return createResponse("Keep going!", result, user);
    }

    private String getDailyWord() {
        LocalDate today = LocalDate.now();
        Optional<GameSettings> gameSettingsOpt = gameSettingsRepository.findByDate(today);

        if (gameSettingsOpt.isPresent()) {
            return gameSettingsOpt.get().getWordOfTheDay();
        }

        // No word for the day found, pick a new one
        String newWord = pickDailyWord();
        
        // Save the new word for the day
        GameSettings gameSettings = new GameSettings(newWord, today);
        gameSettingsRepository.save(gameSettings);
        
        return newWord;
    }

    // Helper method to pick a random word for the day
    private String pickDailyWord() {
        long count = wordRepository.count();
        int index = new java.util.Random().nextInt((int) count);
        return wordRepository.findAll().get(index).getWord();
    }

    // Helper method to evaluate the guess and store it
    private String evaluateGuess(User user, String guess, String dailyWord) {
        String result = evaluateGuessResult(guess.toUpperCase(), dailyWord);

        // Store the guess attempt in the database
        long attemptsToday = getAttemptsCountForToday(user);
        UserAttempt userAttempt = new UserAttempt();
        userAttempt.setUser(user);
        userAttempt.setGuessedWord(guess.toUpperCase());
        userAttempt.setResult(result);
        userAttempt.setAttemptDate(LocalDate.now());
        userAttempt.setAttemptNumber(attemptsToday + 1);
        userAttemptRepository.save(userAttempt);

        return result;
    }

    // Helper method to count attempts for today
    private long getAttemptsCountForToday(User user) {
        return userAttemptRepository.countByUserIdAndAttemptDate(user.getId(), LocalDate.now());
    }

    // Helper method to check if the user has already won today
    public boolean hasUserWonToday(User user, String dailyWord) {
        return userAttemptRepository.existsByUserIdAndResultAndAttemptDate(user.getId(), "GGGGG", LocalDate.now());
    }

    // Helper method to evaluate the guess result
    private String evaluateGuessResult(String guess, String dailyWord) {
        StringBuilder result = new StringBuilder();
        StringBuilder tempDailyWord = new StringBuilder(dailyWord);

        // First pass to mark 'G' (green) positions
        for (int i = 0; i < guess.length(); i++) {
            if (guess.charAt(i) == dailyWord.charAt(i)) {
                result.append("G"); // Green
                tempDailyWord.setCharAt(i, '_'); // Mark this letter as processed
            } else {
                result.append("_");
            }
        }
        
        // Second pass for 'Y' (yellow) and 'B' (black)
        for (int i = 0; i < guess.length(); i++) {
            if (result.charAt(i) == 'G') continue; // Skip already marked green

            char guessChar = guess.charAt(i);
            if (tempDailyWord.toString().contains(Character.toString(guessChar))) {
                result.setCharAt(i, 'Y'); // Yellow
                tempDailyWord.setCharAt(tempDailyWord.indexOf(Character.toString(guessChar)), '_');
            } else {
                result.setCharAt(i, 'B'); // Black
            }
        }

        return result.toString();
    }

    // Helper method to create a GuessResponse
    private GuessResponse createResponse(String message, String currentResult, User user) {
        GuessResponse response = new GuessResponse();
        response.setMessage(message);
        response.setCurrentResult(currentResult);
        response.setPreviousGuesses(getPreviousGuesses(user));
        return response;
    }

    // Get previous guesses for the user
    public List<GuessSummary> getPreviousGuesses(User user) {
        LocalDate currentDate = LocalDate.now();  // Get today's date

        return userAttemptRepository.findByUserIdAndAttemptDateOrderByAttemptDateAsc(user.getId(), currentDate)
            .stream()
            .map(attempt -> new GuessSummary(attempt.getGuessedWord(), attempt.getResult()))
            .collect(Collectors.toList());
    }
    
    public List<GuessSummary> getAllGuesses(Long userId) {
    	User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found with ID: " + userId));

        return getPreviousGuesses(user);
    }
    
}
