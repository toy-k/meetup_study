package com.example.meetup_study.announce;

import com.example.meetup_study.announce.domain.Announce;
import com.example.meetup_study.announce.domain.dto.AnnounceDto;
import com.example.meetup_study.announce.domain.dto.RequestAnnounceDto;
import com.example.meetup_study.announce.domain.repository.AnnounceRepository;
import com.example.meetup_study.image.announceImage.domain.AnnounceImage;
import com.example.meetup_study.room.domain.dto.RoomDto;
import com.example.meetup_study.user.domain.User;
import com.example.meetup_study.user.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AnnounceServiceImpl implements AnnounceService {

    private final AnnounceRepository announceRepository;
    private final UserRepository userRepository;

    @Transactional
    @Override
    public Optional<Announce> createAnnounce(RequestAnnounceDto requestAnnounceDto) {
        Optional<User> userOpt = userRepository.findById(requestAnnounceDto.getUserId());

        if(userOpt.isPresent()){

            AnnounceImage announceImage = new AnnounceImage(requestAnnounceDto.getImagePath());

            Announce announce = announceRepository.save(new Announce(requestAnnounceDto.getTitle(), requestAnnounceDto.getDescription(), userOpt.get(), announceImage));

            userOpt.get().addAnnounce(announce);

            return Optional.ofNullable(announce);
        }else{
            throw new IllegalArgumentException("User가 없습니다.");
        }
    }

    @Override
    public Optional<Announce> getAnnounce(Long announceId) {
        Optional<Announce> announceOpt = announceRepository.findById(announceId);
        if(announceOpt.isPresent()) {
            Announce announce = announceOpt.get();
//            Long viewCount = this.incrementViewCount(id);
//            announce.changeViewCount(viewCount);
        }

        return announceOpt;
    }

    @Override
    public List<AnnounceDto> getAnnounceList() {
        List<Announce> announce = announceRepository.findAll();
        List<AnnounceDto> announceDtos = announce.stream().map(r -> new AnnounceDto().convertToAnnounceDto(r)).collect(Collectors.toList());
        return announceDtos;
    }

    @Override
    public Optional<AnnounceDto> updateAnnounce(AnnounceDto announceDto, Long userId) {

        Optional<Announce> announceOpt = announceRepository.findById(announceDto.getId());
        if(announceOpt.isPresent() && (announceOpt.get().getUser().getId().equals(userId))) {

            Optional<AnnounceDto> announceDtoOpt = announceOpt.map(announce->{
                if(announceDto.getTitle() != null) announce.changeTitle(announceDto.getTitle());
                if(announceDto.getDescription() != null) announce.changeDescription(announceDto.getDescription());
                return new AnnounceDto().convertToAnnounceDto(announce);
            });

            return announceDtoOpt;
        }else{
            throw new IllegalArgumentException("존재하지 않는 게시글이거나, 권한이 없습니다.");
        }

    }

    @Override
    public Optional<AnnounceDto> deleteAnnounce(Long announceId, Long userId) {
        Optional<Announce> announceOpt = announceRepository.findById(announceId);
        if(announceOpt.isPresent() && (announceOpt.get().getUser().getId().equals(userId))) {
            announceRepository.deleteById(announceId);
            Optional<AnnounceDto> announceDto = announceOpt.map(r -> new AnnounceDto().convertToAnnounceDto(r));
            return announceDto;
        }else{
            throw new IllegalArgumentException("존재하지 않는 게시글이거나, 권한이 없습니다.");
        }
    }
}