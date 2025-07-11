package com.team.dev.service.impl;

import com.team.dev.dto.PlanificationRequestDto;
import com.team.dev.dto.PlanificationResponseDto;
import com.team.dev.mapper.Mapper;
import com.team.dev.model.Activite;
import com.team.dev.model.Planification;
import com.team.dev.model.User;
import com.team.dev.repository.PlaningRepository;
import com.team.dev.service.ActiviteService;
import com.team.dev.service.AuthService;
import com.team.dev.service.PlanificationService;
import jakarta.transaction.Transactional;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@Log4j2
public class PlanificationServiceImpl implements PlanificationService {
	private final ActiviteService activiteService;
	
	private final PlaningRepository planificationRepository;
	private final AuthService service;

	@Autowired
	public PlanificationServiceImpl(PlaningRepository planificationRepository, ActiviteService activiteService, AuthService service) {
		this.activiteService = activiteService;
		this.planificationRepository = planificationRepository;
		this.service = service;
	}

	@Override
	public Planification getPlanification(Long planificationId) {
		return planificationRepository.findById(planificationId).orElseThrow(
				() -> new IllegalArgumentException("Il n'existe pas une planification avec id : " + planificationId));	
			
	}

	@Override
	public PlanificationResponseDto switchStatusPlanification(Long planificationId) {
		Planification planification = getPlanification(planificationId);
		if (!planification.getStatus().isEmpty()){
			if (planification.getStatus().equalsIgnoreCase("OUI")) {
				planification.setStatus("NON");
			} else {
				planification.setStatus("OUI");
			}
		}

		return Mapper.planificationToPlanificationResponseDto(this.planificationRepository.save(planification));
	}

	@Transactional
	@Override
	public PlanificationResponseDto addPlanification(PlanificationRequestDto planificationRequestDto) {
		Planification planification = new Planification();
		User authenticatedUser = service.getAuthUser();
		Long idEntreprise = authenticatedUser.getEntreprise().getEntrepriseId();

		planification.setTitrePlanification(planificationRequestDto.getTitrePlanification());
		planification.setEntrepriseId(idEntreprise);
		planification.setImageUrl(planification.getImageUrl());
		planification.setDescription(planification.getDescription());
		//planification.setStatus("NON");
		planification.setDate_start(planificationRequestDto.getDate_start());
		planification.setDate_end(planificationRequestDto.getDate_end());
//		planificationRepository.save(planification);
//		return Mapper.planificationToPlanificationResponseDto(planification);
	
		
		if(planificationRequestDto.getActiviteId() == null ) {
			throw new IllegalArgumentException("L'activitée est requise pour la planification !");
		}
		Activite activite = activiteService.getActivite(planificationRequestDto.getActiviteId());
		planification.setActivite(activite);
		
		Planification planifications = planificationRepository.save(planification);
		return Mapper.planificationToPlanificationResponseDto(planifications);
	}
	

	@Override
	public List<PlanificationResponseDto> getPlanifications() {
		List<Planification> planifications = new ArrayList<>(planificationRepository.findAll());
		return Mapper.planificationToPlanificationResponseDtos(planifications);
		}

	@Override
	public PlanificationResponseDto getPlanificationById(Long planificationId) {
		Planification planification = getPlanification(planificationId);
		return Mapper.planificationToPlanificationResponseDto(planification);
	
	}

	@Override
	public PlanificationResponseDto deletePlanification(Long planificationId) {
		Planification planification = getPlanification(planificationId);
		planificationRepository.delete(planification);
		return Mapper.planificationToPlanificationResponseDto(planification);
	}
	@Transactional
	@Override
	public PlanificationResponseDto editPlanification(Long planificationId,
			PlanificationRequestDto planificationRequestDto) {
		Planification planificationToEdit = getPlanification(planificationId);
		planificationToEdit.setTitrePlanification(planificationRequestDto.getTitrePlanification());
		planificationToEdit.setDate_start(planificationRequestDto.getDate_start());
		planificationToEdit.setDate_end(planificationRequestDto.getDate_end());
		planificationToEdit.setImageUrl(planificationRequestDto.getImageUrl());
		planificationToEdit.setDescription(planificationRequestDto.getDescription());

		if(planificationRequestDto.getActiviteId() != null ) {
			Activite activite = activiteService.getActivite(planificationRequestDto.getActiviteId());
			planificationToEdit.setActivite(activite);
			
		}
		
		return Mapper.planificationToPlanificationResponseDto(planificationToEdit);
	}

