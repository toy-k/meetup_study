package com.example.meetup_study.admin;

import com.example.meetup_study.hostReview.domain.HostReview;
import com.example.meetup_study.hostReview.domain.repository.HostReviewRepository;
import com.example.meetup_study.hostReview.exception.HostReviewNotFoundException;
import com.example.meetup_study.review.domain.Review;
import com.example.meetup_study.review.domain.repository.ReviewRepository;
import com.example.meetup_study.review.exception.ReviewNotFoundException;
import com.example.meetup_study.room.domain.Room;
import com.example.meetup_study.room.domain.dto.RoomDto;
import com.example.meetup_study.room.domain.repository.RoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class AdminServiceImpl implements AdminService{

    private final ReviewRepository reviewRepository;
    private final RoomRepository roomRepository;
    private final HostReviewRepository hostReviewRepository;

    @Override
    public Optional<RoomDto> deleteRoom(Long id, Long userId) {
        Optional <Room> room = roomRepository.findById(id);
        roomRepository.delete(room.get());
        Optional<RoomDto> roomDto = room.map(r -> new RoomDto().convertToRoomDto(r));

        return roomDto;
    }

    @Override
    public Optional<Review> deleteReview(Long reviewId, Long UserId) {
        Optional<Review> review = reviewRepository.findById(reviewId);
        if(review.isPresent()){
            reviewRepository.deleteById(reviewId);
            return review;
        }else{
            throw new ReviewNotFoundException();
        }
    }


    @Override
    public Optional<HostReview> deleteHostReview(Long hostReviewId, Long userId) {
        Optional<HostReview> hostReviewOpt = hostReviewRepository.findById(hostReviewId);
        if(hostReviewOpt.isPresent()){
            hostReviewRepository.deleteById(hostReviewId);
            return hostReviewOpt;
        }else{
            throw new HostReviewNotFoundException();
        }

    }

}
