package com.team.dev.service;

import com.team.dev.dto.PlanificationRequestDto;
import com.team.dev.dto.PlanificationResponseDto;
import com.team.dev.model.Planification;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public interface PlanificationService {
	//service to do  add ,edit ,delete ,list
	public Planification getPlanification(Long planificationId);
	public PlanificationResponseDto switchStatusPlanification(Long planificationId);
	public PlanificationResponseDto addPlanification(PlanificationRequestDto planificationRequestDto );
	public List<PlanificationResponseDto> getPlanifications();
	public PlanificationResponseDto getPlanificationById(Long planificationId);
	public PlanificationResponseDto deletePlanification(Long planificationId);
	public PlanificationResponseDto editPlanification(Long planificationId, PlanificationRequestDto planificationRequestDto);
	
	//pour add et remove
	public PlanificationResponseDto addActiviteToPlanification(Long planificationId, Long activiteId);
	
	public PlanificationResponseDto removeActiviteFromPlanification(Long planificationId, Long activiteId);
	public PlanificationResponseDto getAllPlanifications(int pageNo, int pageSize, String sortBy, String sortDir);
	
	//pour faire la rechercher
    public List<PlanificationRequestDto> searchPlanification(String keyword);

	PlanificationResponseDto findAllPlanification(int pageNo, int pageSize, String sortBy, String sortDir,
												  String keyword);
}
