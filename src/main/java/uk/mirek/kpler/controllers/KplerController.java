package uk.mirek.kpler.controllers;

import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;
import uk.mirek.kpler.annotations.LoggableRequest;
import uk.mirek.kpler.dto.CorrelationResponse;
import uk.mirek.kpler.dto.Position;
import uk.mirek.kpler.dto.PositionsRequest;
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
    public CorrelationResponse ingest(@Valid @RequestBody() PositionsRequest request) {
        return kplerService.ingest(request);
    }

    @GetMapping(produces = {"application/json", "application/xml"})
    @LoggableRequest
    public List<Position> all(@RequestParam(required = false) Long mmsi,
                              @RequestParam(required = false) Double fromLat,
                              @RequestParam(required = false) Double fromLon,
                              @RequestParam(required = false) Double toLat,
                              @RequestParam(required = false) Double toLon,
                              @RequestParam(required = false) Long fromTime,
                              @RequestParam(required = false) Long toTime) {
        validateInputs.validatePositionParameters(fromLat, fromLon, toLat, toLon, fromTime, toTime);
        return kplerService.all(mmsi, fromLat, fromLon, toLat, toLon, fromTime, toTime);
    }
}
