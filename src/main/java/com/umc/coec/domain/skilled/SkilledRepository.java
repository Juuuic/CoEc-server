package com.umc.coec.domain.skilled;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface SkilledRepository extends JpaRepository<Skilled, Long> {
    @Query("select s from Skilled s where s.sports.id = :sportsId and s.user.id = :userId")
    public Skilled findSkilled(@Param("sportsId") Long sportsId, @Param("userId") Long userId);
}
