package com.example.meetup_study.room.upload;

import com.example.meetup_study.room.RoomService;
import com.example.meetup_study.room.domain.Room;
import com.example.meetup_study.room.upload.domain.Upload;
import com.example.meetup_study.room.upload.domain.dto.FileDeleteStatus;
import com.example.meetup_study.room.upload.domain.dto.RequestUploadDto;
import com.example.meetup_study.room.upload.domain.dto.UploadDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.FileCopyUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.WebUtils;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@RestController
@RequestMapping("/api/upload") // /api/upload
@RequiredArgsConstructor
public class UploadController {

    private final UploadService uploadService;

    @PostMapping
    public ResponseEntity<List<UploadDto>> fileUpload(@RequestParam("files") List<MultipartFile> files, Long roomId) {


        List<UploadDto> uploadDtos = uploadService.save(files, roomId);

        return ResponseEntity.ok(uploadDtos);
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
