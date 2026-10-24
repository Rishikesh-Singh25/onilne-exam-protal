package exam_portal.exam_portal.service.impl;

import exam_portal.exam_portal.dto.AuthResponse;
import exam_portal.exam_portal.dto.LoginRequest;
import exam_portal.exam_portal.dto.RegisterRequest;
import exam_portal.exam_portal.entity.User;
import exam_portal.exam_portal.exception.DuplicateResourceException;
import exam_portal.exam_portal.exception.InvalidCredentialsException;
import exam_portal.exam_portal.repository.UserRepository;
import exam_portal.exam_portal.security.JwtUtil;
import exam_portal.exam_portal.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    @Override
    public AuthResponse register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new DuplicateResourceException("An account with this email already exists");
        }

        User user = User.builder()
                .name(request.getName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword())) // never store raw password
                .role(request.getRole())
                .build();

        User saved = userRepository.save(user);

        String token = jwtUtil.generateToken(saved.getEmail(), saved.getRole().name(), saved.getId());

        return AuthResponse.builder()
                .token(token)
                .userId(saved.getId())
                .name(saved.getName())
                .email(saved.getEmail())
                .role(saved.getRole())
                .build();
    }

    @Override
    public AuthResponse login(LoginRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new InvalidCredentialsException("Invalid email or password"));

        // matches() re-hashes the raw password with the same salt
        // stored in user.getPassword() and compares the result.
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new InvalidCredentialsException("Invalid email or password");
        }

        String token = jwtUtil.generateToken(user.getEmail(), user.getRole().name(), user.getId());

        return AuthResponse.builder()
                .token(token)
                .userId(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .role(user.getRole())
                .build();
    }
}