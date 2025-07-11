package com.team.dev.controller;

import com.team.dev.dto.CategoryResponseDto;
import com.team.dev.dto.MagasinResponseDto;
import com.team.dev.dto.ProductRequestDto;
import com.team.dev.dto.ProductResponseDto;
import com.team.dev.repository.CategoryRepository;
import com.team.dev.repository.MagasinRepository;
import com.team.dev.repository.UserRepository;
import com.team.dev.service.FileService;
import com.team.dev.service.ProductService;
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

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/products")
@CrossOrigin(origins = "http://localhost:4200")
public class ProductController {

	@Value("${project.image}")
	private String path;
	private final ProductService productService;
	private final MagasinRepository magasinRepository;
	private final CategoryRepository categoryRepository;
	private final FileService fileService;

	public ProductController(ProductService productService, UserRepository userRepository,
							 MagasinRepository magasinRepository, CategoryRepository categoryRepository
			, FileService fileService) {
		this.productService = productService;
		this.magasinRepository = magasinRepository;
		this.categoryRepository = categoryRepository;
		this.fileService = fileService;

	}

	@PostMapping
	//@RolesAllowed({"ROLE_ADMIN"})
	public ResponseEntity<ProductResponseDto> addProduct(@Valid @RequestBody ProductRequestDto productRequestDto) throws Exception {
		ProductResponseDto productResponseDto = productService.addProduct(productRequestDto);
		return new ResponseEntity<ProductResponseDto>(productResponseDto, HttpStatus.OK);
	}

	@GetMapping("/{id}")
	public ResponseEntity<ProductResponseDto> getProduct(@PathVariable final Long id){
		ProductResponseDto productResponseDto = productService.getProductById(id);
		return new ResponseEntity<ProductResponseDto>(productResponseDto, HttpStatus.OK);
	}

	@GetMapping("/getAll")
	public ResponseEntity<List<ProductResponseDto>> getProducts(){
		List<ProductResponseDto> productResponseDtos = productService.getProducts();
		return new ResponseEntity<>(productResponseDtos, HttpStatus.OK);
	}

	@GetMapping
	public ProductResponseDto getAllProducts(
			@RequestParam(value = "keywords", defaultValue = AppConstants.DEFAULT_KEY_WORDS, required = false) String keywords,
			@RequestParam(value = "pageNo", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER, required = false) int pageNo,
			@RequestParam(value = "pageSize", defaultValue = AppConstants.DEFAULT_PAGE_SIZE, required = false) int pageSize,
			@RequestParam(value = "sortBy", defaultValue = AppConstants.DEFAULT_SORT_BY, required = false) String sortBy,
			@RequestParam(value = "sortDir", defaultValue = AppConstants.DEFAULT_SORT_DIRECTION, required = false) String sortDir
	){
		return productService.getAllProducts(pageNo, pageSize, sortBy, sortDir, keywords);
	}
	/** New request **/
//	@GetMapping("/getUsersByProductName")
//	public ResponseEntity<List<AuthenticateResponse>> getUsersByProduct(){
//		List<UserResponseDto> usersResponseDtos = userRepository.getUsersByProduct();
//		return new ResponseEntity<>(usersResponseDtos, HttpStatus.OK);
//	}

	/** New request **/
	@GetMapping("/magasin-produit")
	public ResponseEntity<List<MagasinResponseDto>> getMagasinsByProduct(){
		List<MagasinResponseDto> usersResponseDtos = magasinRepository.getMagasinsByProduct();
		return new ResponseEntity<>(usersResponseDtos, HttpStatus.OK);
	}

	/** New request **/
	@GetMapping("/category-produit")
	public ResponseEntity<List<CategoryResponseDto>> getCategorysByProductName(){
		List<CategoryResponseDto> categoryResponseDtos = categoryRepository.getCategorysByProduct();
		return new ResponseEntity<>(categoryResponseDtos, HttpStatus.OK);
	}


	@DeleteMapping("/{id}")
	//@RolesAllowed({"ROLE_ADMIN"})
	public ResponseEntity<Map<String, Boolean>> deletePrroduct(@PathVariable final Long id){
		productService.deleteProduct(id);
		Map<String, Boolean> response =new  HashMap<>();
		response.put("Le produit a été supprimé avec succès", Boolean.TRUE);
		return ResponseEntity.ok(response);

	}

