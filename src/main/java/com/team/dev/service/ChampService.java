package com.team.dev.service;



import com.team.dev.dto.ChampRequestDto;
import com.team.dev.dto.ChampResponseDto;
import com.team.dev.model.Champ;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ChampService {
	
    public ChampResponseDto addChamp(ChampRequestDto champRequestDto );
	public ChampResponseDto getChampById(Long champId);
	public ChampRequestDto getChampId(Long champId);
	public Champ getChamp(Long champId);
	public List<ChampResponseDto> getChamps();
	public ChampResponseDto deleteChamp(Long champId);
	public ChampResponseDto editChamp(Long champId, ChampRequestDto champRequestDto);
	
	
	/**
	//public ChampResponseDto addActiviteToChamp(Long champId, Long activiteId);
	public ChampResponseDto addVaneToChamp(Long champId, Long vaneId);
	public ChampResponseDto addSousChampToChamp(Long champId, Long souschampId);
	public ChampResponseDto removeSousChampFromChamp(Long champId, Long souschampId);
	public ChampResponseDto removeVaneFromChamp(Long champId, Long vaneId);
	
	//public ChampResponseDto removeActiviteFromChamp(Long champId, Long activiteId);
	 
	**/
	public ChampResponseDto removeLocaliteFromChamp(Long champId, Long localiteId);
	public ChampResponseDto addLocaliteToChamp(Long champId, Long localiteId);
	public ChampResponseDto getAllChamps(int pageNo, int pageSize, String sortBy, String sortDir);
	
// pour faire la rechercher
	  public List<ChampRequestDto> searchChamp(String keyword);
		
	  //public List<ChampRequestDto> searchChampFull(String keyword);
	ChampResponseDto searchChampFull(int pageNo, int pageSize, String sortBy, String sortDir, String keyword);
}