package com.team.dev.service.impl;

import com.team.dev.dto.AppResponse;
import com.team.dev.dto.RecolteRequest;
import com.team.dev.dto.RecolteResponse;
import com.team.dev.exception.ApiException;
import com.team.dev.mapper.Mapper;
import com.team.dev.model.Champ;
import com.team.dev.model.Recolte;
import com.team.dev.model.SousChamp;
import com.team.dev.model.User;
import com.team.dev.repository.RecolteRepository;
import com.team.dev.repository.SousChampRepository;
import com.team.dev.service.AuthService;
import com.team.dev.service.ChampService;
import com.team.dev.service.RecolteService;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;


@Service
@Log4j2
public class RecolteServiceImpl implements RecolteService {
    private final RecolteRepository recolteRepository;
    private final AuthService service;
    private final com.team.dev.service.ChampService champService;

    public RecolteServiceImpl(RecolteRepository recolteRepository, AuthService service,ChampService champService) {
        this.recolteRepository = recolteRepository;
        this.service = service;
        this.champService = champService;
    }

    @Override
    public RecolteResponse newRecolte(RecolteRequest request) {
        Recolte recolte = new Recolte();

        User authenticatedUser = service.getAuthUser();
        Long idEntreprise = authenticatedUser.getEntreprise().getEntrepriseId();

        if(request.getQuantite().compareTo(BigDecimal.ZERO) == 0){
            throw new ApiException("La quantité ne doit pas être égale à 0");
        }else if (request.getQuantite().compareTo(BigDecimal.ZERO) <0){
            throw new ApiException("La quantité ne doit pas être négative");
        }

        if (this.champService.getChamp(request.getChampId()) == null){
            throw new ApiException("Il n'existe pas de planche avec ID "+request.getChampId());
        }
        Champ champ = this.champService.getChamp(request.getChampId());
        recolte.setChamp(champ);
        //recolte.setDesc(request.getDesc());
        recolte.setEntrepriseId(idEntreprise);
        recolte.setQuantite(request.getQuantite());
        //recolte.setConditioMeteo(request.getConditioMeteo());
        recolte.setQualite(request.getQualite());
        recolte.setUniteMesure(request.getUniteMesure());
        recolte.setTypeCulture(request.getTypeCulture());
        recolte.setStartDate(request.getStartDate());
        recolte.setEndDate(request.getEndDate());

        Recolte saved = this.recolteRepository.save(recolte);
        return Mapper.recolteToRecolteResponse(saved);
    }

    @Override
    public RecolteResponse edit(RecolteRequest request, Long recolteId) {
        Recolte response = this.getRecolte(recolteId);
        //response.setDesc(request.getDesc());
        response.setImageUrl(request.getImageUrl());
        response.setStartDate(request.getStartDate());
        response.setEndDate(request.getEndDate());
        response.setTypeCulture(request.getTypeCulture());
        response.setQuantite(request.getQuantite());
        response.setQualite(request.getQualite());
        //response.setConditioMeteo(request.getConditioMeteo());
        response.setUniteMesure(request.getUniteMesure());

        if(request.getChampId() != null ) {
            Champ champ = this.champService.getChamp(request.getChampId());
            response.setChamp(champ);
        }
        return Mapper.recolteToRecolteResponse(response);
    }

    @Override
    public void delete(Long recolteId) {
        Recolte recolte= this.recolteRepository.findById(recolteId)
                .orElseThrow(() -> new IllegalArgumentException("Il n'existe pas de recolte avec id : " + recolteId));
        this.recolteRepository.delete(recolte);
    }

    @Override
    public AppResponse getAll(int pageNo, int pageSize, String sortBy, String sortDir, String keyword) {
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        User authenticatedUser = service.getAuthUser();
        Long entrepriseId = authenticatedUser.getEntreprise().getEntrepriseId();

        log.info("IcI entrepriseid::::::::::{}", entrepriseId);
        // create Pageable instance
        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);

        Page<Recolte> recoltes = this.recolteRepository.findByTypeCultureContainingAndEntrepriseId(pageable, "%"+keyword+"%", entrepriseId);
        List<Recolte> listOfRecoltes = recoltes.getContent();
        //List<RecolteRequest> content= listOfRecoltes.stream().map(this::mapToDTO).collect(Collectors.toList());
        List<RecolteRequest> content = Mapper.recolteToRecolteRequests(listOfRecoltes);
        log.info("content :::{}", content);
        AppResponse response = new AppResponse();
        response.setContent(content);
        response.setPageNo(recoltes.getNumber());
        response.setPageSize(recoltes.getSize());
        response.setTotalElements(recoltes.getTotalElements());
        response.setTotalPages(recoltes.getTotalPages());
        response.setLast(recoltes.isLast());
        return response;
    }

    private RecolteRequest mapToDTO(Recolte recolte) {
        RecolteRequest request = new RecolteRequest();
        request.setId(recolte.getId());
        request.setQuantite(recolte.getQuantite());
        request.setEndDate(recolte.getEndDate());
        request.setStartDate(recolte.getStartDate());
        request.setQualite(recolte.getQualite());
        //request.setConditioMeteo(recolte.getConditioMeteo());
        request.setUniteMesure(recolte.getUniteMesure());
        //request.setDesc(recolte.getDesc());
        request.setImageUrl(recolte.getImageUrl());
        request.setEntrepriseId(recolte.getEntrepriseId());
        request.setTypeCulture(recolte.getTypeCulture());
        request.setChamp(recolte.getChamp().getNomChamp());
        request.setChampId(recolte.getChamp().getId());
        return request;
    }

    @Override
    public RecolteResponse getById(Long recolteId) {
        Recolte recolte = this.recolteRepository.findById(recolteId)
                .orElseThrow(() -> new IllegalArgumentException("Il n'existe pas de recolte avec id : " + recolteId));

        return Mapper.recolteToRecolteResponse(recolte);
    }

    @Override
    public RecolteRequest getId(Long recolteId) {
        Recolte recolte = this.recolteRepository.findById(recolteId)
                .orElseThrow(() -> new IllegalArgumentException("Il n'existe pas de recolte avec id : " + recolteId));
        log.info("find :::{}", recolte);
        return Mapper.recolteToRecolteRequest(recolte);
    }

    public Recolte getRecolte(Long recolteId) {
        return this.recolteRepository.findById(recolteId)
                .orElseThrow(() -> new IllegalArgumentException("Il n'existe pas de recolte avec id : " + recolteId));
    }

    @Override
    public List<RecolteRequest> getBetweenDates(Date startDate, Date endDate){
        User authenticatedUser = service.getAuthUser();
        Long idEntreprise = authenticatedUser.getEntreprise().getEntrepriseId();
        return Mapper.recolteToRecolteRequests(recolteRepository.findDistinctByEntrepriseIdAndStartDateBetween(idEntreprise, startDate, endDate));
    }


}
