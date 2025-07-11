package com.team.dev.service;


import com.team.dev.dto.ActiviteRequestDto;
import com.team.dev.dto.ActiviteResponseDto;
import com.team.dev.model.Activite;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ActiviteService {
	
	    /** New codes **/
	    
	    public Activite getActivite(Long activiteId);
		public ActiviteResponseDto addActivite(ActiviteRequestDto activiteRequestDto );
		public List<ActiviteResponseDto> getActivites();
		
		public ActiviteResponseDto getAllActivites(int pageNo, int pageSize, String sortBy, String sortDir, String keyword);
		 ActiviteResponseDto getActiviteById(Long activiteId);
		public ActiviteResponseDto deleteActivite(Long activiteId);
		public ActiviteResponseDto editActivite(Long activiteId, ActiviteRequestDto activiteRequestDto);
		
		public ActiviteResponseDto addChampToActivite(Long activiteId, Long champId);
		public ActiviteResponseDto removeChampFromActivite(Long activiteId, Long champId);
		
	       public List<ActiviteRequestDto> searchActivite(String keyword);
		
		  //public List<ActiviteRequestDto> searchActiviteFull(String keyword);
	       public ActiviteResponseDto searchActiviteFull(int pageNo, int pageSize, String sortBy, String sortDir, String keyword);

}
