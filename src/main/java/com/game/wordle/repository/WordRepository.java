package com.game.wordle.repository;

import com.game.wordle.entity.WordDictionary;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WordRepository extends JpaRepository<WordDictionary, Long> {
}
