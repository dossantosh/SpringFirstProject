package com.dossantosh.springfirstproject.perfume.utils.lock;

import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import com.dossantosh.springfirstproject.common.global.UserLoggedOutEvent;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class PerfumeLockManager {

    private final Map<Long, LockInfo> perfumeLocks = new ConcurrentHashMap<>();

    @EventListener
    public void handleUserLoggedOut(UserLoggedOutEvent event) {
        releaseAllLocksByUser(event.getUsername());
    }

    public boolean tryLock(Long perfumeId, String username) {
        LockInfo existing = perfumeLocks.get(perfumeId);

        if (existing == null) {
            perfumeLocks.put(perfumeId, new LockInfo(username));
            return true;
        }

        if (existing.username.equals(username)) {
            existing.refresh();
            return false;
        }

        return false;
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
        return (lock != null) ? lock.username : null;
    }

    public void releaseAllLocksByUser(String username) {
        perfumeLocks.entrySet().removeIf(entry -> entry.getValue().username.equals(username));
    }

    public void refreshLock(Long perfumeId, String username) {
        LockInfo lock = perfumeLocks.get(perfumeId);
        if (lock != null) {
            if (lock.username.equals(username)) {
                lock.refresh();
            }
        }
    }

    public Map<Long, LockInfo> getPerfumeLocks() {
        return perfumeLocks;
    }

    // Clase interna para manejar info del lock
    public static class LockInfo {
        String username;
        long lastAccessTime;

        LockInfo(String username) {
            this.username = username;
            this.lastAccessTime = System.currentTimeMillis();
        }

        void refresh() {
            this.lastAccessTime = System.currentTimeMillis();
        }
    }
}
