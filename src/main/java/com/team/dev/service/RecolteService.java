package com.team.dev.service;

import com.team.dev.dto.AppResponse;
import com.team.dev.dto.RecolteRequest;
import com.team.dev.dto.RecolteResponse;
import com.team.dev.model.Recolte;

import java.util.Date;
import java.util.List;

public interface RecolteService {

    public RecolteResponse newRecolte(RecolteRequest request);
    public RecolteResponse edit(RecolteRequest request,Long recolteId);
    public void delete(Long recolteId);
    public AppResponse getAll(int pageNo, int pageSize, String sortBy, String sortDir, String keyword);

    public RecolteResponse getById(Long recolteId);

    public RecolteRequest getId(Long recolteId);

    public List<RecolteRequest> getBetweenDates(Date startDate, Date endDate);
}
