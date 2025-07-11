package com.team.dev.service;

import com.team.dev.dto.CategoryRequestDto;
import com.team.dev.dto.CategoryResponseDto;
import com.team.dev.model.Category;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface CategoryService{

	public Category getCategory(Long categoryId);
	public CategoryResponseDto addCategory(CategoryRequestDto categoryRequestDto );
	public List<CategoryResponseDto> getCategories();	
	public CategoryResponseDto getAllCategories(int pageNo, int pageSize, String sortBy, String sortDir, String keyword);
	public CategoryResponseDto getCategoryById(Long categoryId);
	public CategoryResponseDto deleteCategory(Long categoryId);
	public CategoryResponseDto editCategory(Long categoryId, CategoryRequestDto categoryRequestDto);
	
	public List<CategoryRequestDto> searchCategory(String keyword);
}
