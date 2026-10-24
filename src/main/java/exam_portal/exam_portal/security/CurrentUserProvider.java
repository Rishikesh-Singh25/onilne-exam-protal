package exam_portal.exam_portal.security;

import exam_portal.exam_portal.entity.User;
import exam_portal.exam_portal.exception.UnauthorizedAccessException;
import exam_portal.exam_portal.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

/**
 * Reads "who is making this request" out of Spring Security's
 * SecurityContext (populated earlier by JwtAuthenticationFilter)
 * and resolves it to our actual User entity. Any service that
 * needs to know the logged-in user calls getCurrentUser() instead
 * of re-implementing this lookup itself.
 */
@Component
@RequiredArgsConstructor
public class CurrentUserProvider {

    private final UserRepository userRepository;

    public User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            throw new UnauthorizedAccessException("No authenticated user found for this request");
        }

        // The filter set the principal's username to the user's email (see
        // CustomUserDetailsService), so authentication.getName() is the email.
        String email = authentication.getName();

        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UnauthorizedAccessException("Authenticated user no longer exists"));
    }
}