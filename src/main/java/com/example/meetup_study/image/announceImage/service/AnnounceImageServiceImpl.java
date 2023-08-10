package com.example.meetup_study.image.announceImage.service;

import com.example.meetup_study.announce.service.AnnounceService;
import com.example.meetup_study.announce.domain.Announce;
import com.example.meetup_study.announce.domain.dto.AnnounceDto;
import com.example.meetup_study.announce.domain.repository.AnnounceRepository;
import com.example.meetup_study.image.announceImage.domain.AnnounceImage;
import com.example.meetup_study.image.announceImage.domain.dto.AnnounceImageDto;
import com.example.meetup_study.image.announceImage.domain.repository.AnnounceImageRepository;
import com.example.meetup_study.image.exception.ImageInvalidRequestException;
import com.example.meetup_study.image.exception.ImageNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AnnounceImageServiceImpl implements AnnounceImageService{


    private final AnnounceImageRepository announceImageRepository;
    private final AnnounceService announceService;
    private final AnnounceRepository announceRepository;

    @Override
    public Optional<AnnounceImageDto> updateAnnounceImage(MultipartFile file, Long announceId) {
        try{

            if (file.isEmpty()) {
                throw new ImageNotFoundException();
            }



            Optional<Announce> announceOpt = announceRepository.findById(announceId);

            if (!announceOpt.isPresent()) throw new ImageNotFoundException();

            AnnounceImage announceImage = announceOpt.get().getAnnounceImage();

            String fileExtension = getFileExtension(file.getOriginalFilename());
            byte[] data = compressFile(file.getBytes(), fileExtension);

            AnnounceImageDto announceImageDtoOpt;

            if(announceImage == null){
                AnnounceImage announceImageInst = new AnnounceImage(data);

                AnnounceImage newAnnounceImage = announceImageRepository.save(announceImageInst);

                announceOpt.get().changeAnnounceImage(newAnnounceImage);

                announceImageDtoOpt = new AnnounceImageDto(newAnnounceImage.getProfile());


            }else{
                Optional<AnnounceImage> announceImageOpt = announceImageRepository.findById(announceImage.getId());

                if (!announceImageOpt.isPresent()) {
                    throw new ImageNotFoundException();
                }

                announceImageOpt.get().changeProfile(data);
                announceImageRepository.save(announceImageOpt.get());

                announceImageDtoOpt = new AnnounceImageDto(announceImage.getProfile());


            }

            return Optional.of(announceImageDtoOpt);


        }catch (IOException e){
            throw new ImageInvalidRequestException();
        }    }

    @Override
    public Optional<AnnounceImageDto> getAnnounceImage(Long announceId) {

        Optional<AnnounceDto> announceDtoOpt = announceService.getAnnounce(announceId);

        if(!announceDtoOpt.isPresent()){
            throw new ImageNotFoundException();
        }

        Optional<Announce> announceOpt = announceRepository.findById(announceId);

        AnnounceImage announceImage = announceOpt.get().getAnnounceImage();

        AnnounceImageDto announceImageDto = new AnnounceImageDto(announceImage.getProfile());

        return Optional.of(announceImageDto);       }

    @Override
    public Optional<AnnounceImageDto> deleteAnnounceImage(Long announceId) {


        Optional<Announce> announceOpt = announceRepository.findById(announceId);

        if(!announceOpt.isPresent()){
            throw new ImageNotFoundException();
        }

        AnnounceImage announceImage = announceOpt.get().getAnnounceImage();


        announceOpt.get().getAnnounceImage().changeProfile(null);

        announceImageRepository.save(announceImage);

        AnnounceImageDto announceImageDto = new AnnounceImageDto(announceImage.getProfile());

        return Optional.of(announceImageDto);
    }

    private byte[] compressFile(byte[] data, String fileExtension) throws IOException {

        if (fileExtension.equalsIgnoreCase("jpeg") || fileExtension.equalsIgnoreCase("jpg")) {

            BufferedImage image = ImageIO.read(new ByteArrayInputStream(data));
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            ImageIO.write(image, "jpeg", outputStream);
            return outputStream.toByteArray();
        } else if (fileExtension.equalsIgnoreCase("png")) {

            BufferedImage image = ImageIO.read(new ByteArrayInputStream(data));
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            ImageIO.write(image, "png", outputStream);
            return outputStream.toByteArray();
        } else {

            throw new ImageInvalidRequestException("유저 이미지 업로드 요청 파일 확장자가 잘못되었습니다.");
        }
    }
    private String getFileExtension(String fileName) {
        if (fileName.lastIndexOf(".") != -1 && fileName.lastIndexOf(".") != 0) {
            return fileName.substring(fileName.lastIndexOf(".") + 1);
        } else {
            return "";
        }
    }
}
