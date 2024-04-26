package uk.mirek.kpler.controllers;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.core.Is.is;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class StatusControllerTest {
    @InjectMocks
    private StatusController controller;

    @Test
    void shouldReturnStatus() {
        var status = controller.getStatus();

        assertThat(status.status(), is("OK"));
        assertThat(status.correlationId(), notNullValue());
    }
}