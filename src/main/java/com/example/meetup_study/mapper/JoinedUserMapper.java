package com.example.meetup_study.mapper;

import com.example.meetup_study.joinedUser.domain.JoinedUser;
import com.example.meetup_study.joinedUser.domain.dto.JoinedUserDto;
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
public interface JoinedUserMapper {


    JoinedUserMapper INSTANCE = Mappers.getMapper(JoinedUserMapper.class);

    default JoinedUserDto toJoinedUserDto(JoinedUser joinedUser){
        if (joinedUser == null) {
            return null;
        }

        return new JoinedUserDto(
                joinedUser.getId(),
                joinedUser.getRoom().getId(),
                joinedUser.getUser().getId());
    }
}
