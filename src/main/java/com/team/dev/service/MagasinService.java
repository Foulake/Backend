package com.team.dev.service;

import com.team.dev.dto.MagasinRequestDto;
import com.team.dev.dto.MagasinResponseDto;
import com.team.dev.model.Magasin;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface MagasinService {

	MagasinResponseDto getAllMagasins(int pageNo, int pageSize, String sortBy, String sortDir, String keyword);
	public Magasin getMagasin(Long magasinId);
	public MagasinResponseDto addMagasin(MagasinRequestDto magasinRequestDto);
	public List<MagasinResponseDto> getMagasins();
	public MagasinResponseDto getMagasinById(Long magasinId);
	public MagasinResponseDto deleteMagasin(Long magasinId);

	public MagasinResponseDto editMagasin(Long magasinId, MagasinRequestDto magasinRequestDto);

	public List<MagasinRequestDto> searchMagasin(String keyword);
}
