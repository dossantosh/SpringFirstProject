package com.dossantosh.springfirstproject.perfume.controller;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.springframework.stereotype.Component;

@Component
public class PerfumeLockManager {

    private static final long TIMEOUT_MILLIS =  10 * 60 * 1000; // 10 minutos

    private final Map<Long, LockInfo> perfumeLocks = new ConcurrentHashMap<>();

    public PerfumeLockManager() {
        // Limpieza periódica cada minuto
        Executors.newSingleThreadScheduledExecutor().scheduleAtFixedRate(
            this::removeExpiredLocks,
            1, 1, TimeUnit.MINUTES
        );
    }

    public boolean tryLock(Long perfumeId, String username) {
        LockInfo existing = perfumeLocks.get(perfumeId);
        long now = System.currentTimeMillis();

        if (existing == null || (now - existing.timestamp) > TIMEOUT_MILLIS) {
            perfumeLocks.put(perfumeId, new LockInfo(username, now));
            return true;
        }

        return existing.username.equals(username);
    }

    public boolean isLockedByAnother(Long perfumeId, String username) {
        LockInfo lock = perfumeLocks.get(perfumeId);
        if (lock == null) return false;

        long now = System.currentTimeMillis();
        if ((now - lock.timestamp) > TIMEOUT_MILLIS) {
            perfumeLocks.remove(perfumeId); // expiró
            return false;
        }

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

    public void refreshLock(Long perfumeId, String username) {
        LockInfo lock = perfumeLocks.get(perfumeId);
        if (lock != null && lock.username.equals(username)) {
            lock.timestamp = System.currentTimeMillis();
        }
    }

    private void removeExpiredLocks() {
        long now = System.currentTimeMillis();
        perfumeLocks.entrySet().removeIf(entry -> now - entry.getValue().timestamp > TIMEOUT_MILLIS);
    }

    private static class LockInfo {
        String username;
        volatile long timestamp;

        LockInfo(String username, long timestamp) {
            this.username = username;
            this.timestamp = timestamp;
        }
    }
}

