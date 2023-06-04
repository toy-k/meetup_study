package com.example.meetup_study.image.userImage;

import com.example.meetup_study.image.userImage.domain.UserImage;
import com.example.meetup_study.image.userImage.domain.dto.UserImageDto;
import com.example.meetup_study.image.userImage.domain.repository.UserImageRepository;
import com.example.meetup_study.upload.roomUpload.domain.dto.UploadDto;
import com.example.meetup_study.user.UserService;
import com.example.meetup_study.user.domain.User;
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
    @Transactional
    public Optional<UserImage> createUserImage(String path, Long userId) {
        Optional<User> userOpt = userService.findById(userId);
        if(!userOpt.isPresent()) throw new IllegalArgumentException("존재하지 않는 유저입니다.");

        UserImage userImage = new UserImage(path);
        userOpt.get().changeUserImage(userImage);

        return Optional.of(userImageRepository.save(userImage));
    }

    @Override
    public Optional<UserImage> updateUserImage(String path, Long userId) {
        Optional<User> userOpt = userService.findById(userId);
        if(!userOpt.isPresent()) throw new IllegalArgumentException("존재하지 않는 유저입니다.");

        UserImage userImage = userOpt.get().getUserImage();

        userImage.changePath(path);

        return Optional.of(userImageRepository.save(userImage));

    }

    @Override
    public Optional<UserImage> getUserImage(Long userId) {
        Optional<User> userOpt = userService.findById(userId);
        if(!userOpt.isPresent()) throw new IllegalArgumentException("존재하지 않는 유저입니다.");


        return Optional.ofNullable(userOpt.get().getUserImage());
    }

    @Override
    public Optional<UserImage> deleteUserImage(Long userId) {

        Optional<User> userOpt = userService.findById(userId);
        if(!userOpt.isPresent()) throw new IllegalArgumentException("존재하지 않는 유저입니다.");

        UserImage userImage = userOpt.get().getUserImage();

        userOpt.get().changeUserImage(null);

        userImageRepository.delete(userImage);

        return Optional.of(userImage);

    }


    //byte CREUD

    @Override
    public Optional<UserImageDto> uploadUserImage(MultipartFile file, Long userId) throws Exception {

        Optional<User> userOpt = userService.findById(userId);
        if(!userOpt.isPresent()) throw new IllegalArgumentException("존재하지 않는 유저입니다.");

        if (file.isEmpty()) {
            throw new IllegalArgumentException("파일이 없습니다.");
        }

        try{
            byte[] data = compressFile(file.getBytes());

            UserImage userImage = new UserImage(data);

            UserImage userImageOpt = userImageRepository.save(userImage);

            UserImageDto userImageDtoOpt = new UserImageDto(userImageOpt.getProfile());


            return Optional.of(userImageDtoOpt);
        }catch (IOException e){
            throw new IllegalArgumentException("파일 압축 및 업로드 실패했습니다.");
        }
    }

    @Override
    public Optional<UserImageDto> updateUserImagee(MultipartFile file, Long userId) {

        Optional<User> userOpt = userService.findById(userId);
        if(!userOpt.isPresent()) throw new IllegalArgumentException("존재하지 않는 유저입니다.");

        if (file.isEmpty()) {
            throw new IllegalArgumentException("파일이 없습니다.");
        }

        try{
            byte[] data = compressFile(file.getBytes());

            UserImage userImage = new UserImage(data);

            userImage.changeProfile(data);

            UserImage userImageOpt = userImageRepository.save(userImage);

            UserImageDto userImageDtoOpt = new UserImageDto(userImage.getProfile());

            return Optional.of(userImageDtoOpt);
        }catch (IOException e){
            throw new IllegalArgumentException("파일 압축 및 업로드 실패했습니다.");
        }

    }

    @Override
    public Optional<UserImageDto> getUserImagee(Long userId) {

        Optional<User> userOpt = userService.findById(userId);
        if(!userOpt.isPresent()) throw new IllegalArgumentException("존재하지 않는 유저입니다.");

        UserImage userImage = userOpt.get().getUserImage();

        UserImageDto userImageDto = new UserImageDto(userImage.getProfile());

        return Optional.of(userImageDto);

    }

    @Override
    public Optional<UserImageDto> deleteUserImagee(Long userId) {

        Optional<User> userOpt = userService.findById(userId);
        if(!userOpt.isPresent()) throw new IllegalArgumentException("존재하지 않는 유저입니다.");

        UserImage userImage = userOpt.get().getUserImage();

        userOpt.get().changeUserImage(null);

        userImageRepository.delete(userImage);

        UserImageDto userImageDto = new UserImageDto(userImage.getProfile());

        return Optional.of(userImageDto);

    }

    private byte[] compressFile(byte[] data) throws IOException {
        // 파일을 압축하거나 처리하는 로직을 구현해주세요.
        // 여기서는 간단히 이미지를 JPEG 형식으로 압축하는 예시를 제공합니다.

        // 이미지 로딩
        BufferedImage image = ImageIO.read(new ByteArrayInputStream(data));

        // 이미지를 JPEG 형식으로 압축
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ImageIO.write(image, "jpeg", outputStream);

        return outputStream.toByteArray();
    }


}
