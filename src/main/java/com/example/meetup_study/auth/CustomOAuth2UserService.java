package com.example.meetup_study.auth;

import com.example.meetup_study.image.userImage.UserImageService;
import com.example.meetup_study.image.userImage.domain.UserImage;
import com.example.meetup_study.image.userImage.domain.repository.UserImageRepository;
import com.example.meetup_study.user.domain.ProviderType;
import com.example.meetup_study.user.domain.RoleType;
import com.example.meetup_study.user.domain.User;
import com.example.meetup_study.user.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final UserRepository userRepository;
    private final UserImageRepository userImageRepository;

    @Transactional
    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {

        OAuth2User user =  super.loadUser(userRequest);

        try{
            return this.process(userRequest, user);
        }catch (Exception e){
            throw new OAuth2AuthenticationException(e.getMessage());
        }
    }

    private OAuth2User process(OAuth2UserRequest userRequest, OAuth2User user) {

        ProviderType providerType = ProviderType.valueOf(userRequest.getClientRegistration().getRegistrationId().toUpperCase());

        CustomOauth2UserInfo customOauth2UserInfo = OAuth2UserInfoOptimizer.optimize(providerType, user.getAttributes());

        Optional<User> userOpt = userRepository.findByEmail(customOauth2UserInfo.getEmail());

        User userInDb;

        if(userOpt.isPresent()){
            userInDb = userOpt.get();
            if(userInDb.getProviderType() != providerType){
                throw new IllegalArgumentException("이미 가입된 이메일이 존재합니다.");
            }
            updateUser(userInDb, customOauth2UserInfo);
        }else{
            userInDb = createUser(customOauth2UserInfo, providerType);
        };

        return UserPrincipal.create(userInDb, user.getAttributes());
    }

    @Transactional
    public User createUser(CustomOauth2UserInfo customOauth2UserInfo, ProviderType providerType) {


        UserImage userImage = new UserImage(customOauth2UserInfo.getImageUrl());

        User user = new User(
                customOauth2UserInfo.getName(),
                userImage,
                customOauth2UserInfo.getEmail(),
                providerType.toString(),
                RoleType.USER,
                providerType,
                customOauth2UserInfo.getId()
        );
        userImageRepository.save(userImage);
        return userRepository.saveAndFlush(user);
    }

    @Transactional
    public void updateUser(User user, CustomOauth2UserInfo customOauth2UserInfo) {

        if(customOauth2UserInfo.getName() != null && !user.getUsername().equals(customOauth2UserInfo.getName())){
            user.changeUsername(customOauth2UserInfo.getName());
        }

        if(customOauth2UserInfo.getImageUrl() != null && !user.getUserImage().getPath().equals((customOauth2UserInfo.getImageUrl()))){

            UserImage userImage = new UserImage(customOauth2UserInfo.getImageUrl());

            user.changeUserImage(userImage);
            userImageRepository.save(userImage);

        }
    }
}