	@PutMapping("/{id}")
	public ResponseEntity<ProductResponseDto> editProduct(@Valid @RequestBody final ProductRequestDto productRequestDto, @PathVariable final Long id) throws Exception{
		ProductResponseDto productResponseDto = productService.editProduct(id, productRequestDto);
		return new ResponseEntity<>(productResponseDto, HttpStatus.OK);
	}

	/* Update image by product */
	@PostMapping("/image/{id}")
	public ResponseEntity<ProductResponseDto> uploadProductImage(
			@RequestParam("image") MultipartFile image,
			@PathVariable Long id
	) throws Exception {
		String fileName = this.fileService.uploadImage(path, image);
		ProductRequestDto product = this.productService.getProduit(id);
		product.setImageName(fileName);
		ProductResponseDto updateProduct = this.productService.editProduct(id, product);
		return new ResponseEntity<>(updateProduct, HttpStatus.OK);
	}

	//Get image by name
	@GetMapping(value = "/image/{imageName}", produces = MediaType.IMAGE_JPEG_VALUE)
	public void downloadImage(
			@PathVariable("imageName") String imageName, HttpServletResponse response
	) throws IOException {

		InputStream resource = this.fileService.getRessource(path, imageName);
		response.setContentType(MediaType.IMAGE_JPEG_VALUE);
		StreamUtils.copy(resource, response.getOutputStream());
	}

	@PostMapping("/add-category/{categoryId}/{productId}")
	public  ResponseEntity<ProductResponseDto> addCategory(@RequestBody final Long categoryId, @PathVariable final Long productId){
		ProductResponseDto productResponseDto = productService.addCategoryToProduct(productId, categoryId);
		return new ResponseEntity<>(productResponseDto, HttpStatus.OK);

	}
	@PostMapping("/add-magasin/{magasinId}/{productId}")
	public  ResponseEntity<ProductResponseDto> addMagasin(@RequestBody final Long magasinId, @PathVariable final Long productId){
		ProductResponseDto productResponseDto = productService.addMagasinToProduct(productId, magasinId);
		return new ResponseEntity<>(productResponseDto, HttpStatus.OK);
	}

	@PostMapping("/add-user/{userId}/{productId}")
	public  ResponseEntity<ProductResponseDto> addUser(@RequestBody final Long userId, @PathVariable final Long productId){
		ProductResponseDto productResponseDto = productService.addUserToProduct(productId, userId);
		return new ResponseEntity<>(productResponseDto, HttpStatus.OK);

	}

	@PostMapping("/remove-category/{categoryId}/{productId}")
	//@RolesAllowed({"ROLE_ADMIN"})
	public  ResponseEntity<ProductResponseDto> removeCategory(@PathVariable final Long categoryId, @PathVariable final Long productId){
		ProductResponseDto productResponseDto = productService.removeCategoryFromProduct(productId, categoryId);
		return new ResponseEntity<>(productResponseDto, HttpStatus.OK);
	}

	@PostMapping("/remove-magasin/{magasinId}/{productId}")
	//@RolesAllowed({"ROLE_ADMIN"})
	public  ResponseEntity<ProductResponseDto> removeMagasin(@PathVariable final Long magasinId, @PathVariable final Long productId){
		ProductResponseDto productResponseDto = productService.removeMagasinFromProduct(productId, magasinId);
		return new ResponseEntity<>(productResponseDto, HttpStatus.OK);
	}

	@PostMapping("/remove-user/{userId}/{productId}")
	//@RolesAllowed({"ROLE_ADMIN"})
	public  ResponseEntity<ProductResponseDto> removeUser(@PathVariable final Long userId, @PathVariable final Long productId){
		ProductResponseDto productResponseDto = productService.removeUserFromProduct(productId, userId);
		return new ResponseEntity<>(productResponseDto, HttpStatus.OK);
	}

	/** Serach **/
	/*@GetMapping("/{keywords}")
	public  ResponseEntity<List<ProductRequestDto>> searchProductByName(@PathVariable("keywords") String keywords){
		List<ProductRequestDto> result= productService.searchProduct(keywords);
		return new ResponseEntity<List<ProductRequestDto>>(result, HttpStatus.OK);
	}*/
}