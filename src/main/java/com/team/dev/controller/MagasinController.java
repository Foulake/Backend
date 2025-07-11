package com.team.dev.controller;

import com.team.dev.dto.MagasinRequestDto;
import com.team.dev.dto.MagasinResponseDto;
import com.team.dev.service.MagasinService;
import com.team.dev.utils.AppConstants;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/v1/magasins")
@CrossOrigin(origins = "http://localhost:4200")
public class MagasinController {
	
	private final MagasinService magasinService;
	
	
	
	public MagasinController(MagasinService magasinService) {
		this.magasinService = magasinService;
	}

	@PostMapping
	//@RolesAllowed({"ROLE_ADMIN"})
	public ResponseEntity<MagasinResponseDto> addMagasin(@Valid @RequestBody final MagasinRequestDto magasinRequestDto){
		MagasinResponseDto magasinResponseDto = magasinService.addMagasin(magasinRequestDto);
		return new ResponseEntity<>(magasinResponseDto, HttpStatus.OK);
	}
	
//	@GetMapping
	//public ResponseEntity<List<MagasinResponseDto>> getMagasins(){
		//List<MagasinResponseDto> magasinResponseDto = magasinService.getMagasins();
		//return new ResponseEntity<>(magasinResponseDto, HttpStatus.OK);
	//}
	
	@GetMapping
    public MagasinResponseDto getAllMagasins(
            @RequestParam(value = "keywords", defaultValue = AppConstants.DEFAULT_KEY_WORDS, required = false) String keywords,
            @RequestParam(value = "pageNo", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER, required = false) int pageNo,
            @RequestParam(value = "pageSize", defaultValue = AppConstants.DEFAULT_PAGE_SIZE, required = false) int pageSize,
            @RequestParam(value = "sortBy", defaultValue = AppConstants.DEFAULT_SORT_BY, required = false) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = AppConstants.DEFAULT_SORT_DIRECTION, required = false) String sortDir
    ){
        return magasinService.getAllMagasins(pageNo, pageSize, sortBy, sortDir, keywords);
    }
	
	@GetMapping("/{id}")
	public ResponseEntity<MagasinResponseDto> getMagasin(@PathVariable final Long id){
		MagasinResponseDto magasinResponseDto = magasinService.getMagasinById(id);
			return new ResponseEntity<>(magasinResponseDto, HttpStatus.OK);
	}

	@DeleteMapping("/{id}")
	//@RolesAllowed({"ROLE_ADMIN"})
	public ResponseEntity<MagasinResponseDto> deleteLocalite(@PathVariable final Long id){
		MagasinResponseDto magasinResponseDto = magasinService.deleteMagasin(id);
		return new ResponseEntity<>(magasinResponseDto, HttpStatus.OK);
	}
	
	@PutMapping("/{id}")
	//@RolesAllowed({"ROLE_ADMIN"})
	public ResponseEntity<MagasinResponseDto> editLocalite(@RequestBody final MagasinRequestDto magasinRequestDto, @PathVariable final Long id){
		MagasinResponseDto magasinResponseDto = magasinService.editMagasin(id, magasinRequestDto);
		return new ResponseEntity<>(magasinResponseDto, HttpStatus.OK);
		
	}
	
	/** Serach **/
	@GetMapping("/search/{keywords}")
	public  ResponseEntity<List<MagasinRequestDto>> searchMagasinByNom(@PathVariable("keywords") String keywords){
		List<MagasinRequestDto> result= magasinService.searchMagasin(keywords);
		return new ResponseEntity<List<MagasinRequestDto>>(result, HttpStatus.OK);
	
	}
 
	
}
