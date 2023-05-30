package com.example.meetup_study.common.scheduler;

import com.example.meetup_study.room.RoomService;
import com.example.meetup_study.room.domain.Room;
import com.example.meetup_study.room.domain.dto.RoomDto;
import com.example.meetup_study.room.domain.repository.RoomRepository;
import com.example.meetup_study.user.domain.User;
import com.example.meetup_study.user.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Random;

@Configuration
@EnableScheduling
@RequiredArgsConstructor
@Slf4j
public class EventEmailScheduler {

    private final UserRepository userRepository;
    private final RoomService roomService;
    private final JavaMailSender javaMailSender;
    private final RoomRepository roomRepository;

    private static final int MAX_RETRIES = 10;
    private int retryCount = 0;

    //    @Scheduled(fixedDelay = 1000) // 1초 마다 실행
@Scheduled(cron = "0 1 9 * * *") // Execute at 9:01 AM every day
    public void fixedDelayTask() {

        boolean success = false;
        while (!success && retryCount < MAX_RETRIES) {
            try {
                retryCount = 0;
                SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
                Long roomId = getRandomRoomId();

                Optional<Room> room = roomRepository.findById(roomId);
                if(!room.isPresent()){
                    throw new IllegalArgumentException("이벤트 알림 룸이 존재하지 않습니다");
                }

                List<User> users = userRepository.findAll();
                String [] receiveList = new String[users.size()];
                for(int i=0; i<users.size(); i++){
                    receiveList[i] = users.get(i).getEmail();
                }
                simpleMailMessage.setTo(receiveList);

                simpleMailMessage.setSubject("Meetup Event");
                simpleMailMessage.setText("" +
                        "\n Meetup Event : " + room.get().getTitle()
                        + " \n MeetupStartDate : " + room.get().getMeetupStartDate()
                        + " \n MeetupEndDate : " + room.get().getMeetupEndDate()
                        + " \n location : " + room.get().getLocation()
                        + " \n MeetupCategory : " + room.get().getCategory()
                );
                javaMailSender.send(simpleMailMessage);

                log.info("Meetup Event AlARM: " + new Date(System.currentTimeMillis()));

                success = true;
            } catch (Exception e) {
                retryCount++;
                try {
                    Thread.sleep(5000); // Wait for 5 seconds before retrying
                } catch (InterruptedException ex) {
                    Thread.currentThread().interrupt();
                }
            }
        }

    }

    private Long getRandomRoomId() {

        Long count = 0L;
        Long roomId = 0L;

        while (count < 1L) {
            Random random = new Random();

            List<RoomDto> roomDtos = roomService.getRoomListAfterMeetupStart();
            Long roomCnt = Long.valueOf(roomDtos.size());


            Long randomId = Math.abs(random.nextLong() % roomCnt);

            RoomDto randomRoomDto = roomDtos.get(randomId.intValue());

            if (randomRoomDto != null) {
                count = count + 1L;
                roomId = randomRoomDto.getId();
                break;
            }
        }

        return roomId;
    }

}