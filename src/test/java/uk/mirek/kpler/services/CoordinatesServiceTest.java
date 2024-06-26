package uk.mirek.kpler.services;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import uk.mirek.kpler.dto.Position;
import uk.mirek.kpler.dto.PositionRequest;
import uk.mirek.kpler.dto.PositionsRequest;
import uk.mirek.kpler.models.PositionModel;
import uk.mirek.kpler.repositories.PositionRepo;

import java.util.List;
import java.util.UUID;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.core.Is.is;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CoordinatesServiceTest {

    @Mock
    private PositionRepo repo;

    @InjectMocks
    private CoordinatesService service;

    @AfterEach
    void afterEach() {
        verifyNoMoreInteractions(repo);
    }

    @Test
    void shouldIngestData() {
        var request = new PositionsRequest(UUID.randomUUID().toString(), List.of(
                new Position(1234L, 1L, 1L, 1L, 1.1, 1.1, 1, 1, "1", 1L),
                new Position(123L, 1L, 1L, 1L, 1.1, 1.1, 1, 1, "1", 1L)
        ));

        var response = service.ingest(request);

        assertThat(response.correlationId(), notNullValue());
        assertThat(response.correlationId(), is(request.correlationId()));
        verify(repo, times(2)).saveAndFlush(ArgumentMatchers.any(PositionModel.class));
    }

    @Test
    void shouldIngestSingle() {
        var request = new PositionRequest(UUID.randomUUID().toString(),
                new Position(1234L, 1L, 1L, 1L, 1.1, 1.1, 1, 1, "1", 1L)
        );

        var response = service.ingestSingle(request);

        assertThat(response.correlationId(), notNullValue());
        assertThat(response.correlationId(), is(request.correlationId()));
        verify(repo).saveAndFlush(ArgumentMatchers.any(PositionModel.class));
    }

    @Test
    void shouldGetData() {
        var position = new Position(12345L, 2L, 1L, 2L, 1.1, 1.2, 1, 1, "rot", 6789L);
        var data = List.of(
                position.toModel(),
                new PositionModel("2", 123L, 1L, 1L, 1L, 1.1, 1.1, 1, 1, "1", 1L)
        );


        when(repo.findAllWithParams(position.mmsi(),
                position.lat(), position.lon(), position.lat(),
                position.lon(), position.timestamp(), position.timestamp() + 10)).thenReturn(data);

        var response = service.all(position.mmsi(),
                position.lat(), position.lon(), position.lat(),
                position.lon(), position.timestamp(), position.timestamp() + 10);

        assertThat(response, notNullValue());
        assertThat(response.size(), is(2));

        verify(repo).findAllWithParams(position.mmsi(),
                position.lat(), position.lon(), position.lat(),
                position.lon(), position.timestamp(), position.timestamp() + 10);
    }
}