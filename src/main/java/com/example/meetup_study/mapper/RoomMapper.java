package com.example.meetup_study.mapper;

import com.example.meetup_study.Category.domain.Category;
import com.example.meetup_study.Category.domain.CategoryEnum;
import com.example.meetup_study.hostUser.domain.HostUser;
import com.example.meetup_study.room.domain.Room;
import com.example.meetup_study.room.domain.dto.RoomDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Component;

import java.util.List;

import static org.mapstruct.NullValueMappingStrategy.RETURN_DEFAULT;


@Mapper(componentModel = "spring",
        nullValueMappingStrategy = RETURN_DEFAULT,
        nullValueMapMappingStrategy = RETURN_DEFAULT,
        nullValueIterableMappingStrategy = RETURN_DEFAULT
)
@Component
public interface RoomMapper {

    RoomMapper INSTANCE = Mappers.getMapper(RoomMapper.class);


    default RoomDto toRoomDto(Room room) {
        return new RoomDto(
                room.getId(),
                room.getTitle(),
                room.getDescription(),
                convertToCategoryEnum(room.getCategory()),
                room.getLocation(),
                room.getMeetupStartDate(),
                room.getMeetupEndDate(),
                room.getMaxJoinNumber(),
                room.getCurrentJoinNumber(),
                room.getPrice(),
                room.getRoomStatus(),
                room.getRoomType(),
                room.getViewCount(),
                convertToHostUserId(room.getHostUserList()),
                room.getCreatedAt(),
                room.getUpdatedAt()
        );
    }


    //    @Mapping(target = "category", qualifiedByName = "convertToCategoryEnum")
//    @Mapping(target = "hostUserId", source = "hostUserList", qualifiedByName = "convertToHostUserId")
//    RoomDto toRoomDto(Room room);
//
//    @Named("convertToCategoryEnum")
    default CategoryEnum convertToCategoryEnum(Category category) {
        return category.getName();
    }
    //
//    @Named("convertToHostUserId")
    default Long convertToHostUserId(List<HostUser> hostUserList) {

        Long hostUserId = hostUserList.isEmpty() ? null : hostUserList.get(0).getUser().getId();
        return hostUserId;
    }
}



