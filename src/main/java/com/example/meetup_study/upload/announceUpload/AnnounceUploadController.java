package com.example.meetup_study.upload.announceUpload;

import com.example.meetup_study.announce.AnnounceService;
import com.example.meetup_study.announce.domain.Announce;
import com.example.meetup_study.announce.domain.dto.AnnounceDto;
import com.example.meetup_study.announce.exception.AnnounceNotFoundException;
import com.example.meetup_study.upload.FileDeleteStatus;
import com.example.meetup_study.upload.announceUpload.domain.AnnounceUpload;
import com.example.meetup_study.upload.announceUpload.domain.dto.AnnounceUploadDto;
import com.example.meetup_study.upload.announceUpload.domain.dto.RequestAnnounceUploadDto;
import com.example.meetup_study.upload.announceUpload.domain.dto.RequestDeleteAnnounceUploadDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/announceUpload")
@RequiredArgsConstructor
public class AnnounceUploadController {
    private final AnnounceUploadService announceUploadService;

    @PostMapping
    public ResponseEntity<List<AnnounceUploadDto>> fileUpload(@RequestParam("files") List<MultipartFile> files, @Valid @RequestBody RequestAnnounceUploadDto requestAnnounceUploadDto) {

        List<AnnounceUploadDto> announceUploadDtos = announceUploadService.fileUpload(files, requestAnnounceUploadDto.getAnnounceId());

        return ResponseEntity.ok(announceUploadDtos);
    }

    @GetMapping("/announceId/{announceId}")
    public ResponseEntity<AnnounceUploadDto> findFiles(@PathVariable("announceId") Long announceId) {

        Optional<AnnounceUploadDto> announceUploadDtoOpt = announceUploadService.findByAnnounceId(announceId);

        return ResponseEntity.ok(announceUploadDtoOpt.get());
    }

    @GetMapping("/download/announceId/{announceId}")
    public void downloadZip(HttpServletResponse res, @RequestParam("files") List<String> fileNames, @PathVariable("announceId") Long announceId) {

        announceUploadService.downloadZip(res, fileNames, announceId);
    }

    @DeleteMapping
    public ResponseEntity<String> deleteFile(@Valid @RequestBody RequestDeleteAnnounceUploadDto requestDeleteAnnounceUploadDto) {

        FileDeleteStatus status = announceUploadService.deleteByName(requestDeleteAnnounceUploadDto.getFileName(), requestDeleteAnnounceUploadDto.getAnnounceId());

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
