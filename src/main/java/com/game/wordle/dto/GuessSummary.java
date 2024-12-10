package com.game.wordle.dto;

public class GuessSummary {
    private String guessedWord;
    private String result;

    public GuessSummary(String guessedWord, String result) {
        this.guessedWord = guessedWord;
        this.result = result;
    }

    // Getters and Setters
    public String getGuessedWord() {
        return guessedWord;
    }

    public void setGuessedWord(String guessedWord) {
        this.guessedWord = guessedWord;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }
}
