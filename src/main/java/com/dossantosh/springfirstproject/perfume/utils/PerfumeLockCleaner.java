package com.dossantosh.springfirstproject.perfume.utils;

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

    @Scheduled(fixedRate = 60000)
    public void cleanOldLocks() {
        long now = System.currentTimeMillis();
        long expirationTime = (long) (5 * 60 * 1000L);

        Iterator<Map.Entry<Long, PerfumeLockManager.LockInfo>> it = lockManager.getPerfumeLocks().entrySet().iterator();

        while (it.hasNext()) {
            Map.Entry<Long, PerfumeLockManager.LockInfo> entry = it.next();
            long lastAccess = entry.getValue().lastAccessTime;
            long diff = now - lastAccess;

            System.out.println("Checking perfume ID: " + entry.getKey()
                    + " | lastAccess: " + lastAccess
                    + " | now: " + now
                    + " | diff: " + diff + "ms");

            if (diff > expirationTime) {
                System.out.println("Removing expired lock for perfume ID: " + entry.getKey());
                it.remove();
            }
        }
    }
}