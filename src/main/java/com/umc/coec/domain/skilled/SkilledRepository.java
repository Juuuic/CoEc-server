package com.umc.coec.domain.skilled;

import com.umc.coec.domain.sports.Sports;
import com.umc.coec.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface SkilledRepository extends JpaRepository<Skilled, Long> {
    public Skilled findBySportsAndUser(Sports sports, User user);
}
