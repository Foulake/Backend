package com.team.dev.service.impl;

import com.team.dev.dto.ChampRequestDto;
import com.team.dev.dto.ChampResponseDto;
import com.team.dev.mapper.Mapper;
import com.team.dev.model.Champ;
import com.team.dev.model.Localite;
import com.team.dev.model.User;
import com.team.dev.repository.ChampRepository;
import com.team.dev.service.AuthService;
import com.team.dev.service.ChampService;
import com.team.dev.service.LocaliteService;
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
public class ChampServiceImpl implements ChampService {
	
	private final ChampRepository champRepository;
	//private final ActiviteService activiteService;
	private final LocaliteService localiteService;
	private final AuthService service;

	@Autowired
	public ChampServiceImpl(ChampRepository champRepository, LocaliteService localiteService, AuthService service) {
		
		this.champRepository = champRepository;
		this.localiteService = localiteService;
		this.service = service;
	}

	
	@Override
	public ChampResponseDto addChamp(ChampRequestDto champRequestDto) {

		User authenticatedUser = service.getAuthUser();
		Long idEntreprise = authenticatedUser.getEntreprise().getEntrepriseId();


		Champ champ = new Champ();
		champ.setId(champRequestDto.getId());
		champ.setNomChamp(champRequestDto.getNomChamp());
		champ.setEntrepriseId(idEntreprise);
		champ.setImageUrl(champRequestDto.getImageUrl());
		
//		if(champRequestDto.getSouschampId() == null ) {
//			throw new IllegalArgumentException("Le  sous champ");
//			}
		if(champRequestDto.getLocaliteId() == null) {
			throw new IllegalArgumentException("Le champ a moins de localite");
		}
		
		
		Localite localite = localiteService.getLocalite(champRequestDto.getLocaliteId());
		champ.setLocalite(localite);
	
		Champ champs = champRepository.save(champ);
		return Mapper.champToChampResponseDto(champs);
	}


	@Override
	public ChampResponseDto getChampById(Long champId) {
		Champ champ = getChamp(champId);
		return Mapper.champToChampResponseDto(champ);
	}

	@Override
	public ChampRequestDto getChampId(Long champId) {
		Champ champ = getChamp(champId);
		return Mapper.champToChampRequestDto(champ);
	}

	@Override
	public Champ getChamp(Long champId) {
		Champ champ = champRepository.findById(champId)
				.orElseThrow(() -> new IllegalArgumentException("Il n'existe pas de champ avec id : " + champId));
		return champ;
	}

	@Override
	public List<ChampResponseDto> getChamps() {
		List<Champ> champs = StreamSupport
				.stream(champRepository.findAll().spliterator(), false)
				.collect(Collectors.toList());
		
		return Mapper.champToChampResponseDtos(champs);
	
		}																												
	@Transactional
	@Override
	public ChampResponseDto deleteChamp(Long champId) {
		Champ champ = getChamp(champId);
		champRepository.delete(champ);
		return Mapper.champToChampResponseDto(champ);
	}
	@Transactional
	@Override
	public ChampResponseDto editChamp(Long champId, ChampRequestDto champRequestDto) {
		Champ champEdit = getChamp(champId);
		champEdit.setNomChamp(champRequestDto.getNomChamp());
		champEdit.setImageUrl(champRequestDto.getImageUrl());
		champEdit.setDownloadUrl(champRequestDto.getDownloadUrl());
		
		if(champRequestDto.getLocaliteId() != null ) {
			Localite localite = localiteService.getLocalite(champRequestDto.getLocaliteId());
			champEdit.setLocalite(localite);
		}
		
		return Mapper.champToChampResponseDto(champEdit);
}

	/**
	 * 
	@Override
	public ChampResponseDto addActiviteToChamp(Long champId, Long activiteId) {
		Champ champ = getChamp(champId);
		Activite activite = activiteService.getActivite(activiteId);
		if(Objects.nonNull(champ.getActivite())) {
			throw new IllegalArgumentException("Il exist déjà un champ avec cette activite");
		}
		champ.setActivite(activite);
		activite.addChamp(champ);
		return Mapper.champToChampResponseDto(champ);
		}

	@Override
	public ChampResponseDto addVaneToChamp(Long champId, Long vaneId) {
		Champ champ = getChamp(champId);
		Vane vane = vaneService.getVane(vaneId);
		if(Objects.nonNull(champ.getVanes())) {
			throw new IllegalArgumentException("Il exist déjà un champ avec cette vane");
		}
		champ.setVane(vane);
		vane.addChamp(champ);
		return Mapper.champToChampResponseDto(champ);}
		@Override
		
	public ChampResponseDto addSousChampToChamp(Long champId, Long souschampId) {
		Champ champ = getChamp(champId);
		SousChamp souschamp = souschampService.getSousChamp(souschampId);
		if(Objects.nonNull(champ.getSouschamp())) {
			throw new IllegalArgumentException("Il exist déjà un champ avec cette souschamp");
		}
		champ.setSouschamp(souschamp);
		souschamp.addChamp(champ);
		return Mapper.champToChampResponseDto(champ);}
		
		@Override
	public ChampResponseDto removeVaneFromChamp(Long champId, Long vaneId) {
		Champ champ = getChamp(champId);
		Vane vane = vaneService.getVane(vaneId);
		if(Objects.nonNull(champ.getVanes())) {
			throw new IllegalArgumentException("Il exist déjà un champ avec cette categorie");
		}
		champ.setVane(null);
		vane.removeChamp(champ);
		return Mapper.champToChampResponseDto(champ);
	}
	
		@Override
	public ChampResponseDto removeSousChampFromChamp(Long champId, Long souschampId) {
		Champ champ = getChamp(champId);
		SousChamp souschamp = souschampService.getSousChamp(souschampId);
		if(Objects.nonNull(champ.getSouschamp())) {
			throw new IllegalArgumentException("Il exist déjà un champ avec cette categorie");
		}
		champ.setSouschamp(null);
		souschamp.removeChamp(champ);
		return Mapper.champToChampResponseDto(champ);
	}
	
	
		
	**/
	@Override
	public ChampResponseDto addLocaliteToChamp(Long champId, Long localiteId) {
		Champ champ = getChamp(champId);
		Localite localite = localiteService.getLocalite(localiteId);
		if(Objects.nonNull(champ.getLocalite())) {
			throw new IllegalArgumentException("Il exist déjà un champ avec cette localite");
		}
		champ.setLocalite(localite);
		localite.addChamp(champ);
		return Mapper.champToChampResponseDto(champ);
		}


