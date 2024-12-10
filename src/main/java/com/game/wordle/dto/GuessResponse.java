package com.game.wordle.dto;

import java.util.List;

public class GuessResponse {
    private String message;
    private String currentResult;
    private List<GuessSummary> previousGuesses;

    // Getters and Setters

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getCurrentResult() {
        return currentResult;
    }

    public void setCurrentResult(String currentResult) {
        this.currentResult = currentResult;
    }

    public List<GuessSummary> getPreviousGuesses() {
        return previousGuesses;
    }

    public void setPreviousGuesses(List<GuessSummary> previousGuesses) {
        this.previousGuesses = previousGuesses;
    }
}
