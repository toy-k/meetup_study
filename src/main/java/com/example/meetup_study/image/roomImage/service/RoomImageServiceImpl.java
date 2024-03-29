package com.example.meetup_study.image.roomImage.service;

import com.example.meetup_study.hostUser.service.HostUserService;
import com.example.meetup_study.hostUser.domain.HostUser;
import com.example.meetup_study.image.exception.ImageInvalidRequestException;
import com.example.meetup_study.image.exception.ImageNotFoundException;
import com.example.meetup_study.image.roomImage.domain.RoomImage;
import com.example.meetup_study.image.roomImage.domain.dto.RoomImageDto;
import com.example.meetup_study.image.roomImage.domain.repository.RoomImageRepository;
import com.example.meetup_study.room.exception.RoomNotFoundException;
import com.example.meetup_study.room.service.RoomService;
import com.example.meetup_study.room.domain.Room;
import com.example.meetup_study.user.fakeUser.exception.UserInvalidRequestException;
import com.example.meetup_study.user.fakeUser.exception.UserNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RoomImageServiceImpl implements RoomImageService{

    private final RoomImageRepository roomImageRepository;
    private final RoomService roomService;
    private final HostUserService hostUserService;

    @Override
    public Optional<RoomImageDto> updateRoomImage(MultipartFile file, Long roomId, Long userId) {
        try{

            Optional<Room> roomOpt = roomImageRepository.findRoomWithCategoryAndHostUsersAndImage(roomId);
            if (!roomOpt.isPresent()) throw new RoomNotFoundException();

            HostUser hostUser = roomOpt.get().getHostUserList().get(0);

            if (hostUser.getUser().getId() != userId) throw new UserInvalidRequestException("해당 방의 방장이 아닙니다.");
            if (file.isEmpty()) {
                throw new ImageNotFoundException();
            }

            RoomImage roomImage = roomOpt.get().getRoomImage();

            String fileExtension = getFileExtension(file.getOriginalFilename());
            byte[] data = compressFile(file.getBytes(), fileExtension);

            RoomImageDto roomImageDtoOpt;
            if(roomImage == null){
                RoomImage roomImageInst = new RoomImage(data);

                RoomImage newRoomImage = roomImageRepository.save(roomImageInst);

                roomOpt.get().changeRoomImage(newRoomImage);

                roomImageDtoOpt = new RoomImageDto(newRoomImage.getProfile());


            }else{
                Optional<RoomImage> roomImageOpt = roomImageRepository.findById(roomImage.getId());
                if (!roomImageOpt.isPresent()) {
                    throw new ImageNotFoundException();
                }

                roomImageOpt.get().changeProfile(data);
                roomImageRepository.save(roomImageOpt.get());
                roomImageDtoOpt = new RoomImageDto(roomImage.getProfile());

            }
            return Optional.of(roomImageDtoOpt);

        }catch (IOException e){
            throw new ImageInvalidRequestException();
        }

    }

    @Override
    public Optional<RoomImageDto> getRoomImage(Long roomId) {

        Optional<Room> roomOpt = roomImageRepository.findRoomAndRoomImageByRoomId(roomId);

        if(!roomOpt.isPresent()){
            throw new RoomNotFoundException();
        }

        RoomImage roomImage = roomOpt.get().getRoomImage();

        RoomImageDto roomImageDto = new RoomImageDto(roomImage.getProfile());

        return Optional.of(roomImageDto);
    }

    @Override
    public Optional<RoomImageDto> deleteRoomImage(Long roomId) {

        Optional<Room> roomOpt = roomService.getRoom(roomId);
        if(!roomOpt.isPresent()){
            throw new RoomNotFoundException();
        }

        RoomImage roomImage = roomOpt.get().getRoomImage();

        roomOpt.get().getRoomImage().changeProfile(null);

        roomImageRepository.save(roomImage);

        RoomImageDto roomImageDto = new RoomImageDto(roomImage.getProfile());

        return Optional.of(roomImageDto);
    }


    private byte[] compressFile(byte[] data, String fileExtension) throws IOException {

        if (fileExtension.equalsIgnoreCase("jpeg") || fileExtension.equalsIgnoreCase("jpg")) {

            BufferedImage image = ImageIO.read(new ByteArrayInputStream(data));
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            ImageIO.write(image, "jpeg", outputStream);
            return outputStream.toByteArray();
        } else if (fileExtension.equalsIgnoreCase("png")) {

            BufferedImage image = ImageIO.read(new ByteArrayInputStream(data));
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            ImageIO.write(image, "png", outputStream);
            return outputStream.toByteArray();
        } else {

            throw new ImageInvalidRequestException("유저 이미지 업로드 요청 파일 확장자가 잘못되었습니다.");
        }
    }
    private String getFileExtension(String fileName) {
        if (fileName.lastIndexOf(".") != -1 && fileName.lastIndexOf(".") != 0) {
            return fileName.substring(fileName.lastIndexOf(".") + 1);
        } else {
            return "";
        }
    }
}
