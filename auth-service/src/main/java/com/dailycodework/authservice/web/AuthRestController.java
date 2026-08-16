package com.dailycodework.authservice.web;

import com.dailycodework.authservice.dto.AuthResponse;
import com.dailycodework.authservice.dto.LoginRequest;
import com.dailycodework.authservice.dto.RegisterRequest;
import com.dailycodework.authservice.dto.UserResponse;
import com.dailycodework.authservice.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthRestController {

    private final AuthService authService;
    @PutMapping("/internal/users/{id}/role")
    public ResponseEntity<UserResponse> updateUserRoleInternal(
            @PathVariable Long id,
            @RequestParam String role,
            @RequestHeader("X-Internal-Secret") String secret
    ) {
        if (!"smartlog-internal-secret".equals(secret)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        return ResponseEntity.ok(authService.updateUserRole(id, role));
    }

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@RequestBody RegisterRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(authService.register(request));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest request) {
        return ResponseEntity.ok(authService.login(request));
    }

    @GetMapping("/me")
    public ResponseEntity<UserResponse> me(@RequestHeader("X-User-Id") Long userId) {
        return ResponseEntity.ok(authService.getUserById(userId));
    }
    @GetMapping("/users/by-email")
    public ResponseEntity<UserResponse> getUserByEmail(@RequestParam String email) {
        return ResponseEntity.ok(authService.getUserByEmail(email));
    }
}