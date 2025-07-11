package com.team.dev.controller;

import com.team.dev.dto.ChampRequestDto;
import com.team.dev.dto.ChampResponseDto;
import com.team.dev.dto.LocaliteResponseDto;
import com.team.dev.repository.LocaliteRepository;
import com.team.dev.service.ChampService;
import com.team.dev.service.FileService;
import com.team.dev.utils.AppConstants;
import jakarta.servlet.ServletContext;
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
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;


@RestController
@RequestMapping("/api/v1/champs")
//@CrossOrigin(origins = "http://localhost:4200", maxAge = 3600, allowCredentials="true")
@CrossOrigin(origins = "http://localhost:4200")
public class ChampController {

	@Value("${project.image}")
	private String path;
	private final ChampService champService;
	private final FileService fileService;
	private final LocaliteRepository localiteRepository;

	@Autowired
	private ServletContext context;

	public ChampController(ChampService champService, FileService fileService, LocaliteRepository localiteRepository) {
		this.champService = champService;
		this.fileService = fileService;
		this.localiteRepository = localiteRepository;
	}
	
	@PostMapping
	public ResponseEntity<ChampResponseDto> addChamp(@Valid @RequestBody final ChampRequestDto champRequestDto){
		ChampResponseDto champResponseDto = champService.addChamp(champRequestDto);
		return new ResponseEntity<ChampResponseDto>(champResponseDto, HttpStatus.OK);
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<ChampResponseDto> getChamp(@PathVariable final Long id){
		ChampResponseDto champResponseDto = champService.getChampById(id);
		return new ResponseEntity<ChampResponseDto>(champResponseDto, HttpStatus.OK);
	}
	
	/*@GetMapping
	public ResponseEntity<List<ChampResponseDto>> getChamps(){
		List<ChampResponseDto> champResponseDtos = champService.getChamps();
		return new ResponseEntity<>(champResponseDtos, HttpStatus.OK);
	}*/
	//pour la pagination 
	@GetMapping
    public ChampResponseDto getAllProducts(
			@RequestParam(value ="keywords", defaultValue = AppConstants.DEFAULT_KEY_WORDS, required = false) String keywords,
            @RequestParam(value = "pageNo", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER, required = false) int pageNo,
            @RequestParam(value = "pageSize", defaultValue = AppConstants.DEFAULT_PAGE_SIZE, required = false) int pageSize,
            @RequestParam(value = "sortBy", defaultValue = AppConstants.DEFAULT_SORT_BY, required = false) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = AppConstants.DEFAULT_SORT_DIRECTION, required = false) String sortDir
    ){
        return champService.searchChampFull(pageNo, pageSize, sortBy, sortDir, keywords);
    }

