package com.example.meetup_study.upload.roomUpload;

import com.example.meetup_study.room.RoomService;
import com.example.meetup_study.room.domain.Room;
import com.example.meetup_study.room.exception.RoomNotFoundException;
import com.example.meetup_study.upload.FileDeleteStatus;
import com.example.meetup_study.upload.roomUpload.domain.dto.RequestDeleteUploadDto;
import com.example.meetup_study.upload.roomUpload.domain.dto.RequestUploadDto;
import com.example.meetup_study.upload.roomUpload.domain.dto.UploadDto;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.*;

@RestController
@RequestMapping("/api/upload") // /api/upload
@RequiredArgsConstructor
public class UploadController {

    private final UploadService uploadService;

    @ApiOperation(value = "파일 업로드", notes = "파일을 업로드합니다.")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "files", value = "파일", dataTypeClass = MultipartFile.class, required = true, paramType = "form"),
            @ApiImplicitParam(name = "body", value = "Request Body", dataTypeClass = RequestUploadDto.class, required = true, paramType = "body")
    })
    @PostMapping()
    public ResponseEntity<List<UploadDto>> fileUpload(@RequestParam("files") List<MultipartFile> files,@RequestParam("roomId") Long roomId) {


        List<UploadDto> uploadDtos = uploadService.fileUpload(files, roomId);

        return ResponseEntity.ok(uploadDtos);
    }


    @ApiOperation(value = "파일 조회", notes = "파일을 조회합니다.")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "roomId", value = "방 아이디", dataTypeClass = Long.class, required = true, paramType = "path")
    })
    @GetMapping("/roomId/{roomId}")
    public ResponseEntity<UploadDto> findFiles(@PathVariable("roomId") Long roomId) {

        Optional<UploadDto> uploadDtoOpt = uploadService.findByRoomId(roomId);

        return ResponseEntity.ok(uploadDtoOpt.get());
    }

    @ApiOperation(value = "파일 다운로드", notes = "파일을 다운로드합니다.")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "files", value = "파일 이름", dataTypeClass = String.class, required = true, paramType = "query"),
            @ApiImplicitParam(name = "roomId", value = "방 아이디", dataTypeClass = Long.class, required = true, paramType = "path")
    })
    @GetMapping("/download/roomId/{roomId}")
    public void downloadZip(HttpServletResponse res, @RequestParam("files") List<String> fileNames, @PathVariable("roomId") Long roomId) {

        uploadService.downloadZip(res, fileNames, roomId);
    }

    @ApiOperation(value = "파일 삭제", notes = "파일을 삭제합니다.")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "body", value = "Request Body", dataTypeClass = RequestDeleteUploadDto.class, required = true, paramType = "body")
    })
    @DeleteMapping
    public ResponseEntity<String> deleteFile(@Valid @RequestBody RequestDeleteUploadDto requestDeleteUploadDto) {


        FileDeleteStatus status = uploadService.deleteByName(requestDeleteUploadDto.getFileName(), requestDeleteUploadDto.getRoomId());

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

    @PostConstruct
    public void fileCleanUp() {

        uploadService.fileCleanUp();
    }



}
