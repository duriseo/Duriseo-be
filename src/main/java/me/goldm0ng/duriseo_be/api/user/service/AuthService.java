package me.goldm0ng.duriseo_be.api.user.service;

import lombok.RequiredArgsConstructor;
import me.goldm0ng.duriseo_be.api.user.dto.LoginRequest;
import me.goldm0ng.duriseo_be.api.user.dto.SignupRequest;
import me.goldm0ng.duriseo_be.api.user.dto.UserProfileResponse;
import me.goldm0ng.duriseo_be.db.user.entity.User;
import me.goldm0ng.duriseo_be.db.user.repository.UserRepository;
import me.goldm0ng.duriseo_be.enums.user.Role;
import me.goldm0ng.duriseo_be.secruity.JwtTokenProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

    public Void signup(SignupRequest dto) {
        if (userRepository.existsByEmail(dto.getEmail())) {
            throw new IllegalArgumentException("이미 존재하는 이메일입니다.");
        }
        User user = User.builder()
                .email(dto.getEmail())
                .passwordHash(passwordEncoder.encode(dto.getPassword()))
                .name(dto.getName())
                .role(Role.valueOf(dto.getRole()))
                .build();

        userRepository.save(user);

        return null;
    }

    public String login(LoginRequest dto) {
        User user = userRepository.findByEmail(dto.getEmail())
                .orElseThrow(() -> new BadCredentialsException("이메일 또는 비밀번호가 올바르지 않습니다."));
        if (!passwordEncoder.matches(dto.getPassword(), user.getPasswordHash())) {
            throw new BadCredentialsException("이메일 또는 비밀번호가 올바르지 않습니다.");
        }
        return jwtTokenProvider.createToken(user.getEmail(), user.getRole().name());
    }

    @Transactional(readOnly = true)
    public UserProfileResponse getProfile(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자입니다."));
        return new UserProfileResponse(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getRole().name(),
                user.getCreatedAt(),
                user.getUpdatedAt()
        );
    }
}
