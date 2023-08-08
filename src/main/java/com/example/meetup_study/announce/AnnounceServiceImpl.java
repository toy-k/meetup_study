package com.example.meetup_study.announce;

import com.example.meetup_study.announce.domain.Announce;
import com.example.meetup_study.announce.domain.dto.AnnounceDto;
import com.example.meetup_study.announce.domain.dto.RequestAnnounceDto;
import com.example.meetup_study.announce.domain.repository.AnnounceRepository;
import com.example.meetup_study.announce.exception.AnnounceInvalidRequestException;
import com.example.meetup_study.announce.exception.AnnounceNotFoundException;
import com.example.meetup_study.image.announceImage.domain.AnnounceImage;
import com.example.meetup_study.mapper.AnnounceMapper;
import com.example.meetup_study.room.domain.dto.RoomDto;
import com.example.meetup_study.user.domain.User;
import com.example.meetup_study.user.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AnnounceServiceImpl implements AnnounceService {

    private final AnnounceRepository announceRepository;
    private final UserRepository userRepository;
    private final RedisTemplate<String, String> redisTemplate;
    private final AnnounceMapper announceMapper;

    @Transactional
    @Override
    public Optional<AnnounceDto> createAnnounce(RequestAnnounceDto requestAnnounceDto) {
        Optional<User> userOpt = userRepository.findById(requestAnnounceDto.getUserId());


        AnnounceImage announceImage = new AnnounceImage();

        Announce announce = announceRepository.save(new Announce(requestAnnounceDto.getTitle(), requestAnnounceDto.getDescription(), userOpt.get(), announceImage));

        userOpt.get().addAnnounce(announce);

//        AnnounceDto announceDto = new AnnounceDto().convertToAnnounceDto(announce);

        AnnounceDto announceDto = announceMapper.toAnnounceDto(announce);

        return Optional.of(announceDto);
    }

    @Override
    public Optional<AnnounceDto> getAnnounce(Long announceId) {
        Optional<Announce> announceOpt = announceRepository.findById(announceId);
        if(announceOpt.isPresent()) {
            Announce announce = announceOpt.get();
            Long viewCount = this.incrementViewCount(announceId);
            announce.changeViewCount(viewCount);
        }

        AnnounceDto announceDto = announceMapper.toAnnounceDto(announceOpt.get());

        return Optional.of(announceDto);

    }

    @Override
    public List<AnnounceDto> getAnnounceList(Integer page, Integer size) {

//        PageRequest pageRequest = PageRequest.of(page-1, size);
        PageRequest pageRequest = PageRequest.of(page-1, size, Sort.by("id").descending());
        Page<Announce> announcePage = announceRepository.findAll(pageRequest);

        List<Announce> announce = announcePage.getContent();
        if(announce.isEmpty()){
            return new ArrayList<>();
        }

        List<AnnounceDto> announceDtos = announce.stream().map(r -> announceMapper.toAnnounceDto(r)).collect(Collectors.toList());

        return announceDtos;
    }

    @Override
    public Optional<AnnounceDto> updateAnnounce(AnnounceDto announceDto, Long userId) {

        Optional<Announce> announceOpt = announceRepository.findById(announceDto.getId());

        if(!announceOpt.isPresent()){
            throw new AnnounceNotFoundException();
        }

        if (!announceOpt.get().getUser().getId().equals(userId)) {
            throw new AnnounceInvalidRequestException();
        }

        Optional<AnnounceDto> announceDtoOpt = announceOpt.map(announce->{
            if(announceDto.getTitle() != null) announce.changeTitle(announceDto.getTitle());
            if(announceDto.getDescription() != null) announce.changeDescription(announceDto.getDescription());
            return announceMapper.toAnnounceDto(announce);
        });
        announceRepository.save(announceOpt.get());

        return announceDtoOpt;

    }

    @Override
    public Optional<AnnounceDto> deleteAnnounce(Long announceId, Long userId) {


        Optional<Announce> announceOpt = announceRepository.findById(announceId);

        if(!announceOpt.isPresent()){
            throw new AnnounceNotFoundException();
        }

        if (!announceOpt.get().getUser().getId().equals(userId)) {
            throw new AnnounceInvalidRequestException();
        }
        announceRepository.deleteById(announceId);
        Optional<AnnounceDto> announceDto = announceOpt.map(r -> announceMapper.toAnnounceDto(r));

        return announceDto;
    }

    @Override
    public Long getAnnounceCount() {
        return announceRepository.count();
    }

    private Long incrementViewCount(Long announceId){
        String key = "announce:" + announceId + ":viewCount";
        Long count = redisTemplate.opsForValue().increment(key);
        return count.longValue();
    }


}
