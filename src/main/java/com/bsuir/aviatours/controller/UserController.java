package com.bsuir.aviatours.controller;

import com.bsuir.aviatours.dto.UserDTO;
import com.bsuir.aviatours.model.Role;
import com.bsuir.aviatours.model.User;
import com.bsuir.aviatours.security.UserDetailsConf;
import com.bsuir.aviatours.security.UserDetailsServiceConf;
import com.bsuir.aviatours.service.implementations.RoleServiceImpl;
import com.bsuir.aviatours.service.implementations.UserServiceImpl;
import com.bsuir.aviatours.service.interfaces.EntityService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping
@CrossOrigin
public class UserController {

    private final EntityService<User> entityService;
    private final UserDetailsServiceConf userDetailsService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final AuthenticationManager authenticationManager;
    private final UserServiceImpl entityServiceUser;
    private final RoleServiceImpl roleServiceImpl;
    private final EntityService<User> userService;

    public UserController(EntityService<User> entityService,
                          UserDetailsServiceConf userDetailsService,
                          BCryptPasswordEncoder bCryptPasswordEncoder,
                          AuthenticationManager authenticationManager,
                          UserServiceImpl entityServiceUser,
                          RoleServiceImpl roleServiceImpl,
                          EntityService<User> userService) {
        this.entityService = entityService;
        this.userDetailsService = userDetailsService;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.authenticationManager = authenticationManager;
        this.entityServiceUser = entityServiceUser;
        this.roleServiceImpl = roleServiceImpl;
        this.userService = userService;
    }

    @PostMapping("/user/add")
    public ResponseEntity<String> add(@RequestBody UserDTO userDTO) {
        User newUser = userDTO.toEntity();
        newUser.setPassword(bCryptPasswordEncoder.encode(userDTO.getPassword()));
        Role role = roleServiceImpl.findEntityById(userDTO.getRole().getId());
        newUser.setRole(role);
        entityService.saveEntity(newUser);
        return ResponseEntity.ok("User saved successfully");
    }

    @GetMapping("/user/getAll")
    public List<UserDTO> getAll() {
        return userService.getAllEntities()
                .stream()
                .map(UserDTO::fromEntity)
                .collect(Collectors.toList());
    }

    @GetMapping("/user/{id}")
    public ResponseEntity<UserDTO> getById(@PathVariable int id) {
        User user = userService.findEntityById(id);
        return ResponseEntity.ok(UserDTO.fromEntity(user));
    }

    @DeleteMapping("/user/{id}")
    public ResponseEntity<String> delete(@PathVariable int id) {
        User user = userService.findEntityById(id);
        userService.deleteEntity(user);
        return ResponseEntity.ok("User deleted successfully");
    }

    @PutMapping("/user/changePassword")
    public ResponseEntity<String> changePassword(@RequestBody Map<String, String> passwordChangeRequest) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            UserDetailsConf userDetails = (UserDetailsConf) authentication.getPrincipal();
            User currentUser = entityService.findEntityById(userDetails.getUserDTO().getId());

            String currentPassword = passwordChangeRequest.get("currentPassword");
            String newPassword = passwordChangeRequest.get("newPassword");

            if (!bCryptPasswordEncoder.matches(currentPassword, currentUser.getPassword())) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Current password is incorrect");
            }

            currentUser.setPassword(bCryptPasswordEncoder.encode(newPassword));
            entityService.updateEntity(currentUser);

            return ResponseEntity.ok("Password changed successfully");
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    @DeleteMapping("/user/deleteCurrent")
    public ResponseEntity<String> deleteCurrent() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            UserDetailsConf userDetails = (UserDetailsConf) authentication.getPrincipal();
            Integer userId = userDetails.getUserDTO().getId();
            User currentUser = entityService.findEntityById(userId);
            if (currentUser != null) {
                entityService.deleteEntity(currentUser);
                return ResponseEntity.ok("Account deleted successfully");
            }
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    @PutMapping("/user/update")
    public ResponseEntity<UserDTO> update(@RequestBody UserDTO userDTO) {
        User existingUser = entityService.findEntityById(userDTO.getId());

        if (existingUser == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

        existingUser.setUsername(userDTO.getUsername());

        if (userDTO.getPassword() != null && !userDTO.getPassword().isEmpty()) {
            existingUser.setPassword(bCryptPasswordEncoder.encode(userDTO.getPassword()));
        }

        existingUser.setRole(roleServiceImpl.findEntityById(userDTO.getRole().getId()));

        User updatedUser = entityService.updateEntity(existingUser);
        return ResponseEntity.ok(UserDTO.fromEntity(updatedUser));
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout(HttpServletRequest request, HttpServletResponse response) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null) {
            new SecurityContextLogoutHandler().logout(request, response, auth);
        }
        return ResponseEntity.ok("Logged out successfully");
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest,
                                   HttpServletRequest request) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequest.getUsername(),
                            loginRequest.getPassword()
                    )
            );

            SecurityContextHolder.getContext().setAuthentication(authentication);
            HttpSession session = request.getSession(true);
            session.setAttribute("SPRING_SECURITY_CONTEXT", SecurityContextHolder.getContext());
            session.setAttribute("user", loginRequest.getUsername());
            return ResponseEntity.ok(Map.of(
                    "status", "success",
                    "message", "Login successful",
                    "username", loginRequest.getUsername()
            ));
        } catch (AuthenticationException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", "Authentication failed"));
        }
    }

    @GetMapping("/current")
    public ResponseEntity<?> getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        Object principal = authentication.getPrincipal();
        if (principal instanceof UserDetails) {
            UserDetails userDetails = (UserDetails) principal;
            return ResponseEntity.ok(Map.of(
                    "username", userDetails.getUsername(),
                    "authorities", userDetails.getAuthorities().stream()
                            .map(GrantedAuthority::getAuthority)
                            .collect(Collectors.toList())
            ));
        }

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }


    @PostMapping("/register")
    public ResponseEntity<UserDTO> register(@RequestBody UserDTO userDTO) {
        User user = userDTO.toEntity();
        Role defaultRole = roleServiceImpl.findEntityById(2);
        user.setRole(defaultRole);
        user.setPassword(bCryptPasswordEncoder.encode(userDTO.getPassword()));

        User savedUser = entityService.saveEntity(user);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(UserDTO.fromEntity(savedUser));
    }
}