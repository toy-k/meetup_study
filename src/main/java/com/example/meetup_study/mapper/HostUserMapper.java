package com.example.meetup_study.mapper;

import com.example.meetup_study.hostUser.domain.HostUser;
import com.example.meetup_study.hostUser.domain.dto.HostUserDto;
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
public interface HostUserMapper {

    HostUserMapper INSTANCE = Mappers.getMapper(HostUserMapper.class);

    default HostUserDto toHostUserDto(HostUser hostUser) {
        if (hostUser == null) {
            return null;
        }

        return new HostUserDto(
                hostUser.getId(),
                hostUser.getRoom().getId(),
                hostUser.getUser().getId());
    }
}
