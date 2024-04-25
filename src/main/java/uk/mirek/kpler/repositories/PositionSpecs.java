package uk.mirek.kpler.repositories;

import org.apache.commons.lang3.ObjectUtils;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;
import uk.mirek.kpler.models.PositionModel;

import java.util.Optional;

@Component
public class PositionSpecs {
    public Specification<PositionModel> hasMmsi(long mmis) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("mmsi"), mmis);
    }

    public Specification<PositionModel> betweenTimes(long fromTime, long toTime) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.between(root.get("timestamp"), fromTime, toTime);
    }

    public Specification<PositionModel> betweenCoordinates(double fromLat, double toLat, double fromLon, double toLon) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.and(
                        criteriaBuilder.between(root.get("lon"), fromLon, toLon),
                        criteriaBuilder.between(root.get("lat"), fromLat, toLat)
                );
    }

    public Specification<PositionModel> getQuerySpecifications(Long mmsi, Double fromLat, Double fromLon, Double toLat, Double toLon, Long fromTime, Long toTime) {
        Specification<PositionModel> specs = Specification.where(null);

        specs = Optional.ofNullable(mmsi)
                .map(this::hasMmsi)
                .map(specs::and)
                .orElse(specs);

        if (ObjectUtils.allNotNull(fromTime, toTime)) {
            specs = specs.and(betweenTimes(fromTime, toTime));
        }

        if (ObjectUtils.allNotNull(fromLat, toLat, fromLon, toLon)) {
            specs = specs.and(betweenCoordinates(fromLat, toLat, fromLon, toLon));
        }

        return specs;
    }
}
