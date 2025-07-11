package com.team.dev.service.impl;

import com.team.dev.dto.CategoryRequestDto;
import com.team.dev.dto.CategoryResponseDto;
import com.team.dev.exception.ApiException;
import com.team.dev.mapper.Mapper;
import com.team.dev.model.Category;
import com.team.dev.model.Product;
import com.team.dev.model.User;
import com.team.dev.repository.CategoryRepository;
import com.team.dev.service.AuthService;
import com.team.dev.service.CategoryService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class CategoryServiceImpl implements CategoryService {

	private final CategoryRepository categoryRepository;
	private final AuthService service;

	
	@Autowired
	public CategoryServiceImpl(CategoryRepository categoryRepository, AuthService service) {
		this.categoryRepository = categoryRepository;
		this.service = service;
	}

	@Override
	public CategoryResponseDto addCategory(CategoryRequestDto categoryRequestDto) {
		Category category = new Category();

		User authenticatedUser = service.getAuthUser();
		Long idEntreprise = authenticatedUser.getEntreprise().getEntrepriseId();

		if(categoryRepository.existsCategorysByNom(categoryRequestDto.getNom())) {
			throw new ApiException(" Il existe bien une categorie avec cet nom : " +categoryRequestDto.getNom());
		}else {
			category.setNom(categoryRequestDto.getNom());
			category.setEntrepriseId(idEntreprise);
		}
			categoryRepository.save(category);
		return Mapper.categoryToCategoryResponseDto(category);
	}

	@Override
	public List<CategoryResponseDto> getCategories() {
		List<Category> categories = StreamSupport
				.stream(categoryRepository.findAll().spliterator(), false).collect(Collectors.toList());
		return Mapper.categoryToCategoryResponseDtos(categories);
	}

	@Override
	public Category getCategory(Long categoryId) {
		return categoryRepository.findById(categoryId).orElseThrow(
		() ->  new ApiException("Il n'existe pas une categorie avec id : " + categoryId));
	}
	
	@Override
	public CategoryResponseDto getCategoryById(Long categoryId) {
		Category category = getCategory(categoryId);
		return Mapper.categoryToCategoryResponseDto(category);
	}
	

	@Override
	public CategoryResponseDto deleteCategory(Long categoryId) {
		Category category = getCategory(categoryId);
		categoryRepository.delete(category);
		return Mapper.categoryToCategoryResponseDto(category);
	}

	@Transactional
	@Override
	public CategoryResponseDto editCategory(Long categoryId, CategoryRequestDto categoryRequestDto) {
		Category categoryToEdit = getCategory(categoryId);
		categoryToEdit.setNom(categoryRequestDto.getNom());
		
		return Mapper.categoryToCategoryResponseDto(categoryToEdit);
	}

	@Override
	public CategoryResponseDto getAllCategories(int pageNo, int pageSize, String sortBy, String sortDir, String keyword) {
		
		Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

		User authenticatedUser = service.getAuthUser();
		Long idEntreprise = authenticatedUser.getEntreprise().getEntrepriseId();


		// create Pageable instance
        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);

        Page<Category> categories = categoryRepository.findByNomContainingAndEntrepriseId(pageable, "%"+keyword+"%", idEntreprise);

        // get content for page object
        List<Category> listOfCategorys = categories.getContent();

        List<CategoryRequestDto> content= listOfCategorys.stream().map(category -> mapToDTO(category)).collect(Collectors.toList());


        CategoryResponseDto categoryResponseDto = new CategoryResponseDto();
        categoryResponseDto.setContent(content);
        categoryResponseDto.setPageNo(categories.getNumber());
        categoryResponseDto.setPageSize(categories.getSize());
        categoryResponseDto.setTotalElements(categories.getTotalElements());
        categoryResponseDto.setTotalPages(categories.getTotalPages());
        categoryResponseDto.setLast(categories.isLast());

        return categoryResponseDto;
	}

	
	// convert Entity into DTO
    private CategoryRequestDto mapToDTO(Category category){
      //  CategoryRequestDto categoryRequestDto = mapper.map(category, CategoryRequestDto.class);
        CategoryRequestDto categoryRequestDto = new CategoryRequestDto();
        categoryRequestDto.setId(category.getId());
        categoryRequestDto.setNom(category.getNom());
        /** new codes **/
        List<String> names = new ArrayList<>();
		List<Product> products = category.getProducts();
		for(Product product: products) {
			names.add(product.getName());
		}
		categoryRequestDto.setProductNames(names);
        
        return categoryRequestDto;
    }

    /** Search **/
	@Override
	public List<CategoryRequestDto> searchCategory(String keyword) {
		List<Category> categories = this.categoryRepository.findByNom(keyword);
		List<CategoryRequestDto> categoryRequestDtos=categories.stream().map(category -> mapToDTO(category)).collect(Collectors.toList());
		return categoryRequestDtos;
		
	}


}
