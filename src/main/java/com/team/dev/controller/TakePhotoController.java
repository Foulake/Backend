package com.team.dev.controller;

import com.team.dev.dto.ChampRequestDto;
import com.team.dev.dto.ChampResponseDto;
import com.team.dev.service.FileService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;


@RestController
@RequestMapping("/api/v1/photo")
@Log4j2

@CrossOrigin(origins = "http://localhost:4200")
public class TakePhotoController {

    @Value("${project.image}")
    private String path;
    private final FileService fileService;

    public TakePhotoController(FileService fileService) {
        this.fileService = fileService;
    }

    @PostMapping(consumes = MediaType.IMAGE_JPEG_VALUE)
    public ResponseEntity<String> uploadProductImage(
            @RequestParam("file") MultipartFile file
    ) throws Exception {
        String fileName = this.fileService.uploadImage(path, file);
        String url = "";
        url = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/images/")
                .path(fileName).toUriString();
        System.out.println("''''''''''''''''''''+File Url2 + ''''''''''''''" + url);

        log.info("filename {} :::::::::", fileName);

        return new ResponseEntity<>(fileName, HttpStatus.OK);
    }
}
