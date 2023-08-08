package com.example.meetup_study.mapper;

import com.example.meetup_study.hostReview.domain.HostReview;
import com.example.meetup_study.hostReview.domain.dto.HostReviewDto;
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
public interface HostReviewMapper {

    HostReviewMapper INSTANCE = Mappers.getMapper(HostReviewMapper.class);

     default HostReviewDto toHostReviewDto(HostReview hostReview) {

        if (hostReview == null) {
            return null;
        }

        return new HostReviewDto(
                hostReview.getId(),
                hostReview.getUser().getId(),
                hostReview.getRoom().getId(),
                hostReview.getReviewId(),
                hostReview.getContent());
     }
}
