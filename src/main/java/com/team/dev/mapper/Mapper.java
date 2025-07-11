package com.team.dev.mapper;

import com.team.dev.dto.*;
import com.team.dev.model.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.ArrayList;
import java.util.List;

public class Mapper {
	
public static ProductResponseDto productToProductResponseDto(Product product) {

		ProductResponseDto productResponseDto = new ProductResponseDto();
		productResponseDto.setId(product.getId());
		productResponseDto.setCategoryNom(product.getCategory().getNom());
		productResponseDto.setCategoryId(product.getCategory().getId());
		productResponseDto.setMagasinNom(product.getMagasin().getNomMagasin());
		productResponseDto.setMagasinId(product.getMagasin().getId());
		productResponseDto.setEmail(product.getUser().getEmail());
		productResponseDto.setUserId(product.getUser().getId());
		productResponseDto.setEntrepriseId(product.getEntrepriseId());

		//neeew
		productResponseDto.setCode(product.getCode());
		productResponseDto.setPrice(product.getPrice());
		productResponseDto.setQte(product.getQte().doubleValue());
		productResponseDto.setName(product.getName());
		productResponseDto.setImageName(product.getImageName());
		
		return productResponseDto;
	}
	
	public static List<ProductResponseDto> productToProductResponseDtos( List<Product> products){
		
		List<ProductResponseDto> productResponseDtos = new ArrayList<>();
		for(Product product: products) {
			productResponseDtos.add(productToProductResponseDto(product));
		}
		
		return productResponseDtos;
		
	}
	
	public static SortieResponse sortieProductToSortieProductResponse(SortieProduit sortie) {
		
		SortieResponse sortieResponse = new SortieResponse();
		sortieResponse.setId(sortie.getId());
		sortieResponse.setQuantite(sortie.getQuantite().doubleValue());
		sortieResponse.setDateSortie(sortie.getDateSortie());
		sortieResponse.setProductNames(sortie.getProduct().getName());
		sortieResponse.setTypeSortie(sortie.getTypeSortie());
		sortieResponse.setUserNames(sortie.getUser().getPrenom());
		sortieResponse.setEntrepriseId(sortie.getEntrepriseId());
		return sortieResponse;
	}
	
	public static List<SortieResponse> sortieToSortieResponses( List<SortieProduit> sortieProduits){
		
		List<SortieResponse> sortieResponses = new ArrayList<>();
		for(SortieProduit sortieProduit: sortieProduits) {
			sortieResponses.add(sortieProductToSortieProductResponse(sortieProduit));
		}
		
		return sortieResponses;
		
	}

	
	public static CategoryResponseDto categoryToCategoryResponseDto(Category category) {
		CategoryResponseDto categoryResponseDto = new CategoryResponseDto();
		categoryResponseDto.setId(category.getId());
		categoryResponseDto.setNom(category.getNom());
		categoryResponseDto.setEntrepriseId(category.getEntrepriseId());
//		List<String> names = new ArrayList<>();
//		List<Product> products = category.getProducts();
//		for(Product product: products) {
//			names.add(product.getName());
//		}
//		categoryResponseDto.setProductNames(names);
		return categoryResponseDto;
	}
	
	public static List<CategoryResponseDto> categoryToCategoryResponseDtos(List<Category> categories){
		List<CategoryResponseDto> categoryResponseDtos = new ArrayList<>();
		for(Category category: categories) {
			categoryResponseDtos.add(categoryToCategoryResponseDto(category));
		}
		return categoryResponseDtos;
	}
	
	public static MagasinResponseDto magasinToMagasinResponseDto(Magasin magasin) {
		MagasinResponseDto magasinResponseDto = new MagasinResponseDto();
		magasinResponseDto.setId(magasin.getId());
		magasinResponseDto.setNomMagasin(magasin.getNomMagasin());
		magasinResponseDto.setEntrepriseId(magasin.getEntrepriseId());
//		magasinResponseDto.setNomLocalite(magasin.getLocalite().getNom());
//		magasinResponseDto.setDescription(magasin.getLocalite().getDescription());
//		List<String> names = new ArrayList<>();
//		List<Product> products = magasin.getProducts();
//		for(Product product: products) {
//			names.add(product.getName());
//		}
//		magasinResponseDto.setProductNames(names);
		return magasinResponseDto;
	}

	public static List<MagasinResponseDto> magasinToMagasinResponseDtos(List<Magasin> magasins){
		List<MagasinResponseDto> magasinResponseDtos = new ArrayList<>();
		for(Magasin magasin: magasins) {
			magasinResponseDtos.add(magasinToMagasinResponseDto(magasin));
		}
		return magasinResponseDtos;
	}

