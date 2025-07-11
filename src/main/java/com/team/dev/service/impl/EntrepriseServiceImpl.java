package com.team.dev.service.impl;

import com.team.dev.dto.*;
import com.team.dev.exception.ApiException;
import com.team.dev.mapper.Mapper;
import com.team.dev.model.Entreprise;
import com.team.dev.model.Role;
import com.team.dev.model.User;
import com.team.dev.repository.EntrepriseRepository;
import com.team.dev.repository.RoleRepository;
import com.team.dev.service.AuthService;
import com.team.dev.service.EntrepriseService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.mail.MailException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class EntrepriseServiceImpl implements EntrepriseService {

	private final EntrepriseRepository entrepriseRepository;
	//private final AuthService authService;
	private final RoleRepository roleRepository;

	@Autowired
	public EntrepriseServiceImpl(EntrepriseRepository entrepriseRepository, RoleRepository roleRepository) {
		this.entrepriseRepository = entrepriseRepository;
		//this.authService = authService;
		this.roleRepository = roleRepository;
	}

	@Override
	//@Transactional
	public EntrepriseResponseDto addEntreprise(EntrepriseRequestDto entrepriseRequestDto) {
		if (entrepriseRepository.existsEntrepriseByNom(entrepriseRequestDto.getNom())) {
			throw new ApiException(" Il existe bien une entreprise avec cet nom : " + entrepriseRequestDto.getNom());
		}

		Entreprise entreprise = Entreprise.builder()
				.entrepriseId(entrepriseRequestDto.getIdEntreprise())
				.nom(entrepriseRequestDto.getNom())
				.adresse(entrepriseRequestDto.getAdresse())
				.contact(entrepriseRequestDto.getContact())
				.email(entrepriseRequestDto.getEmail())
				.description(entrepriseRequestDto.getDescription())
				.imageUrl(entrepriseRequestDto.getImageUrl())
				.build();

		Entreprise entreprise1 = this.entrepriseRepository.save(entreprise);

		/*RegisterRequest request = fromUserDto(entreprise1);
		this.authService.register(request, servletRequest);*/
			return Mapper.EntrepriseToEntrepriseResponseDto(entreprise1);

	}

	@Override
	public List<EntrepriseResponseDto> getentreprises() {
		List<Entreprise> entreprises = StreamSupport
				.stream(entrepriseRepository.findAll().spliterator(), false).collect(Collectors.toList());
		return Mapper.EntrepriseToEntrepriseResponseDtos(entreprises);
	}

	@Override
	public Entreprise getEntreprise(Long idEntreprise) {
		return entrepriseRepository.findById(idEntreprise).orElseThrow(
				() -> new ApiException("Il n'existe pas une entreprise avec id : " + idEntreprise));
	}

	@Override
	public EntrepriseResponseDto getAllEntreprises(int pageNo, int pageSize, String sortBy, String sortDir, String keyword) {
		Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending()
				: Sort.by(sortBy).descending();

		// create Pageable instance
		Pageable pageable = PageRequest.of(pageNo, pageSize, sort);

		Page<Entreprise> entreprises = entrepriseRepository.findByNomContaining(pageable, "%"+keyword+"%");

		// get content for page object
		List<Entreprise> listOfEntreprises = entreprises.getContent();

		List<EntrepriseRequestDto> content = listOfEntreprises.stream().map(Entreprise -> mapToDTO(Entreprise)).collect(Collectors.toList());


		EntrepriseResponseDto EntrepriseResponseDto = new EntrepriseResponseDto();
		EntrepriseResponseDto.setContent(content);
		EntrepriseResponseDto.setPageNo(entreprises.getNumber());
		EntrepriseResponseDto.setPageSize(entreprises.getSize());
		EntrepriseResponseDto.setTotalElements(entreprises.getTotalElements());
		EntrepriseResponseDto.setTotalPages(entreprises.getTotalPages());
		EntrepriseResponseDto.setLast(entreprises.isLast());

		return EntrepriseResponseDto;
	}

	@Override
	public EntrepriseResponseDto getEntrepriseById(Long idEntreprise) {
		Entreprise entreprise = getEntreprise(idEntreprise);
		return Mapper.EntrepriseToEntrepriseResponseDto(entreprise);
	}

	@Override
	public EntrepriseRequestDto getEntrepriseId(Long entrepriseId) {
		Entreprise entreprise = getEntreprise(entrepriseId);

		return Mapper.EntrepriseToEntrepriseRequestDto(entreprise);
	}

	@Override
	public EntrepriseResponseDto deleteEntreprise(Long idEntreprise) {
		Entreprise entreprise = getEntreprise(idEntreprise);
		entrepriseRepository.delete(entreprise);
		return Mapper.EntrepriseToEntrepriseResponseDto(entreprise);
	}

	@Transactional
	@Override
	public EntrepriseResponseDto editEntreprise(Long idEntreprise, EntrepriseRequestDto EntrepriseRequestDto) {
		Entreprise entrepriseToEdit = getEntreprise(idEntreprise);
		entrepriseToEdit.setNom(EntrepriseRequestDto.getNom());
		entrepriseToEdit.setEmail(EntrepriseRequestDto.getEmail());
		entrepriseToEdit.setAdresse(EntrepriseRequestDto.getAdresse());
		entrepriseToEdit.setContact(EntrepriseRequestDto.getContact());
		entrepriseToEdit.setDescription(EntrepriseRequestDto.getDescription());
		entrepriseToEdit.setImageUrl(EntrepriseRequestDto.getImageUrl());

		return Mapper.EntrepriseToEntrepriseResponseDto(entrepriseToEdit);
	}

	// convert Entity into DTO
	private EntrepriseRequestDto mapToDTO(Entreprise entreprise) {
		//  EntrepriseRequestDto EntrepriseRequestDto = mapper.map(Entreprise, EntrepriseRequestDto.class);
		EntrepriseRequestDto EntrepriseRequestDto = new EntrepriseRequestDto();
		EntrepriseRequestDto.setIdEntreprise(entreprise.getEntrepriseId());
		EntrepriseRequestDto.setNom(entreprise.getNom());
		EntrepriseRequestDto.setEmail(entreprise.getEmail());
		EntrepriseRequestDto.setAdresse(entreprise.getAdresse());
		EntrepriseRequestDto.setDescription(entreprise.getDescription());
		EntrepriseRequestDto.setContact(entreprise.getContact());
		EntrepriseRequestDto.setImageUrl(entreprise.getImageUrl());
		/** new codes **/
		List<String> names = new ArrayList<>();
		List<User> users = entreprise.getUsers();
		for (User user : users) {
			names.add(user.getPrenom() + " " + user.getNom());
		}
		EntrepriseRequestDto.setUserNames(names.toString());

		return EntrepriseRequestDto;
	}

	private RegisterRequest fromUserDto( Entreprise entreprise){
		Role roleUsers = new Role("ADMIN");
		if (roleRepository.findByRoleName("ADMIN").isPresent()) {
			roleUsers.setRoleName(roleUsers.getRoleName());
		}else {
			roleRepository.save(roleUsers);
		}
		Role roleUser = (Role) roleRepository.findByRoleName("ADMIN").orElseThrow();

		Collection<Role> roles = new HashSet<>();
		roles.add(roleUser);
		return RegisterRequest.builder()
				.nom(entreprise.getNom())
				.prenom(entreprise.getAdresse())
				.entreprise(entreprise)
				.email(entreprise.getEmail())
				.imageUrl(entreprise.getImageUrl())
				.isEnabled(false)
				.password(entreprise.getContact())
				.roles(roles)
				.build();

	}

}