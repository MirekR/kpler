package uk.mirek.kpler.aop;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.Logger;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LoggingAspectTest {
    @Mock
    private ObjectMapper objectMapper;

    @Spy
    @InjectMocks
    private LoggingAspect loggingAspect;

    @Mock
    private Logger logger;

    @Mock
    private ProceedingJoinPoint proceedingJoinPoint;

    @Mock
    private Signature signature;

    @Test
    void shouldInputAndOutputParams() throws Throwable {
        var sig = "Cool method";
        var args = new String[]{"Cthulhu"};
        var res = "Cthulhu eats humankind";

        when(loggingAspect.getLogger()).thenReturn(logger);
        when(proceedingJoinPoint.getSignature()).thenReturn(signature);
        when(signature.toString()).thenReturn(sig);
        when(proceedingJoinPoint.getArgs()).thenReturn(args);
        when(proceedingJoinPoint.proceed()).thenReturn(res);
        when(objectMapper.writeValueAsString(args)).thenReturn("[\"Cthulhu\"]");
        when(objectMapper.writeValueAsString(res)).thenReturn("[\"Cthulhu eats humankind\"]");

        loggingAspect.logRequest(proceedingJoinPoint);

        var logCaptorFirst = ArgumentCaptor.forClass(String.class);
        var logCaptorSecond = ArgumentCaptor.forClass(String.class);
        var logCaptorThird = ArgumentCaptor.forClass(String.class);
        var logCaptorFourth = ArgumentCaptor.forClass(String.class);

        verify(logger, times(2)).info(logCaptorFirst.capture(), logCaptorSecond.capture(), logCaptorThird.capture(), logCaptorFourth.capture());

        assertThat(logCaptorFirst.getAllValues(), is(List.of("{}: {}: values {}", "{}: {}: values {}")));
        assertThat(logCaptorSecond.getAllValues(), is(List.of("--->", "<---")));
        assertThat(logCaptorThird.getAllValues(), is(List.of("Cool method", "Cool method")));
        assertThat(logCaptorFourth.getAllValues(), is(List.of("[\"Cthulhu\"]", "[\"Cthulhu eats humankind\"]")));
    }
}