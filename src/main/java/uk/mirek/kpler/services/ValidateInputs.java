package uk.mirek.kpler.services;

import jakarta.validation.ValidationException;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.stereotype.Service;

@Service
public class ValidateInputs {
    public void validatePositionParameters(Double fromLat, Double fromLon, Double toLat, Double toLon, Long fromTime, Long toTime) {
        if (ObjectUtils.anyNotNull(fromLat, fromLon, toLat, toLon) && !ObjectUtils.allNotNull(fromLat, fromLon, toLat, toLon)) {
            throw new ValidationException("To filter by coordinates, all for has to be provided");
        }
        
        if (ObjectUtils.anyNotNull(fromTime, toTime) && !ObjectUtils.allNotNull(fromTime, toTime)) {
            throw new ValidationException("To filter by time from and to has to be provided");
        }
    }
}