	@Override
	public ChampResponseDto removeLocaliteFromChamp(Long champId, Long localiteId) {
		Champ champ = getChamp(champId);
		Localite localite = localiteService.getLocalite(localiteId);
		if(Objects.nonNull(champ.getLocalite())) {
			throw new IllegalArgumentException("Il exist déjà un champ avec cette categorie");
		}
		champ.setLocalite(null);
		localite.removeChamp(champ);
		return Mapper.champToChampResponseDto(champ);
	}

	@Override
	public ChampResponseDto getAllChamps(int pageNo, int pageSize, String sortBy, String sortDir) {
		Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        // create Pageable instance
        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);
        
        Page<Champ> champs = champRepository.findAll(pageable);
        
        List<Champ> listOfChamps = champs.getContent();
        
        List<ChampRequestDto> content = listOfChamps.stream().map(champ -> mapToDTO(champ)).collect(Collectors.toList());
		
       ChampResponseDto champResponseDto = new ChampResponseDto();
       champResponseDto.setContent(content);
       champResponseDto.setPageNo(champs.getNumber());
       champResponseDto.setPageSize(champs.getSize());
       champResponseDto.setTotalElements(champs.getTotalElements());
       champResponseDto.setTotalPages(champs.getTotalPages());
       champResponseDto.setLast(champs.isLast());

        return champResponseDto;
	}

	 // convert Entity into DTO
    private ChampRequestDto mapToDTO(Champ champ){
      //  ActiviteRequestDtoactiviteDto = mapper.map(activite, ActiviteRequestDto.class);
        ChampRequestDto champDto = new ChampRequestDto();
        champDto.setId(champ.getId());
        champDto.setNomChamp(champ.getNomChamp());
		champDto.setImageUrl(champ.getImageUrl());
		champDto.setDownloadUrl(champ.getDownloadUrl());
        champDto.setNomLocalite(champ.getLocalite().getNom());
//       champDto.setVaneId(champ.getVanes().getId());
//       champDto.setLocaliteId(champ.getLocalite().getId());
//       champDto.setSouschampId(champ.getSouschamp().getId());
     
        
//       activiteDto.setDescription(activite.getDescription());
//       activiteDto.setContent(activite.getContent());
        return champDto;
    }


    @Override
	public List<ChampRequestDto> searchChamp(String keyword) {
		List<Champ> champs = this.champRepository.findByNomChamp(keyword);
		List<ChampRequestDto> champRequestDtos= champs.stream().map(champ -> mapToDTO(champ)).collect(Collectors.toList());
		return champRequestDtos;
	}



	@Override
	public ChampResponseDto searchChampFull(int pageNo, int pageSize, String sortBy, String sortDir, String keyword) {
		Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        // create Pageable instance
        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);


		User authenticatedUser = service.getAuthUser();
		Long idEntreprise = authenticatedUser.getEntreprise().getEntrepriseId();

		Page<Champ> champs = champRepository.findByNomChampContainingAndEntrepriseId(pageable, "%"+keyword+"%", idEntreprise);
        
        List<Champ> listOfChamps = champs.getContent();
        
        List<ChampRequestDto> content = listOfChamps.stream().map(champ -> mapToDTO(champ)).collect(Collectors.toList());
		
        ChampResponseDto champResponseDto = new ChampResponseDto();
        champResponseDto.setContent(content);
        champResponseDto.setPageNo(champs.getNumber());
        champResponseDto.setPageSize(champs.getSize());
        champResponseDto.setTotalElements(champs.getTotalElements());
        champResponseDto.setTotalPages(champs.getTotalPages());
        champResponseDto.setLast(champs.isLast());

        return champResponseDto;
	}
    
}
