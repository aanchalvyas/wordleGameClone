package com.game.wordle.repository;

import com.game.wordle.entity.GameSettings;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.Optional;

public interface GameSettingsRepository extends JpaRepository<GameSettings, Long> {

    Optional<GameSettings> findByDate(LocalDate date);
}
