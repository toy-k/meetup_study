package com.example.meetup_study.upload.announceUpload;

import com.example.meetup_study.upload.FileDeleteStatus;
import com.example.meetup_study.upload.announceUpload.domain.dto.AnnounceUploadDto;
import com.example.meetup_study.upload.announceUpload.domain.dto.RequestAnnounceUploadDto;
import com.example.meetup_study.upload.announceUpload.domain.dto.RequestDeleteAnnounceUploadDto;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
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

    @ApiOperation(value = "파일 업로드", notes = "파일을 업로드합니다.")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "files", value = "파일", dataTypeClass = MultipartFile.class, required = true, paramType = "form"),
            @ApiImplicitParam(name = "body", value = "Request Body", dataTypeClass = RequestAnnounceUploadDto.class, required = true, paramType = "body")
    })
    @PostMapping
    public ResponseEntity<List<AnnounceUploadDto>> fileUpload(@RequestParam("files") List<MultipartFile> files, @Valid @RequestBody RequestAnnounceUploadDto requestAnnounceUploadDto) {

        List<AnnounceUploadDto> announceUploadDtos = announceUploadService.fileUpload(files, requestAnnounceUploadDto.getAnnounceId());

        return ResponseEntity.ok(announceUploadDtos);
    }

    @ApiOperation(value = "파일 조회", notes = "파일을 조회합니다.")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "announceId", value = "공지사항 아이디", dataTypeClass = Long.class, required = true, paramType = "path")
    })
    @GetMapping("/announceId/{announceId}")
    public ResponseEntity<AnnounceUploadDto> findFiles(@PathVariable("announceId") Long announceId) {

        Optional<AnnounceUploadDto> announceUploadDtoOpt = announceUploadService.findByAnnounceId(announceId);

        if(announceUploadDtoOpt.isPresent()){
            return ResponseEntity.ok(announceUploadDtoOpt.get());
        }else {
            return ResponseEntity.ok(null);
        }
    }

    @ApiOperation(value = "파일 다운로드", notes = "파일을 다운로드합니다.")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "files", value = "파일 이름", dataTypeClass = String.class, required = true, paramType = "query"),
            @ApiImplicitParam(name = "announceId", value = "공지사항 아이디", dataTypeClass = Long.class, required = true, paramType = "path")
    })
    @GetMapping("/download/announceId/{announceId}")
    public void downloadZip(HttpServletResponse res, @RequestParam("files") List<String> fileNames, @PathVariable("announceId") Long announceId) {

        announceUploadService.downloadZip(res, fileNames, announceId);
    }

    @ApiOperation(value = "파일 삭제", notes = "파일을 삭제합니다.")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "body", value = "Request Body", dataTypeClass = RequestDeleteAnnounceUploadDto.class, required = true, paramType = "body")
    })
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
