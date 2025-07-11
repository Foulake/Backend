package com.team.dev.service;

import com.team.dev.dto.CategoryRequestDto;
import com.team.dev.dto.EntrepriseRequestDto;
import com.team.dev.dto.EntrepriseResponseDto;
import com.team.dev.model.Entreprise;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Service;
import java.util.List;

public interface EntrepriseService {

	EntrepriseResponseDto addEntreprise(EntrepriseRequestDto entrepriseRequestDto);

	List<EntrepriseResponseDto> getentreprises();

	public Entreprise getEntreprise(Long entrepriseId);
	public EntrepriseResponseDto getAllEntreprises(int pageNo, int pageSize, String sortBy, String sortDir, String keyword);
	public EntrepriseResponseDto getEntrepriseById(Long entrepriseId);
	public EntrepriseRequestDto getEntrepriseId(Long entrepriseId);
	public EntrepriseResponseDto deleteEntreprise(Long categoryId);
	public EntrepriseResponseDto editEntreprise(Long entrepriseId, EntrepriseRequestDto entrepriseRequestDto);


}
