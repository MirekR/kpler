package uk.mirek.kpler.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import uk.mirek.kpler.services.UserService;

import java.io.IOException;

// This should be integrated with some OAuth provide,
// like Firebase, for example: https://medium.com/vectoscalar/authenticating-user-via-firebase-authorizer-using-spring-boot-ff0a86c659d1
// based on this we would get user and
@Component
public class UserRateLimitFilter extends OncePerRequestFilter {
    private final UserService userService;

    public UserRateLimitFilter(UserService userService) {
        this.userService = userService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {
        String uid = request.getHeader("user");

        if (userService.isRateLimited(uid)) {
            response.setStatus(HttpStatus.TOO_MANY_REQUESTS.value());
        } else {
            userService.registerRequest(uid);
            chain.doFilter(request, response);
        }
    }
}
