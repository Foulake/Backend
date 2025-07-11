package com.team.dev.service.impl;

import com.team.dev.dto.MagasinRequestDto;
import com.team.dev.dto.MagasinResponseDto;
import com.team.dev.exception.ApiException;
import com.team.dev.mapper.Mapper;
import com.team.dev.model.Magasin;
import com.team.dev.model.Product;
import com.team.dev.model.User;
import com.team.dev.repository.MagasinRepository;
import com.team.dev.service.AuthService;
import com.team.dev.service.MagasinService;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class MagasinServiceImpl implements MagasinService {
	private final MagasinRepository magasinRepository;

	private final AuthService service;

	public MagasinServiceImpl(MagasinRepository magasinRepository, AuthService service) {
		this.magasinRepository = magasinRepository;
		this.service = service;
	}

	@Override
	public MagasinResponseDto addMagasin(MagasinRequestDto magasinRequestDto) {
		Magasin magasin = new Magasin();
		User authenticatedUser = service.getAuthUser();
		Long idEntreprise = authenticatedUser.getEntreprise().getEntrepriseId();

		if(magasinRepository.existsByNomMagasin(magasinRequestDto.getNomMagasin())) {
			throw new ApiException(" Il existe bien un magasin avec cet nom : " +magasinRequestDto.getNomMagasin());

		}else{
		magasin.setNomMagasin(magasinRequestDto.getNomMagasin());
		magasin.setEntrepriseId(idEntreprise);
		}

//		if(magasinRequestDto.getLocaliteId() == null ) {
//			throw new IllegalArgumentException("Le magasin manque de Localité !");
//		}
		magasinRepository.save(magasin);
		return Mapper.magasinToMagasinResponseDto(magasin);
		}

	@Override
	public List<MagasinResponseDto> getMagasins() {
		List<Magasin> magasins = StreamSupport
				.stream(magasinRepository.findAll().spliterator(), false).collect(Collectors.toList());
		return Mapper.magasinToMagasinResponseDtos(magasins);
	}

	@Override
	public Magasin getMagasin(Long magasinId) {
		return magasinRepository.findById(magasinId).orElseThrow(
		() -> new ApiException("Il n'existe pas de magasin avec id : " + magasinId));
	}

	@Override
	public MagasinResponseDto getMagasinById(Long magasinId) {
		Magasin magasin = getMagasin(magasinId);
		return Mapper.magasinToMagasinResponseDto(magasin);
	}


	@Override
	public MagasinResponseDto deleteMagasin(Long magasinId) {
		Magasin magasin = getMagasin(magasinId);
		magasinRepository.delete(magasin);
		return Mapper.magasinToMagasinResponseDto(magasin);
	}

	@Transactional
	@Override
	public MagasinResponseDto editMagasin(Long magasinId, MagasinRequestDto magasinRequestDto) {
		Magasin magasinToEdit = getMagasin(magasinId);
		magasinToEdit.setNomMagasin(magasinRequestDto.getNomMagasin());

		return Mapper.magasinToMagasinResponseDto(magasinToEdit);
		}

	@Override
	public MagasinResponseDto getAllMagasins(int pageNo, int pageSize, String sortBy, String sortDir, String keyword) {

        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

		User authenticatedUser = service.getAuthUser();
		Long idEntreprise = authenticatedUser.getEntreprise().getEntrepriseId();

		// create Pageable instance
        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);

        Page<Magasin> magasins = magasinRepository.findByNomMagasinContainingAndEntrepriseId(pageable, "%"+keyword+"%", idEntreprise);

        // get content for page object
        List<Magasin> listOfMagasins = magasins.getContent();

        List<MagasinRequestDto> content= listOfMagasins.stream().map(magasin -> mapToDTO(magasin)).collect(Collectors.toList());

        MagasinResponseDto magasinResponseDto = new MagasinResponseDto();
        magasinResponseDto.setContent(content);
        magasinResponseDto.setPageNo(magasins.getNumber());
        magasinResponseDto.setPageSize(magasins.getSize());
        magasinResponseDto.setTotalElements(magasins.getTotalElements());
        magasinResponseDto.setTotalPages(magasins.getTotalPages());
        magasinResponseDto.setLast(magasins.isLast());

        return magasinResponseDto;
    }
	
	// convert Entity into DTO
    private MagasinRequestDto mapToDTO(Magasin magasin){
      //  MagasinRequestDto magasinRequestDto = mapper.map(champ, MagasinRequestDto.class);
        MagasinRequestDto magasinRequestDto = new MagasinRequestDto();
        magasinRequestDto.setNomMagasin(magasin.getNomMagasin());
        magasinRequestDto.setId(magasin.getId());
 //       magasinRequestDto.setNomLocalite(magasin.getLocalite().getNom());
        /** new codes **/
        List<String> names = new ArrayList<>();
		List<Product> products = magasin.getProducts();
		for(Product product: products) {
			names.add(product.getName());
		}
		magasinRequestDto.setProductNames(names);
        
        return magasinRequestDto;
    }

	@Override
	public List<MagasinRequestDto> searchMagasin(String keyword) {
		
		List<Magasin> magasins = this.magasinRepository.findByNomMagasin(keyword);
		List<MagasinRequestDto> magasinRequestDtos=magasins.stream().map(magasin -> mapToDTO(magasin)).collect(Collectors.toList());
		return magasinRequestDtos;
	}

	
}
