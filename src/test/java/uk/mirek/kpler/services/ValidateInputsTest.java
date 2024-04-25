package uk.mirek.kpler.services;

import jakarta.validation.ValidationException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
class ValidateInputsTest {
    @InjectMocks
    private ValidateInputs validateInputs;

    @Test
    void shouldThrowOnIncompleteTime() {
        assertThrows(ValidationException.class, () -> {
            validateInputs.validatePositionParameters(null, null, null, null, 123L, null);
        });
    }

    @Test
    void shouldThrowOnIncompleteCoordinates() {
        assertThrows(ValidationException.class, () -> {
            validateInputs.validatePositionParameters(1234.1, null, null, null, null, null);
        });
    }

    @Test
    void shouldNotThrowOnAcceptableParams() {
        assertDoesNotThrow(() -> {
            validateInputs.validatePositionParameters(null, null, null, null, null, null);
        });
    }
}