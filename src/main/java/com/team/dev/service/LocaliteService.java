package com.team.dev.service;


import com.team.dev.dto.LocaliteRequestDto;
import com.team.dev.dto.LocaliteResponseDto;
import com.team.dev.model.Localite;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface LocaliteService {
	
	public Localite getLocalite(Long localiteId);
	public LocaliteResponseDto addLocalite(LocaliteRequestDto localiteRequestDto );
	public List<LocaliteResponseDto> getLocalites();
	public LocaliteResponseDto getAllLocalites(int pageNo, int pageSize, String sortBy, String sortDir, String keyword);
	public LocaliteResponseDto getLocaliteById(Long localiteId);
	public LocaliteResponseDto deleteLocalite(Long localiteId);
	public LocaliteResponseDto editLocalite(Long localiteId, LocaliteRequestDto localiteRequestDto);
	
	public List<LocaliteRequestDto> searchLocalite(String keyword);
	
}
