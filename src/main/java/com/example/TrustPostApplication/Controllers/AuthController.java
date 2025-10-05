package com.example.TrustPostApplication.Controllers;

import com.example.TrustPostApplication.Models.DTOs.SignInRequestDTO;
import com.example.TrustPostApplication.Models.DTOs.SignInResponseDTO;
import com.example.TrustPostApplication.Models.DTOs.SignUpRequestDTO;
import com.example.TrustPostApplication.Models.DTOs.SignUpResponseDTO;
import com.example.TrustPostApplication.Services.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.tags.Tags;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@Tag(name = "Authentication API", description = "This is Authentication controller API to manage all the SignUp,SignIn operations of User.")
@SecurityRequirement(name = "bearerAuth") // tells Swagger this endpoint requires auth
public class AuthController {

    @Autowired
    private AuthService authService;

    //signUp
    @PostMapping("/signup")
    @Operation(summary = "User will get signUp using this API")
    public ResponseEntity<SignUpResponseDTO> signUp(@RequestBody SignUpRequestDTO request){
        return ResponseEntity.ok(authService.signUp(request));
    }

    //signIn
    @PostMapping("/signin")
    @Operation(summary = "User will get signIn using this API")
    public ResponseEntity<SignInResponseDTO> signIn(@RequestBody SignInRequestDTO request){
        return ResponseEntity.ok(authService.signIn(request));
    }
}
