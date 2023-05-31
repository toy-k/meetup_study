package com.example.meetup_study.image.userImage;

import com.example.meetup_study.image.userImage.domain.UserImage;
import com.example.meetup_study.image.userImage.domain.repository.UserImageRepository;
import com.example.meetup_study.user.UserService;
import com.example.meetup_study.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
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
    public Optional<UserImage> getUserImage(Long userId) {
        Optional<User> userOpt = userService.findById(userId);
        if(!userOpt.isPresent()) throw new IllegalArgumentException("존재하지 않는 유저입니다.");


        return Optional.ofNullable(userOpt.get().getUserImage());
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
    public Optional<UserImage> deleteUserImage(Long userId) {

        Optional<User> userOpt = userService.findById(userId);
        if(!userOpt.isPresent()) throw new IllegalArgumentException("존재하지 않는 유저입니다.");

        UserImage userImage = userOpt.get().getUserImage();

        userOpt.get().changeUserImage(null);

        userImageRepository.delete(userImage);

        return Optional.of(userImage);

    }
}