	@Override
	public PlanificationResponseDto addActiviteToPlanification(Long planificationId, Long activiteId) {
		Planification planification = getPlanification(planificationId);
		Activite activite = activiteService.getActivite(activiteId);
		if(Objects.nonNull(planification.getActivite())) {
			throw new IllegalArgumentException("Il exist déjà une planification ");
		}
		planification.setActivite(activite);
		activite.addPlanification(planification);
		return Mapper.planificationToPlanificationResponseDto(planification);
	}

	@Override
	public PlanificationResponseDto removeActiviteFromPlanification(Long planificationId, Long activiteId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public PlanificationResponseDto getAllPlanifications(int pageNo, int pageSize, String sortBy, String sortDir) {
		Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        // create Pageable instance
        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);
        
        Page<Planification> planifications = planificationRepository.findAll(pageable);
        
        List<Planification> listOfPlanifications = planifications.getContent();
        
        List<PlanificationRequestDto> content = listOfPlanifications.stream().map(planification -> mapToDTO(planification)).collect(Collectors.toList());
		
        PlanificationResponseDto planificationResponseDto = new PlanificationResponseDto();
        planificationResponseDto.setContent(content);
        planificationResponseDto.setPageNo(planifications.getNumber());
        planificationResponseDto.setPageSize(planifications.getSize());
        planificationResponseDto.setTotalElements(planifications.getTotalElements());
        planificationResponseDto.setTotalPages(planifications.getTotalPages());
        planificationResponseDto.setLast(planifications.isLast());

        return planificationResponseDto;
	}

	 // convert Entity into DTO
    private PlanificationRequestDto mapToDTO(Planification planification){
      //  ActiviteRequestDtoactiviteDto = mapper.map(activite, ActiviteRequestDto.class);
       PlanificationRequestDto planificationDto = new PlanificationRequestDto();
       planificationDto.setId(planification.getId());
       planificationDto.setTitrePlanification(planification.getTitrePlanification());
       planificationDto.setImageUrl(planification.getImageUrl());
       planificationDto.setDescription(planification.getDescription());
	   planificationDto.setDate_start(planification.getDate_start());
	   planificationDto.setDate_end(planification.getDate_end());
	   planificationDto.setEntrepriseId(planification.getEntrepriseId());
	   planificationDto.setActiviteNom(planification.getActivite().getNom());
       planificationDto.setActiviteId(planification.getActivite().getId());
      
        return planificationDto;
    }


    //

	


	@Override
	public List<PlanificationRequestDto> searchPlanification(String keyword) {
		List<Planification> planifications = this.planificationRepository.findByTitrePlanification(keyword);
		//List<Activite> activites = this.activiteRepository.findAll(keyword);
		List<PlanificationRequestDto> planificationRequestDtos= planifications.stream().map(planification -> mapToDTO(planification)).collect(Collectors.toList());
		return planificationRequestDtos;
	}

	

	@Override
	public PlanificationResponseDto findAllPlanification(int pageNo, int pageSize, String sortBy, String sortDir,
														 String keyword) {
		Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

		User authenticatedUser = service.getAuthUser();
		Long idEntreprise = authenticatedUser.getEntreprise().getEntrepriseId();

		// create Pageable instance
        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);
        
        Page<Planification> planifications = planificationRepository.findByTitrePlanificationContainingAndEntrepriseId(pageable, "%"+keyword+"%", idEntreprise);
        
        List<Planification> listOfPlanifications = planifications.getContent();
        
        List<PlanificationRequestDto> content = listOfPlanifications.stream().map(planification -> mapToDTO(planification)).collect(Collectors.toList());
		log.info("content :: {}", content);
        PlanificationResponseDto planificationResponseDto = new PlanificationResponseDto();
        planificationResponseDto.setContent(content);
        planificationResponseDto.setPageNo(planifications.getNumber());
        planificationResponseDto.setPageSize(planifications.getSize());
        planificationResponseDto.setTotalElements(planifications.getTotalElements());
        planificationResponseDto.setTotalPages(planifications.getTotalPages());
        planificationResponseDto.setLast(planifications.isLast());

        return planificationResponseDto;
	}
	}


