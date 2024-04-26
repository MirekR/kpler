package uk.mirek.kpler.controllers;

import io.swagger.v3.oas.annotations.Operation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import uk.mirek.kpler.annotations.LoggableRequest;
import uk.mirek.kpler.dto.Status;

import java.util.UUID;

@RestController()
@RequestMapping("/status")
public class StatusController {
    @GetMapping
    @LoggableRequest
    @Operation(summary = "Returns status of the service and in the future status of underlying systems, if applicable")
    public Status getStatus() {
        return new Status(UUID.randomUUID().toString(), "OK");
    }
}
