package com.example.meetup_study.upload.roomUpload;

import com.example.meetup_study.room.domain.Room;
import com.example.meetup_study.room.domain.repository.RoomRepository;
import com.example.meetup_study.room.exception.RoomNotFoundException;
import com.example.meetup_study.upload.exception.UploadInvalidRequestException;
import com.example.meetup_study.upload.exception.UploadNotFoundException;
import com.example.meetup_study.upload.roomUpload.domain.Upload;
import com.example.meetup_study.upload.FileDeleteStatus;
import com.example.meetup_study.upload.roomUpload.domain.dto.UploadDto;
import com.example.meetup_study.upload.roomUpload.domain.repository.UploadRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
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

    private String UPLOADPATH = "/src/main/resources/uploadFile/room/";
    private String ZIPFILENAME = "files.zip";

    private final UploadRepository uploadRepository;
    private final RoomRepository roomRepository;

    @Override
    public List<UploadDto> fileUpload(List<MultipartFile> files, Long roomId) {

        List<UploadDto> uploadDtos = new ArrayList<>();

        Optional<Room> roomOpt = roomRepository.findById(roomId);
        if(!roomOpt.isPresent()){
            throw new RoomNotFoundException();
        }

        for (MultipartFile file : files) {
            if (file.isEmpty()) {
                throw new UploadNotFoundException();
            }

            if (file.getSize() > 10 * 1024 * 1024) {
                throw new UploadInvalidRequestException("파일 크기는 10MB를 초과할 수 없습니다.");
            }

            String fileName = roomId + "-" + file.getOriginalFilename();
            String folderPath = System.getProperty("user.dir") + UPLOADPATH + "/" + roomOpt.get().getId() + "/";

            try {

                File folder = new File(folderPath);
                boolean folderCreated = folder.mkdirs();

                if(folderCreated){
                    File newFile = new File(folder, fileName);
                    boolean fileCreated= newFile.createNewFile();

                    if(fileCreated){
                        System.out.println("파일 생성 성공");
                        file.transferTo(newFile);
                    }else{
                        System.out.println("파일 생성 실패");
                    }

                }else{
                    System.out.println("폴더 생성 실패");
                }


                UploadDto uploadDto = new UploadDto(fileName, folderPath+fileName);

                uploadDtos.add(uploadDto);
            } catch (IOException e) {
                throw new UploadInvalidRequestException("파일 업로드 실패");
            }
        }
        List<Upload> uploads = uploadDtos.stream()
                .map(uploadDto -> new Upload(roomOpt.get(),uploadDto.getFileName(), uploadDto.getFilePath()))
                .collect(Collectors.toList());
        uploadRepository.saveAll(uploads);

        return uploadDtos;

    }

    @Override
    public Optional<UploadDto> findByRoomId(Long roomId) {

        Optional<Room> roomOpt = roomRepository.findById(roomId);
        if(!roomOpt.isPresent()){
            throw new RoomNotFoundException();
        }

        Optional<Upload> uploadOpt = uploadRepository.findByRoomId(roomId);
        if(!uploadOpt.isPresent()){
            return Optional.empty();
        }

        UploadDto uploadDto = new UploadDto(uploadOpt.get().getFileName(), uploadOpt.get().getFilePath());

        return Optional.of(uploadDto);
    }

    @Override
    public void downloadZip(HttpServletResponse res, List<String> fileNames, Long roomId) {


        String zipFileName = ZIPFILENAME;
        Optional<Room> roomOpt = roomRepository.findById(roomId);
        if(!roomOpt.isPresent()){
            throw new RoomNotFoundException();
        }

        try {
            res.setContentType("application/octet-stream");
            res.setHeader("Content-Disposition", "attachment; filename=\"" + zipFileName + "\"");

            String filePath = System.getProperty("user.dir") + UPLOADPATH + "/" + roomOpt.get().getId() + "/";
            ZipOutputStream zipOut = new ZipOutputStream(res.getOutputStream());

            for (String fileName : fileNames) {

                Optional<Upload> uploadOpt = uploadRepository.findByFileName(fileName);
                if(!uploadOpt.isPresent()){
                    throw new UploadInvalidRequestException("존재하지 않는 파일입니다.");
                }

                File file = new File(filePath +  fileName);

                if (!file.exists()) {
                    throw new UploadInvalidRequestException("존재하지 않는 파일입니다. (STORAGE)");
                }

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
            throw new RoomNotFoundException();
        }

        Optional<Upload> uploadOpt = uploadRepository.findByFileName(fileName);
        if(!uploadOpt.isPresent()){
            throw new UploadNotFoundException();
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

    @Override
    public void fileCleanUp(){
        String filePath = System.getProperty("user.dir") + UPLOADPATH;
        File uploadDirectory = new File(filePath);

        if (uploadDirectory.exists() && uploadDirectory.isDirectory()) {
            deleteDirectory(uploadDirectory);
        }

    }
    private void deleteDirectory(File directory) {
        File[] files = directory.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isDirectory()) {
                    deleteDirectory(file);
                } else {
                    file.delete();
                }
            }
        }

        // Delete the empty directory
        directory.delete();
    }
}
