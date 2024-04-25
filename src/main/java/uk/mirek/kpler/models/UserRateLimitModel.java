package uk.mirek.kpler.models;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.util.UUID;

@Entity
@Table(name = "user_rate_limit")
public class UserRateLimitModel {
    @Id
    private String id = UUID.randomUUID().toString();
    private Long time = System.currentTimeMillis();
    private String uid;

    public UserRateLimitModel() {
    }

    public UserRateLimitModel(String uid) {
        this.uid = uid;
    }

    public String getUid() {
        return uid;
    }
}
