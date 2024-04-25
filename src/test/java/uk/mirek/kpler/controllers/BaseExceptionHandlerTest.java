package uk.mirek.kpler.controllers;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.web.method.annotation.HandlerMethodValidationException;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BaseExceptionHandlerTest {
    @InjectMocks
    BaseExceptionHandler handler;

    @Test
    void shouldHandleGenericException() {
        var message = "Encountered internal issue, please try again";

        var response = handler.handleException(new Exception());

        assertThat(response.getStatusCode(), is(HttpStatus.INTERNAL_SERVER_ERROR));
        assertThat(response.getBody().error(), is(message));
    }

    @Test
    void shouldHandleValidationException() {
        var message = "Cthulhu crawled out and eat people";

        var exception = mock(HandlerMethodValidationException.class);
        when(exception.getMessage()).thenReturn(message);

        var response = handler.handleValidationException(exception);

        assertThat(response.getStatusCode(), is(HttpStatus.BAD_REQUEST));
        assertThat(response.getBody().error(), is(message));
    }
}