package com.example.meetup_study.upload.announceUpload;

import com.example.meetup_study.announce.domain.Announce;
import com.example.meetup_study.announce.domain.dto.AnnounceDto;
import com.example.meetup_study.announce.domain.repository.AnnounceRepository;
import com.example.meetup_study.announce.exception.AnnounceNotFoundException;
import com.example.meetup_study.upload.FileDeleteStatus;
import com.example.meetup_study.upload.announceUpload.domain.AnnounceUpload;
import com.example.meetup_study.upload.announceUpload.domain.dto.AnnounceUploadDto;
import com.example.meetup_study.upload.announceUpload.domain.repository.AnnounceUploadRepository;
import com.example.meetup_study.upload.exception.UploadInvalidRequestException;
import com.example.meetup_study.upload.exception.UploadNotFoundException;
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

@Service
@RequiredArgsConstructor
public class AnnounceUploadServiceImpl implements AnnounceUploadService{

    private String UPLOADPATH = "/src/main/resources/uploadFile/announce/";
    private String ZIPFILENAME = "files.zip";
    private final AnnounceUploadRepository announceUploadRepository;
    private final AnnounceRepository announceRepository;

    @Override
    public List<AnnounceUploadDto> fileUpload(List<MultipartFile> files, Long announceId) {


        List<AnnounceUploadDto> announceUploadDtos = new ArrayList<>();

        Optional<Announce> announceOpt = announceRepository.findById(announceId);
        if(!announceOpt.isPresent()){
            throw new AnnounceNotFoundException();
        }

        for (MultipartFile file : files) {
            if (file.isEmpty()) {
                throw new UploadNotFoundException();
            }

            if (file.getSize() > 10 * 1024 * 1024) {
                throw new UploadInvalidRequestException("파일 크기는 10MB를 초과할 수 없습니다.");
            }

            String fileName = announceId + "-" + file.getOriginalFilename();
            String folderPath = System.getProperty("user.dir") + UPLOADPATH + "/" + announceOpt.get().getId() + "/";

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


                AnnounceUploadDto announceUploadDto = new AnnounceUploadDto(fileName, folderPath+fileName);

                announceUploadDtos.add(announceUploadDto);


            } catch (IOException e) {
                throw new UploadInvalidRequestException("파일 업로드 실패");
            }

        }
        List<AnnounceUpload> announceUploads = announceUploadDtos.stream().map(AnnounceUploadDto -> new AnnounceUpload(announceOpt.get(), AnnounceUploadDto.getFileName(), AnnounceUploadDto.getFilePath())).collect(Collectors.toList());

        announceUploadRepository.saveAll(announceUploads);

        return announceUploadDtos;
    }

    @Override
    public Optional<AnnounceUploadDto> findByAnnounceId(Long announceId) {

        Optional<AnnounceUpload> announceUploadOpt = announceUploadRepository.findByAnnounceId(announceId);
        if(!announceUploadOpt.isPresent()){
            throw new UploadNotFoundException();
        }

        AnnounceUploadDto announceUploadDto = new AnnounceUploadDto(announceUploadOpt.get().getFileName(), announceUploadOpt.get().getFilePath());

        return Optional.ofNullable(announceUploadDto);
    }

    @Override
    public void downloadZip(HttpServletResponse res, List<String> fileNames, Long announceId) {


        String zipFileName = ZIPFILENAME;

        Optional<Announce> announceOpt = announceRepository.findById(announceId);
        if(!announceOpt.isPresent()){
            throw new AnnounceNotFoundException();
        }

        try {
            res.setContentType("application/octet-stream");
            res.setHeader("Content-Disposition", "attachment; filename=\"" + zipFileName + "\"");

            String filePath = System.getProperty("user.dir") + UPLOADPATH + "/" + announceOpt.get().getId() + "/";
            ZipOutputStream zipOut = new ZipOutputStream(res.getOutputStream());

            for (String fileName : fileNames) {

                Optional<AnnounceUpload> announceUploadOpt = announceUploadRepository.findByFileName(fileName);
                if(!announceUploadOpt.isPresent()){
                    throw new UploadInvalidRequestException("존재하지 않는 파일입니다. (DB)");
                }

                File file = new File(filePath + announceId + "-" + fileName);

                if (!file.exists()) {
                   throw new UploadInvalidRequestException("존재하지 않는 파일입니다. (STORAGE)");
                }

                FileInputStream fis = new FileInputStream(file);

                ZipEntry zipEntry = new ZipEntry( announceId + "-" + fileName);
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
    public FileDeleteStatus deleteByName(String fileName, Long announceId) {

        Optional<Announce> announceOpt = announceRepository.findById(announceId);
        if(!announceOpt.isPresent()){
            throw new AnnounceNotFoundException();
        }

        Optional<AnnounceUpload> announceUploadOpt = announceUploadRepository.findByFileName(fileName);
        if(!announceUploadOpt.isPresent()){
            throw new UploadNotFoundException();
        }


        String filePath = System.getProperty("user.dir") + UPLOADPATH + "/" + announceOpt.get().getId() + "/";
        File file = new File(filePath +  announceId + "-" + fileName);

        if (file.exists()) {
            if (file.delete()) {
                announceUploadRepository.deleteByFileName( announceId + "-" + fileName);
                return FileDeleteStatus.SUCCESS;
            } else {
                return FileDeleteStatus.FAIL;
            }
        } else {
            return FileDeleteStatus.NOT_FOUND;
        }
    }
}
