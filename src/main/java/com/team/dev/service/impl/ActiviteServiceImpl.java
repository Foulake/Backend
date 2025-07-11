package com.team.dev.service.impl;

import com.team.dev.dto.ActiviteRequestDto;
import com.team.dev.dto.ActiviteResponseDto;
import com.team.dev.mapper.Mapper;
import com.team.dev.model.Activite;
import com.team.dev.model.Champ;
import com.team.dev.model.User;
import com.team.dev.repository.ActiviteRepository;
import com.team.dev.service.ActiviteService;
import com.team.dev.service.AuthService;
import com.team.dev.service.ChampService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;


@Service
public class ActiviteServiceImpl implements ActiviteService {
	
	private final ActiviteRepository activiteRepository;
	private final ChampService champService;
	private final AuthService service;

	@Autowired
	public ActiviteServiceImpl(ActiviteRepository activiteRepository,
							   ChampService champService, AuthService service) {
		this.activiteRepository = activiteRepository;
		this.champService = champService;

		this.service = service;
	}
	
	@Transactional
	@Override
	public ActiviteResponseDto addActivite(ActiviteRequestDto activiteRequestDto) {

		User authenticatedUser = service.getAuthUser();
		Long idEntreprise = authenticatedUser.getEntreprise().getEntrepriseId();


		Activite activite = new Activite();
		activite.setNom(activiteRequestDto.getNom());
		activite.setEntrepriseId(idEntreprise);

		if(activiteRequestDto.getChampId() == null) {
			throw new IllegalArgumentException("L'activite n'a pas de champ");
		}

		Champ champ = champService.getChamp(activiteRequestDto.getChampId());
		activite.setChamp(champ);
	
		
		Activite activite1 = activiteRepository.save(activite);
		return Mapper.activiteToActiviteResponseDto(activite1);
	}

	@Override
	public ActiviteResponseDto getActiviteById(Long activiteId) {
		Activite activite = getActivite(activiteId);
		return Mapper.activiteToActiviteResponseDto(activite);
	}

	@Override
	public Activite getActivite(Long activiteId) {
		Activite activite = activiteRepository.findById(activiteId)
				.orElseThrow(() -> new IllegalArgumentException("Il n'existe pas de activite avec id : " + activiteId));
		return activite;
	}
	
	@Transactional
	@Override
	public ActiviteResponseDto deleteActivite(Long activiteId) {
		Activite activite = getActivite(activiteId);
		activiteRepository.delete(activite);
		return Mapper.activiteToActiviteResponseDto(activite);
	}
	@Transactional
	@Override
	public ActiviteResponseDto editActivite(Long activiteId, ActiviteRequestDto activiteRequestDto) {
		Activite activiteEdit = getActivite(activiteId);
		activiteEdit.setNom(activiteRequestDto.getNom());
		activiteEdit.setDescription(activiteRequestDto.getDescription());

		if(activiteRequestDto.getChampId() != null ) {
			Champ champ = champService.getChamp(activiteRequestDto.getChampId());
			activiteEdit.setChamp(champ);
		}
		return Mapper.activiteToActiviteResponseDto(activiteEdit);
	}

	@Override
	public ActiviteResponseDto addChampToActivite(Long activiteId, Long champId) {
		Activite activite = getActivite(activiteId);
		Champ champ = champService.getChamp(champId);		
		if(Objects.nonNull(activite.getChamp())) {
			throw new IllegalArgumentException("Il exist déjà une activite avec cet champ");
		}
		activite.setChamp(champ);
		champ.addActivite(activite);
		return Mapper.activiteToActiviteResponseDto(activite);
	}

	@Override
	public ActiviteResponseDto removeChampFromActivite(Long activiteId, Long champId) {
		Activite activite = getActivite(activiteId);
		Champ champ = champService.getChamp(champId);
		if(Objects.nonNull(activite.getChamp())) {
			throw new IllegalArgumentException("Il exist déjà un activite avec cet champ");
		}
		activite.setChamp(null);
		champ.removeActivite(activite);
	//	planification.removeActivite(activite);
		return Mapper.activiteToActiviteResponseDto(activite);
	}

	

