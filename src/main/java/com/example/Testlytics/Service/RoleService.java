package com.example.Testlytics.Service;

import com.example.Testlytics.Entity.Role;
import com.example.Testlytics.Repository.RoleRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RoleService {

    private final RoleRepository roleRepository;

    public RoleService(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    // Get all available roles
    public List<Role> getAllRoles() {
        return roleRepository.findAll();
    }

    // Get role by name
    public Optional<Role> getRoleByName(String roleName) {
        return roleRepository.findByRoleName(roleName); // Fixed method name
    }
}
