package com.example.meetup_study.room.upload;

import com.example.meetup_study.room.RoomService;
import com.example.meetup_study.room.domain.Room;
import com.example.meetup_study.room.upload.domain.Upload;
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

@RestController
@RequestMapping("/test") // /api/upload
@RequiredArgsConstructor
public class UploadController {

    private String UPLOADPATH = "/src/main/resources/upload/";

    @PostMapping
    public ResponseEntity<List<RequestUploadDto>> fileUpload(HttpServletRequest req, @RequestParam("files") List<MultipartFile> files) {


        List<RequestUploadDto> uploadDtos = new ArrayList<>();

        for (MultipartFile file : files) {
            if (file.isEmpty()) {
                throw new IllegalArgumentException("파일이 없습니다.");
            }

            String fileName = file.getOriginalFilename();
            String filePath = System.getProperty("user.dir") + UPLOADPATH;

            try {
                // 파일 저장 로직
                String fullPath = filePath + fileName;
                File storage = new File(filePath);
                if (!storage.exists()) {
                    storage.mkdirs();
                }

                File newFile = new File(fullPath);
                if (!newFile.exists()) {
                    newFile.createNewFile();
                }

                file.transferTo(newFile);

                // RequestUploadDto 객체 생성
                RequestUploadDto requestUploadDto = new RequestUploadDto(fileName, fullPath);
                uploadDtos.add(requestUploadDto);
            } catch (IOException e) {
                throw new RuntimeException("파일 업로드 실패", e);
            }
        }

        return ResponseEntity.ok(uploadDtos);
    }
    @GetMapping
    public byte[] fileDownload(HttpServletRequest req, HttpServletResponse res, @RequestParam("file") String fileName){
        byte[] down = null;

        try {
            String filePath = System.getProperty("user.dir") + UPLOADPATH;
            File file = new File(filePath + fileName);

            down = FileCopyUtils.copyToByteArray(file);



            String filename = new String(file.getName().getBytes("UTF-8"), "ISO-8859-1");

            res.setHeader("Content-Disposition", "attachment; filename=\"" + filename + "\"");

            res.setContentLength(down.length);;

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return down;
    }

    @DeleteMapping
    public ResponseEntity<String> deleteFile(@RequestParam("file") String fileName) {
        String filePath = System.getProperty("user.dir") + UPLOADPATH;
        File file = new File(filePath + fileName);

        if (file.exists()) {
            if (file.delete()) {
                return ResponseEntity.ok("파일 삭제 성공");
            } else {
                return ResponseEntity.status(500).body("파일 삭제 실패");
            }
        } else {
            return ResponseEntity.status(404).body("파일을 찾을 수 없습니다.");
        }
    }



}
