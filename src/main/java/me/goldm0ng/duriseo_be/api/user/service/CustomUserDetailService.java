package me.goldm0ng.duriseo_be.api.user.service;

import lombok.RequiredArgsConstructor;
import me.goldm0ng.duriseo_be.db.user.entity.User;
import me.goldm0ng.duriseo_be.db.user.repository.UserRepository;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CustomUserDetailService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("user not found: "+ email));
        return new org.springframework.security.core.userdetails.User(
                user.getEmail(), user.getPasswordHash(), List.of(new SimpleGrantedAuthority(user.getRole().name())));

    }
}
