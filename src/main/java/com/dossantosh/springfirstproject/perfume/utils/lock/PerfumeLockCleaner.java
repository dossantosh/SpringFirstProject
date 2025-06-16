package com.dossantosh.springfirstproject.perfume.utils.lock;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Iterator;
import java.util.Map;

@Component
public class PerfumeLockCleaner {

    private final PerfumeLockManager lockManager;

    public PerfumeLockCleaner(PerfumeLockManager lockManager) {
        this.lockManager = lockManager;
    }

    @Scheduled(fixedRate = (long) (2 * 60 * 1000))
    public void cleanOldLocks() {
        long now = System.currentTimeMillis();
        long expirationTime = (long) (4 * 60 * 1000L);

        Iterator<Map.Entry<Long, PerfumeLockManager.LockInfo>> it = lockManager.getPerfumeLocks().entrySet().iterator();

        while (it.hasNext()) {
            Map.Entry<Long, PerfumeLockManager.LockInfo> entry = it.next();
            long lastAccess = entry.getValue().lastAccessTime;
            long diff = now - lastAccess;

            if (diff > expirationTime) {
                it.remove();
            }
        }
    }
}