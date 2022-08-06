package com.umc.coec.domain.location;

import com.umc.coec.domain.BaseTimeEntity;
import com.umc.coec.domain.enums.Status;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class Location extends BaseTimeEntity  {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;

    @Enumerated(EnumType.STRING)
    private Status status=Status.ACTIVE;


    private BigDecimal latitude;
    private BigDecimal longitude;

    private String siDo;
    private String siGunGu;
    private String eupMyunDongLi;

}
