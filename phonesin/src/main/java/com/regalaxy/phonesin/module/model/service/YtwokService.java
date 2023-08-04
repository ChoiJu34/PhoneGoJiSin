package com.regalaxy.phonesin.module.model.service;

import com.regalaxy.phonesin.module.model.YtwokDto;
import com.regalaxy.phonesin.module.model.entity.Ytwok;
import com.regalaxy.phonesin.module.model.repository.YtwokRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.UriUtils;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

@Service
@RequiredArgsConstructor
public class YtwokService {

    private final YtwokRepository ytwokRepository;

    public YtwokDto saveImage(MultipartFile file) throws Exception {
        String contentType = file.getContentType();
        String extension;
        //파일의 Content Type 이 있을 경우 Content Type 기준으로 확장자 확인
        if (StringUtils.hasText(contentType)) {
            if (contentType.equals("image/jpg")) {
                extension = "image/jpg";
            } else if (contentType.equals("image/png")) {
                extension = "image/png";
            } else if (contentType.equals("image/jpeg")) {
                extension = "image/jpeg";
            }else if (contentType.equals("image/*")) {
                extension = "image/*";
            }
            else{
                // contentType 존재하지 않는 경우 처리
                throw new Exception("사진 파일이 아닙니다.");
            }
            String uploadPath = new File("").getAbsolutePath() + "\\" + "images/y2k";
            Ytwok resultEntity = null;
            // 파일이 비었는지 검증
            if (!file.isEmpty()) {

                String saveFolder = uploadPath + File.separator;

                File folder = new File(saveFolder);

                // 폴더가 존재하는지 검증
                if (!folder.exists())
                    folder.mkdirs();

                String originalFileName = file.getOriginalFilename();
                String saveFileName = originalFileName;

                // file name 중복경우 예외처리
                if (!originalFileName.isEmpty()) {
                    saveFileName = UUID.randomUUID().toString()
                            + originalFileName.substring(originalFileName.lastIndexOf('.'));
                    file.transferTo(new File(folder, saveFileName));
                }

                // Entity build
                Ytwok ytwok = Ytwok.builder()
                        .originalFile(originalFileName)
                        .saveFile(saveFileName)
                        .build();

                resultEntity = ytwokRepository.saveAndFlush(ytwok);

            }
            return YtwokDto.builder()
                    .ytwokId(resultEntity.getYtwokId())
                    .saveFile(resultEntity.getSaveFile())
                    .originalFile(resultEntity.getOriginalFile())
                    .contentType(extension)
                    .build();
        }
        else{
            // contentType 존재하지 않는 경우 처리
            throw new Exception("유효한 확장자를 가진 파일이(가) 아닙니다.");
        }
    }

    public ResponseEntity<Resource> loadImage(long fileId) throws Exception {
        Ytwok file = ytwokRepository.findById(fileId).orElse(null);

        // file not found
        if (file == null) return null;

        String SaveFileName = file.getSaveFile();
        String uploadFileName = file.getOriginalFile();

        // 한글 인코딩
        String encodedUploadFileName = UriUtils.encode(uploadFileName, StandardCharsets.UTF_8);
        String contentDisposition = "attachment; filename=\"" + encodedUploadFileName + "\"";

        String absolutePath = new File("").getAbsolutePath() + "\\" + "images/y2k/" + SaveFileName;

        Resource resource = new UrlResource("file:" + absolutePath);

//        Path path = Paths.get(absolutePath);
//        Resource resource2 = new InputStreamResource(Files.newInputStream(path)); // 파일 resource 얻기
//
//        File file2 = new File(absolutePath);
//        HttpHeaders headers = new HttpHeaders();
//        headers.setContentDisposition(ContentDisposition.builder("attachment").filename(file2.getName()).build());  // 다운로드 되거나 로컬에 저장되는 용도로 쓰이는지를 알려주는 헤더
//
//        return new ResponseEntity<Object>(resource2, headers, HttpStatus.OK);
//        File file = new File(path);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, contentDisposition)
                .header(HttpHeaders.CONTENT_TYPE, file.getContentType())
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(resource);
    }

}