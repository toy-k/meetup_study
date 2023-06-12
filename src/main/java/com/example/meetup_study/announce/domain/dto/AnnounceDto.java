package com.example.meetup_study.announce.domain.dto;

import com.example.meetup_study.announce.domain.Announce;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;

@Getter
@Setter
@NoArgsConstructor
public class AnnounceDto {

    @Schema(description = "공지사항 id", example = "1", required = true)
    @Positive
    @NotBlank(message = "id는 필수 입력 값입니다.")
    private Long id;

    @Schema(description = "공지사항 제목", example = "공지사항 제목", required = true)
    @NotBlank(message = "title은 필수 입력 값입니다.")
    private String title;

    @Schema(description = "공지사항 내용", example = "공지사항 내용", required = true)
    @NotBlank(message = "description은 필수 입력 값입니다.")
    private String description;

    @Schema(description = "공지사항 작성자 id", example = "1", required = true)
    @NotBlank(message = "userId은 필수 입력 값입니다.")
    private Long userId;

    public AnnounceDto(Long id, String title, String description, Long userId) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.userId = userId;
    }

    public AnnounceDto convertToAnnounceDto(Announce announce) {
        return new AnnounceDto(
                announce.getId(),
                announce.getTitle(),
                announce.getDescription(),
                announce.getUser().getId()
        );

    }
}
