package com.example.meetup_study.hostReview;

import com.example.meetup_study.hostReview.domain.HostReview;
import com.example.meetup_study.hostReview.domain.dto.RequestHostReviewDto;
import com.example.meetup_study.hostReview.domain.repository.HostReviewRepository;
import com.example.meetup_study.review.domain.Review;
import com.example.meetup_study.review.domain.repository.ReviewRepository;
import com.example.meetup_study.room.domain.Room;
import com.example.meetup_study.room.domain.repository.RoomRepository;
import com.example.meetup_study.user.domain.User;
import com.example.meetup_study.user.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class HostReviewServiceImpl implements HostReviewService{

    private final HostReviewRepository hostReviewRepository;
    private final ReviewRepository reviewRepository;
    private final RoomRepository roomRepository;
    private final UserRepository userRepository;

    @Override
    public Optional<HostReview> createHostReview(RequestHostReviewDto requestHostReviewDto, Long userId) {
        Optional<User> userOpt = userRepository.findById(userId);
        Optional<Room> roomOpt = roomRepository.findById(requestHostReviewDto.getRoomId());
        Optional<Review> reviewOpt = reviewRepository.findById(requestHostReviewDto.getReviewId());
        if(!userOpt.isPresent() || !roomOpt.isPresent() || !reviewOpt.isPresent()){
            throw new IllegalArgumentException("User 또는 Room 또는 Review가 없습니다.");
        }
        HostReview hostReview = hostReviewRepository.save(new HostReview(userOpt.get(), roomOpt.get(), requestHostReviewDto.getContent(), requestHostReviewDto.getReviewId()));
        return Optional.ofNullable(hostReview);
    }

    @Override
    public Optional<HostReview> deleteHostReview(Long hostReviewId, Long userId) {
        Optional<HostReview> hostReviewOpt = hostReviewRepository.findById(hostReviewId);
        if(hostReviewOpt.isPresent() && hostReviewOpt.get().getUser().getId().equals(userId)){
            hostReviewRepository.deleteById(hostReviewId);
            return hostReviewOpt;
        }else{
            throw new IllegalArgumentException("HostReview가 없거나, 해당 User가 작성한 HostReview가 아닙니다.");
        }

    }
}
