package com.team.dev.controller;

import com.team.dev.dto.*;
import com.team.dev.service.EntrepriseService;
import com.team.dev.service.FileService;
import com.team.dev.utils.AppConstants;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/api/v1/entreprises")
@CrossOrigin(origins = "http://localhost:4200")
public class EntrepriseController {

    @Value("${project.image}")
    private String path;
    private final EntrepriseService entrepriseService;
    private final FileService fileService;

    @Autowired
    public EntrepriseController(EntrepriseService entrepriseService, FileService fileService) {
        this.entrepriseService = entrepriseService;
        this.fileService = fileService;
    }

    @PostMapping("/create")
    public ResponseEntity<EntrepriseResponseDto> addEntreprise(@Valid @RequestBody final EntrepriseRequestDto EntrepriseRequestDto){
        EntrepriseResponseDto entrepriseResponseDto = entrepriseService.addEntreprise(EntrepriseRequestDto);
        return new ResponseEntity<>(entrepriseResponseDto, HttpStatus.OK);
    }

    @GetMapping("/test")
    public List<EntrepriseResponseDto> getentreprises(){
        return this.entrepriseService.getentreprises();
    }
    @GetMapping
    public EntrepriseResponseDto getAllCategories(
            @RequestParam(value = "keywords", defaultValue = AppConstants.DEFAULT_KEY_WORDS, required = false) String keywords,
            @RequestParam(value = "pageNo", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER, required = false) int pageNo,
            @RequestParam(value = "pageSize", defaultValue = AppConstants.DEFAULT_PAGE_SIZE, required = false) int pageSize,
            @RequestParam(value = "sortBy", defaultValue = AppConstants.DEFAULT_SORT_BY, required = false) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = AppConstants.DEFAULT_SORT_DIRECTION, required = false) String sortDir
    ){
        return entrepriseService.getAllEntreprises(pageNo, pageSize, sortBy, sortDir, keywords);
    }

    @GetMapping("/{idEntreprise}")
    public ResponseEntity<EntrepriseResponseDto> getEntreprise(@PathVariable final Long idEntreprise){
        EntrepriseResponseDto entrepriseResponseDto = entrepriseService.getEntrepriseById(idEntreprise);
        return new ResponseEntity<>(entrepriseResponseDto, HttpStatus.OK);
    }

    @DeleteMapping("/{idEntreprise}")
    public ResponseEntity<ErrorDetails> deleteEntreprise(@PathVariable final Long idEntreprise){
        EntrepriseResponseDto entrepriseResponseDto = entrepriseService.deleteEntreprise(idEntreprise);
        return new ResponseEntity<ErrorDetails>(new ErrorDetails(new Date(), "L'entréprise a été supprimée avec succès !!", ""), HttpStatus.OK);
    }

    @PutMapping("/{idEntreprise}")
    public ResponseEntity<EntrepriseResponseDto> editEntreprise(@RequestBody final EntrepriseRequestDto EntrepriseRequestDto, @PathVariable final Long idEntreprise){
        EntrepriseResponseDto entrepriseResponseDto = entrepriseService.editEntreprise(idEntreprise, EntrepriseRequestDto);
        return new ResponseEntity<>(entrepriseResponseDto, HttpStatus.OK);

    }

    @PostMapping("/image/{id}")
    public ResponseEntity<EntrepriseResponseDto> uploadEntrepriseImage(
            @RequestParam("image") MultipartFile image,
            @PathVariable Long id
    ) throws Exception {
        String fileName = this.fileService.uploadImages(path, id, image);
        EntrepriseRequestDto auth = this.entrepriseService.getEntrepriseId(id);
        String url = "";
        url = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/images/")
                .path(fileName).toUriString();
        auth.setImageUrl(fileName);
        EntrepriseResponseDto updateProduct = this.entrepriseService.editEntreprise(id, auth);
        return new ResponseEntity<>(updateProduct, HttpStatus.OK);
    }

    @GetMapping(value = "/image/{imageName}", produces = MediaType.IMAGE_JPEG_VALUE)
    public void downloadImage(
            @PathVariable("imageName") String imageName, HttpServletResponse response
    ) throws IOException {

        String url = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/images/")
                .path(imageName).toString();

        InputStream resource = this.fileService.getRessource(path, imageName);
        StreamUtils.copy(resource, response.getOutputStream());
    }
}