	public static RecolteResponse recolteToRecolteResponse(Recolte recolte) {
		RecolteResponse Rresponse = new RecolteResponse();
		Rresponse.setId(recolte.getId());
		Rresponse.setChamp(recolte.getChamp().getNomChamp());
		//Rresponse.setDesc(recolte.getDesc());
		Rresponse.setImageUrl(recolte.getImageUrl());
		Rresponse.setEntrepriseId(recolte.getEntrepriseId());
		Rresponse.setQuantite(recolte.getQuantite());
		Rresponse.setChampId(recolte.getChamp().getId());
		Rresponse.setEndDate(recolte.getEndDate());
		Rresponse.setStartDate(recolte.getStartDate());
		Rresponse.setTypeCulture(recolte.getTypeCulture());
		//Rresponse.setConditioMeteo(recolte.getConditioMeteo());
		Rresponse.setQualite(recolte.getQualite());
		Rresponse.setUniteMesure(recolte.getUniteMesure());

		return Rresponse;
	}

	public static RecolteRequest recolteToRecolteRequest(Recolte recolte) {
		RecolteRequest response = new RecolteRequest();
		response.setId(recolte.getId());
		response.setChampId(recolte.getChamp().getId());
		response.setChamp(recolte.getChamp().getNomChamp());
		//response.setDesc(recolte.getDesc());
		response.setImageUrl(recolte.getImageUrl());
		response.setEntrepriseId(recolte.getEntrepriseId());
		response.setQuantite(recolte.getQuantite());
		response.setEndDate(recolte.getEndDate());
		response.setStartDate(recolte.getStartDate());
		response.setTypeCulture(recolte.getTypeCulture());
		//response.setConditioMeteo(recolte.getConditioMeteo());
		response.setQualite(recolte.getQualite());
		response.setUniteMesure(recolte.getUniteMesure());

		return response;
	}

	public static List<RecolteResponse> recolteToRecolteResponses(List<Recolte> recoltes){
		List<RecolteResponse> recolteResponses = new ArrayList<>();
		for(Recolte recolte: recoltes) {
			recolteResponses.add(recolteToRecolteResponse(recolte));
		}
		return recolteResponses;
	}
	public static List<RecolteRequest> recolteToRecolteRequests(List<Recolte> recoltes){
		List<RecolteRequest> recolteResponses = new ArrayList<>();
		for(Recolte recolte: recoltes) {
			recolteResponses.add(recolteToRecolteRequest(recolte));
		}
		return recolteResponses;
	}
	
	public static AuthenticateResponse userToUserResponseDto(User user) {
		AuthenticateResponse userResponseDto = new AuthenticateResponse();
		userResponseDto.setId(user.getId());
		userResponseDto.setNom(user.getNom());
		userResponseDto.setPrenom(user.getPrenom());
		userResponseDto.setEmail(user.getEmail());
		userResponseDto.setFirstLogin(user.isFirstLogin());
		userResponseDto.setImageUrl(user.getImageUrl());
		userResponseDto.setEntrepriseId(user.getEntreprise().getEntrepriseId());
		userResponseDto.setEntreprise(user.getEntreprise().getNom());
		userResponseDto.setEnabled(user.isEnabled());
		userResponseDto.setRoles(user.getRoles());
		userResponseDto.setPassword(new BCryptPasswordEncoder().encode(user.getPassword()));
//		List<String> names = new ArrayList<>();
//		List<Product> products = user.getProducts();
//		for(Product product: products) {
//			names.add(product.getName());
//		}
//		userResponseDto.setProductNames(names);
		return userResponseDto;
	}
	
	public static List<AuthenticateResponse> userToUserResponseDtos(List<User> users){
		List<AuthenticateResponse> userResponseDtos = new ArrayList<>();
		for(User user: users) {
			userResponseDtos.add(userToUserResponseDto(user));
		}
		return userResponseDtos;
	}
	
	public static LocaliteResponseDto localiteToLocaliteResponseDto(Localite localite) {
		LocaliteResponseDto localiteResponseDto = new LocaliteResponseDto();
		localiteResponseDto.setId(localite.getId());
		localiteResponseDto.setNom(localite.getNom());
		localiteResponseDto.setEntrepriseId(localite.getEntrepriseId());
		localiteResponseDto.setDescription(localite.getDescription());
//		List<String> names = new ArrayList<>();
//		List<Product> products = category.getProducts();
//		for(Product product: products) {
//			names.add(product.getName());
//		}
//		categoryResponseDto.setProductNames(names);
		return localiteResponseDto;
	}
	
