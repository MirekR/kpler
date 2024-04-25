package uk.mirek.kpler.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import uk.mirek.kpler.models.UserRateLimitModel;

@Repository
public interface UserRateLimitRepo extends JpaRepository<UserRateLimitModel, String> {
    @Query("select count(*) from UserRateLimitModel where uid = :uid and time > :time")
    int countRequests(@Param("uid") String uid, @Param("time") long time);
}
