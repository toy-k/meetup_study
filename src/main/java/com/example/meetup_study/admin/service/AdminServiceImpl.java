package com.example.meetup_study.admin.service;

import com.example.meetup_study.hostReview.domain.HostReview;
import com.example.meetup_study.hostReview.domain.dto.HostReviewDto;
import com.example.meetup_study.hostReview.domain.repository.HostReviewRepository;
import com.example.meetup_study.hostReview.exception.HostReviewNotFoundException;
import com.example.meetup_study.hostReview.service.HostReviewService;
import com.example.meetup_study.hostUser.service.HostUserService;
import com.example.meetup_study.mapper.HostReviewMapper;
import com.example.meetup_study.mapper.ReviewMapper;
import com.example.meetup_study.mapper.RoomMapper;
import com.example.meetup_study.review.domain.Review;
import com.example.meetup_study.review.domain.dto.ReviewDto;
import com.example.meetup_study.review.domain.repository.ReviewRepository;
import com.example.meetup_study.review.exception.ReviewInvalidRequestException;
import com.example.meetup_study.review.exception.ReviewNotFoundException;
import com.example.meetup_study.review.service.ReviewService;
import com.example.meetup_study.room.domain.Room;
import com.example.meetup_study.room.domain.dto.RoomDto;
import com.example.meetup_study.room.domain.repository.RoomRepository;
import com.example.meetup_study.room.exception.RoomNotFoundException;
import com.example.meetup_study.room.service.RoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class AdminServiceImpl implements AdminService{

    private final ReviewRepository reviewRepository;
    private final RoomRepository roomRepository;
    private final HostReviewRepository hostReviewRepository;
    private final RoomMapper roomMapper;
    private final HostReviewMapper hostReviewMapper;
    private final ReviewMapper reviewMapper;
    private final RoomService roomService;
    private final ReviewService reviewService;

    @Override
    public Boolean deleteRoom(Long id, Long userId) {

        Optional<Room> roomOpt = roomService.getRoom(id);
        if(!roomOpt.isPresent()){
            throw new RoomNotFoundException();
        }

        roomRepository.delete(roomOpt.get());

        return true;
    }

    @Override
    public Boolean deleteReview(Long reviewId, Long userId) {

        Boolean res = reviewService.deleteReview(reviewId, userId);

        return res;
    }


}
