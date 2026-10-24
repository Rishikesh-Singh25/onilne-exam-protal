package exam_portal.exam_portal.dto;

import exam_portal.exam_portal.entity.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Returned after a successful login. The frontend stores the token
 * and attaches it as "Authorization: Bearer <token>" on every
 * subsequent request to a protected endpoint.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuthResponse {

    private String token;
    private Long userId;
    private String name;
    private String email;
    private Role role;
}