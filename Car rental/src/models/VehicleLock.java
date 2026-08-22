package models;

import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class VehicleLock {

    private final String vehicleId;
    private final String userId;
    private final LocalDateTime expiresAt;

    public VehicleLock(String vehicleId, String userId, LocalDateTime expiresAt) {
        this.vehicleId = vehicleId;
        this.userId = userId;
        this.expiresAt = expiresAt;
    }

    public boolean isExpired() {
        return LocalDateTime.now().isAfter(expiresAt);
    }
}