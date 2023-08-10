package com.example.meetup_study.admin.service;

import com.example.meetup_study.hostReview.domain.HostReview;
import com.example.meetup_study.hostReview.domain.dto.HostReviewDto;
import com.example.meetup_study.hostReview.domain.repository.HostReviewRepository;
import com.example.meetup_study.hostReview.exception.HostReviewNotFoundException;
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
    public Optional<RoomDto> deleteRoom(Long id, Long userId) {

        Optional<Room> roomOpt = roomService.getRoom(id);

        if(!roomOpt.isPresent()){
            throw new RoomNotFoundException();
        }

        Optional <Room> room = roomRepository.findById(id);

        roomRepository.delete(room.get());
        Optional<RoomDto> roomDto = room.map(r -> roomMapper.toRoomDto(r));

        return roomDto;
    }

    @Override
    public Optional<ReviewDto> deleteReview(Long reviewId, Long UserId) {

        Optional<ReviewDto> reviewDtoOpt = reviewService.findById(reviewId);

        Long roomId = reviewDtoOpt.get().getRoomId();

        Optional<Room> roomOpt = roomService.getRoom(roomId);
        if(!roomOpt.isPresent()){
            throw new RoomNotFoundException();
        }

        Optional<Review> review = reviewRepository.findById(reviewId);
        if(review.isPresent()){

            if(review.get().getIsHostReview()){
                throw new ReviewInvalidRequestException("이미 호스트가 리뷰 남겨서 삭제할 수 없습니다.");
            }

            reviewRepository.deleteById(reviewId);

            ReviewDto reviewDto = reviewMapper.toReviewDto(review.get());
            return Optional.of(reviewDto);
        }else{
            throw new ReviewNotFoundException();
        }
    }


    @Transactional
    @Override
    public Optional<HostReviewDto> deleteHostReview(Long hostReviewId, Long userId) {
        Optional<HostReview> hostReviewOpt = hostReviewRepository.findById(hostReviewId);
        if(hostReviewOpt.isPresent()){

            Optional<Review> reviewOpt = reviewRepository.findById(hostReviewOpt.get().getReviewId());

            if (!reviewOpt.isPresent()) {
                throw new HostReviewNotFoundException();
            }

            hostReviewRepository.deleteById(hostReviewId);
            reviewOpt.get().changeIsHostReview(false);

            HostReviewDto hostReviewDto = hostReviewMapper.toHostReviewDto(hostReviewOpt.get());

            return Optional.of(hostReviewDto);

        }else{
            throw new HostReviewNotFoundException();
        }

    }

}
