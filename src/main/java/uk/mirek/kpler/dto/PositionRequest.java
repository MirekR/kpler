package uk.mirek.kpler.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

public record PositionRequest(@NotNull String correlationId, @NotNull @Valid Position position) {
}
