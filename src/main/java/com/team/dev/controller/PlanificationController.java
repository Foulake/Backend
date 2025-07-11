package com.team.dev.controller;

import com.team.dev.dto.*;
import com.team.dev.mapper.Mapper;
import com.team.dev.repository.ActiviteRepository;
import com.team.dev.service.FileService;
import com.team.dev.service.PlanificationService;
import com.team.dev.utils.AppConstants;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
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
@RequestMapping("/api/v1/planifications")
@CrossOrigin(origins = "http://localhost:4200")
public class PlanificationController {

	@Value("${project.image}")
	private String path;

	private  final PlanificationService planificationService;
	private final ActiviteRepository activiteRepository;
	private final FileService fileService;
	public PlanificationController(PlanificationService planificationService,
								   ActiviteRepository activiteRepository, FileService fileService) {
		this.planificationService = planificationService;
	
		this.activiteRepository = activiteRepository;

		this.fileService = fileService;
	}

	@PostMapping
	//@RolesAllowed({"ROLE_ADMIN"})
	public ResponseEntity<PlanificationResponseDto> addPlanification(@Valid @RequestBody final PlanificationRequestDto planificationRequestDto){
		PlanificationResponseDto planificationResponseDto = planificationService.addPlanification(planificationRequestDto);
		return new ResponseEntity<>(planificationResponseDto, HttpStatus.OK);
	}
	

	// pour la pagination
		@GetMapping
	    public PlanificationResponseDto getAllPlanifications(
	            @RequestParam(value = "pageNo", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER, required = false) int pageNo,
	            @RequestParam(value = "pageSize", defaultValue = AppConstants.DEFAULT_PAGE_SIZE, required = false) int pageSize,
	            @RequestParam(value = "sortBy", defaultValue = AppConstants.DEFAULT_SORT_BY, required = false) String sortBy,
	            @RequestParam(value = "sortDir", defaultValue = AppConstants.DEFAULT_SORT_DIRECTION, required = false) String sortDir,
	            @RequestParam(value = "keywords", defaultValue = AppConstants.DEFAULT_KEY_WORDS, required = false) String keyword
	    ){
	        return planificationService.findAllPlanification(pageNo, pageSize, sortBy, sortDir,keyword);
	    }
		@GetMapping("/{id}")
		public ResponseEntity<PlanificationResponseDto> getPlanification(@PathVariable final Long id){
			PlanificationResponseDto planificationResponseDto = planificationService.getPlanificationById(id);
				return new ResponseEntity<>(planificationResponseDto, HttpStatus.OK);
		}
		
		/** New request **/
		@GetMapping("/planification/activites")
		public ResponseEntity<List<ActiviteResponseDto>> getActivitesByPlanification(){
			List<ActiviteResponseDto> activitesResponseDtos = activiteRepository.getActivitesByPlanification();
			return new ResponseEntity<>(activitesResponseDtos, HttpStatus.OK);
		}

	@DeleteMapping("/{id}")
	//@RolesAllowed({"ROLE_ADMIN"})
	public ResponseEntity<PlanificationResponseDto> deletePlanification(@PathVariable final Long id){
		PlanificationResponseDto planificationResponseDto = planificationService.deletePlanification(id);
		return new ResponseEntity<>(planificationResponseDto, HttpStatus.OK);
	}
	
	@PutMapping("/{id}")
	//@RolesAllowed({"ROLE_ADMIN"})
	public ResponseEntity<PlanificationResponseDto> editPlanification( @Valid @RequestBody final PlanificationRequestDto planificationRequestDto, @PathVariable final Long id){
		PlanificationResponseDto planificationResponseDto = planificationService.editPlanification(id, planificationRequestDto);
		return new ResponseEntity<>(planificationResponseDto, HttpStatus.OK);
	}

	@PostMapping("/image/{id}")
	public ResponseEntity<PlanificationResponseDto> uploadProductImage(
			@RequestParam("image") MultipartFile image,
			@PathVariable Long id
	) throws Exception {
		String fileName = this.fileService.uploadImage(path, image);
		PlanificationRequestDto auth = Mapper.planificationToPlanificationRequest(this.planificationService.getPlanificationById(id));
		auth.setImageUrl(fileName);
		return new ResponseEntity<>(this.planificationService.editPlanification(id, auth), HttpStatus.OK);
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


	@PatchMapping("/status/{idPlaning}")
	public ResponseEntity<PlanificationResponseDto> switchStatusPlanification( @Valid @PathVariable(value = "idPlaning") final Long  idPlaning){
		PlanificationResponseDto responseDto = this.planificationService.switchStatusPlanification(idPlaning);
		return new ResponseEntity<>(responseDto, HttpStatus.OK);
	}

}
