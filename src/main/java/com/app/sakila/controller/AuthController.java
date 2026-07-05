package com.app.sakila.controller;

import com.app.sakila.dto.AuthRequest;
import com.app.sakila.dto.AuthResponse;
import com.app.sakila.dto.RegisterRequest;
import com.app.sakila.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@Tag(name = "Autenticación", description = "Endpoints públicos para registrar nuevos usuarios e iniciar sesión en el sistema.")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    @Operation(summary = "Registrar un nuevo usuario",
               description = "Crea una cuenta de usuario en el sistema con nombre de usuario, correo electrónico y contraseña. " +
                             "El usuario registrado obtiene automáticamente el rol 'USER' y recibe un token JWT válido para autenticarse.")
    public ResponseEntity<AuthResponse> register(@Valid @RequestBody RegisterRequest request) {
        return ResponseEntity.ok(authService.register(request));
    }

    @PostMapping("/login")
    @Operation(summary = "Iniciar sesión",
               description = "Autentica un usuario existente con su nombre de usuario y contraseña. " +
                             "Devuelve un token JWT que debe enviarse en el encabezado 'Authorization' como 'Bearer <token>' " +
                             "para acceder a los endpoints protegidos.")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody AuthRequest request) {
        return ResponseEntity.ok(authService.login(request));
    }
}
