package com.example.meetup_study.announce;

import com.example.meetup_study.announce.domain.Announce;
import com.example.meetup_study.announce.domain.dto.AnnounceDto;
import com.example.meetup_study.announce.domain.dto.RequestAnnounceDto;

import java.util.List;
import java.util.Optional;

public interface AnnounceService {
    Optional<AnnounceDto> createAnnounce(RequestAnnounceDto requestAnnounceDto);

    Optional<AnnounceDto> getAnnounce(Long announceId);

    List<AnnounceDto> getAnnounceList(Integer page, Integer size);

    Optional<AnnounceDto> updateAnnounce(AnnounceDto announceDto, Long userId);

    Optional<AnnounceDto> deleteAnnounce(Long announceId, Long userId);

    Long getAnnounceCount();
}
