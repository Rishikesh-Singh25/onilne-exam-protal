package exam_portal.exam_portal.service;

import exam_portal.exam_portal.dto.AuthResponse;
import exam_portal.exam_portal.dto.LoginRequest;
import exam_portal.exam_portal.dto.RegisterRequest;

public interface AuthService {
    AuthResponse register(RegisterRequest request);
    AuthResponse login(LoginRequest request);
}