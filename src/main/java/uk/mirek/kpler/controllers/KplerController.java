package uk.mirek.kpler.controllers;

import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;
import uk.mirek.kpler.annotations.LoggableRequest;
import uk.mirek.kpler.dto.CorrelationResponse;
import uk.mirek.kpler.dto.Position;
import uk.mirek.kpler.services.KplerService;
import uk.mirek.kpler.services.ValidateInputs;

import java.util.List;

@RestController
@RequestMapping("/kpler")
public class KplerController {
    private final KplerService kplerService;
    private final ValidateInputs validateInputs;

    public KplerController(KplerService kplerService, ValidateInputs validateInputs) {
        this.kplerService = kplerService;
        this.validateInputs = validateInputs;
    }

    @PostMapping
    @LoggableRequest
    public CorrelationResponse ingest(@Valid @RequestBody() List<Position> positions) {
        return kplerService.ingest(positions);
    }

    @GetMapping
    @LoggableRequest
    public List<Position> all(Long mmsi, Double fromLat, Double fromLon, Double toLat, Double toLon, Long fromTime, Long toTime) {
        validateInputs.validatePositionParameters(fromLat, fromLon, toLat, toLon, fromTime, toTime);
        return kplerService.all(mmsi, fromLat, fromLon, toLat, toLon, fromTime, toTime);
    }
}
