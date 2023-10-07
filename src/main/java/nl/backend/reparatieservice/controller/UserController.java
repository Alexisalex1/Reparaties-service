package nl.backend.reparatieservice.controller;

import jakarta.validation.Valid;
import nl.backend.reparatieservice.dto.UserDto;
import nl.backend.reparatieservice.model.Role;
import nl.backend.reparatieservice.repository.RoleRepository;
import nl.backend.reparatieservice.model.User;
import nl.backend.reparatieservice.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
public class UserController {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    public UserController(UserRepository userRepos, RoleRepository roleRepos, PasswordEncoder encoder) {
        this.userRepository = userRepos;
        this.roleRepository = roleRepos;
        this.passwordEncoder = encoder;
    }
    @PostMapping("/users")
    public ResponseEntity<String> createUser(@RequestBody @Valid UserDto userDto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            List<FieldError> fieldErrors = bindingResult.getFieldErrors();
            String errorMessage = "Validation error(s): ";
            for (FieldError fieldError : fieldErrors) {
                errorMessage += fieldError.getDefaultMessage() + "; ";
            }
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorMessage);
        }

        try {

            Optional<User> existingUser = userRepository.findById(userDto.username);
            if (existingUser.isPresent()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Username already exists.");
            }

            User newUser = new User();
            newUser.setUsername(userDto.username);
            newUser.setPassword(passwordEncoder.encode(userDto.password));

            List<Role> userRoles = new ArrayList<>();
            for (String rolename : userDto.roles) {
                Optional<Role> or = roleRepository.findById("ROLE_" + rolename);
                userRoles.add(or.get());
            }
            newUser.setRoles(userRoles);

            userRepository.save(newUser);

            return ResponseEntity.status(HttpStatus.CREATED).body("User created successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error creating user: " + e.getMessage());
        }
    }

    @GetMapping("/users")
    public ResponseEntity<List<UserDto>> getAllUsers() {
        try {
            List<UserDto> userDtos = new ArrayList<>();
            for (User user : userRepository.findAll()) {
                UserDto userDto = new UserDto();
                userDto.username = user.getUsername();
                List<String> userRoleNamesArrayList = new ArrayList<>();
                for (Role r : user.getRoles()) {
                    userRoleNamesArrayList.add(r.getRolename());
                }
                String[] userRoleNames = userRoleNamesArrayList.toArray(new String[0]);
                userDto.roles = userRoleNames;
                userDtos.add(userDto);
            }
            return ResponseEntity.ok(userDtos);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
}

