package com.game.wordle.controller;

import com.game.wordle.dto.CustomUserDetails;
import com.game.wordle.dto.GuessDto;
import com.game.wordle.dto.GuessResponse;
import com.game.wordle.dto.GuessSummary;
import com.game.wordle.service.GameService;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
@RequestMapping("/game")
public class GameController {

    @Autowired
    private GameService gameService;

    @PostMapping("/guess")
    public GuessResponse guess(@RequestBody GuessDto guessDto) {
        Long userId = ((CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUserId();
        return gameService.processGuess(userId, guessDto.getGuess());
    }
    
    @GetMapping("/previousGuesses")
    public List<GuessSummary> guess() {
        Long userId = ((CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUserId();
        return gameService.getAllGuesses(userId);
    }
}
