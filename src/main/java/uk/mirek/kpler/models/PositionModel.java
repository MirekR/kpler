package uk.mirek.kpler.models;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import uk.mirek.kpler.dto.Position;

@Entity
@Table(name = "positions")
public class PositionModel {
    @Id
    private String id;
    private Long mmsi;
    private Long status;
    private Long stationId;
    private Long speed;
    private Double lon;
    private Double lat;
    private Integer course;
    private Integer heading;
    private String rot;
    private Long timestamp;

    public PositionModel() {
    }

    public PositionModel(String id, Long mmsi, Long status, Long stationId, Long speed, Double lon, Double lat, Integer course, Integer heading, String rot, Long timestamp) {
        this.id = id;
        this.mmsi = mmsi;
        this.status = status;
        this.stationId = stationId;
        this.speed = speed;
        this.lon = lon;
        this.lat = lat;
        this.course = course;
        this.heading = heading;
        this.rot = rot;
        this.timestamp = timestamp;
    }

    public Position toDto() {
        return new Position(
                mmsi,
                status,
                stationId,
                speed,
                lon,
                lat,
                course,
                heading,
                rot,
                timestamp
        );
    }
}
