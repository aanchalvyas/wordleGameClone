package com.game.wordle.entity;

import java.time.LocalDate;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "game_settings")
public class GameSettings {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String wordOfTheDay;

    private LocalDate date; 

    public GameSettings(String wordOfTheDay, LocalDate date) {
        this.wordOfTheDay = wordOfTheDay;
        this.date = date;
    }

    public GameSettings() {
    }

    public String getWordOfTheDay() {
        return wordOfTheDay;
    }

    public void setWordOfTheDay(String wordOfTheDay) {
        this.wordOfTheDay = wordOfTheDay;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }
}
