package com.silviomoser.mhz.services;

import lombok.extern.slf4j.Slf4j;
import net.sf.ehcache.CacheManager;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class CacheManagerService {
    private CacheManager cacheManager = CacheManager.getInstance();

    public void clearCache() {
        cacheManager.clearAll();
    }

    public void clearCache(String cacheName) {
        cacheManager.getCache(cacheName).flush();
    }
}
