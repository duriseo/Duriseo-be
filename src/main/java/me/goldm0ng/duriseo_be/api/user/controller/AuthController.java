package me.goldm0ng.duriseo_be.api.user.controller;

import lombok.RequiredArgsConstructor;
import me.goldm0ng.duriseo_be.api.user.dto.AuthResponse;
import me.goldm0ng.duriseo_be.api.user.dto.LoginRequest;
import me.goldm0ng.duriseo_be.api.user.dto.SignupRequest;
import me.goldm0ng.duriseo_be.api.user.dto.UserProfileResponse;
import me.goldm0ng.duriseo_be.api.user.service.AuthService;
import me.goldm0ng.duriseo_be.common.response.APISuccessResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("${api.prefix}/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/signup")
    public ResponseEntity<APISuccessResponse<Void>> signup(@RequestBody @Validated SignupRequest dto) {
        return APISuccessResponse.of(HttpStatus.CREATED,authService.signup(dto));
    }

    @PostMapping("/login")
    public ResponseEntity<APISuccessResponse<AuthResponse>> login(@RequestBody @Validated LoginRequest dto) {
        return APISuccessResponse.of(HttpStatus.OK, new AuthResponse(authService.login(dto)));
    }

    @GetMapping("/profile")
    public ResponseEntity<APISuccessResponse<UserProfileResponse>> getProfile(@AuthenticationPrincipal UserDetails principal) {
        return APISuccessResponse.of(HttpStatus.OK, authService.getProfile(principal.getUsername()));
    }
}
