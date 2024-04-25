package uk.mirek.kpler.controllers;

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
    public Status getStatus() {
        return new Status(UUID.randomUUID().toString(), "OK");
    }
}
