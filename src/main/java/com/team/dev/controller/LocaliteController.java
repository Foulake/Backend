package com.team.dev.controller;

import com.team.dev.dto.LocaliteRequestDto;
import com.team.dev.dto.LocaliteResponseDto;
import com.team.dev.service.LocaliteService;
import com.team.dev.utils.AppConstants;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/localites")
@CrossOrigin(origins = "http://localhost:4200")
public class LocaliteController {
	
	private final LocaliteService localiteService;
	@Autowired
	public LocaliteController(LocaliteService localiteService) {
		this.localiteService = localiteService;
	}
	
	@PostMapping
	//@RolesAllowed({"ROLE_ADMIN"})
	public ResponseEntity<LocaliteResponseDto> addLocalite(@Valid @RequestBody final LocaliteRequestDto localiteRequestDto){
		LocaliteResponseDto localiteResponseDto = localiteService.addLocalite(localiteRequestDto);
		return new ResponseEntity<>(localiteResponseDto, HttpStatus.OK);
	}
	/**
	@GetMapping("/getAll")
	public ResponseEntity<List<LocaliteResponseDto>> getLocalites(){
		List<LocaliteResponseDto> localiteResponseDto = localiteService.getLocalites();
		return new ResponseEntity<>(localiteResponseDto, HttpStatus.OK);
	}   **/
	
	@GetMapping
    public LocaliteResponseDto getAllLocalites(
            @RequestParam(value = "keywords", defaultValue = AppConstants.DEFAULT_KEY_WORDS, required = false) String keywords,
            @RequestParam(value = "pageNo", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER, required = false) int pageNo,
            @RequestParam(value = "pageSize", defaultValue = AppConstants.DEFAULT_PAGE_SIZE, required = false) int pageSize,
            @RequestParam(value = "sortBy", defaultValue = AppConstants.DEFAULT_SORT_BY, required = false) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = AppConstants.DEFAULT_SORT_DIRECTION, required = false) String sortDir
    ){
        return localiteService.getAllLocalites(pageNo, pageSize, sortBy, sortDir, keywords);
    }
	
	@GetMapping("/{id}")
	public ResponseEntity<LocaliteResponseDto> getLocalite(@PathVariable final Long id){
		LocaliteResponseDto localiteResponseDto = localiteService.getLocaliteById(id);
			return new ResponseEntity<>(localiteResponseDto, HttpStatus.OK);
	}
	
	
	@DeleteMapping("/{id}")
	public ResponseEntity<LocaliteResponseDto> deleteLocalite(@PathVariable final Long id){
		LocaliteResponseDto localiteResponseDto = localiteService.deleteLocalite(id);
		return new ResponseEntity<>(localiteResponseDto, HttpStatus.OK);
	}
	
	@PutMapping("/{id}")
	//@RolesAllowed({"ROLE_ADMIN"})
	public ResponseEntity<LocaliteResponseDto> editLocalite(@Valid @RequestBody final LocaliteRequestDto localiteRequestDto, @PathVariable final Long id){
		LocaliteResponseDto localiteResponseDto = localiteService.editLocalite(id, localiteRequestDto);
		return new ResponseEntity<>(localiteResponseDto, HttpStatus.OK);
		
	}
	
	/** Serach
	@GetMapping("/{keywords}")
	public  ResponseEntity<List<LocaliteRequestDto>> searchLocaliteByNom(@PathVariable("keywords") String keywords){
		List<LocaliteRequestDto> result= localiteService.searchLocalite(keywords);
		return new ResponseEntity<List<LocaliteRequestDto>>(result, HttpStatus.OK);
	
	}	 **/

}
