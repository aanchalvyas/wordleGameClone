package com.game.wordle.repository;

import com.game.wordle.entity.UserAttempt;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface UserAttemptRepository extends JpaRepository<UserAttempt, Long> {
    
    // Find attempts by user and attempt date, ordered by attempt date
    List<UserAttempt> findByUserIdAndAttemptDateOrderByAttemptDateAsc(Long userId, LocalDate attemptDate);

    // Count the number of attempts by a user for a specific date
    long countByUserIdAndAttemptDate(Long userId, LocalDate attemptDate);

    // Check if a user has a successful guess (GGGGG) for a specific date
    boolean existsByUserIdAndResultAndAttemptDate(Long userId, String result, LocalDate attemptDate);
}
