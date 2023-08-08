package com.example.meetup_study.mapper;

import com.example.meetup_study.announce.domain.Announce;
import com.example.meetup_study.announce.domain.dto.AnnounceDto;
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
public interface AnnounceMapper {

    AnnounceMapper INSTANCE = Mappers.getMapper(AnnounceMapper.class);

    default AnnounceDto toAnnounceDto(Announce announce){
        if (announce == null) {
            return null;
        }

        return new AnnounceDto(
                announce.getId(),
                announce.getTitle(),
                announce.getDescription(),
                announce.getUser().getId(),
                announce.getCreatedAt(),
                announce.getUpdatedAt()
        );
    }
}
