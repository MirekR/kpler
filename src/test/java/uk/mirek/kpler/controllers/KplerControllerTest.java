package uk.mirek.kpler.controllers;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import uk.mirek.kpler.dto.CorrelationResponse;
import uk.mirek.kpler.dto.Position;
import uk.mirek.kpler.dto.PositionRequest;
import uk.mirek.kpler.dto.PositionsRequest;
import uk.mirek.kpler.services.KplerService;
import uk.mirek.kpler.services.ValidateInputs;

import java.util.List;
import java.util.UUID;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class KplerControllerTest {
    @Mock
    private KplerService service;

    @Mock
    private ValidateInputs validateInputs;

    @InjectMocks
    private KplerController controller;

    @AfterEach
    void afterEach() {
        verifyNoMoreInteractions(service, validateInputs);
    }

    @Test
    void shouldIngestRequest() {
        var request = new PositionsRequest(UUID.randomUUID().toString(),
                List.of(new Position(123L, 1L, 1L, 1L, 1.1, 1.1, 1, 1, "1", 1L)
                ));

        when(service.ingest(request)).thenReturn(new CorrelationResponse(request.correlationId()));

        var response = controller.ingest(request);
        assertThat(response.correlationId(), is(request.correlationId()));
        verify(service).ingest(request);
    }

    @Test
    void shouldIngestSinglePosition() {
        var request = new PositionRequest(UUID.randomUUID().toString(), new Position(123L, 1L, 1L, 1L, 1.1, 1.1, 1, 1, "1", 1L));
        when(service.ingestSingle(request))
                .thenReturn(new CorrelationResponse(request.correlationId()));
        var response = controller.ingestSinglePosition(request);
        assertThat(response.correlationId(), is(request.correlationId()));
        verify(service).ingestSingle(request);
    }

    @Test
    void shouldRetrieveData() {
        var position = new Position(123L, 1L, 2L, 3L, 1.1, 1.2, 1, 1, "1", 987654321L);
        var data = List.of(position);
        when(service.all(anyLong(), anyDouble(), anyDouble(), anyDouble(), anyDouble(), anyLong(), anyLong())).thenReturn(data);

        var response = controller.all(position.mmsi(), position.lat(), position.lon(), position.lat(), position.lon(), position.timestamp(), position.timestamp() + 10);
        assertThat(response, is(data));
        verify(service).all(position.mmsi(), position.lat(), position.lon(), position.lat(), position.lon(), position.timestamp(), position.timestamp() + 10);
        verify(validateInputs).validatePositionParameters(position.lat(), position.lon(), position.lat(), position.lon(), position.timestamp(), position.timestamp() + 10);
    }
}