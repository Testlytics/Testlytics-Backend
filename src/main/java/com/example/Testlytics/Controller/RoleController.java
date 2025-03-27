package com.example.Testlytics.Controller;

import com.example.Testlytics.Entity.Role;
import com.example.Testlytics.Service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@CrossOrigin(origins = "http://localhost:5173")  
@RestController
@RequestMapping("/api/roles")
public class RoleController {

    @Autowired
    private RoleService roleService;

    @GetMapping
    public List<Role> getAllRoles() {
        return roleService.getAllRoles();
    }

    @GetMapping("/{roleName}")
    public Optional<Role> getRoleByName(@PathVariable String roleName) {
        return roleService.getRoleByName(roleName);
    }
    @PostMapping
    public Role createRole(@RequestBody Role role) {
        return roleService.saveRole(role);
}

}