	@GetMapping("/filter")
    public ChampResponseDto searchChamp(
			@RequestParam(value ="keywords") String keywords,
            @RequestParam(value = "pageNo", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER, required = false) int pageNo,
            @RequestParam(value = "pageSize", defaultValue = AppConstants.DEFAULT_PAGE_SIZE, required = false) int pageSize,
            @RequestParam(value = "sortBy", defaultValue = AppConstants.DEFAULT_SORT_BY, required = false) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = AppConstants.DEFAULT_SORT_DIRECTION, required = false) String sortDir
    ){
        return champService.searchChampFull(pageNo,pageSize,sortBy,sortDir,keywords);
    }
	/** New request **/
	/*@GetMapping("/localite/champ")
	public ResponseEntity<List<LocaliteResponseDto>> getLocalitesByChamp(){
		List<LocaliteResponseDto> localitesResponseDtos = localiteRepository.getLocalitesByChamp();
		return new ResponseEntity<>(localitesResponseDtos, HttpStatus.OK);
	}*/
	
	
	//fin pagination
	
	@DeleteMapping("/{id}")
	public ResponseEntity<ChampResponseDto> deletePrroduct(@PathVariable final Long id){
		ChampResponseDto champResponseDto = champService.deleteChamp(id);
		return new ResponseEntity<>(champResponseDto, HttpStatus.OK);
	
	}
	
	@PutMapping("/{id}")
	public ResponseEntity<ChampResponseDto> editChamp( @Valid @RequestBody final ChampRequestDto champRequestDto, @PathVariable final Long id){
		ChampResponseDto champResponseDto = champService.editChamp(id, champRequestDto);
		return new ResponseEntity<>(champResponseDto, HttpStatus.OK);
	}

	@PostMapping("/image/{id}")
	public ResponseEntity<ChampResponseDto> uploadProductImage(
			@RequestParam("image") MultipartFile image,
		@PathVariable Long id
	) throws Exception {
		String fileName = this.fileService.uploadImage(path, image);
		ChampRequestDto auth = this.champService.getChampId(id);
		String url = "";
		url = ServletUriComponentsBuilder.fromCurrentContextPath()
				.path("/images/")
				.path(fileName).toUriString();
		System.out.println("''''''''''''''''''''+File Url2 + ''''''''''''''" + url);
		auth.setDownloadUrl(url);
		auth.setImageUrl(fileName);
		ChampResponseDto updateProduct = this.champService.editChamp(id, auth);
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

	/*@GetMapping(value = "/image/{imageNames}")
	public byte[] getImage(
			@PathVariable("imageNames") Long imageNames
	) throws IOException {
		var champ = this.champService.getChamp(imageNames);
		return Files.readAllBytes(Paths.get(context.getRealPath("/images/") + champ.getImageUrl()));

	}*/




//	@PostMapping("/addActivite/{activiteId}/to/{champId}")
//	public  ResponseEntity<ChampResponseDto> addActivite(@RequestBody final Long activiteId, @PathVariable final Long champId){
//		ChampResponseDto champResponseDto = champService.addActiviteToChamp(champId, activiteId);
//		return new ResponseEntity<>(champResponseDto, HttpStatus.OK);
//		
//	}
	
//	@PostMapping("/addVane/{vaneId}/to/{champId}")
//	public  ResponseEntity<ChampResponseDto> addVane(@RequestBody final Long vaneId, @PathVariable final Long champId){
//		ChampResponseDto champResponseDto = champService.addVaneToChamp(champId, vaneId);
//		return new ResponseEntity<>(champResponseDto, HttpStatus.OK);
//		
//	}
	
	
//	@PostMapping("/addSousChamp/{souschampId}/to/{champId}")
//	public  ResponseEntity<ChampResponseDto> addSousChamp(@RequestBody final Long souschampId, @PathVariable final Long champId){
//		ChampResponseDto champResponseDto = champService.addSousChampToChamp(champId, souschampId);
//		return new ResponseEntity<>(champResponseDto, HttpStatus.OK);
//		
//	}
	
//	@PostMapping("/removeActivite/{activiteId}/from/{champ}")
//	public  ResponseEntity<ChampResponseDto> removeActivite(@PathVariable final Long activiteId, @PathVariable final Long champId){
//		ChampResponseDto champResponseDto = champService.removeVaneFromChamp(champId, activiteId);
//		return new ResponseEntity<>(champResponseDto, HttpStatus.OK);
//	}
	
	@PostMapping("/localite/{localiteId}/to/{champId}")
	public  ResponseEntity<ChampResponseDto> addLocalite(@RequestBody final Long localiteId, @PathVariable final Long champId){
		ChampResponseDto champResponseDto = champService.addLocaliteToChamp(champId, localiteId);
		return new ResponseEntity<>(champResponseDto, HttpStatus.OK);
		
	}
	
	@PostMapping("/localite/{localiteId}/from/{champId}")
	public  ResponseEntity<ChampResponseDto> removeLocalite(@PathVariable final Long localiteId, @PathVariable final Long champId){
		ChampResponseDto champResponseDto = champService.removeLocaliteFromChamp(champId, localiteId);
		return new ResponseEntity<>(champResponseDto, HttpStatus.OK);
	}
//	@PostMapping("/removeVane/{vaneId}/from/{champ}")
//	public  ResponseEntity<ChampResponseDto> removeVane(@PathVariable final Long vaneId, @PathVariable final Long champId){
//		ChampResponseDto champResponseDto = champService.removeVaneFromChamp(champId, vaneId);
//		return new ResponseEntity<>(champResponseDto, HttpStatus.OK);
//		
//	}
//	@PostMapping("/removeSouschamp/{souschampId}/from/{champ}")
//	public  ResponseEntity<ChampResponseDto> removeSousChamp(@PathVariable final Long souschampId, @PathVariable final Long champId){
//		ChampResponseDto champResponseDto = champService.removeSousChampFromChamp(champId, souschampId);
//		return new ResponseEntity<>(champResponseDto, HttpStatus.OK);
//

//}
}
