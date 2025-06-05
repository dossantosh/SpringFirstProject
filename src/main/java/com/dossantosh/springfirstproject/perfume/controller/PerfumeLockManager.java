package com.dossantosh.springfirstproject.perfume.controller;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.stereotype.Component;

@Component
public class PerfumeLockManager {

    private final Map<Long, LockInfo> perfumeLocks = new ConcurrentHashMap<>();


    public boolean tryLock(Long perfumeId, String username) {
        LockInfo existing = perfumeLocks.get(perfumeId);

        if (existing == null) {
            perfumeLocks.put(perfumeId, new LockInfo(username));
            return true;
        }

        return existing.username.equals(username);
    }

    public boolean isLockedByAnother(Long perfumeId, String username) {
        LockInfo lock = perfumeLocks.get(perfumeId);
        if (lock == null)
            return false;

        return !lock.username.equals(username);
    }

    public void releaseLock(Long perfumeId, String username) {
        LockInfo lock = perfumeLocks.get(perfumeId);
        if (lock != null && lock.username.equals(username)) {
            perfumeLocks.remove(perfumeId);
        }
    }

    public String lockedBy(Long perfumeId) {
        LockInfo lock = perfumeLocks.get(perfumeId);
        return lock != null ? lock.username : null;
    }

    public void releaseAllLocksByUser(String username) {
        perfumeLocks.entrySet().removeIf(entry -> entry.getValue().username.equals(username));
    }

    private static class LockInfo {
        String username;

        LockInfo(String username) {
            this.username = username;
        }
    }
}
