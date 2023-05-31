package com.example.meetup_study.upload.announceUpload;

import com.example.meetup_study.upload.FileDeleteStatus;
import com.example.meetup_study.upload.announceUpload.domain.AnnounceUpload;
import com.example.meetup_study.upload.announceUpload.domain.dto.AnnounceUploadDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

@RestController
@RequestMapping("/api/announceUpload")
@RequiredArgsConstructor
public class AnnounceUploadController {
    private final AnnounceUploadService announceUploadService;

    @PostMapping
    public ResponseEntity<List<AnnounceUploadDto>> fileUpload(@RequestParam("files") List<MultipartFile> files, Long roomId) {

        List<AnnounceUploadDto> announceUploadDtos = announceUploadService.save(files, roomId);

        return ResponseEntity.ok(announceUploadDtos);
    }

    @GetMapping("/download")
    public void downloadZip(HttpServletResponse res, @RequestParam("files") List<String> fileNames, Long roomId) {

        announceUploadService.downloadZip(res, fileNames, roomId);
    }

    @DeleteMapping
    public ResponseEntity<String> deleteFile(@RequestParam("file") String fileName, Long roomId) {

        FileDeleteStatus status = announceUploadService.deleteByName(fileName, roomId);

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
