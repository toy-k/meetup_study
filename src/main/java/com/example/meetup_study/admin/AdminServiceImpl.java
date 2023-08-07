package com.example.meetup_study.admin;

import com.example.meetup_study.common.aop.Timer;
import com.example.meetup_study.hostReview.domain.HostReview;
import com.example.meetup_study.hostReview.domain.dto.HostReviewDto;
import com.example.meetup_study.hostReview.domain.repository.HostReviewRepository;
import com.example.meetup_study.hostReview.exception.HostReviewNotFoundException;
import com.example.meetup_study.mapper.RoomMapper;
import com.example.meetup_study.review.domain.Review;
import com.example.meetup_study.review.domain.dto.ReviewDto;
import com.example.meetup_study.review.domain.repository.ReviewRepository;
import com.example.meetup_study.review.exception.ReviewInvalidRequestException;
import com.example.meetup_study.review.exception.ReviewNotFoundException;
import com.example.meetup_study.room.domain.Room;
import com.example.meetup_study.room.domain.dto.RoomDto;
import com.example.meetup_study.room.domain.repository.RoomRepository;
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


    @Override
    public Optional<RoomDto> deleteRoom(Long id, Long userId) {
        Optional <Room> room = roomRepository.findById(id);

        roomRepository.delete(room.get());
        Optional<RoomDto> roomDto = room.map(r -> roomMapper.toRoomDto(r));

        return roomDto;
    }

    @Override
    public Optional<ReviewDto> deleteReview(Long reviewId, Long UserId) {
        Optional<Review> review = reviewRepository.findById(reviewId);
        if(review.isPresent()){

            if(review.get().getIsHostReview()){
                throw new ReviewInvalidRequestException("이미 호스트가 리뷰 남겨서 삭제할 수 없습니다.");
            }

            reviewRepository.deleteById(reviewId);

            ReviewDto reviewDto = new ReviewDto().convertToReviewDto(review.get());
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

            HostReviewDto hostReviewDto = new HostReviewDto().convertToHostReviewDto(hostReviewOpt.get());

            return Optional.of(hostReviewDto);

        }else{
            throw new HostReviewNotFoundException();
        }

    }

}