	@Override
	public List<ActiviteResponseDto> getActivites() {
		List<Activite> activites = StreamSupport
				.stream(activiteRepository.findAll().spliterator(), false)
				.collect(Collectors.toList());
		
		return Mapper.activiteToActiviteResponseDtos(activites);
	}
	
	
	///pagination
	@Override
	public ActiviteResponseDto getAllActivites(int pageNo, int pageSize, String sortBy, String sortDir, String keyword) {
		
		Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        // create Pageable instance
        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);


		User authenticatedUser = service.getAuthUser();
		Long idEntreprise = authenticatedUser.getEntreprise().getEntrepriseId();


		Page<Activite> activites = activiteRepository.findByNomContainingAndEntrepriseId(pageable, "%"+keyword+"%", idEntreprise);
        
        List<Activite> listOfActivites = activites.getContent();
        
        List<ActiviteRequestDto> content = listOfActivites.stream().map(activite -> mapToDTO(activite)).collect(Collectors.toList());
		
        ActiviteResponseDto activiteResponseDto = new ActiviteResponseDto();
       activiteResponseDto.setContent(content);
       activiteResponseDto.setPageNo(activites.getNumber());
       activiteResponseDto.setPageSize(activites.getSize());
       activiteResponseDto.setTotalElements(activites.getTotalElements());
       activiteResponseDto.setTotalPages(activites.getTotalPages());
       activiteResponseDto.setLast(activites.isLast());

        return activiteResponseDto;
	}

	 // convert Entity into DTO
    private ActiviteRequestDto mapToDTO(Activite activite){
      //  ActiviteRequestDtoactiviteDto = mapper.map(activite, ActiviteRequestDto.class);
        ActiviteRequestDto activiteDto = new ActiviteRequestDto();
       activiteDto.setId(activite.getId());
       activiteDto.setNom(activite.getNom());
       activiteDto.setDescription(activite.getDescription());
	   activiteDto.setChampNom(activite.getChamp().getNomChamp());
	   activiteDto.setImageUrl(activite.getChamp().getImageUrl());
       activiteDto.setChampId(activite.getChamp().getId());
      
        
//       activiteDto.setDescription(activite.getDescription());
//       activiteDto.setContent(activite.getContent());
        return activiteDto;
    }


    //

    @Override
	public List<ActiviteRequestDto> searchActivite(String keyword) {
		List<Activite> activites = this.activiteRepository.findByNom(keyword);
		//List<Activite> activites = this.activiteRepository.findAll(keyword);
		List<ActiviteRequestDto> activiteRequestDtos= activites.stream().map(activite -> mapToDTO(activite)).collect(Collectors.toList());
		return activiteRequestDtos;
		
	}

	@Override
	public ActiviteResponseDto searchActiviteFull(int pageNo, int pageSize, String sortBy, String sortDir,
			String keyword) {
		Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        // create Pageable instance
        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);
        
        Page<Activite> activites = activiteRepository.findAll(pageable, keyword);
        
        List<Activite> listOfActivites = activites.getContent();
        
        List<ActiviteRequestDto> content = listOfActivites.stream().map(activite -> mapToDTO(activite)).collect(Collectors.toList());
		
        ActiviteResponseDto activiteResponseDto = new ActiviteResponseDto();
        activiteResponseDto.setContent(content);
        activiteResponseDto.setPageNo(activites.getNumber());
        activiteResponseDto.setPageSize(activites.getSize());
        activiteResponseDto.setTotalElements(activites.getTotalElements());
        activiteResponseDto.setTotalPages(activites.getTotalPages());
        activiteResponseDto.setLast(activites.isLast());

        return activiteResponseDto;
	}

	

}