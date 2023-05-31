package com.amarojc.dscatalog.resources;

import java.net.URI;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.amarojc.dscatalog.dtos.RoleDTO;
import com.amarojc.dscatalog.services.RoleService;

@Controller
@RequestMapping(value = "/roles")
public class RoleResource {
	
	@Autowired
	private RoleService roleService;

	@GetMapping()
	public ResponseEntity<Page<RoleDTO>> findAllPagedRole(Pageable pageable){
		Page<RoleDTO> roles = roleService.findAllPagedRole(pageable);
		return ResponseEntity.ok().body(roles);
	}
	
	@GetMapping(value = "/{id}")
	public ResponseEntity<RoleDTO> findByIdRole(@PathVariable Long id){
		return ResponseEntity.ok().body(roleService.findByIdRole(id));
	}
	
	@PostMapping()
	public ResponseEntity<RoleDTO> insertRole(@RequestBody RoleDTO roleDTO){
		roleDTO = roleService.insertRole(roleDTO);
		
		URI uri = ServletUriComponentsBuilder
				.fromCurrentRequest()
				.path("/{id}")
				.buildAndExpand(roleDTO).toUri();
		
		return ResponseEntity.created(uri).body(roleDTO);
	}
	
	@PutMapping(value = "/{id}")
	public ResponseEntity<RoleDTO> updateRole(@PathVariable Long id, @RequestBody RoleDTO roleDTO){
		roleDTO = roleService.updateRole(id, roleDTO);
		return ResponseEntity.ok().body(roleDTO);
	}
	
	@DeleteMapping(value = "/{id}")
	public ResponseEntity<Void> deleteRole(@PathVariable Long id){
		roleService.deleteRole(id);
		return ResponseEntity.noContent().build();
	}
}
