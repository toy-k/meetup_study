package com.example.meetup_study.announce.domain.dto;

import com.example.meetup_study.announce.domain.Announce;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;

@Getter
@Setter
@NoArgsConstructor
public class AnnounceDto {

    @Positive
    @NotBlank(message = "id는 필수 입력 값입니다.")
    private Long id;

    @NotBlank(message = "title은 필수 입력 값입니다.")
    private String title;

    @NotBlank(message = "description은 필수 입력 값입니다.")
    private String description;

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
