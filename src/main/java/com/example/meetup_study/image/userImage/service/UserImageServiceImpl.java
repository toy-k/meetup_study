package com.example.meetup_study.image.userImage.service;

import com.example.meetup_study.image.exception.ImageInvalidRequestException;
import com.example.meetup_study.image.exception.ImageNotFoundException;
import com.example.meetup_study.image.userImage.domain.UserImage;
import com.example.meetup_study.image.userImage.domain.dto.UserImageDto;
import com.example.meetup_study.image.userImage.domain.repository.UserImageRepository;
import com.example.meetup_study.user.fakeUser.exception.UserNotFoundException;
import com.example.meetup_study.user.service.UserService;
import com.example.meetup_study.user.domain.User;
import lombok.RequiredArgsConstructor;
import net.coobird.thumbnailator.Thumbnails;
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
public class UserImageServiceImpl implements UserImageService {

    private final UserImageRepository userImageRepository;
    private final UserService userService;

    @Override
    public Optional<UserImageDto> updateUserImagee(MultipartFile file, Long userId) {

        try{
            if (file.isEmpty()) {
                throw new ImageNotFoundException();
            }

            long fileSize = file.getSize();


            Optional<User> userOpt = userService.findById(userId);
            UserImage userImage = userOpt.get().getUserImage();

            String fileExtension = getFileExtension(file.getOriginalFilename());
            byte[] data = compressFile(file.getBytes(), fileExtension);

            UserImageDto userImageDtoOpt;
            if(userImage == null){
                UserImage userImageInst = new UserImage(data);

                UserImage newUserImage = userImageRepository.save(userImageInst);

                userOpt.get().changeUserImage(newUserImage);

                userImageDtoOpt = new UserImageDto(newUserImage.getProfile());
            }else{
                Optional<UserImage> userImageOpt = userImageRepository.findById(userImage.getId());
                if (!userImageOpt.isPresent()) {
                    throw new ImageNotFoundException();
                }

                userImageOpt.get().changeProfile(data);
                userImageRepository.save(userImageOpt.get());

                userImageDtoOpt = new UserImageDto(userImage.getProfile());
            }
            return Optional.of(userImageDtoOpt);

        }catch (IOException e){
            throw new ImageInvalidRequestException();
        }

    }


    @Override
    public Optional<UserImageDto> getUserImagee(Long userId) {

        Optional<User> userOpt = userService.findById(userId);
        if(!userOpt.isPresent()){
            throw new UserNotFoundException();
        }

        UserImage userImage = userOpt.get().getUserImage();

        UserImageDto userImageDto = new UserImageDto(userImage.getProfile());

        return Optional.of(userImageDto);

    }

    @Override
    public Optional<UserImageDto> deleteUserImagee(Long userId) {

        Optional<User> userOpt = userService.findById(userId);

        UserImage userImage = userOpt.get().getUserImage();


        userOpt.get().getUserImage().changeProfile(null);

        userImageRepository.save(userImage);

        UserImageDto userImageDto = new UserImageDto(userImage.getProfile());

        return Optional.of(userImageDto);

    }

    private byte[] compressFile(byte[] data, String fileExtension) throws IOException {

        if (fileExtension.equalsIgnoreCase("jpeg") || fileExtension.equalsIgnoreCase("jpg")) {

            BufferedImage image = ImageIO.read(new ByteArrayInputStream(data));
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

            Thumbnails.of(image)
                    .size(150, 150)
                    .outputFormat("jpg")
                    .outputQuality(0.8)
                    .toOutputStream(outputStream);

            return outputStream.toByteArray();
        } else if (fileExtension.equalsIgnoreCase("png")) {

            BufferedImage image = ImageIO.read(new ByteArrayInputStream(data));
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            Thumbnails.of(image)
                    .size(150, 150)
                    .outputFormat("png")
                    .outputQuality(0.8)
                    .toOutputStream(outputStream);

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
