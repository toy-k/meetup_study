package com.example.meetup_study.hostReview;

import com.example.meetup_study.hostReview.domain.HostReview;
import com.example.meetup_study.hostReview.domain.dto.HostReviewDto;
import com.example.meetup_study.hostReview.domain.dto.RequestHostReviewDto;
import com.example.meetup_study.hostReview.domain.repository.HostReviewRepository;
import com.example.meetup_study.hostReview.exception.HostReviewInvalidRequestException;
import com.example.meetup_study.hostReview.exception.HostReviewNotFoundException;
import com.example.meetup_study.review.domain.Review;
import com.example.meetup_study.review.domain.repository.ReviewRepository;
import com.example.meetup_study.room.domain.Room;
import com.example.meetup_study.room.domain.repository.RoomRepository;
import com.example.meetup_study.room.exception.RoomNotFoundException;
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
public class HostReviewServiceImpl implements HostReviewService{

    private final HostReviewRepository hostReviewRepository;
    private final ReviewRepository reviewRepository;
    private final RoomRepository roomRepository;
    private final UserRepository userRepository;

    @Transactional
    @Override
    public Optional<HostReviewDto> createHostReview(RequestHostReviewDto requestHostReviewDto, Long userId) {
        Optional<User> userOpt = userRepository.findById(userId);
        Optional<Room> roomOpt = roomRepository.findById(requestHostReviewDto.getRoomId());
        Optional<Review> reviewOpt = reviewRepository.findById(requestHostReviewDto.getReviewId());

        if(reviewOpt.isPresent() && (reviewOpt.get().getIsHostReview() == true)){
            throw new HostReviewInvalidRequestException("이미 답글 달았습니다");
        }

        HostReview hostReview = hostReviewRepository.save(new HostReview(userOpt.get(), roomOpt.get(), requestHostReviewDto.getContent(), requestHostReviewDto.getReviewId()));

        reviewOpt.get().changeIsHostReview(true);



        HostReviewDto hostReviewDto = new HostReviewDto().convertToHostReviewDto(hostReview);

        return Optional.ofNullable(hostReviewDto);
    }

    @Override
    public List<HostReviewDto> findByRoomId(Long roomId) {
        Optional<Room> room = roomRepository.findById(roomId);
        if(room.isPresent()) {

            List<HostReview> hostReviews = hostReviewRepository.findByRoomId(roomId);

            List<HostReviewDto> hostReviewDtoList = hostReviews.stream()
                    .map(hostReview -> new HostReviewDto().convertToHostReviewDto(hostReview))
                    .collect(Collectors.toList());

            return hostReviewDtoList;

        }else{
            throw new RoomNotFoundException();
        }
    }

    @Transactional
    @Override
    public Optional<HostReviewDto> deleteHostReview(Long hostReviewId, Long userId) {

        Optional<HostReview> hostReviewOpt = hostReviewRepository.findById(hostReviewId);

        if(!hostReviewOpt.isPresent()){
            throw new HostReviewNotFoundException();
        }

        if(!hostReviewOpt.get().getUser().getId().equals(userId)){
            throw new HostReviewInvalidRequestException();
        }

        Optional<Review> reviewOpt = reviewRepository.findById(hostReviewOpt.get().getReviewId());
        if (!reviewOpt.isPresent()) {
            throw new HostReviewNotFoundException();
        }

        hostReviewRepository.deleteById(hostReviewId);
        reviewOpt.get().changeIsHostReview(false);

        HostReviewDto hostReviewDto = new HostReviewDto().convertToHostReviewDto(hostReviewOpt.get());

        return Optional.ofNullable(hostReviewDto);
    }
}
