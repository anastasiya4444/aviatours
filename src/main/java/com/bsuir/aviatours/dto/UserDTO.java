package com.bsuir.aviatours.dto;

import com.bsuir.aviatours.model.User;

public class UserDTO {
    private Integer id;
    private RoleDTO role;
    private String username;
    private String password;

    public UserDTO() {}

    public UserDTO(Integer id, RoleDTO role, String username, String password) {
        this.id = id;
        this.role = role;
        this.username = username;
        this.password = password;
    }

    public User toEntity() {
        User user = new User();
        user.setId(this.id);
        if(this.role != null){
            user.setRole(this.role.toEntity());
        }
        user.setUsername(this.username);
        user.setPassword(this.password);
        return user;
    }

    public static UserDTO fromEntity(User user) {
        UserDTO dto = new UserDTO();
        if (user.getId() != null) {
            dto.setId(user.getId());
            if (user.getRole() != null) {
                dto.setRole(RoleDTO.fromEntity(user.getRole()));
            }
            dto.setUsername(user.getUsername());
            dto.setPassword(user.getPassword());
        }
        return dto;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public RoleDTO getRole() {
        return role;
    }

    public void setRole(RoleDTO role) {
        this.role = role;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }


}
