package uk.mirek.kpler.controllers;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import uk.mirek.kpler.annotations.LoggableRequest;
import uk.mirek.kpler.dto.CorrelationResponse;
import uk.mirek.kpler.dto.Position;
import uk.mirek.kpler.dto.PositionRequest;
import uk.mirek.kpler.dto.PositionsRequest;
import uk.mirek.kpler.services.CoordinatesService;
import uk.mirek.kpler.services.ValidateInputs;

import java.util.List;

@RestController
@RequestMapping("/kpler")
public class CoordinatesController {
    private final CoordinatesService coordinatesService;
    private final ValidateInputs validateInputs;

    public CoordinatesController(CoordinatesService coordinatesService, ValidateInputs validateInputs) {
        this.coordinatesService = coordinatesService;
        this.validateInputs = validateInputs;
    }

    @PostMapping
    @LoggableRequest
    @ResponseStatus(HttpStatus.ACCEPTED)
    @Operation(summary = "Bulk ingest of the ships position data")
    public CorrelationResponse ingest(@Valid @RequestBody() PositionsRequest request) {
        return coordinatesService.ingest(request);
    }

    @PostMapping("/single")
    @LoggableRequest
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Single position data ingest")
    public CorrelationResponse ingestSinglePosition(@Valid @RequestBody() PositionRequest request) {
        return coordinatesService.ingestSingle(request);
    }

    @GetMapping(produces = {"application/json", "application/xml"})
    @LoggableRequest
    @Operation(summary = "Returns available data, you can filter by mmsi, coordinates or time frame")
    public List<Position> all(@RequestParam(required = false) Long mmsi,
                              @RequestParam(required = false) Double fromLat,
                              @RequestParam(required = false) Double fromLon,
                              @RequestParam(required = false) Double toLat,
                              @RequestParam(required = false) Double toLon,
                              @RequestParam(required = false) Long fromTime,
                              @RequestParam(required = false) Long toTime) {
        validateInputs.validatePositionParameters(fromLat, fromLon, toLat, toLon, fromTime, toTime);
        return coordinatesService.all(mmsi, fromLat, fromLon, toLat, toLon, fromTime, toTime);
    }
}
