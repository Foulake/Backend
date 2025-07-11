package com.team.dev.service.impl;

import com.team.dev.dto.LocaliteRequestDto;
import com.team.dev.dto.LocaliteResponseDto;
import com.team.dev.exception.ApiException;
import com.team.dev.mapper.Mapper;
import com.team.dev.model.Champ;
import com.team.dev.model.Localite;
import com.team.dev.model.User;
import com.team.dev.repository.LocaliteRepository;
import com.team.dev.service.AuthService;
import com.team.dev.service.LocaliteService;
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
public class LocaliteServiceImpl implements LocaliteService {

private final LocaliteRepository localiteRepository;
	private final AuthService service;


	@Autowired
	public LocaliteServiceImpl(LocaliteRepository localiteRepository, AuthService service) {

		this.localiteRepository = localiteRepository;
		this.service = service;
	}

	@Override
	public LocaliteResponseDto addLocalite(LocaliteRequestDto localiteRequestDto) {
		Localite localite = new Localite();

		User authenticatedUser = service.getAuthUser();
		Long idEntreprise = authenticatedUser.getEntreprise().getEntrepriseId();

		if(localiteRepository.existsLocalitesByNom(localiteRequestDto.getNom())) {
			throw new ApiException(" Il existe bien une localite avec cet nom : " +localiteRequestDto.getNom());
		}else 
		localite.setNom(localiteRequestDto.getNom());
		localite.setEntrepriseId(idEntreprise);
		localite.setDescription(localiteRequestDto.getDescription());
		localiteRepository.save(localite) ;
		return Mapper.localiteToLocaliteResponseDto(localite);
	}

	@Override
	public List<LocaliteResponseDto> getLocalites() {
		List<Localite> localites = StreamSupport
				.stream(localiteRepository.findAll().spliterator(), false).collect(Collectors.toList());
		return Mapper.localiteToLocaliteResponseDtos(localites);
	}

	@Override
	public Localite getLocalite(Long localiteId) {
		return localiteRepository.findById(localiteId).orElseThrow(
		() ->  new ApiException("Il n'existe pas une localite avec cet ID : " + localiteId));
	}
	
	@Override
	public LocaliteResponseDto getLocaliteById(Long localiteId) {
		Localite localite = getLocalite(localiteId);
		return Mapper.localiteToLocaliteResponseDto(localite);
	}
	

	@Override
	public LocaliteResponseDto deleteLocalite(Long localiteId) {
		Localite localite = getLocalite(localiteId);
		localiteRepository.delete(localite);
		return Mapper.localiteToLocaliteResponseDto(localite);
	}

	@Transactional
	@Override
	public LocaliteResponseDto editLocalite(Long localiteId, LocaliteRequestDto localiteRequestDto) {
		Localite localiteToEdit = getLocalite(localiteId);
		localiteToEdit.setNom(localiteRequestDto.getNom());
		localiteToEdit.setDescription(localiteRequestDto.getDescription());
		
		return Mapper.localiteToLocaliteResponseDto(localiteToEdit);
	}

	@Override
	public LocaliteResponseDto getAllLocalites(int pageNo, int pageSize, String sortBy, String sortDir, String keyword) {
		
		Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

		User authenticatedUser = service.getAuthUser();
		Long idEntreprise = authenticatedUser.getEntreprise().getEntrepriseId();

		// create Pageable instance
        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);

        Page<Localite> localites = localiteRepository.findByNomContainingAAndEntrepriseId(pageable, "%"+keyword+"%", idEntreprise) ;

        // get content for page object
        List<Localite> listOfLocalites = localites.getContent();

        List<LocaliteRequestDto> content= listOfLocalites.stream().map(localite -> mapToDTO(localite)).collect(Collectors.toList());

        LocaliteResponseDto localiteResponseDto = new LocaliteResponseDto();
        localiteResponseDto.setContent(content);
        localiteResponseDto.setPageNo(localites.getNumber());
        localiteResponseDto.setPageSize(localites.getSize());
        localiteResponseDto.setTotalElements(localites.getTotalElements());
        localiteResponseDto.setTotalPages(localites.getTotalPages());
        localiteResponseDto.setLast(localites.isLast());

        return localiteResponseDto;
	}

	
	// convert Entity into DTO
    private LocaliteRequestDto mapToDTO(Localite localite){
      //  LocaliteRequestDto localiteRequestDto = mapper.map(localite, LocaliteRequestDto.class);
        LocaliteRequestDto localiteRequestDto = new LocaliteRequestDto();
        localiteRequestDto.setId(localite.getId());
        localiteRequestDto.setNom(localite.getNom());
        localiteRequestDto.setDescription(localite.getDescription());
        /** new codes **/
        List<String> names = new ArrayList<>();
		List<Champ> champs = localite.getChamps();
		for(Champ champ: champs) {
			names.add(champ.getNomChamp());
		}
		localiteRequestDto.setChampNames(names);
      
//    /** new codes **/
//    List<String> name = new ArrayList<>();
//	List<Magasin> magasins = localite.getMagasins();
//	for(Magasin magasin: magasins) {
//		name.add(magasin.getNomMagasin());
//	}
//	localiteRequestDto.setMagasinNom(name);
    
    return localiteRequestDto;
}

    /** Search **/
	@Override
	public List<LocaliteRequestDto> searchLocalite(String keyword) {
		List<Localite> localites = this.localiteRepository.findByNom(keyword);
		List<LocaliteRequestDto> localiteRequestDtos=localites.stream().map(localite -> mapToDTO(localite)).collect(Collectors.toList());
		return localiteRequestDtos;
		
	}

}
