package uk.mirek.kpler.dto;

import jakarta.validation.constraints.NotNull;

import java.util.List;

public record PositionsRequest(@NotNull String correlationId, @NotNull List<Position> positions) {
}
