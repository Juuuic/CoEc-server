package com.umc.coec.domain.sports;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SportsRepository extends JpaRepository<Sports, Long> {
      Optional<Sports> findByName(String sportsName);
}
