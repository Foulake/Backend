package com.team.dev.controller;

import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;
import com.team.dev.dto.ChampRequestDto;
import com.team.dev.dto.ChampResponseDto;
import com.team.dev.dto.RecolteRequest;
import com.team.dev.dto.RecolteResponse;
import com.team.dev.exception.ApiException;
import com.team.dev.mapper.Mapper;
import com.team.dev.model.User;
import com.team.dev.repository.RecolteRepository;
import com.team.dev.service.FileService;
import com.team.dev.service.RecolteService;
import com.team.dev.utils.AppConstants;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.List;

@RequestMapping("api/v1/recoltes")
@RequiredArgsConstructor
@RestController
@Log4j2
public class RecolteController {

    @Value("${project.image}")
    private String path;
    private final RecolteService recolteService;
    private final FileService fileService;


    @GetMapping
    public ResponseEntity<?> getRecoltes(
            @RequestParam(value = "keyword", defaultValue = AppConstants.DEFAULT_KEY_WORDS) String keyword,
            @RequestParam(value = "pageNo", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER, required = false) int pageNo,
            @RequestParam(value = "pageSize", defaultValue = AppConstants.DEFAULT_PAGE_SIZE, required = false) int pageSize,
            @RequestParam(value = "sortBy", defaultValue = AppConstants.DEFAULT_SORT_BY, required = false) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = AppConstants.DEFAULT_SORT_DIRECTION, required = false) String sortDir
    ){
        return new ResponseEntity<>(recolteService.getAll(pageNo,pageSize,sortBy,sortDir,keyword), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<RecolteResponse> saveRecolte(@Valid @RequestBody RecolteRequest request) {
        RecolteResponse response;
        try {
            response = this.recolteService.newRecolte(request);
        } catch (Exception ex) {
            throw new ApiException(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage());
        }
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<RecolteResponse> edit(@RequestBody final RecolteRequest request, @PathVariable final Long id){
        RecolteResponse edited = this.recolteService.edit(request, id);
        return new ResponseEntity<>(edited, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<RecolteRequest> getByID(@PathVariable final Long id){
        RecolteRequest response = this.recolteService.getId(id);
        log.info("find :::{}", response);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PatchMapping("/image/{id}")
    public ResponseEntity<?> uploadProductImage(
            @RequestParam("image") MultipartFile image,
            @PathVariable Long id
    ) throws Exception {
        String fileName = this.fileService.uploadImages(path, id,image);
        RecolteRequest auth = this.recolteService.getId(id);
        ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(path)
                .path(fileName).toUriString();
        //auth.setDownloadUrl(url);
        auth.setImageUrl(fileName);
        RecolteResponse updateRecolte = this.recolteService.edit(auth,id);
        return new ResponseEntity<>(updateRecolte, HttpStatus.OK);
    }

    @GetMapping(value = "/image/{imageName}", produces = MediaType.IMAGE_JPEG_VALUE)
    public void downloadImage(
            @PathVariable("imageName") String imageName, HttpServletResponse response
    ) throws IOException {

        String url = "";
        url = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/images/")
                .path(imageName).toString();

        InputStream resource = this.fileService.getRessource(path, imageName);

        StreamUtils.copy(resource, response.getOutputStream());
    }


    @GetMapping("/dates")
    public ResponseEntity<?> getBetweenDates(@RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date startDate, @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date endDate){


        return new ResponseEntity<>(recolteService.getBetweenDates(startDate, endDate), HttpStatus.OK);
    }

    @GetMapping("/export")
    public void exportCsv(@RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date startDate,
                                       @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date endDate,
                                       HttpServletResponse response) throws CsvRequiredFieldEmptyException, CsvDataTypeMismatchException, IOException {

        List<RecolteRequest> betweenDates = recolteService.getBetweenDates(startDate, endDate);
        this.fileService.exportToCsv(response, betweenDates, "rescolte");

    }


    @GetMapping("/export-csv")
    public ResponseEntity<?> exportCsv(@RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date startDate,
                                       @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date endDate) {
        try {
            // Récupérer la liste des données (RecolteRequest) entre les dates
            List<RecolteRequest> recolteRequestList = recolteService.getBetweenDates(startDate, endDate);
            if (recolteRequestList.isEmpty()) {
                // Si la liste est vide, retourner une erreur 404
                return ResponseEntity.status(404).body("Aucune donnée à exporter pour la période spécifiée.");
            }

            // Exporter les données au format CSV et obtenir le fichier généré
            String filePath = fileService.exportToCsvAndGetFile(recolteRequestList);
            File file = new File(filePath);

            // Vérifier si le fichier a bien été généré
            if (!file.exists()) {
                return ResponseEntity.status(500).body("Erreur lors de la génération du fichier CSV.");
            }

            // Créer un InputStreamResource pour envoyer le fichier en réponse
            FileInputStream fileInputStream = new FileInputStream(file);
            InputStreamResource resource = new InputStreamResource(fileInputStream);

            // Créer la réponse HTTP pour renvoyer le fichier en téléchargement
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"export.csv\"")
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .contentLength(file.length()) // Facultatif : précise la taille du fichier
                    .body(resource);

        } catch (IOException e) {
            // En cas d'erreur, retourner un message d'erreur spécifique
            return ResponseEntity.status(500).body("Une erreur s'est produite lors de l'exportation : " + e.getMessage());
        } catch (Exception e) {
            // Attraper d'autres erreurs non anticipées
            return ResponseEntity.status(500).body("Une erreur inattendue est survenue : " + e.getMessage());
        }
    }

}
