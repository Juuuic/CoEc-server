package com.umc.coec.domain.purpose;

import com.umc.coec.domain.BaseTimeEntity;
import com.umc.coec.domain.enums.Status;
import com.umc.coec.domain.post.Post;
import com.umc.coec.dto.partner_post.UpdatePostReqDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class Purpose extends BaseTimeEntity {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;


    @Enumerated(EnumType.STRING)
    private Status status=Status.ACTIVE;

    @Column(nullable = false)
    private String contents;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "postId")
    private Post post;

    public Purpose update(int i, UpdatePostReqDto updatePostReqDto) {
        contents = updatePostReqDto.getPurposes().get(i);
        status = updatePostReqDto.getStatus().equals("모집중") ? Status.ACTIVE : Status.INACTIVE;

        return this;
    }
}
