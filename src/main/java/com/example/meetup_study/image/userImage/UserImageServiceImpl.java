package com.example.meetup_study.image.userImage;

import com.example.meetup_study.image.exception.ImageInvalidRequestException;
import com.example.meetup_study.image.exception.ImageNotFoundException;
import com.example.meetup_study.image.userImage.domain.UserImage;
import com.example.meetup_study.image.userImage.domain.dto.UserImageDto;
import com.example.meetup_study.image.userImage.domain.repository.UserImageRepository;
import com.example.meetup_study.upload.roomUpload.domain.dto.UploadDto;
import com.example.meetup_study.user.UserService;
import com.example.meetup_study.user.domain.User;
import com.example.meetup_study.user.fakeUser.exception.UserNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import javax.transaction.Transactional;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserImageServiceImpl implements UserImageService {

    private final UserImageRepository userImageRepository;
    private final UserService userService;

    @Override
    public Optional<UserImageDto> updateUserImagee(MultipartFile file, Long userId) {

        try{
            Optional<User> userOpt = userService.findById(userId);
            UserImage userImage = userOpt.get().getUserImage();

            String fileExtension = getFileExtension(file.getOriginalFilename());
            byte[] data = compressFile(file.getBytes(), fileExtension);


            if(userImage == null){
                UserImage userImageInst = new UserImage(data);

                UserImage newUserImage = userImageRepository.save(userImageInst);

                userOpt.get().changeUserImage(newUserImage);

                UserImageDto userImageDtoOpt = new UserImageDto(newUserImage.getProfile());

                return Optional.of(userImageDtoOpt);

            }else{
                Optional<UserImage> userImageOpt = userImageRepository.findById(userImage.getId());
                if (!userImageOpt.isPresent()) {
                    throw new ImageNotFoundException();
                }

                userImageOpt.get().changeProfile(data);
                userImageRepository.save(userImageOpt.get());
                UserImageDto userImageDtoOpt = new UserImageDto(userImage.getProfile());

                return Optional.of(userImageDtoOpt);


            }
        }catch (IOException e){
            throw new ImageInvalidRequestException();
        }

    }

    //byte 응답 = 이미지 브라우저에 띄움
//    @Override
//    public Optional<byte[]> getUserImageee(Long userId) {
//        Optional<User> userOpt = userService.findById(userId);
//
//        if (!userOpt.isPresent()) {
//            throw new UserNotFoundException();
//        }
//
//        UserImage userImage = userOpt.get().getUserImage();
//        byte[] imageBytes = userImage.getProfile();
//
//        return Optional.of(imageBytes);
//    }


    @Override
    public Optional<UserImageDto> getUserImagee(Long userId) {

        Optional<User> userOpt = userService.findById(userId);

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
