package exam_portal.exam_portal.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import exam_portal.exam_portal.dto.ApiResponse;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * Fires when an AUTHENTICATED request hits an endpoint its role
 * isn't allowed to access — e.g. a student calling /api/admin/**.
 */
@Component
public class CustomAccessDeniedHandler implements AccessDeniedHandler {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response,
                       AccessDeniedException accessDeniedException) throws IOException, ServletException {

        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);

        ApiResponse<Object> body = ApiResponse.error("Access denied: you do not have permission to access this resource");
        response.getWriter().write(objectMapper.writeValueAsString(body));
    }
}