package com.team.dev.service;

import com.team.dev.dto.SortieDto;
import com.team.dev.dto.SortieResponse;
import com.team.dev.dto.TypeSortie;
import com.team.dev.model.SortieProduit;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
public interface SortieProduitService {
	
	public SortieResponse addSortieProduct(SortieDto sortieDto );
	//public SortieResponse approProduct(SortieDto sortieDto);
	public SortieResponse getProductById(Long sortieId);
	public List<SortieResponse> mvtStockProduct(Long sortieId);
	public SortieProduit getProduct(Long sortieId);
	public SortieResponse getSortieProducts(SortieResponse response);

	BigDecimal stockReelPructs(Long idProduct);
	public SortieResponse getAllSortieProducts(int pageNo, int pageSize, String sortBy, String sortDir);
	public SortieResponse deleteSortieProduct(Long sortieId);
	public SortieResponse editSortieProduct(Long sortieId, SortieDto sortieDto);
	
	//public SortieResponse searchSortieProduitFull(int pageNo, int pageSize, String sortBy, String sortDir, String keyword);

	SortieResponse searchSortieProduitFull(int pageNo, int pageSize, String sortBy, String sortDir);
	SortieResponse filterByTypesortie(int pageNo, int pageSize, String sortBy, String sortDir, TypeSortie keyword);

	public byte[] exportItemsToExcel(LocalDateTime startDate, LocalDateTime endDate, HttpServletResponse response) throws IOException;
	List<SortieDto> filterRandeDates(int pageNo, int pageSize, String sortBy, String sortDir,LocalDateTime startDate, LocalDateTime endDate);
}
