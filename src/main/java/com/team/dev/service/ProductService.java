package com.team.dev.service;

import com.team.dev.dto.ProductRequestDto;
import com.team.dev.dto.ProductResponseDto;
import com.team.dev.model.Product;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;



public interface ProductService{
	
	public ProductResponseDto addProduct(ProductRequestDto productRequestDto)throws Exception ;
	public ProductResponseDto getProductById(Long productId);
	public Product getProduct(Long productId);
	public List<ProductResponseDto> getProducts();
	public ProductResponseDto getAllProducts(int pageNo, int pageSize, String sortBy, String sortDir, String keyword);
	public ProductResponseDto deleteProduct(Long productId);
	public ProductRequestDto getProduit(Long productId);
	public ProductResponseDto editProduct(Long productId, ProductRequestDto productRequestDto)throws Exception;
	
	public ProductResponseDto addCategoryToProduct(Long productId, Long categoryId);
	public ProductResponseDto addMagasinToProduct(Long productId, Long magasinId);
	
	public ProductResponseDto removeCategoryFromProduct(Long productId, Long categoryId);
	public ProductResponseDto removeMagasinFromProduct(Long productId, Long magasinId);
	public ProductResponseDto addUserToProduct(Long productId, Long userId);
	public ProductResponseDto removeUserFromProduct(Long productId, Long userId);
	
	public List<ProductRequestDto> searchProduct(String keyword);
}
