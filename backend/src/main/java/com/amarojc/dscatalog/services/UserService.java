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
import com.amarojc.dscatalog.dtos.UserDTO;
import com.amarojc.dscatalog.dtos.UserInsertDTO;
import com.amarojc.dscatalog.entities.Role;
import com.amarojc.dscatalog.entities.User;
import com.amarojc.dscatalog.repositories.RoleRepository;
import com.amarojc.dscatalog.repositories.UserRepository;
import com.amarojc.dscatalog.services.exceptions.DatabaseIntegrityViolationException;
import com.amarojc.dscatalog.services.exceptions.ObjectNotFoundException;

@Service
public class UserService {

	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private RoleRepository roleRepository;
	
	@Transactional(readOnly = true)
	public Page<UserDTO> findAllPagedUser(Pageable pageable){
		Page<User> users = userRepository.findAll(pageable);
		return users.map(user -> new UserDTO(user));
	}
	
	@Transactional(readOnly = true)
	public UserDTO findByIdUser(Long id) {
		Optional<User> userOptional = userRepository.findById(id);
		User user = userOptional
				.orElseThrow(
					() -> new ObjectNotFoundException("Id não encontrado / not found " + id)
				);
		return new UserDTO(user); 
	}
	
	@Transactional
	public UserDTO insertUser(UserInsertDTO userDTO) {
		User user = new User();
		copyUserDtoToEntityUser(userDTO, user);
		user.setPassword(userDTO.getPassword());
		user = userRepository.save(user);
	
		return new UserDTO(user);
	}
	
	@Transactional
	public UserDTO updateUser(Long id, UserDTO userDTO) {
		try {
			User user = userRepository.getReferenceById(id);
			copyUserDtoToEntityUser(userDTO, user);			
			user = userRepository.save(user);
			
			return new UserDTO(user);
			
		}catch(EntityNotFoundException ex) {
			throw new ObjectNotFoundException("Usuário não encontrado / User not found " + id);
		}
	}
	
	public void deleteUser(Long id) {
		try {
			userRepository.deleteById(id);
		} catch (EmptyResultDataAccessException e) {
			throw new ObjectNotFoundException("Usuário não encontrado / User not found " + id);
		}catch(DataIntegrityViolationException dx) {
			throw new DatabaseIntegrityViolationException("Violação de integridade / Integrety Violation");
		}
	}
	
	private void copyUserDtoToEntityUser(UserDTO userDTO, User user) {
		user.setFirstName(userDTO.getFirstName());
		user.setLastName(userDTO.getLastName());
		user.setEmail(userDTO.getEmail());
		
		user.getRoles().clear();
		
		for(RoleDTO roleDTO : userDTO.getRoles()) {
			Role role = roleRepository.getReferenceById(roleDTO.getId());
			user.getRoles().add(role);
		}
	}
	
}
