package com.example.meetup_study.image.announceImage;

import com.example.meetup_study.announce.AnnounceService;
import com.example.meetup_study.announce.domain.Announce;
import com.example.meetup_study.image.announceImage.domain.AnnounceImage;
import com.example.meetup_study.image.announceImage.domain.repository.AnnounceImageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AnnounceImageServiceImpl implements AnnounceImageService{


    private final AnnounceImageRepository announceImageRepository;
    private final AnnounceService announceService;

    @Override
    public Optional<AnnounceImage> createAnnounceImage(String path, Long announceId) {
        Optional<Announce> announceOpt = announceService.getAnnounce(announceId);
        if(!announceOpt.isPresent()) throw new IllegalArgumentException("존재하지 않는 게시글입니다.");

        AnnounceImage announceImage = new AnnounceImage(path);
        announceOpt.get().changeAnnounceImage(announceImage);

        return Optional.of(announceImageRepository.save(announceImage));
    }

    @Override
    public Optional<AnnounceImage> getAnnounceImage(Long announceId) {
        Optional<Announce> announceOpt = announceService.getAnnounce(announceId);
        if(!announceOpt.isPresent()) throw new IllegalArgumentException("존재하지 않는 게시글입니다.");

        return Optional.ofNullable(announceOpt.get().getAnnounceImage());
    }

    @Override
    public Optional<AnnounceImage> updateAnnounceImage(String path, Long announceId) {
        Optional<Announce> announceOpt = announceService.getAnnounce(announceId);
        if(!announceOpt.isPresent()) throw new IllegalArgumentException("존재하지 않는 게시글입니다.");

        AnnounceImage announceImage = announceOpt.get().getAnnounceImage();

        announceImage.changePath(path);

        return Optional.of(announceImageRepository.save(announceImage));
    }

    @Override
    public Optional<AnnounceImage> deleteAnnounceImage(Long announceId) {
        Optional<Announce> announceOpt = announceService.getAnnounce(announceId);
        if(!announceOpt.isPresent()) throw new IllegalArgumentException("존재하지 않는 게시글입니다.");

        AnnounceImage announceImage = announceOpt.get().getAnnounceImage();

        announceImageRepository.delete(announceImage);

        return Optional.of(announceImage);
    }
}
