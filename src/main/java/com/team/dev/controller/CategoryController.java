package com.team.dev.controller;

import com.team.dev.dto.CategoryRequestDto;
import com.team.dev.dto.CategoryResponseDto;
import com.team.dev.dto.ErrorDetails;
import com.team.dev.service.CategoryService;
import com.team.dev.utils.AppConstants;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/api/v1/categories")
@CrossOrigin(origins = "http://localhost:4200")
public class CategoryController {
	
	private final CategoryService categoryService;

	@Autowired
	public CategoryController(CategoryService categoryService) {
		this.categoryService = categoryService;
	}
	
	@PostMapping
	public ResponseEntity<CategoryResponseDto> addCategory(@Valid @RequestBody final CategoryRequestDto categoryRequestDto){
		CategoryResponseDto categoryResponseDto = categoryService.addCategory(categoryRequestDto);
		return new ResponseEntity<>(categoryResponseDto, HttpStatus.OK);
	}
	
//	@GetMapping("/getAll")
//	public ResponseEntity<List<CategoryResponseDto>> getCategories(){
//		List<CategoryResponseDto> categoryResponseDto = categoryService.getCategories();
//		return new ResponseEntity<>(categoryResponseDto, HttpStatus.OK);
//	} 
	
	@GetMapping
    public CategoryResponseDto getAllCategories(
            @RequestParam(value = "keywords", defaultValue = AppConstants.DEFAULT_KEY_WORDS, required = false) String keywords,
            @RequestParam(value = "pageNo", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER, required = false) int pageNo,
            @RequestParam(value = "pageSize", defaultValue = AppConstants.DEFAULT_PAGE_SIZE, required = false) int pageSize,
            @RequestParam(value = "sortBy", defaultValue = AppConstants.DEFAULT_SORT_BY, required = false) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = AppConstants.DEFAULT_SORT_DIRECTION, required = false) String sortDir
    ){
        return categoryService.getAllCategories(pageNo, pageSize, sortBy, sortDir, keywords);
    }
	
	@GetMapping("/{id}")
	public ResponseEntity<CategoryResponseDto> getCategory(@PathVariable final Long id){
		CategoryResponseDto categoryResponseDto = categoryService.getCategoryById(id);
			return new ResponseEntity<>(categoryResponseDto, HttpStatus.OK);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<ErrorDetails> deleteCategory(@PathVariable final Long id){
		CategoryResponseDto categoryResponseDto = categoryService.deleteCategory(id);
		return new ResponseEntity<ErrorDetails>(new ErrorDetails(new Date(), "La categorie a été supprimée avec succès !!", ""), HttpStatus.OK);
	}
	
	@PutMapping("/{id}")
	public ResponseEntity<CategoryResponseDto> editCategory(@RequestBody final CategoryRequestDto categoryRequestDto, @PathVariable final Long id){
		CategoryResponseDto categoryResponseDto = categoryService.editCategory(id, categoryRequestDto);
		return new ResponseEntity<>(categoryResponseDto, HttpStatus.OK);
		
	}
	
	/** Serach **/
	@GetMapping("/search/{keywords}")
	public  ResponseEntity<List<CategoryRequestDto>> searchCategoryByNom(@PathVariable("keywords") String keywords){
		List<CategoryRequestDto> result= categoryService.searchCategory(keywords);
		return new ResponseEntity<List<CategoryRequestDto>>(result, HttpStatus.OK);
	
	}
}
