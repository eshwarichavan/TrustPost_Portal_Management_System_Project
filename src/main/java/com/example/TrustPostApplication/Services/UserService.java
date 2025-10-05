package com.example.TrustPostApplication.Services;

import com.example.TrustPostApplication.Models.DTOs.UserRequestDTO;
import com.example.TrustPostApplication.Models.DTOs.UserResponseDTO;
import com.example.TrustPostApplication.Models.Entities.User;
import com.example.TrustPostApplication.Models.Enums.Roles;
import com.example.TrustPostApplication.Repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;


    //create a new user
    public UserResponseDTO createUser(UserRequestDTO userRequestDTO){
        User user=new User();
        user.setEmail(userRequestDTO.getEmail());
        user.setPassword(userRequestDTO.getPassword());
        user.setRole(Roles.valueOf(userRequestDTO.getRole().toUpperCase()));
        user.setCreatedAt(LocalDateTime.now());

        User saved=userRepository.save(user);
        return mapToResponseDTO(saved);
    }

    //mapToResponseDTO
    private UserResponseDTO mapToResponseDTO(User user) {
        UserResponseDTO dto=new UserResponseDTO();
        dto.setId(user.getUserId());
        dto.setEmail(user.getEmail());
        dto.setRole(user.getRole().toString());
        return dto;
    }

    //get user by ID
    public Optional<UserResponseDTO> getUserById(Long id){
        return userRepository.findById(id)
                .map(this::mapToResponseDTO);
    }

    //get all users
    public List<UserResponseDTO> getAllUsers(){
        return userRepository.findAll().stream()
                .map(this::mapToResponseDTO)
                .toList();
    }

    //update users details
    public UserResponseDTO updateUser(Long id, UserRequestDTO dto){
        User user=userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User Not Found..."));

        user.setEmail(dto.getEmail());
        user.setPassword(dto.getPassword());  //Bcrypting it later in security config
        user.setRole(Roles.valueOf(dto.getRole().toUpperCase()));

        User updated=userRepository.save(user);
        return mapToResponseDTO(updated);
    }

    //delete user
    public void deleteUser(Long id){

        //checking for errors first if exists or not
        if (!userRepository.existsById(id)) {
            throw new RuntimeException("User not found");
        }
        userRepository.deleteById(id);
    }

}
