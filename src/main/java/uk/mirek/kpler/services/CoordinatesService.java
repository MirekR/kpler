package uk.mirek.kpler.services;

import org.springframework.stereotype.Service;
import uk.mirek.kpler.dto.CorrelationResponse;
import uk.mirek.kpler.dto.Position;
import uk.mirek.kpler.dto.PositionRequest;
import uk.mirek.kpler.dto.PositionsRequest;
import uk.mirek.kpler.models.PositionModel;
import uk.mirek.kpler.repositories.PositionRepo;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CoordinatesService {
    private final PositionRepo positionRepo;

    public CoordinatesService(PositionRepo positionRepo) {
        this.positionRepo = positionRepo;
    }

    public CorrelationResponse ingestSingle(PositionRequest request) {
        insertSinglePosition(request.position());
        return new CorrelationResponse(request.correlationId());
    }

    public CorrelationResponse ingest(PositionsRequest request) {
        request
                .positions()
                .stream()
                .forEach(this::insertSinglePosition);

        return new CorrelationResponse(request.correlationId());
    }

    private void insertSinglePosition(Position position) {
        Optional
                .ofNullable(position)
                .map(Position::toModel)
                .ifPresent(positionRepo::saveAndFlush);
    }

    public List<Position> all(Long mmsi, Double fromLat, Double fromLon, Double toLat, Double toLon, Long fromTime, Long toTime) {
        return positionRepo.findAllWithParams(mmsi, fromLat, fromLon, toLat, toLon, fromTime, toTime).stream()
                .map(PositionModel::toDto)
                .collect(Collectors.toList());
    }

}
