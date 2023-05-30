package com.example.meetup_study.announce;

import com.example.meetup_study.announce.domain.Announce;
import com.example.meetup_study.announce.domain.dto.AnnounceDto;
import com.example.meetup_study.announce.domain.dto.RequestAnnounceDto;

import java.util.List;
import java.util.Optional;

public interface AnnounceService {
    Optional<Announce> createAnnounce(RequestAnnounceDto requestAnnounceDto);

    Optional<Announce> getAnnounce(Long announceId);

    List<AnnounceDto> getAnnounceList();

    Optional<AnnounceDto> updateAnnounce(AnnounceDto announceDto, Long userId);

    Optional<AnnounceDto> deleteAnnounce(Long announceId, Long userId);
}
