package com.team.dev.controller;

import com.team.dev.dto.ActiviteRequestDto;
import com.team.dev.dto.ActiviteResponseDto;
import com.team.dev.dto.ChampResponseDto;
import com.team.dev.repository.ChampRepository;
import com.team.dev.service.ActiviteService;
import com.team.dev.utils.AppConstants;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/v1/activites")
@CrossOrigin(origins = "http://localhost:4200")
public class ActiviteController {

	private  final ChampRepository champRepository ;
	private final ActiviteService activiteService ;
	
	@Autowired
	public ActiviteController(ActiviteService activiteService,
			ChampRepository champRepository) {
		this.activiteService = activiteService;
		this.champRepository = champRepository;
	}

	@PostMapping
	//@RolesAllowed({"ROLE_ADMIN"})
	public ResponseEntity<ActiviteResponseDto> addActivite(@Valid @RequestBody final ActiviteRequestDto activiteRequestDto){

		ActiviteResponseDto activiteResponseDto = activiteService.addActivite(activiteRequestDto);
		return new ResponseEntity<>(activiteResponseDto, HttpStatus.OK);
	}

	@GetMapping
    public ActiviteResponseDto getAllActivites(
			@RequestParam(value = "keywords", defaultValue = AppConstants.DEFAULT_KEY_WORDS, required = false) String keywords,
            @RequestParam(value = "pageNo", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER, required = false) int pageNo,
            @RequestParam(value = "pageSize", defaultValue = AppConstants.DEFAULT_PAGE_SIZE, required = false) int pageSize,
            @RequestParam(value = "sortBy", defaultValue = AppConstants.DEFAULT_SORT_BY, required = false) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = AppConstants.DEFAULT_SORT_DIRECTION, required = false) String sortDir
    ){
        return activiteService.getAllActivites(pageNo, pageSize, sortBy, sortDir,keywords);
    }

	@GetMapping("/{id}")
	public ResponseEntity<ActiviteResponseDto> getActivite(@PathVariable final Long id){
		ActiviteResponseDto activiteResponseDto = activiteService.getActiviteById(id);
			return new ResponseEntity<>(activiteResponseDto, HttpStatus.OK);
	}
	
	/** New request **/
	@GetMapping("/activite")
	public ResponseEntity<List<ChampResponseDto>> getChampsByActivite(){
		List<ChampResponseDto> champsResponseDtos = champRepository.getChampsByActivite();
		return new ResponseEntity<>(champsResponseDtos, HttpStatus.OK);
	}
	
	
	// fin de pagination

	@DeleteMapping("/{id}")
	//@RolesAllowed({"ROLE_ADMIN"})
	public ResponseEntity<ActiviteResponseDto> deleteActivite(@PathVariable final Long id){
		ActiviteResponseDto activiteResponseDto = activiteService.deleteActivite(id);
		return new ResponseEntity<>(activiteResponseDto, HttpStatus.OK);
	}
	
	@PutMapping("/{id}")
	//@RolesAllowed({"ROLE_ADMIN"})

	public ResponseEntity<ActiviteResponseDto> editActivite( @Valid @RequestBody final ActiviteRequestDto activiteRequestDto, @PathVariable final Long id){
		ActiviteResponseDto activiteResponseDto = activiteService.editActivite(id, activiteRequestDto);
		return new ResponseEntity<>(activiteResponseDto, HttpStatus.OK);
		


}

	
	@PostMapping("/{champId}/to/{activiteId}")
	public  ResponseEntity<ActiviteResponseDto> addChamp(@RequestBody final Long champId, @PathVariable final Long activiteId){
		ActiviteResponseDto activiteResponseDto = activiteService.addChampToActivite(activiteId, champId);
		return new ResponseEntity<>(activiteResponseDto, HttpStatus.OK);
		
	}

	@PostMapping("/{planificationId}/from/{activite}")
	public  ResponseEntity<ActiviteResponseDto> removeActivite(@PathVariable final Long champId, @PathVariable final Long activiteId){
		ActiviteResponseDto activiteResponseDto = activiteService.removeChampFromActivite(activiteId, champId);
		return new ResponseEntity<>(activiteResponseDto, HttpStatus.OK);
	}
	/** Serach
	@GetMapping("/{keywords}")
	public  ResponseEntity<List<ActiviteRequestDto>> searchActiviteByNom(@PathVariable("keywords") String keywords){
		List<ActiviteRequestDto> result= activiteService.searchActivite(keywords);
		return new ResponseEntity<List<ActiviteRequestDto>>(result, HttpStatus.OK);
	
	}
	 **/

	/** Full Serach **/
	@GetMapping("/full/{keywords}")
	public  ResponseEntity<ActiviteResponseDto> searchActiviteByFull(
			 @RequestParam(value = "pageNo", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER, required = false) int pageNo,
	            @RequestParam(value = "pageSize", defaultValue = AppConstants.DEFAULT_PAGE_SIZE, required = false) int pageSize,
	            @RequestParam(value = "sortBy", defaultValue = AppConstants.DEFAULT_SORT_BY, required = false) String sortBy,
	            @RequestParam(value = "sortDir", defaultValue = AppConstants.DEFAULT_SORT_DIRECTION, required = false) String sortDir,
			@PathVariable("keywords") String keywords){
		ActiviteResponseDto result= activiteService.searchActiviteFull(pageNo, pageSize, sortBy, sortBy, keywords);
				
		return new ResponseEntity<ActiviteResponseDto>(result, HttpStatus.OK);
	
	}
	
	
}
