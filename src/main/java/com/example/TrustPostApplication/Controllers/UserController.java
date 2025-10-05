package com.example.TrustPostApplication.Controllers;

import com.example.TrustPostApplication.Models.DTOs.PromoteUserRequest;
import com.example.TrustPostApplication.Models.DTOs.UserRequestDTO;
import com.example.TrustPostApplication.Models.DTOs.UserResponseDTO;
import com.example.TrustPostApplication.Models.Entities.User;
import com.example.TrustPostApplication.Models.Enums.Roles;
import com.example.TrustPostApplication.Repositories.UserRepository;
import com.example.TrustPostApplication.Services.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/users")
@Tag(name = "Users API", description ="This is user controller API to manage all the CRUD operations of User.")  //for swagger
@SecurityRequirement(name = "bearerAuth") // tells Swagger this endpoint requires auth
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    //create user
    @PostMapping("/create")
    @PreAuthorize("hasRole('SUPER_ADMIN')")  // Only Super Admin can hit this endpoint
    @Operation(summary = "Creates User" ,
               description = "Only Super Admin can hit this endpoint to create the user")  //gives the summary of what this endpoint is going to do
    public ResponseEntity<UserResponseDTO> createUser(@RequestBody UserRequestDTO userRequestDTO) {
        return ResponseEntity.ok(userService.createUser(userRequestDTO));
    }


    //get user by Id
    @GetMapping("/getbyId")
    @Operation(summary = "Get User by ID" ,
               description ="Returns a single user by their ID")

    public ResponseEntity<UserResponseDTO> getUserbyId(@RequestBody UserRequestDTO requestDTO) {
        return userService.getUserById(requestDTO.getId())
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }


    //get all user
    @GetMapping
    @Operation(summary = "Returns all users ")
    public ResponseEntity<List<UserResponseDTO>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }


    //update user details
    @PutMapping("/update")
    @Operation(summary = "Updates the user details by its id")
    public ResponseEntity<UserResponseDTO> updateUser(
            @RequestBody UserRequestDTO dto) {
        return ResponseEntity.ok(userService.updateUser(dto.getId(), dto));
    }


    //delete user by id
    @DeleteMapping("/delete")
    @Operation(summary = "Deletes the user details by its id")
    public ResponseEntity<String> deleteUser(@RequestBody Long id) {
        userService.deleteUser(id);
        return ResponseEntity.ok("User deleted Successfully...");
    }


    //promoted users by super admin
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    @PutMapping("/changeRole")
    @Operation(summary = "Super admin promotes the user by giving it the role")
    public ResponseEntity<?> promoteUserToModerator(@RequestBody PromoteUserRequest request) {
        Optional<User> optionalUser = userRepository.findByEmail(request.getEmail());

        if (optionalUser.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }

        User user = optionalUser.get();

        if (user.getRole() == request.getRole()) {
            return ResponseEntity.badRequest().body("User already has role: " + request.getRole());
        }

        user.setRole(request.getRole());
        userRepository.save(user);

        return ResponseEntity.ok("User role updated to " + request.getRole());
    }


}
