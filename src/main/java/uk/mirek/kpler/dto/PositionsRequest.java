package uk.mirek.kpler.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record PositionsRequest(@NotNull String correlationId, @NotNull @Valid List<Position> positions) {
}
