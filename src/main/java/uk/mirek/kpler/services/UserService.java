package uk.mirek.kpler.services;

import org.springframework.stereotype.Service;
import uk.mirek.kpler.models.UserRateLimitModel;
import uk.mirek.kpler.repositories.UserRateLimitRepo;

import java.util.Optional;

@Service
public class UserService {
    private final UserRateLimitRepo userRateLimitRepo;

    public UserService(UserRateLimitRepo userRateLimitRepo) {
        this.userRateLimitRepo = userRateLimitRepo;
    }

    public boolean isRateLimited(String uid) {
        var inPastTime = System.currentTimeMillis() - (60 * 1000); // The rate limit would be stored as part of registered user
        var maxRequests = 10; // The max requests would be stored as part of registered user
        return userRateLimitRepo.countRequests(getUidOrFreeUser(uid), inPastTime) >= maxRequests;
    }

    public void registerRequest(String uid) {
        userRateLimitRepo.saveAndFlush(new UserRateLimitModel(getUidOrFreeUser(uid)));
    }

    private String getUidOrFreeUser(String uid) {
        return Optional.ofNullable(uid).orElse("FREE_ACCESS");
    }
}
