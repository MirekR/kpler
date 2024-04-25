package uk.mirek.kpler.dto;

public record ErrorResponse(String error, String correlationId) {
}
