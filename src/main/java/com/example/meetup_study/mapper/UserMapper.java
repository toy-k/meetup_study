package com.example.meetup_study.mapper;


import com.example.meetup_study.user.domain.User;
import com.example.meetup_study.user.domain.dto.UserDto;
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
public interface UserMapper {

    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    default UserDto toUserDto(User user) {
        if (user == null) {
            return null;
        }

        return new UserDto(
                user.getId(),
                user.getUsername(),
                user.getUserImage().getProfile(),
                user.getEmail(),
                user.getDescription(),
                user.getRoleType()
        );
    }

}