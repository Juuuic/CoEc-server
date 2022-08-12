package com.umc.coec.domain.purpose;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PurposeRepository extends JpaRepository<Purpose, Long> {

}
