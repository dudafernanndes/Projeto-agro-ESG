package br.com.vits.orc.vits_agrochain.controller;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.vits.orc.vits_agrochain.dto.LoginRequest;
import br.com.vits.orc.vits_agrochain.dto.RegisterRequest;
import br.com.vits.orc.vits_agrochain.dto.TokenResponse;
import br.com.vits.orc.vits_agrochain.service.TokenService;
import br.com.vits.orc.vits_agrochain.service.UserService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final TokenService tokenService;
    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    
    public AuthController(TokenService tokenService, UserService userService, AuthenticationManager authenticationManager) {
        this.tokenService = tokenService;
        this.userService = userService;
        this.authenticationManager = authenticationManager;
    }

    @PostMapping("/login")
    public TokenResponse login(@Valid @RequestBody LoginRequest request) {
        var authentication = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(request.email(), request.password())
        );
        return tokenService.generateToken(authentication);
    }

    @PostMapping("/register")
    public TokenResponse register(@Valid @RequestBody RegisterRequest request) {
        userService.createUser(request.userName(), request.email(), request.password(), request.cpf());
        var authentication = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(request.email(), request.password())
        );
        return tokenService.generateToken(authentication);
    }
}
