package com.example.meetup_study.upload.roomUpload;

import com.example.meetup_study.room.domain.Room;
import com.example.meetup_study.room.domain.repository.RoomRepository;
import com.example.meetup_study.upload.roomUpload.domain.Upload;
import com.example.meetup_study.upload.FileDeleteStatus;
import com.example.meetup_study.upload.roomUpload.domain.dto.UploadDto;
import com.example.meetup_study.upload.roomUpload.domain.repository.UploadRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@RequiredArgsConstructor
@Service
public class UploadServiceImpl implements UploadService{

    private String UPLOADPATH = "/src/main/resources/upload/room/";
    private String ZIPFILENAME = "files.zip";

    private final UploadRepository uploadRepository;
    private final RoomRepository roomRepository;

    @Override
    public List<UploadDto> save(List<MultipartFile> files, Long roomId) {

        List<UploadDto> uploadDtos = new ArrayList<>();

        Optional<Room> roomOpt = roomRepository.findById(roomId);
        if(!roomOpt.isPresent()){
            throw new IllegalArgumentException("존재하지 않는 방입니다.");
        }

        for (MultipartFile file : files) {
            if (file.isEmpty()) {
                throw new IllegalArgumentException("파일이 없습니다.");
            }


            String fileName = roomId + "-" + file.getOriginalFilename();
            String filePath = System.getProperty("user.dir") + UPLOADPATH + "/" + roomOpt.get().getId() + "/";

            try {
                String fullPath = filePath + fileName;
                File storage = new File(filePath);
                if (!storage.exists()) {
                    storage.mkdirs();
                    storage.createNewFile();
                }

                file.transferTo(storage);


                UploadDto uploadDto = new UploadDto(fileName, fullPath);

                uploadDtos.add(uploadDto);
            } catch (IOException e) {
                throw new RuntimeException("파일 업로드 실패", e);
            }
        }
        List<Upload> uploads = uploadDtos.stream()
                .map(uploadDto -> new Upload(roomOpt.get(),uploadDto.getFileName(), uploadDto.getFilePath()))
                .collect(Collectors.toList());
        uploadRepository.saveAll(uploads);

        return uploadDtos;

    }

    @Override
    public void downloadZip(HttpServletResponse res, List<String> fileNames, Long roomId) {

        String zipFileName = ZIPFILENAME;
        Optional<Room> roomOpt = roomRepository.findById(roomId);
        if(!roomOpt.isPresent()){
            throw new IllegalArgumentException("존재하지 않는 방입니다.");
        }

        try {
            res.setContentType("application/octet-stream");
            res.setHeader("Content-Disposition", "attachment; filename=\"" + zipFileName + "\"");

            String filePath = System.getProperty("user.dir") + UPLOADPATH + "/" + roomOpt.get().getId() + "/";
            ZipOutputStream zipOut = new ZipOutputStream(res.getOutputStream());

            for (String fileName : fileNames) {
                File file = new File(filePath +  roomId + "-" + fileName);
                FileInputStream fis = new FileInputStream(file);

                ZipEntry zipEntry = new ZipEntry( roomId + "-" + fileName);
                zipOut.putNextEntry(zipEntry);

                byte[] bytes = new byte[1024];
                int length;
                while ((length = fis.read(bytes)) >= 0) {
                    zipOut.write(bytes, 0, length);
                }

                fis.close();
                zipOut.closeEntry();
            }

            zipOut.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Transactional
    @Override
    public FileDeleteStatus deleteByName(String fileName, Long roomId) {
        Optional<Room> roomOpt = roomRepository.findById(roomId);
        if(!roomOpt.isPresent()){
            throw new IllegalArgumentException("존재하지 않는 방입니다.");
        }

        String filePath = System.getProperty("user.dir") + UPLOADPATH + "/" + roomOpt.get().getId() + "/";
        File file = new File(filePath +  roomId + "-" + fileName);

        if (file.exists()) {
            if (file.delete()) {
                uploadRepository.deleteByFileName( roomId + "-" + fileName);
                return FileDeleteStatus.SUCCESS;
            } else {
                return FileDeleteStatus.FAIL;
            }
        } else {
            return FileDeleteStatus.NOT_FOUND;
        }
    }
}
