package com.example.Testlytics.Service;

import com.example.Testlytics.Entity.Role;
import com.example.Testlytics.Repository.RoleRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class RoleService {

    private final RoleRepository roleRepository;

    // Constructor Injection (Recommended)
    public RoleService(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    // Fetch role by ID (from feature-swetha)
    public Optional<Role> getRoleById(Long roleId) {
        return roleRepository.findById(roleId);
    }

    // Fetch all roles
    public List<Role> getAllRoles() {
        return roleRepository.findAll();
    }

    // Fetch role by name
    public Optional<Role> getRoleByName(String roleName) {
        return roleRepository.findByRoleName(roleName);
    }

    // Save a new role
    @Transactional
    public Role saveRole(Role role) {
        return roleRepository.save(role);
    }
}
