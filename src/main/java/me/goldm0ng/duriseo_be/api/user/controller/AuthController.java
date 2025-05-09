package me.goldm0ng.duriseo_be.api.user.controller;

import lombok.RequiredArgsConstructor;
import me.goldm0ng.duriseo_be.api.user.dto.LoginRequest;
import me.goldm0ng.duriseo_be.api.user.dto.SignupRequest;
import me.goldm0ng.duriseo_be.api.user.dto.UserProfileResponse;
import me.goldm0ng.duriseo_be.api.user.service.AuthService;
import me.goldm0ng.duriseo_be.common.response.APISuccessResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;
import java.util.Map;

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
    public ResponseEntity<Map<String,String>> login(@RequestBody @Validated LoginRequest dto) {
        String jwt = authService.login(dto);
        ResponseCookie cookie = ResponseCookie.from("token", jwt)
                .httpOnly(true)
                .secure(true) // HTTPS에서만 동작 (로컬 개발 시 false 가능)
                .path("/")
                .maxAge(Duration.ofDays(7))
                .sameSite("Lax")
                .build();

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .body(Map.of("message", "Login successful"));
    }

    @GetMapping("/profile")
    public ResponseEntity<APISuccessResponse<UserProfileResponse>> getProfile(@AuthenticationPrincipal UserDetails principal) {
        return APISuccessResponse.of(HttpStatus.OK, authService.getProfile(principal.getUsername()));
    }
}
