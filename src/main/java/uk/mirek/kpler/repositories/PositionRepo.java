package uk.mirek.kpler.repositories;

import org.apache.commons.lang3.ObjectUtils;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import uk.mirek.kpler.models.PositionModel;

import java.util.List;
import java.util.Optional;

@Repository
public interface PositionRepo extends JpaRepository<PositionModel, String>, JpaSpecificationExecutor<PositionModel> {
    default List<PositionModel> findAllWithParams(Long mmsi, Double fromLat, Double fromLon, Double toLat, Double toLon, Long fromTime, Long toTime) {
        var specs = this
                .getQuerySpecifications(mmsi, fromLat, fromLon, toLat, toLon, fromTime, toTime);

        return this.findAll(specs);
    }

    default Specification<PositionModel> hasMmsi(long mmis) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("mmsi"), mmis);
    }

    default Specification<PositionModel> betweenTimes(long fromTime, long toTime) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.between(root.get("timestamp"), fromTime, toTime);
    }

    default Specification<PositionModel> betweenCoordinates(double fromLat, double toLat, double fromLon, double toLon) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.and(
                        criteriaBuilder.between(root.get("lon"), fromLon, toLon),
                        criteriaBuilder.between(root.get("lat"), fromLat, toLat)
                );
    }

    default Specification<PositionModel> getQuerySpecifications(Long mmsi, Double fromLat, Double fromLon, Double toLat, Double toLon, Long fromTime, Long toTime) {
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
