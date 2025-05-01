package com.bsuir.aviatours.dto;

import com.bsuir.aviatours.model.Role;

public class RoleDTO {
    private Integer id;
    private String name;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Role toEntity() {
        Role role = new Role();
        role.setId(this.id);
        role.setName(this.name);
        return role;
    }

    public static RoleDTO fromEntity(Role role) {
        RoleDTO dto = new RoleDTO();
        if (role.getId() != null) {
            dto.setId(role.getId());
            dto.setName(role.getName());
        }
        return dto;
    }
}
