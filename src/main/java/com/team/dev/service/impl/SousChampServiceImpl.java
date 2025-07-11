package com.team.dev.service.impl;

import com.team.dev.dto.SousChampRequestDto;
import com.team.dev.dto.SousChampResponseDto;
import com.team.dev.exception.ApiException;
import com.team.dev.mapper.Mapper;
import com.team.dev.model.Champ;
import com.team.dev.model.SousChamp;
import com.team.dev.model.User;
import com.team.dev.repository.ChampRepository;
import com.team.dev.repository.SousChampRepository;
import com.team.dev.service.AuthService;
import com.team.dev.service.ChampService;
import com.team.dev.service.SousChampService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;


@Service
public class SousChampServiceImpl implements SousChampService {
	private final ChampService champService;
	private SousChampRepository sousChampRepository;
    private ChampRepository champRepository;
	private final AuthService service;

	//private ModelMapper mapper;
    @Autowired
    public SousChampServiceImpl(SousChampRepository sousChampRepository, ChampRepository champRepository, ChampService champService, AuthService service) {
        this.champService = champService;
		this.sousChampRepository = sousChampRepository;
        this.champRepository = champRepository;
        //this.mapper = mapper;
		this.service = service;
	}
	@Transactional
    @Override
	public SousChampResponseDto addSousChamp(SousChampRequestDto souschampRequestDto ) {
		SousChamp souschamp = new SousChamp();
		User authenticatedUser = service.getAuthUser();
		Long idEntreprise = authenticatedUser.getEntreprise().getEntrepriseId();

		souschamp.setNom(souschampRequestDto.getNom());
		souschamp.setTypeCulture(souschampRequestDto.getTypeCulture());
		souschamp.setEntrepriseId(idEntreprise);
		souschamp.setNumero(souschampRequestDto.getNumero());

//		if(souschampRequestDto.getChampId() == null ) {
//			throw new ApiException("Le sous champ manque de champ");
//		}
//		Champ champ = champService.getChamp(souschampRequestDto.getChampId());
//		souschamp.setChamp(champ);
		
		
		sousChampRepository.save(souschamp);
		return Mapper.sousChampToSousChampResponseDto(souschamp);
	}
    
    @Override
	public SousChampResponseDto deleteSousChamp(Long souschampId) {
		SousChamp souschamp = getSousChamp(souschampId);
		sousChampRepository.delete(souschamp);
		return Mapper.sousChampToSousChampResponseDto(souschamp);
	}

	@Transactional
	@Override
	public SousChampResponseDto editSousChamp(Long souschampId, SousChampRequestDto souschampRequestDto) {
		SousChamp souschampToEdit = getSousChamp(souschampId);
		souschampToEdit.setNom(souschampRequestDto.getNom());
		souschampToEdit.setImageUrl(souschampRequestDto.getImageUrl());
		souschampToEdit.setNumero(souschampRequestDto.getNumero());
		souschampToEdit.setTypeCulture(souschampRequestDto.getTypeCulture());

//		if(souschampRequestDto.getChampId() != null ) {
//			Champ champ = champService.getChamp(souschampRequestDto.getChampId());
//			souschampToEdit.setChamp(champ);
//
//		}
		
		return Mapper.sousChampToSousChampResponseDto(souschampToEdit);
	}
	
	
    @Override
    public List<SousChampRequestDto> getSousChampsByChampId(Long champId) {
        // retrieve sousChamps by champId
        //List<SousChamp> sousChamps = sousChampRepository.findByChampId(champId);
		return null;
        // convert list of sousChamp entities to list of sousChamp dto's
        //return sousChamps.stream().map(sousChamp -> mapToDTO(sousChamp)).collect(Collectors.toList());
    }

  

	@Override
	public SousChampResponseDto getAllSousChamps(int pageNo, int pageSize, String sortBy, String sortDir, String keyword) {
		
		Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

		User authenticatedUser = service.getAuthUser();
		Long idEntreprise = authenticatedUser.getEntreprise().getEntrepriseId();

		// create Pageable instance
        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);

        Page<SousChamp> sousChamps = sousChampRepository.findByNomContainingAndEntrepriseId(pageable, "%"+keyword+"%", idEntreprise);

        // get content for page object
        List<SousChamp> listOfSousChamps = sousChamps.getContent();

        List<SousChampRequestDto> content= listOfSousChamps.stream().map(sousChamp -> mapToDTO(sousChamp)).collect(Collectors.toList());

        SousChampResponseDto sousChampResponseDto = new SousChampResponseDto();
        sousChampResponseDto.setContent(content);
        sousChampResponseDto.setPageNo(sousChamps.getNumber());
        sousChampResponseDto.setPageSize(sousChamps.getSize());
        sousChampResponseDto.setTotalElements(sousChamps.getTotalElements());
        sousChampResponseDto.setTotalPages(sousChamps.getTotalPages());
        sousChampResponseDto.setLast(sousChamps.isLast());

        return sousChampResponseDto;
	}
	
	 // convert Entity into DTO
    private SousChampRequestDto mapToDTO(SousChamp sousChamp){
      //  SousChampDto sousChampDto = mapper.map(sousChamp, SousChampDto.class);
        SousChampRequestDto sousChampDto = new SousChampRequestDto();
        sousChampDto.setId(sousChamp.getId());
        sousChampDto.setNom(sousChamp.getNom());
        sousChampDto.setNumero(sousChamp.getNumero());
//        sousChampDto.setChampId(sousChamp.getChamp().getId());
//		sousChampDto.setNomChamp(sousChamp.getChamp().getNomChamp());
		sousChampDto.setImageUrl(sousChamp.getImageUrl());
        sousChampDto.setTypeCulture(sousChamp.getTypeCulture());
//        sousChampDto.setContent(sousChamp.getContent());
        return sousChampDto;
    }

	@Override
	public List<SousChampRequestDto> searchSousChampFull(String keyword) {
		//List<Product> products = this.productRepository.findByName(keyword);
				List<SousChamp> sousChamps = this.sousChampRepository.findAll(keyword);
				List<SousChampRequestDto> productRequestDtos=sousChamps.stream().map(sousChamp -> mapToDTO(sousChamp)).collect(Collectors.toList());
				return productRequestDtos;
	}

	public SousChamp getSousChamp(Long sousChampId) {
		return sousChampRepository.findById(sousChampId).orElseThrow(
		() -> new ApiException("Il n'existe pas de planche avec ID : " + sousChampId));
	}

	@Override
	public List<SousChampResponseDto> getSousChamps() {
		List<SousChamp> souschamps = StreamSupport
				.stream(sousChampRepository.findAll().spliterator(), false)
				.collect(Collectors.toList());
		return Mapper.souschampToSousChampResponseDtos(souschamps);
		}

	@Override
	public SousChampRequestDto getSousChampId(Long id) {
		SousChamp sousChamp = getSousChamp(id);
		return Mapper.sousChampToSousChampRequestDto(sousChamp);
	}

	@Override
	public SousChampResponseDto getSousChampById(Long souschampId) {
		SousChamp sousChamp = getSousChamp(souschampId);
		return Mapper.sousChampToSousChampResponseDto(sousChamp);
		}

	
}
