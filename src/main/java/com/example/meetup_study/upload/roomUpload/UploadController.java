package com.example.meetup_study.upload.roomUpload;

import com.example.meetup_study.room.RoomService;
import com.example.meetup_study.room.domain.Room;
import com.example.meetup_study.upload.FileDeleteStatus;
import com.example.meetup_study.upload.roomUpload.domain.dto.UploadDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.*;

@RestController
@RequestMapping("/api/upload") // /api/upload
@RequiredArgsConstructor
public class UploadController {

    private final UploadService uploadService;
    private final RoomService roomService;

    @PostMapping
    public ResponseEntity<List<UploadDto>> fileUpload(@RequestParam("files") List<MultipartFile> files, Long roomId) {


        List<UploadDto> uploadDtos = uploadService.save(files, roomId);

        return ResponseEntity.ok(uploadDtos);
    }


    @GetMapping
    public ResponseEntity<UploadDto> findFiles(Long roomId) {

        Optional<Room> roomOpt = roomService.getRoom(roomId);
        if (roomOpt.isEmpty()) {
            throw new IllegalArgumentException("존재하지 않는 스터디룸입니다.");
        }

        Optional<UploadDto> uploadDtoOpt = uploadService.findByRoomId(roomId);

        return ResponseEntity.ok(uploadDtoOpt.get());
    }

    @GetMapping("/download")
    public void downloadZip(HttpServletResponse res, @RequestParam("files") List<String> fileNames, Long roomId) {

        uploadService.downloadZip(res, fileNames, roomId);
    }

    @DeleteMapping
    public ResponseEntity<String> deleteFile(@RequestParam("file") String fileName, Long roomId) {


        FileDeleteStatus status = uploadService.deleteByName(fileName, roomId);

        if (status != FileDeleteStatus.NOT_FOUND) {
            if (status == FileDeleteStatus.SUCCESS) {
                return ResponseEntity.ok("파일 삭제 성공");
            } else {
                return ResponseEntity.status(500).body("파일 삭제 실패");
            }
        } else {
            return ResponseEntity.status(404).body("파일을 찾을 수 없습니다.");
        }
    }



}
