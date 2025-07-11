package com.team.dev.service.impl;

import com.team.dev.dto.ProductRequestDto;
import com.team.dev.dto.ProductResponseDto;
import com.team.dev.exception.ApiException;
import com.team.dev.exception.ResourceNotFoundException;
import com.team.dev.mapper.Mapper;
import com.team.dev.model.Category;
import com.team.dev.model.Magasin;
import com.team.dev.model.Product;
import com.team.dev.model.User;
import com.team.dev.repository.ProductRepository;
import com.team.dev.service.*;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class ProductServiceImpl implements ProductService {

	
	private final ProductRepository productRepository;
	private final CategoryService categoryService;
	private final AuthService userService;
	private final MagasinService magasinService;
	private  final FileService fileService;

	@Value("${project.image}")
	private String path;
	private final AuthService service;


	@Autowired
	public ProductServiceImpl(ProductRepository productRepository,
							  CategoryService categoryService,
							  AuthService userService,
							  MagasinService magasinService,
							  final FileService fileService, AuthService service) {
		
		this.productRepository = productRepository;
		this.categoryService = categoryService;
		this.userService = userService;
		this.magasinService = magasinService;
		this.fileService = fileService;
		this.service = service;
	}
	
	@Transactional     
	@Override
	public ProductResponseDto addProduct(ProductRequestDto productRequestDto)throws Exception {
		Product product = new Product();
		User authenticatedUser = service.getAuthUser();
		Long idEntreprise = authenticatedUser.getEntreprise().getEntrepriseId();

		if(productRepository.existsProductByCode(productRequestDto.getCode())) {
			throw new ApiException(" Il existe bien un produit avec cet code : " +productRequestDto.getCode());
		}else {
		product.setName(productRequestDto.getName());
		product.setEntrepriseId(idEntreprise);
		product.setUser(authenticatedUser);
		product.setPrice(productRequestDto.getPrice());
		product.setQte(BigDecimal.valueOf(productRequestDto.getQte()));
		product.setCode(productRequestDto.getCode());
		product.setImageName(productRequestDto.getImageName());
		}
		if(productRequestDto.getCategoryId() == null ) {
			throw new IllegalArgumentException("Le produit manque de Categorie !");
		}else {
			Category category = categoryService.getCategory(productRequestDto.getCategoryId());
			product.setCategory(category);
		}
		
		if(productRequestDto.getMagasinId() == null) {
			throw new IllegalArgumentException("Le produit manque de Magasin !");
			}else {

				Magasin magasin = magasinService.getMagasin(productRequestDto.getMagasinId());
				product.setMagasin(magasin);
			}
		
		/*if(productRequestDto.getUserId() == null) {
			throw new IllegalArgumentException("Le produit manque du nom de l'utilisateur !");
		}else {
			User user = userService.getUser(productRequestDto.getUserId());;
			product.setUser(user);
		}*/
		
		product.setName(product.getName());
		product.setPrice(product.getPrice());
		product.setQte(product.getQte());
		product.setCode(product.getCode());
		product.setImageName(product.getImageName());
		
		Product product1 = productRepository.save(product);
		return Mapper.productToProductResponseDto(product1);
	}

	@Override
	public ProductResponseDto getProductById(Long productId) {
		Product product = getProduct(productId);
		return Mapper.productToProductResponseDto(product);
	}

	@Override
	public ProductRequestDto getProduit(Long productId) {
		Product product = getProduct(productId);
		return Mapper.productToProductRequestDto(product);
	}

	@Override
	public Product getProduct(Long productId) {
		Product product = productRepository.findById(productId)
				.orElseThrow(() -> new ResourceNotFoundException("Il n'existe pas de product avec id : " + productId));
		return product;
	}


	@Override
	public ProductResponseDto deleteProduct(Long productId) {
		Product product = getProduct(productId);
		productRepository.delete(product);
		return Mapper.productToProductResponseDto(product);
	}

	@Transactional
	@Override
	public ProductResponseDto editProduct(Long productId, ProductRequestDto productRequestDto)throws Exception {
		Product productEdit = getProduct(productId);
		productEdit.setName(productRequestDto.getName());
		productEdit.setPrice(productRequestDto.getPrice());
		productEdit.setQte(BigDecimal.valueOf(productRequestDto.getQte()));
		productEdit.setImageName(productRequestDto.getImageName());
		productEdit.setCode(productRequestDto.getCode());
		productEdit.setImageName((productRequestDto.getImageName()));

		if(productRequestDto.getCategoryId() != null ) {
			Category category = categoryService.getCategory(productRequestDto.getCategoryId());
			productEdit.setCategory(category);
		}
		if(productRequestDto.getMagasinId() != null ) {
			Magasin magasin = magasinService.getMagasin(productRequestDto.getMagasinId());
			productEdit.setMagasin(magasin);
		}
		
		if(productRequestDto.getUserId() != null ) {
			User user = userService.getUser(productRequestDto.getUserId());
			productEdit.setUser(user);
		}
		
		return Mapper.productToProductResponseDto(productEdit);
	}

	@Override
	public ProductResponseDto addCategoryToProduct(Long productId, Long categoryId) {
		Product product = getProduct(productId);
		Category category = categoryService.getCategory(categoryId);
		if(Objects.nonNull(product.getCategory())) {
			throw new IllegalArgumentException("Il exist déjà un product avec cette categorie");
		}
		product.setCategory(category);
		category.addProduct(product);
		return Mapper.productToProductResponseDto(product);
	}

	@Override
	public ProductResponseDto addMagasinToProduct(Long productId, Long magasinId) {
		Product product = getProduct(productId);
		Magasin magasin = magasinService.getMagasin(magasinId);		
		if(Objects.nonNull(product.getMagasin())) {
			throw new IllegalArgumentException("Il exist déjà un product avec cet Magasin");
		}
		product.setMagasin(magasin);
		magasin.addProduct(product);
		return Mapper.productToProductResponseDto(product);
	}

	@Override
	public ProductResponseDto removeCategoryFromProduct(Long productId, Long categoryId) {
		Product product = getProduct(productId);
		Category category = categoryService.getCategory(categoryId);
		if(!(Objects.nonNull(product.getCategory()))) {
			throw new ResourceNotFoundException("Il exist pas un product avec cette categorie ID " +categoryService.getCategory(categoryId));
			//IllegalArgumentException("Il exist pas un product avec cette categorie ID " +categoryService.getCategory(categoryId));
		}
		product.setCategory(null);
		category.removeProduct(product);
		return Mapper.productToProductResponseDto(product);
	}

	@Override
	public ProductResponseDto removeMagasinFromProduct(Long productId, Long magasinId) {
		Product product = getProduct(productId);
		Magasin magasin = magasinService.getMagasin(magasinId);
		if(!(Objects.nonNull(product.getMagasin()))) {
			throw new IllegalArgumentException("Il exist pas un product dans cet magasin avec cet ID :" + magasinService.getMagasin(magasinId));
		}
		product.setMagasin(null);
		magasin.removeProduct(product);
		return Mapper.productToProductResponseDto(product);
	}


	@Override
	public List<ProductResponseDto> getProducts() {
		List<Product> products = StreamSupport
				.stream(productRepository.findAll().spliterator(), false)
				.collect(Collectors.toList());
		
		return Mapper.productToProductResponseDtos(products);
	}

	@Override
	public ProductResponseDto addUserToProduct(Long productId, Long userId) {
		
		Product product = getProduct(productId);
		User user = userService.getUser(userId);		
		if(Objects.nonNull(product.getUser())) {
			throw new IllegalArgumentException("Il exist déjà un product avec cet user");
		}
		product.setUser(user);
		user.addProduct(product);
		return Mapper.productToProductResponseDto(product);
	}

	@Override
	public ProductResponseDto removeUserFromProduct(Long productId, Long userId) {
		
		Product product = getProduct(productId);
		User user = userService.getUser(userId);
		if(!(Objects.nonNull(product.getUser()))) {
			throw new IllegalArgumentException("Il exist pas un product avec cet user ID " +userService.getUser(userId));
		}
		product.setUser(null);
		user.removeProduct(product);
		return Mapper.productToProductResponseDto(product);
	}

	@Override
	public ProductResponseDto getAllProducts(int pageNo, int pageSize, String sortBy, String sortDir, String keyword) {
		
		Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

		User authenticatedUser = service.getAuthUser();
		Long idEntreprise = authenticatedUser.getEntreprise().getEntrepriseId();

		// create Pageable instance
        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);
        
        Page<Product> products = productRepository.findByNameContainingAndEntrepriseId(pageable, "%"+keyword+"%", idEntreprise);
        
        List<Product> listOfProducts = products.getContent();
        
        List<ProductRequestDto> content = listOfProducts.stream().map(this::mapToDTO).collect(Collectors.toList());
		
        ProductResponseDto productResponseDto = new ProductResponseDto();
        productResponseDto.setContent(content);
        productResponseDto.setPageNo(products.getNumber());
        productResponseDto.setPageSize(products.getSize());
        productResponseDto.setTotalElements(products.getTotalElements());
        productResponseDto.setTotalPages(products.getTotalPages());
        productResponseDto.setLast(products.isLast());

        return productResponseDto;
	}

	 // convert Entity into DTO
    private ProductRequestDto mapToDTO(Product product){
      //  ProductRequestDto productDto = mapper.map(product, ProductRequestDto.class);
        ProductRequestDto productDto = new ProductRequestDto();
        productDto.setId(product.getId());
        productDto.setName(product.getName());
        productDto.setCode(product.getCode());
        productDto.setPrice(product.getPrice());
        productDto.setQte(product.getQte().doubleValue());
		productDto.setImageName(product.getImageName());
        productDto.setMagasinNom(product.getMagasin().getNomMagasin());
        productDto.setCategoryNom(product.getCategory().getNom());
       productDto.setEmail(product.getUser().getEmail());
        
//        productDto.setDescription(product.getDescription());
//        productDto.setContent(product.getContent());
        return productDto;
    }

    /** Search **/
	@Override
	public List<ProductRequestDto> searchProduct(String keyword) {
		List<Product> products = this.productRepository.findByName(keyword);
		//List<Product> products = this.productRepository.findAll(keyword);
		List<ProductRequestDto> productRequestDtos=products.stream().map(this::mapToDTO).collect(Collectors.toList());
		return productRequestDtos;
		
		
	}
}
