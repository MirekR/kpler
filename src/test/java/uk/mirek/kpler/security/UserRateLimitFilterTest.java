package uk.mirek.kpler.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import uk.mirek.kpler.services.UserService;

import java.io.IOException;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserRateLimitFilterTest {
    @Mock
    private UserService userService;

    @Mock
    private HttpServletRequest request;

    @Mock
    private FilterChain filterChain;

    @Mock
    private HttpServletResponse response;

    @InjectMocks
    private UserRateLimitFilter filter;

    private static final String USER = "Cthulhu";

    @AfterEach
    void afterEach() {
        verifyNoMoreInteractions(userService, filterChain, request, response);
    }

    @Test
    void shouldAllowAndRecordAccess() throws ServletException, IOException {
        when(request.getHeader("user")).thenReturn(USER);
        when(userService.isRateLimited(USER)).thenReturn(false);

        filter.doFilterInternal(request, response, filterChain);

        verify(userService).isRateLimited(USER);
        verify(userService).registerRequest(USER);
        verify(filterChain).doFilter(request, response);
    }

    @Test
    void shouldNotAllowAccess() throws ServletException, IOException {
        when(request.getHeader("user")).thenReturn(USER);
        when(userService.isRateLimited(USER)).thenReturn(true);

        filter.doFilterInternal(request, response, filterChain);

        verify(userService).isRateLimited(USER);
        verify(response).setStatus(HttpStatus.TOO_MANY_REQUESTS.value());
    }
}