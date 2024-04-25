package uk.mirek.kpler.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import uk.mirek.kpler.models.PositionModel;

@Repository
public interface PositionRepo extends JpaRepository<PositionModel, String>, JpaSpecificationExecutor<PositionModel> {
}