	public static List<LocaliteResponseDto> localiteToLocaliteResponseDtos(List<Localite> localites){
		List<LocaliteResponseDto> localiteResponseDtos = new ArrayList<>();
		for(Localite localite: localites) {
			localiteResponseDtos.add(localiteToLocaliteResponseDto(localite));
		}
		return localiteResponseDtos;
	}
	
public static SousChampResponseDto sousChampToSousChampResponseDto(SousChamp sousChamp) {
		
	SousChampResponseDto sousChampResponseDto = new SousChampResponseDto();
	sousChampResponseDto.setId(sousChamp.getId());
	//sousChampResponseDto.setNomChamp(sousChamp.getChamp().getNomChamp());
	sousChampResponseDto.setTypeCulture(sousChamp.getTypeCulture());
	sousChampResponseDto.setEntrepriseId(sousChamp.getEntrepriseId());
	sousChampResponseDto.setNom(sousChamp.getNom());
	sousChampResponseDto.setImageUrl(sousChamp.getImageUrl());
	sousChampResponseDto.setNumero(sousChamp.getNumero());
		
		return sousChampResponseDto;
	}
	public static SousChampRequestDto sousChampToSousChampRequestDto(SousChamp sousChamp) {

	SousChampRequestDto sousChampResponseDto = new SousChampRequestDto();
	sousChampResponseDto.setId(sousChamp.getId());
	//sousChampResponseDto.setNomChamp(sousChamp.getChamp().getNomChamp());
	sousChampResponseDto.setEntrepriseId(sousChamp.getEntrepriseId());
	sousChampResponseDto.setTypeCulture(sousChamp.getTypeCulture());
	sousChampResponseDto.setNom(sousChamp.getNom());
	sousChampResponseDto.setImageUrl(sousChamp.getImageUrl());
	sousChampResponseDto.setNumero(sousChamp.getNumero());

		return sousChampResponseDto;
	}
	
	public static List<SousChampResponseDto> productToSousChampResponseDtos( List<SousChamp> sousChamps){
		
		List<SousChampResponseDto> sousChampResponseDtos = new ArrayList<>();
		for(SousChamp sousChamp: sousChamps) {
			sousChampResponseDtos.add(sousChampToSousChampResponseDto(sousChamp));
		}
		
		return sousChampResponseDtos;
		
	}
	
	
	
	// pour activite
	public static ActiviteResponseDto activiteToActiviteResponseDto(Activite activite) {
		ActiviteResponseDto activiteResponseDto = new ActiviteResponseDto();
		activiteResponseDto.setId(activite.getId());
		activiteResponseDto.setNom(activite.getNom());
		activiteResponseDto.setEntrepriseId(activite.getEntrepriseId());
		activiteResponseDto.setDescription(activite.getDescription());
		activiteResponseDto.setChampNom(activite.getChamp().getNomChamp());
		activiteResponseDto.setImageUrl(activite.getChamp().getImageUrl());
		activiteResponseDto.setChampId(activite.getChamp().getId());

		return activiteResponseDto;
	}
	
	//activite liste
	public static List<ActiviteResponseDto> activiteToActiviteResponseDtos( List<Activite> activites){
		
		List<ActiviteResponseDto> activiteResponseDtos = new ArrayList<>();
		for(Activite activite: activites) {
			activiteResponseDtos.add(activiteToActiviteResponseDto(activite));
		}
		
		return activiteResponseDtos;
		
	}
		// pour sous champ
		public static SousChampResponseDto souschampToSousChampResponseDto(SousChamp souschamp) {
			SousChampResponseDto souschampResponseDto = new SousChampResponseDto();
		souschampResponseDto.setId(souschamp.getId());
			souschampResponseDto.setNom(souschamp.getNom());
			souschampResponseDto.setEntrepriseId(souschamp.getEntrepriseId());
			souschampResponseDto.setNumero(souschamp.getNumero());
			souschampResponseDto.setImageUrl(souschamp.getImageUrl());

			//List<String> names = new ArrayList<>();
			//List<Champ> champs = souschamp.getChamps();
			//for(Champ champ: champs) {
				//names.add(champ.getNomChamp());
			//}
			//souschampResponseDto.setChampNames(names);
			return souschampResponseDto;
		}
		
