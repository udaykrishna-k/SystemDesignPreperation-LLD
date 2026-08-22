package service;

import models.VehicleLock;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class VehicleLockManager {

    private final Map<String, VehicleLock> locks = new HashMap<>();

    public synchronized boolean tryLock(
            String vehicleId,
            String userId,
            Duration duration) {

        VehicleLock existingLock = locks.get(vehicleId);

        if (existingLock != null && !existingLock.isExpired()) {
            return false;
        }

        locks.put(
                vehicleId,
                new VehicleLock(
                        vehicleId,
                        userId,
                        LocalDateTime.now().plus(duration)
                )
        );

        return true;
    }

    public boolean isLockedByOther(String vehicleId, String userId){
        VehicleLock lock = locks.get(vehicleId);
        if (lock == null) {
            return false;
        }

        if (lock.isExpired()) {
            locks.remove(vehicleId);
            return false;
        }

        return !lock.getUserId().equals(userId);
    }

    public synchronized void unlock(
            String vehicleId,
            String userId) {

        VehicleLock lock = locks.get(vehicleId);

        if (lock != null && lock.getUserId().equals(userId)) {
            locks.remove(vehicleId);
        }
    }
}