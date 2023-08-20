package com.example.meetup_study.cart.domain.dto;

import com.example.meetup_study.category.domain.CategoryEnum;
import com.example.meetup_study.room.domain.enums.RoomStatus;
import com.example.meetup_study.room.domain.enums.RoomType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
@AllArgsConstructor
public class CartDto implements Serializable {


    @Schema(description = "방 id", example = "1", required = true)
    @Positive
    @NotNull(message = "roomId는 필수 입력 값입니다.")
    private Long roomId;

    @Schema(description = "방 제목", example = "방 제목", required = true)
    @NotBlank(message = "title은 필수 입력 값입니다.")
    private String title;

    @Schema(description = "모임 설명", example = "모임 설명", required = true)
    @NotBlank(message = "description은 필수 입력 값입니다.")
    private String description;



}
