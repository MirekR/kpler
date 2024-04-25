package uk.mirek.kpler.services;

import org.springframework.stereotype.Service;
import uk.mirek.kpler.dto.CorrelationResponse;
import uk.mirek.kpler.dto.Position;
import uk.mirek.kpler.dto.PositionsRequest;
import uk.mirek.kpler.models.PositionModel;
import uk.mirek.kpler.repositories.PositionRepo;
import uk.mirek.kpler.repositories.PositionSpecs;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class KplerService {
    private final PositionRepo positionRepo;
    private final PositionSpecs positionSpecs;

    public KplerService(PositionRepo positionRepo, PositionSpecs positionSpecs) {
        this.positionRepo = positionRepo;
        this.positionSpecs = positionSpecs;
    }

    public CorrelationResponse ingest(PositionsRequest request) {
        request
                .positions()
                .stream()
                .map(Position::toModel)
                .forEach(positionRepo::saveAndFlush);

        return new CorrelationResponse(request.correlationId());
    }

    public List<Position> all(Long mmsi, Double fromLat, Double fromLon, Double toLat, Double toLon, Long fromTime, Long toTime) {
        var specs = positionSpecs
                .getQuerySpecifications(mmsi, fromLat, fromLon, toLat, toLon, fromTime, toTime);

        return positionRepo.findAll(specs).stream()
                .map(PositionModel::toDto)
                .collect(Collectors.toList());
    }

}
