package uk.mirek.kpler.dto;

import jakarta.validation.constraints.NotNull;
import uk.mirek.kpler.models.PositionModel;

public record Position(@NotNull Long mmsi, @NotNull Long status, @NotNull Long stationId, @NotNull Long speed,
                       @NotNull Double lon,
                       @NotNull Double lat, @NotNull Integer course, @NotNull Integer heading,
                       String rot, @NotNull Long timestamp) {

    public PositionModel toModel() {
        return new PositionModel(
                getPositionId(),
                mmsi(),
                status(),
                stationId(),
                speed(),
                lon(),
                lat(),
                course(),
                heading(),
                rot(),
                timestamp()
        );
    }

    private String getPositionId() {
        return mmsi() + "_" + stationId() + "_" + timestamp();
    }
}