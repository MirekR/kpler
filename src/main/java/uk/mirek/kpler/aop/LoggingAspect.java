package uk.mirek.kpler.aop;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class LoggingAspect {
    private final Logger logger = LoggerFactory.getLogger(LoggingAspect.class);

    private final ObjectMapper objectMapper;

    public LoggingAspect(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Around("@annotation(uk.mirek.kpler.annotations.LoggableRequest)")
    public Object logRequest(ProceedingJoinPoint joinPoint) throws Throwable {
        String name = joinPoint.getSignature().toString();
        log("--->", name, joinPoint.getArgs());

        Object response = joinPoint.proceed();

        log("<---", name, response);

        return response;
    }

    private void log(String prefix, String name, Object object) {
        try {
            getLogger().info("{}: {}: values {}", prefix, name, objectMapper.writeValueAsString(object));
        } catch (Exception ex) {
            getLogger().warn("Unable to log request {}", ex.toString());
        }
    }

    protected Logger getLogger() {
        return logger;
    }
}
