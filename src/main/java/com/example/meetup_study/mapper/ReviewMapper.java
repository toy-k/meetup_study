package com.example.meetup_study.mapper;


import com.example.meetup_study.review.domain.Review;
import com.example.meetup_study.review.domain.dto.ReviewDto;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Component;

import static org.mapstruct.NullValueMappingStrategy.RETURN_DEFAULT;

@Mapper(componentModel = "spring",
        nullValueMappingStrategy = RETURN_DEFAULT,
        nullValueMapMappingStrategy = RETURN_DEFAULT,
        nullValueIterableMappingStrategy = RETURN_DEFAULT
)
@Component
public interface ReviewMapper {

    ReviewMapper INSTANCE = Mappers.getMapper(ReviewMapper.class);

    default ReviewDto toReviewDto(Review review){
        if (review == null) {
            return null;
        }

        return new ReviewDto(
                review.getId(),
                review.getUser().getId(),
                review.getRoom().getId(),
                review.getRating(),
                review.getContent());
    }

}
