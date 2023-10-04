package nl.backend.reparatieservice.controller;

import nl.backend.reparatieservice.dto.RoleDto;
import nl.backend.reparatieservice.model.Role;
import nl.backend.reparatieservice.repository.RoleRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
public class RoleController {

    private final RoleRepository roleRepository;

    public RoleController(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    public ResponseEntity<List<RoleDto>> getRoles() {
        try {
            List<RoleDto> roleDtos = new ArrayList<>();
            for (Role role : roleRepository.findAll()) {
                RoleDto rdto = new RoleDto();
                rdto.rolename = role.getRolename();
                roleDtos.add(rdto);
            }
            return ResponseEntity.ok(roleDtos);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
}
