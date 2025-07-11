package com.team.dev.service;

import com.team.dev.dto.SousChampRequestDto;
import com.team.dev.dto.SousChampResponseDto;
import com.team.dev.model.SousChamp;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface SousChampService {
	

	public List<SousChampRequestDto> getSousChampsByChampId(Long champId);
	SousChampResponseDto getAllSousChamps(int pageNo, int pageSize, String sortBy, String sortDir, String keyword);

	List<SousChampRequestDto> searchSousChampFull(String keyword);

	public SousChamp getSousChamp(Long souschampId);
	public SousChampResponseDto addSousChamp(SousChampRequestDto souschampRequestDto );
	//public List<SousChampResponseDto> getSousChamps();	
	public SousChampResponseDto getSousChampById(Long souschampId);
	public SousChampResponseDto deleteSousChamp(Long souschampId);
	public SousChampResponseDto editSousChamp(Long souschampId, SousChampRequestDto souschampRequestDto);
	public List<SousChampResponseDto> getSousChamps();


	SousChampRequestDto getSousChampId(Long id);
}
