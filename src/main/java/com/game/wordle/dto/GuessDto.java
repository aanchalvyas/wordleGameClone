package com.game.wordle.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class GuessDto {
    @NotBlank
    private String guess;

	public String getGuess() {
		return guess;
	}

	public void setGuess(String guess) {
		this.guess = guess;
	}
    
    
}
