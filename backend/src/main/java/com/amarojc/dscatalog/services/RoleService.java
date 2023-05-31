package com.amarojc.dscatalog.services;

import java.util.Optional;

import javax.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.amarojc.dscatalog.dtos.RoleDTO;
import com.amarojc.dscatalog.entities.Role;
import com.amarojc.dscatalog.repositories.RoleRepository;
import com.amarojc.dscatalog.services.exceptions.DatabaseIntegrityViolationException;
import com.amarojc.dscatalog.services.exceptions.ObjectNotFoundException;

@Service
public class RoleService {

	@Autowired
	private RoleRepository roleRepository;
	
	@Transactional(readOnly = true)
	public Page<RoleDTO> findAllPagedRole(Pageable pageable){
		Page<Role> roles = roleRepository.findAll(pageable);
		return roles.map(role -> new RoleDTO(role));
	}
	
	@Transactional(readOnly = true)
	public RoleDTO findByIdRole(Long id) {
		Optional<Role> roleOptional = roleRepository.findById(id);
		Role role = roleOptional
				.orElseThrow(
					() -> new ObjectNotFoundException("Perfil não encontrado / Role not found " + id)
				);
		return new RoleDTO(role);
	}
	
	@Transactional
	public RoleDTO insertRole(RoleDTO roleDTO) {
		Role role = new Role();
		role.setAuthority(roleDTO.getAuthority());
		role = roleRepository.save(role);
		
		return new RoleDTO(role);
	}
	
	@Transactional
	public RoleDTO updateRole(Long id, RoleDTO roleDTO) {
		try{
			Role role = roleRepository.getReferenceById(id);
			role.setAuthority(roleDTO.getAuthority());
			
			return new RoleDTO(roleRepository.save(role));
		}catch(EntityNotFoundException ex) {
			throw new ObjectNotFoundException("Perfil não encontrado / Role not found " + id);
		}		
	}
	
	public void deleteRole(Long id) {
		try {
			roleRepository.deleteById(id);
		}catch(EmptyResultDataAccessException ex) {
			throw new ObjectNotFoundException("Perfil não encontrado / Role not found " + id);
		}catch(DataIntegrityViolationException dex) {
			throw new DatabaseIntegrityViolationException("Violação de integridade / Integrety Violation");
		}
	}
}
