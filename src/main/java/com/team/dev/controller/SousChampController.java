package com.team.dev.controller;

import com.team.dev.dto.ChampRequestDto;
import com.team.dev.dto.ChampResponseDto;
import com.team.dev.dto.SousChampRequestDto;
import com.team.dev.dto.SousChampResponseDto;
import com.team.dev.service.FileService;
import com.team.dev.service.SousChampService;
import com.team.dev.utils.AppConstants;
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
import java.util.List;

@RestController
@RequestMapping("/api/v1/sous-champs")
@CrossOrigin (origins = "http://localhost:4200", maxAge = 3600)
public class SousChampController {

	@Value("${project.image}")
	private String path;
	private final SousChampService sousChampService;
	private final FileService  fileService;

	@Autowired
    public SousChampController(SousChampService sousChampService, FileService fileService) {
        this.sousChampService = sousChampService;
		this.fileService = fileService;
	}

    @PostMapping
    //@RolesAllowed({"ROLE_ADMIN"})
	public ResponseEntity<SousChampResponseDto> addSousChamp(@Valid @RequestBody final SousChampRequestDto souschampRequestDto){
		SousChampResponseDto souschampResponseDto = sousChampService.addSousChamp(souschampRequestDto);
		return new ResponseEntity<>(souschampResponseDto, HttpStatus.OK);
	}


    @GetMapping
    public SousChampResponseDto getAllSousChamps(
            @RequestParam(value = "keywords", defaultValue = AppConstants.DEFAULT_KEY_WORDS, required = false) String keywords,
            @RequestParam(value = "pageNo", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER, required = false) int pageNo,
            @RequestParam(value = "pageSize", defaultValue = AppConstants.DEFAULT_PAGE_SIZE, required = false) int pageSize,
            @RequestParam(value = "sortBy", defaultValue = AppConstants.DEFAULT_SORT_BY, required = false) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = AppConstants.DEFAULT_SORT_DIRECTION, required = false) String sortDir
    ){
        return sousChampService.getAllSousChamps(pageNo, pageSize, sortBy, sortDir, keywords);
    }

	/*@GetMapping
	public ResponseEntity<List<SousChampResponseDto>> getSousChamps(){
		List<SousChampResponseDto> souschampResponseDto = sousChampService.getSousChamps();
		return new ResponseEntity<>(souschampResponseDto, HttpStatus.OK);
	} */
	@GetMapping("/{id}")
	public ResponseEntity<SousChampResponseDto> getSousChamp(@PathVariable final Long id){
		SousChampResponseDto souschampResponseDto = sousChampService.getSousChampById(id);
			return new ResponseEntity<>(souschampResponseDto, HttpStatus.OK);
	}

    @GetMapping("/sous-champs/{champId}")
    public List<SousChampRequestDto> getSousChampsByChampId(@PathVariable(value = "champId") Long champId){
        return sousChampService.getSousChampsByChampId(champId);
    }

	@PostMapping("/image/{id}")
	public ResponseEntity<SousChampResponseDto> uploadProductImage(
			@RequestParam("image") MultipartFile image,
			@PathVariable Long id
	) throws Exception {
		String fileName = this.fileService.uploadImage(path, image);
		SousChampRequestDto auth = this.sousChampService.getSousChampId(id);
		auth.setImageUrl(fileName);
		SousChampResponseDto updateProduct = this.sousChampService.editSousChamp(id, auth);
		return new ResponseEntity<>(updateProduct, HttpStatus.OK);
	}

	//Get image by name
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
 /**
    @GetMapping("/champs/{champId}/get/{id}")
    public ResponseEntity<SousChampRequestDto> getSousChampById(@PathVariable(value = "champId") Long champId,
                                                     @PathVariable(value = "id") Long sousChampId){
        SousChampRequestDto sousChampDto = sousChampService.getSousChampById(champId, sousChampId);
        return new ResponseEntity<>(sousChampDto, HttpStatus.OK);
    }
    **/

    @PutMapping("/{id}")
    //@RolesAllowed({"ROLE_ADMIN"})

	public ResponseEntity<SousChampResponseDto> editSousChamp(@Valid @RequestBody final SousChampRequestDto souschampRequestDto, @PathVariable final Long id){
		SousChampResponseDto souschampResponseDto = sousChampService.editSousChamp(id, souschampRequestDto);
		return new ResponseEntity<>(souschampResponseDto, HttpStatus.OK);
		
	}

    @DeleteMapping("/{id}")
	public ResponseEntity<SousChampResponseDto> deleteSousChamp(@PathVariable final Long id){
		SousChampResponseDto souschampResponseDto = sousChampService.deleteSousChamp(id);
		return new ResponseEntity<>(souschampResponseDto, HttpStatus.OK);
	}


	/** Full Serach
	@GetMapping("/{keywords}")
	public  ResponseEntity<List<SousChampRequestDto>> searchProductByFull(@PathVariable("keywords") String keywords){
		List<SousChampRequestDto> result= sousChampService.searchSousChampFull(keywords);
		return new ResponseEntity<List<SousChampRequestDto>>(result, HttpStatus.OK);
	
	}    **/
}
