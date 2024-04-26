package uk.mirek.kpler;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import uk.mirek.kpler.dto.CorrelationResponse;
import uk.mirek.kpler.dto.Position;
import uk.mirek.kpler.dto.PositionsRequest;

import java.io.IOException;
import java.io.InputStream;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.core.Is.is;

@SpringBootTest
class KplerApplicationTests {
    private final ObjectMapper mapper = new ObjectMapper();

    private final TestRestTemplate template = new TestRestTemplate();

    @Test
    void sunnyPathForBulkUpload() throws IOException {
        ClassLoader classloader = Thread.currentThread().getContextClassLoader();
        InputStream is = classloader.getResourceAsStream("data/ship_positions_reducted.json");

        //When
        var ingest = template.postForEntity("http://localhost:8080/kpler", mapper.readValue(is, PositionsRequest.class), CorrelationResponse.class);

        //Then
        assertThat(ingest.getBody(), notNullValue());
        assertThat(ingest.getBody().correlationId(), is("randomCorrelationId"));

        //And
        var fullData = template.getForEntity("http://localhost:8080/kpler", Position[].class);
        //Then
        assertThat(fullData.getBody(), notNullValue());
        assertThat(fullData.getBody().length, is(6));

        //And
        var queryMmsi = template.getForEntity("http://localhost:8080/kpler?mmsi=247039300", Position[].class);
        //Then
        assertThat(queryMmsi.getBody(), notNullValue());
        assertThat(queryMmsi.getBody().length, is(2));
    }
}