		// pour la liste souschamp
				public static List<SousChampResponseDto> souschampToSousChampResponseDtos(List<SousChamp> souschamps){
					List<SousChampResponseDto> souschampResponseDtos = new ArrayList<>();
					for(SousChamp souschamp: souschamps) {
						souschampResponseDtos.add(souschampToSousChampResponseDto(souschamp));
					}
					return souschampResponseDtos;
				}
				

		// pour la planification
		public static PlanificationResponseDto planificationToPlanificationResponseDto(Planification planification) {
			PlanificationResponseDto planificationResponseDto = new PlanificationResponseDto();
			planificationResponseDto.setId(planification.getId());
			planificationResponseDto.setActiviteNom(planification.getActivite().getNom());
			planificationResponseDto.setImageUrl(planification.getImageUrl());
			planificationResponseDto.setDescription(planification.getDescription());
			planificationResponseDto.setStatus(planification.getStatus());
			planificationResponseDto.setEntrepriseId(planification.getEntrepriseId());
			planificationResponseDto.setTitrePlanification(planification.getTitrePlanification());
			planificationResponseDto.setDate_start(planification.getDate_start());
			planificationResponseDto.setDate_end(planification.getDate_end());
			//List<String> names = new ArrayList<>();
			//planificationResponseDto.setActiviteNames(names);
			return planificationResponseDto;
		}
	public static PlanificationRequestDto planificationToPlanificationRequest(PlanificationResponseDto planification) {
		PlanificationRequestDto planificationResponseDto = new PlanificationRequestDto();
		planificationResponseDto.setId(planification.getId());
		planificationResponseDto.setImageUrl(planification.getImageUrl());
		planificationResponseDto.setDescription(planification.getDescription());
		planificationResponseDto.setStatus(planification.getStatus());
		planificationResponseDto.setActiviteNom(planification.getActiviteNom());
		planificationResponseDto.setEntrepriseId(planification.getEntrepriseId());
		planificationResponseDto.setTitrePlanification(planification.getTitrePlanification());
		planificationResponseDto.setDate_start(planification.getDate_start());
		planificationResponseDto.setDate_end(planification.getDate_end());
		//List<String> names = new ArrayList<>();
		//planificationResponseDto.setActiviteNames(names);
		return planificationResponseDto;
	}
	//pour la liste de planification
		public static List<PlanificationResponseDto> planificationToPlanificationResponseDtos(List<Planification> planifications){
			List<PlanificationResponseDto> planificationResponseDtos = new ArrayList<>();
			for(Planification planification: planifications) {
				planificationResponseDtos.add(planificationToPlanificationResponseDto(planification));
			}
			return planificationResponseDtos;
		}
		
//		public static List<PlanificationResponseDto> planificationToPlanificationResponseDto(
//				List<Planification> planifications) {
//			List<PlanificationResponseDto> planificationResponseDtos = new ArrayList<>();
//			for(Planification planification: planifications) {
//				planificationResponseDtos.add(planificationToPlanificationResponseDto(planification));
//			}
//			return planificationResponseDtos;
//		}
//		
		public static ChampResponseDto champToChampResponseDto(Champ champ) {
			ChampResponseDto champResponseDto   = new ChampResponseDto();
			champResponseDto.setId(champ.getId());
			champResponseDto.setNomChamp(champ.getNomChamp());
			champResponseDto.setSuperficie(champ.getSuperficie());
			champResponseDto.setImageUrl(champ.getImageUrl());
			champResponseDto.setDownloadUrl(champ.getDownloadUrl());
			champResponseDto.setNomLocalite(champ.getLocalite().getNom());
			champResponseDto.setEntrepriseId(champ.getEntrepriseId());
			//champResponseDto.setDescription(champ.getLocalite().getDescription());
//			List<String> names = new ArrayList<>();
//			List<Product> products = magasin.getProducts();
//			for(Product product: products) {
//				names.add(product.getName());
//			}
//			magasinResponseDto.setProductNames(names);
			return champResponseDto;
		}
	public static ChampRequestDto champToChampRequestDto(Champ champ) {
		ChampRequestDto champResponseDto   = new ChampRequestDto();
		champResponseDto.setId(champ.getId());
		champResponseDto.setNomChamp(champ.getNomChamp());
		champResponseDto.setSuperficie(champ.getSuperficie());
		champResponseDto.setEntrepriseId(champ.getEntrepriseId());
		champResponseDto.setLocaliteId(champ.getLocalite().getId());
		champResponseDto.setImageUrl(champ.getImageUrl());
		champResponseDto.setDownloadUrl(champ.getDownloadUrl());
		champResponseDto.setNomLocalite(champ.getLocalite().getNom());

		return champResponseDto;
	}
		
