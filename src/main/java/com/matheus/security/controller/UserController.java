package com.matheus.security.controller;

import com.matheus.security.controller.dto.CreateUserDto;
import com.matheus.security.entities.Role;
import com.matheus.security.entities.User;
import com.matheus.security.repository.RoleRepository;
import com.matheus.security.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.Set;

@RestController
public class UserController {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final BCryptPasswordEncoder enconder;

    public UserController(UserRepository userRepository, RoleRepository roleRepository, BCryptPasswordEncoder enconder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.enconder = enconder;
    }

    @Transactional
    @PostMapping("/users")
        public ResponseEntity<Void> newUser(@RequestBody CreateUserDto dto) {

        var basicRole = roleRepository.findByName(Role.Values.BASIC.name());

        var userFromDb = userRepository.findByUsername(dto.username());
        if (userFromDb.isPresent()) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY);
        }

        var user = new User();
        user.setUsername(dto.username());
        user.setPassword(enconder.encode(dto.password()));
        user.setRoles(Set.of(basicRole));

        userRepository.save(user);

        return ResponseEntity.ok().build();

        };

}
