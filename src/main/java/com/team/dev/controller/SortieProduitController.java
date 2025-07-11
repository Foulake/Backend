package com.team.dev.controller;

import com.team.dev.dto.SortieDto;
import com.team.dev.dto.SortieResponse;
import com.team.dev.dto.TypeSortie;
import com.team.dev.exception.ApiException;
import com.team.dev.service.SortieProduitService;
import com.team.dev.utils.AppConstants;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/sortieProduits")
@CrossOrigin(origins = "http://localhost:4200")
public class SortieProduitController {
	
	private final SortieProduitService sortieProduitService;
	
	public SortieProduitController(SortieProduitService sortieProduitService) {
		this.sortieProduitService = sortieProduitService;
	}
	
	@PostMapping
	//@RolesAllowed({"ROLE_ADMIN"})
	public ResponseEntity<SortieResponse> sortieStock(@Valid @RequestBody SortieDto sortieProduitRequestDto){
		SortieResponse sortieProduitResponseDto = sortieProduitService.addSortieProduct(sortieProduitRequestDto);
		return new ResponseEntity<SortieResponse>(sortieProduitResponseDto, HttpStatus.OK);
	}

	/*@PatchMapping("/entree")
	//@RolesAllowed({"ROLE_ADMIN"})
	public ResponseEntity<SortieResponse> SortieStock(@Valid @RequestBody SortieDto sortieProduitRequestDto){
		SortieResponse sortieProduitResponseDto = sortieProduitService.approProduct(sortieProduitRequestDto);
		return new ResponseEntity<SortieResponse>(sortieProduitResponseDto, HttpStatus.OK);
	}*/
	
	@GetMapping("/{id}")
	public ResponseEntity<SortieResponse> getSortieProduit(@PathVariable(value = "id") final Long id){
		SortieResponse sortieProduitResponseDto = sortieProduitService.getProductById(id);
		return new ResponseEntity<SortieResponse>(sortieProduitResponseDto, HttpStatus.OK);
	}

	@GetMapping("/product/{idProduct}")
	public ResponseEntity<BigDecimal> getStockReelPructs(@PathVariable(value = "idProduct") final Long idProduct){
		//SortieResponse sortieProduitResponseDto = sortieProduitService.getProductById(id);
		return new ResponseEntity<BigDecimal>(sortieProduitService.stockReelPructs(idProduct), HttpStatus.OK);
	}

	@GetMapping("/filter/product/{idProduct}")
	public ResponseEntity<List<SortieResponse>>  mvtStockProduct(@PathVariable(value = "idProduct") final Long productId){
		List<SortieResponse> list = null;
		try {
			list = sortieProduitService.mvtStockProduct(productId);
		}catch (Exception ex){
			throw new ApiException("Une erreur est survenu !");
		}
		return new ResponseEntity<>(list, HttpStatus.OK) ;
	}
	
	@GetMapping
    public SortieResponse getAllSortieProduits(
            @RequestParam(value = "pageNo", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER, required = false) int pageNo,
            @RequestParam(value = "pageSize", defaultValue = AppConstants.DEFAULT_PAGE_SIZE, required = false) int pageSize,
            @RequestParam(value = "sortBy", defaultValue = AppConstants.DEFAULT_SORT_BY, required = false) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = AppConstants.DEFAULT_SORT_DIRECTION, required = false) String sortDir
    ){
        return sortieProduitService.searchSortieProduitFull(pageNo, pageSize, sortBy, sortDir);
    }

	@GetMapping("/filter/{type}")
	public SortieResponse FilteringByType(
			@PathVariable TypeSortie type,
			@RequestParam(value = "pageNo", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER, required = false) int pageNo,
			@RequestParam(value = "pageSize", defaultValue = AppConstants.DEFAULT_PAGE_SIZE, required = false) int pageSize,
			@RequestParam(value = "sortBy", defaultValue = AppConstants.DEFAULT_SORT_BY, required = false) String sortBy,
			@RequestParam(value = "sortDir", defaultValue = AppConstants.DEFAULT_SORT_DIRECTION, required = false) String sortDir
	){
		return sortieProduitService.filterByTypesortie(pageNo, pageSize, sortBy, sortDir, type);
	}

	@PostMapping("/search")
	public ResponseEntity<?> filterSortieProduit(@RequestBody SortieResponse sortieDto){
		return new ResponseEntity<>(sortieProduitService.getSortieProducts(sortieDto), HttpStatus.OK);
	}

	/** New request 
	@GetMapping("/getUsersBySortieProduitName")
	public ResponseEntity<List<UserResponseDto>> getUsersBySortieProduit(){
		List<UserResponseDto> usersResponseDtos = userRepository.getUsersBySortieProduit();
		return new ResponseEntity<>(usersResponseDtos, HttpStatus.OK);
	}
	
	**/
	
	@DeleteMapping("/{id}")
	//@RolesAllowed({"ROLE_ADMIN"})
	public ResponseEntity<Map<String, Boolean>> undoAppro(@PathVariable final Long id){
		sortieProduitService.deleteSortieProduct(id);
		Map<String, Boolean> response =new  HashMap<>();
    	response.put("La sortie a été annulée avec succès", Boolean.TRUE);
    	return ResponseEntity.ok(response);
	
	}
	/** @DeleteMapping("/delete/{id}")
	public ResponseEntity<SortieResponse> deletePrroduct(@PathVariable final Long id){
		SortieResponse sortieProduitResponseDto = sortieProduitService.deleteSortieProduit(id);
		return new ResponseEntity<>(sortieProduitResponseDto, HttpStatus.OK);
	
	}	**/
	
	@PutMapping("/{id}")
	public ResponseEntity<SortieResponse> editSortieProduit(@Valid @RequestBody final SortieDto sortieProduitRequestDto, @PathVariable final Long id){
		SortieResponse sortieProduitResponseDto = sortieProduitService.editSortieProduct(id, sortieProduitRequestDto);
		return new ResponseEntity<>(sortieProduitResponseDto, HttpStatus.OK);
	}

	@GetMapping("/export/excel")
	public ResponseEntity<byte[]> exportItemsToExcel(
			@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDateTime startDate,
			@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDateTime endDate,
			HttpServletResponse response) throws IOException {

		return new ResponseEntity<>(sortieProduitService.exportItemsToExcel(startDate, endDate, response), HttpStatus.OK);
	}

	@GetMapping("/filter")
	public ResponseEntity<?> rangeDates(
			@RequestParam(value = "pageNo", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER, required = false) int pageNo,
			@RequestParam(value = "pageSize", defaultValue = AppConstants.DEFAULT_PAGE_SIZE, required = false) int pageSize,
			@RequestParam(value = "sortBy", defaultValue = AppConstants.DEFAULT_SORT_BY, required = false) String sortBy,
			@RequestParam(value = "sortDir", defaultValue = AppConstants.DEFAULT_SORT_DIRECTION, required = false) String sortDir,
			@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDateTime startDate,
			@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDateTime endDate) throws IOException {

		return new ResponseEntity<>(sortieProduitService.filterRandeDates(pageNo,pageSize,sortBy,sortDir,startDate,endDate),HttpStatus.OK);
	}

}
