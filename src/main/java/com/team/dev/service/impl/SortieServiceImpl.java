package com.team.dev.service.impl;

import com.team.dev.dto.SortieDto;
import com.team.dev.dto.SortieResponse;
import com.team.dev.dto.TypeSortie;
import com.team.dev.exception.ApiException;
import com.team.dev.mapper.Mapper;
import com.team.dev.model.Product;
import com.team.dev.model.SortieProduit;
import com.team.dev.model.User;
import com.team.dev.repository.ProductRepository;
import com.team.dev.repository.SortieProduitRepository;
import com.team.dev.repository.UserRepository;
import com.team.dev.service.AuthService;
import com.team.dev.service.ProductService;
import com.team.dev.service.SortieProduitService;
import jakarta.persistence.criteria.Predicate;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.convert.QueryByExamplePredicateBuilder;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class SortieServiceImpl implements SortieProduitService {

	private final SortieProduitRepository sortieProduitRepository;
	private final ProductRepository productRepository;
	private final ProductService produitService;
	private final UserRepository userRepository;
	private final AuthService userService;
	private final AuthService service;


	public SortieServiceImpl(SortieProduitRepository sortieProduitRepository,
							 ProductRepository productRepository, ProductService produitService,
							 UserRepository userRepository,
							 AuthService userService, AuthService service) {
		this.sortieProduitRepository = sortieProduitRepository;
		this.productRepository = productRepository;
		this.produitService = produitService;
		this.userRepository = userRepository;
		this.userService = userService;
		this.service = service;
	}

	@Transactional
	@Override
	public SortieResponse addSortieProduct(SortieDto sortieDto) {
		SortieProduit sortieProduit = new SortieProduit();
		//sortieDto.setTypeSortie(TypeSortie.SORTIE);
		BigDecimal qteRestante;
		BigDecimal valSeuil = BigDecimal.valueOf(2);
		User authenticatedUser = service.getAuthUser();
		Long idEntreprise = authenticatedUser.getEntreprise().getEntrepriseId();

		Product produit = produitService.getProduct(sortieDto.getProductId());


		if(sortieDto.getTypeSortie().equals(TypeSortie.SORTIE) && (produit.getQte().subtract(sortieDto.getQuantite())).compareTo(valSeuil) < 0 ) {
			throw new ApiException(" La quantité démandée n'est pas disponible : " +sortieDto.getQuantite());
		}else {
			sortieProduit.setQuantite(sortieDto.getQuantite());
			//sortieProduit.setProduct(product);
			sortieProduit.setDateSortie(sortieDto.getDateSortie());
		}

		if(sortieDto.getProductId() == null ) {
			throw new IllegalArgumentException("Veuillez selectioner le produit !");
		}else {
			if (sortieDto.getTypeSortie().equals(TypeSortie.SORTIE)) {
				qteRestante = produit.getQte().subtract(sortieDto.getQuantite());
				produit.setQte(qteRestante);
			} else if (sortieDto.getTypeSortie().equals(TypeSortie.ENTREE)) {
				qteRestante = produit.getQte().add(sortieDto.getQuantite());
				produit.setQte(qteRestante);
			}
			sortieProduit.setProduct(produit);
		}

		if(sortieDto.getTypeSortie() == null ) {
			throw new IllegalArgumentException("Selectionnez le type mouvement de stock !");
		}

		if(sortieDto.getUserId() == null) {
			throw new IllegalArgumentException("Il manque le nom du magasinier !");
		}else {
			User user = userService.getUser(sortieDto.getUserId());;
			sortieProduit.setUser(user);
		}


		//sortieProduit.setDateSortie(LocalDateTime.now());
		sortieProduit.setTypeSortie(sortieDto.getTypeSortie());
		sortieProduit.setDateSortie(sortieProduit.getDateSortie());
		sortieProduit.setQuantite(sortieProduit.getQuantite());
		sortieProduit.setEntrepriseId(idEntreprise);

		SortieProduit sortieProduit1 = sortieProduitRepository.save(sortieProduit);;
//		sortieProduit1 = sortieProduitRepository.save(sortieProduit);

		return Mapper.sortieProductToSortieProductResponse(sortieProduit1);

	}

	@Override
	public SortieResponse getProductById(Long sortieId) {
		SortieProduit sortieProduit = getProduct(sortieId);
		return Mapper.sortieProductToSortieProductResponse(sortieProduit);
	}

	@Override
	public List<SortieResponse> mvtStockProduct(Long productId) {
		List<SortieProduit> allById = sortieProduitRepository.findAllByProductId(productId);
		return Mapper.sortieToSortieResponses(allById);
	}

	@Override
	public SortieProduit getProduct(Long sortieId) {
		SortieProduit sortieProduit = sortieProduitRepository.findById(sortieId)
				.orElseThrow(() -> new ApiException("Il n'existe pas dans les produits stockés dont ID : " + sortieId));
		return sortieProduit;
	}

	public Specification<SortieProduit> getSpecFromDatesAndExample(
			LocalDateTime from, LocalDateTime to, Example<SortieProduit> example) {

		return (Specification<SortieProduit>) (root, query, builder) -> {
			final List<Predicate> predicates = new ArrayList<>();

			if (from != null) {
				predicates.add(builder.greaterThan(root.get("dateSortie"), from));
			}
			if (to != null) {
				predicates.add(builder.lessThan(root.get("dateSortie"), to));
			}
			if (to != null) {
				predicates.add(builder.lessThan(root.get("dateSortie"), to));
			}

			if (example != null) {
				Predicate examplePredicate = QueryByExamplePredicateBuilder.getPredicate(root, builder, example);
				if (examplePredicate != null) {
					predicates.add(examplePredicate);
				}
			}

			//predicates.add(QueryByExamplePredicateBuilder.getPredicate(root, builder, example));

			return builder.and(predicates.toArray(new Predicate[predicates.size()]));
		};
	};

	@Override
	public SortieResponse getSortieProducts( SortieResponse response) {

		LocalDateTime dateSortie = response.getDateSortie();
		LocalDateTime to = response.getDateEnd(); // Define the 'to' date as needed

		   SortieProduit sortie = new SortieProduit();
		   sortie.setTypeSortie(response.getTypeSortie());
		   sortie.setDateSortie(response.getDateSortie());

		ExampleMatcher matcher= ExampleMatcher.matchingAll()
				.withMatcher("typeSortie", ExampleMatcher.GenericPropertyMatchers.contains().ignoreCase())
				.withMatcher("dateSortie", ExampleMatcher.GenericPropertyMatchers.contains().ignoreCase())
				.withNullHandler(ExampleMatcher.NullHandler.IGNORE)
				.withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING)
				.withIgnoreCase()
				.withIgnoreNullValues();
		Example<SortieProduit> example = Example.of(sortie, matcher);
		List<SortieProduit> list = sortieProduitRepository.findAll(getSpecFromDatesAndExample(dateSortie, to, example));
		List<SortieDto> content = list.stream().map(this::mapToDTO).collect(Collectors.toList());

		SortieResponse sortieResponse = new SortieResponse();
		sortieResponse.setContent(content);

		return sortieResponse;
	}

	@Override
	public BigDecimal stockReelPructs(Long idProduct) {
		produitService.getProductById(idProduct);
		return this.sortieProduitRepository.stockReelProduct(idProduct);
	}

	@Override
	public SortieResponse getAllSortieProducts(int pageNo, int pageSize, String sortBy, String sortDir) {
		Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending()
	            : Sort.by(sortBy).descending();

	    // create Pageable instance
	    Pageable pageable = PageRequest.of(pageNo, pageSize, sort);
	    
	    Page<SortieProduit> sortieProducts = sortieProduitRepository.findAll(pageable);
	    
	    List<SortieProduit> listOfProducts = sortieProducts.getContent();
	    
	    List<SortieDto> content = listOfProducts.stream().map(this::mapToDTO).collect(Collectors.toList());
		
	    SortieResponse sortieResponse = new SortieResponse();
	    sortieResponse.setContent(content);
	    sortieResponse.setPageNo(sortieProducts.getNumber());
	    sortieResponse.setPageSize(sortieProducts.getSize());
	    sortieResponse.setTotalElements(sortieProducts.getTotalElements());
	    sortieResponse.setTotalPages(sortieProducts.getTotalPages());
	    sortieResponse.setLast(sortieProducts.isLast());
		return sortieResponse;
	    
	}

	@Override
	public SortieResponse deleteSortieProduct(Long sortieId) {
		BigDecimal qteRestante ;
		
		SortieProduit sortieProduit = getProduct(sortieId);
		Product produit = produitService.getProduct(sortieProduit.getProduct().getId());
		if(produit != null) {
			qteRestante = produit.getQte().add(sortieProduit.getQuantite());
			produit.setQte(qteRestante);
		}
		sortieProduitRepository.delete(sortieProduit);
		return Mapper.sortieProductToSortieProductResponse(sortieProduit);
	}

	@Transactional
	@Override
	public SortieResponse editSortieProduct(Long sortieId, SortieDto sortieDto) {
		BigDecimal qteRestante ;
		BigDecimal qteEdit;
		SortieProduit sortieProduitEdit = getProduct(sortieId);
		sortieProduitEdit.setDateSortie(sortieDto.getDateSortie());
		
		if(sortieDto.getProductId() != null) {
			Product product = produitService.getProduct(sortieDto.getProductId());
			qteRestante = product.getQte().add(sortieProduitEdit.getQuantite()) ;
			qteEdit = qteRestante.subtract(sortieDto.getQuantite());
			product.setQte(qteEdit);
			//product.setEntrepriseId(sortieDto.getIdEntreprise());
			sortieProduitEdit.setProduct(product);
		}
		sortieProduitEdit.setQuantite(sortieDto.getQuantite());
		
		if(sortieDto.getUserId() != null ) {
			User user = userService.getUser(sortieDto.getUserId());
			sortieProduitEdit.setUser(user);
		}
		
		return Mapper.sortieProductToSortieProductResponse(sortieProduitEdit);
	}

	@Override
	public SortieResponse searchSortieProduitFull(int pageNo, int pageSize, String sortBy, String sortDir
												) {
		Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

		User authenticatedUser = service.getAuthUser();
		Long idEntreprise = authenticatedUser.getEntreprise().getEntrepriseId();

		// create Pageable instance
        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);
        
        Page<SortieProduit> sorties = sortieProduitRepository.findAllByEntrepriseIdAndUserId(pageable, idEntreprise, authenticatedUser.getId());
        
        List<SortieProduit> listOfSortieProduits = sorties.getContent();
        
        List<SortieDto> content = listOfSortieProduits.stream().map(sortieProduit -> mapToDTO(sortieProduit)).collect(Collectors.toList());
        
        SortieResponse sortieResponse = new SortieResponse();
	    sortieResponse.setContent(content);
	    sortieResponse.setPageNo(sorties.getNumber());
	    sortieResponse.setPageSize(sorties.getSize());
	    sortieResponse.setTotalElements(sorties.getTotalElements());
	    sortieResponse.setTotalPages(sorties.getTotalPages());
	    sortieResponse.setLast(sorties.isLast());
		return sortieResponse;
	}

	@Override
	public SortieResponse filterByTypesortie(int pageNo, int pageSize, String sortBy, String sortDir, TypeSortie keyword)
	{
		Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending()
				: Sort.by(sortBy).descending();

		//SortieProduit sortie= new SortieProduit();
		User authenticatedUser = service.getAuthUser();
		Long idEntreprise = authenticatedUser.getEntreprise().getEntrepriseId();

		// create Pageable instance
		Pageable pageable = PageRequest.of(pageNo, pageSize, sort);

		Page<SortieProduit> sorties = sortieProduitRepository.findSortieProduitsByTypeSortieAndEntrepriseIdAndUserId(pageable, keyword,idEntreprise, authenticatedUser.getId());

		List<SortieProduit> listOfSortieProduits = sorties.getContent();

		List<SortieDto> content = listOfSortieProduits.stream().map(sortieProduit -> mapToDTO(sortieProduit)).collect(Collectors.toList());

		SortieResponse sortieResponse = new SortieResponse();
		sortieResponse.setContent(content);
		sortieResponse.setPageNo(sorties.getNumber());
		sortieResponse.setPageSize(sorties.getSize());
		sortieResponse.setTotalElements(sorties.getTotalElements());
		sortieResponse.setTotalPages(sorties.getTotalPages());
		sortieResponse.setLast(sorties.isLast());
		return sortieResponse;
	}


	// Méthode pour générer un fichier Excel avec les éléments filtrés
	public byte[] exportItemsToExcel(LocalDateTime startDate, LocalDateTime endDate, HttpServletResponse response) throws IOException {
		// Récupérer les éléments filtrés par date
		User authenticatedUser = service.getAuthUser();
		Long idEntreprise = authenticatedUser.getEntreprise().getEntrepriseId();

		List<SortieResponse> items = Mapper.sortieToSortieResponses(sortieProduitRepository.findAllByEntrepriseIdAndUserIdAndDateSortieBetween(idEntreprise, authenticatedUser.getId(), startDate, endDate));

		byte[] byteArray;
		try {
			// Créer un classeur Excel
			Workbook workbook = new XSSFWorkbook();
			Sheet sheet = workbook.createSheet("MvtStock");

			// Ajouter les en-têtes dans le fichier Excel
			Row headerRow = sheet.createRow(0);
			headerRow.createCell(0).setCellValue("ID");
			headerRow.createCell(1).setCellValue("Quantité");
			headerRow.createCell(2).setCellValue("Unité");
			headerRow.createCell(3).setCellValue("DateSortie");
			headerRow.createCell(4).setCellValue("Mouvement");
			headerRow.createCell(5).setCellValue("Produit");
			headerRow.createCell(6).setCellValue("Agent");

			// Remplir les données
			int rowNum = 1;
			for (SortieResponse item : items) {
				Row row = sheet.createRow(rowNum++);
				row.createCell(0).setCellValue(item.getId());
				row.createCell(1).setCellValue(item.getQuantite());
				row.createCell(2).setCellValue("");
				row.createCell(3).setCellValue(item.getDateSortie().toString());
				row.createCell(4).setCellValue(item.getTypeSortie().toString());
				row.createCell(5).setCellValue(item.getProductNames());
				row.createCell(6).setCellValue(item.getUserNames());
			}

			// Configurer la réponse HTTP pour télécharger le fichier Excel
			response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
			response.setHeader("Content-Disposition", "attachment; filename=mvtStock.xlsx");
			// Écrire le fichier Excel dans la réponse HTTP
			ByteArrayOutputStream stream = new ByteArrayOutputStream();
			//workbook.write(response.getOutputStream());
			workbook.write(stream);
			workbook.close();

			byteArray = stream.toByteArray();

		} catch (Exception ex) {
			throw new ApiException("Erreur serveur!");
		}

		return byteArray;
	}

	@Override
	public List<SortieDto> filterRandeDates(int pageNo, int pageSize, String sortBy, String sortDir,LocalDateTime startDate, LocalDateTime endDate) {
		// Récupérer les éléments filtrés par date
		User authenticatedUser = service.getAuthUser();
		Long idEntreprise = authenticatedUser.getEntreprise().getEntrepriseId();

		Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending()
				: Sort.by(sortBy).descending();

		// create Pageable instance
		Pageable pageable = PageRequest.of(pageNo, pageSize, sort);

		Page<SortieProduit> list = sortieProduitRepository.findAllByEntrepriseIdAndUserIdAndDateSortieBetween(pageable, idEntreprise, authenticatedUser.getId(), startDate, endDate);

		List<SortieProduit> listOfSortieProduits = list.getContent();
		List<SortieResponse> items = Mapper.sortieToSortieResponses(listOfSortieProduits);

		if(startDate.isAfter(endDate)){
			throw new ApiException("La date de debut doit être inférieur à celle de la fin!");
		}

		return items.stream().map(item -> SortieDto.builder()
				.id(item.getId())
				.dateSortie(item.getDateSortie())
				.quantite(BigDecimal.valueOf(item.getQuantite()))
				.typeSortie(item.getTypeSortie())
				.ProductNames(item.getProductNames())
				.UserNames(item.getUserNames())
				.entrepriseId(item.getEntrepriseId())
				.build()).collect(Collectors.toList()) ;
	}


	// convert Entity into DTO
    private SortieDto mapToDTO(SortieProduit sortieProduit){
      //  ProductRequestDto sortieDto = mapper.map(sortieProduit, ProductRequestDto.class);
        SortieDto sortieDto = new SortieDto();
        sortieDto.setId(sortieProduit.getId());
        sortieDto.setDateSortie(sortieProduit.getDateSortie());
        sortieDto.setQuantite(sortieProduit.getQuantite());
        sortieDto.setTypeSortie(sortieProduit.getTypeSortie());
        sortieDto.setProductNames(sortieProduit.getProduct().getName());
        sortieDto.setUserNames(sortieProduit.getUser().getPrenom());
        sortieDto.setEntrepriseId(sortieProduit.getEntrepriseId());
        return sortieDto;
    }

}
