package exam_portal.exam_portal.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import exam_portal.exam_portal.dto.ApiResponse;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * Fires when an UNAUTHENTICATED request hits an endpoint that
 * requires authentication (missing/invalid/expired token).
 * Without this, Spring Security returns a blank 401 with no body.
 */
@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
                         AuthenticationException authException) throws IOException, ServletException {

        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);

        ApiResponse<Object> body = ApiResponse.error("Authentication required: missing or invalid token");
        response.getWriter().write(objectMapper.writeValueAsString(body));
    }
}