		public static List<ChampResponseDto> champToChampResponseDtos(List<Champ> champs) {
				List<ChampResponseDto> champResponseDtos = new ArrayList<>();
				for(Champ champ: champs) {
					champResponseDtos.add(champToChampResponseDto(champ));
				}
				return champResponseDtos;
		}

	public static ProductRequestDto productToProductRequestDto(Product product) {

		ProductRequestDto productResponseDto = new ProductRequestDto();
		productResponseDto.setId(product.getId());
		productResponseDto.setQte(product.getQte().doubleValue());
		productResponseDto.setEntrepriseId(product.getEntrepriseId());
		productResponseDto.setCategoryNom(product.getCategory().getNom());
		productResponseDto.setCategoryId(product.getCategory().getId());
		productResponseDto.setMagasinNom(product.getMagasin().getNomMagasin());
		productResponseDto.setMagasinId(product.getMagasin().getId());
		productResponseDto.setEmail(product.getUser().getEmail());
		productResponseDto.setUserId(product.getUser().getId());

		//neeew
		productResponseDto.setCode(product.getCode());
		productResponseDto.setPrice(product.getPrice());
		productResponseDto.setName(product.getName());
		productResponseDto.setImageName(product.getImageName());

		return productResponseDto;
	}

	public static EntrepriseResponseDto EntrepriseToEntrepriseResponseDto(Entreprise Entreprise) {
		EntrepriseResponseDto EntrepriseResponseDto = new EntrepriseResponseDto();
		EntrepriseResponseDto.setIdEntreprise(Entreprise.getEntrepriseId());
		EntrepriseResponseDto.setNom(Entreprise.getNom());
		EntrepriseResponseDto.setEmail(Entreprise.getEmail());
		EntrepriseResponseDto.setContact(Entreprise.getContact());
		EntrepriseResponseDto.setAdresse(Entreprise.getAdresse());
		EntrepriseResponseDto.setDescription(Entreprise.getDescription());
		EntrepriseResponseDto.setImageUrl(Entreprise.getImageUrl());
//		List<String> names = new ArrayList<>();
//		List<User> users = Entreprise.getUsers();
//		for(User user: users) {
//			names.add(user.getNom());
//		}
//		EntrepriseResponseDto.setEntrepriseNames(names);
		return EntrepriseResponseDto;
	}public static EntrepriseRequestDto EntrepriseToEntrepriseRequestDto(Entreprise Entreprise) {
		EntrepriseRequestDto EntrepriseResponseDto = new EntrepriseRequestDto();
		EntrepriseResponseDto.setIdEntreprise(Entreprise.getEntrepriseId());
		EntrepriseResponseDto.setNom(Entreprise.getNom());
		EntrepriseResponseDto.setContact(Entreprise.getContact());
		EntrepriseResponseDto.setAdresse(Entreprise.getAdresse());
		EntrepriseResponseDto.setImageUrl(Entreprise.getImageUrl());
//		List<String> names = new ArrayList<>();
//		List<User> users = Entreprise.getUsers();
//		for(User user: users) {
//			names.add(user.getNom());
//		}
//		EntrepriseResponseDto.setEntrepriseNames(names);
		return EntrepriseResponseDto;
	}

	public static List<EntrepriseResponseDto> EntrepriseToEntrepriseResponseDtos(List<Entreprise> entreprises){
		List<EntrepriseResponseDto> EntrepriseResponseDtos = new ArrayList<>();
		for(Entreprise Entreprise: entreprises) {
			EntrepriseResponseDtos.add(EntrepriseToEntrepriseResponseDto(Entreprise));
		}
		return EntrepriseResponseDtos;
	}

//		public static List<RoleResponseDto> roleToRoleResponseDtos(List<Role> roles) {
//				List<RoleResponseDto> roleResponseDtos = new ArrayList<>();
//				for(Role role: roles) {
//					roleResponseDtos.add(roleToRoleResponseDto(role));
//				}
//				return roleResponseDtos ;
//			}
//
//
//		public static RoleResponseDto roleToRoleResponseDto(Role role) {
//			RoleResponseDto roleResponseDto = new RoleResponseDto();
//			roleResponseDto.setId(role.getId());
//			roleResponseDto.setName(role.getName());
////			List<String> names = new ArrayList<>();
////			List<Product> products = category.getProducts();
////			for(Product product: products) {
////				names.add(product.getName());
////			}
////			categoryResponseDto.setProductNames(names);
//			return roleResponseDto;
//		}
}
