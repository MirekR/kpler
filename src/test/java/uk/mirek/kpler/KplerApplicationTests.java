package uk.mirek.kpler;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import uk.mirek.kpler.dto.CorrelationResponse;
import uk.mirek.kpler.dto.ErrorResponse;
import uk.mirek.kpler.dto.Position;
import uk.mirek.kpler.dto.PositionsRequest;

import java.io.IOException;
import java.net.URISyntaxException;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.core.Is.is;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class KplerApplicationTests {
    @LocalServerPort
    private int serverPort;
    private String baseUrl;

    private final ObjectMapper mapper = new ObjectMapper();

    private final TestRestTemplate template = new TestRestTemplate();

    @BeforeEach
    void setup() {
        baseUrl = "http://localhost:" + serverPort;
    }

    @Test
    void sunnyPathForBulkUpload() throws IOException {
        var classloader = Thread.currentThread().getContextClassLoader();
        var is = classloader.getResourceAsStream("data/ship_positions_reducted.json");

        //When
        var ingest = template.postForEntity(baseUrl + "/kpler", mapper.readValue(is, PositionsRequest.class), CorrelationResponse.class);

        //Then
        assertThat(ingest.getStatusCode(), is(HttpStatus.ACCEPTED));
        assertThat(ingest.getBody(), notNullValue());
        assertThat(ingest.getBody().correlationId(), is("randomCorrelationId"));

        //And
        var fullData = template.getForEntity(baseUrl + "/kpler", Position[].class);
        //Then
        assertThat(fullData.getStatusCode(), is(HttpStatus.OK));
        assertThat(fullData.getBody(), notNullValue());
        assertThat(fullData.getBody().length, is(4));

        //And
        var queryMmsi = template.getForEntity(baseUrl + "/kpler?mmsi=247039300", Position[].class);
        //Then
        assertThat(queryMmsi.getStatusCode(), is(HttpStatus.OK));
        assertThat(queryMmsi.getBody(), notNullValue());
        assertThat(queryMmsi.getBody().length, is(2));
    }

    @Test
    void validationFailedPath() throws IOException, URISyntaxException {
        var classloader = Thread.currentThread().getContextClassLoader();
        var is = classloader.getResourceAsStream("data/invalid_ship_positions.json");
        
        //When
        var ingest = template.postForEntity(baseUrl + "/kpler", mapper.readValue(is, PositionsRequest.class), ErrorResponse.class);

        //Then
        assertThat(ingest.getStatusCode(), is(HttpStatus.BAD_REQUEST));
        assertThat(ingest.getBody(), notNullValue());
        assertThat(ingest.getBody().error(), notNullValue());
    }
}
