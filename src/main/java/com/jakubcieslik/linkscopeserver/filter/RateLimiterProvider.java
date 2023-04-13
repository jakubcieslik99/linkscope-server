package com.jakubcieslik.linkscopeserver.filter;

import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.LoadingCache;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
public class RateLimiterProvider {

  private final int windowTimeInSeconds = 30;
  private final int maxIpAddressRequestsPerWindow = 30;
  private final LoadingCache<String, Boolean> windowPerIpAddress;
  private final LoadingCache<String, Integer> requestsPerIpAddress;

  public RateLimiterProvider() {
    this.windowPerIpAddress = Caffeine.newBuilder().expireAfterWrite(windowTimeInSeconds, TimeUnit.SECONDS).build(ip -> false);
    this.requestsPerIpAddress = Caffeine.newBuilder().build(ip -> 0);
  }

  public boolean checkRate(String ipAddress) {
    Boolean window = windowPerIpAddress.get(ipAddress);

    if (window) {
      Integer requests = requestsPerIpAddress.get(ipAddress);

      if (requests >= maxIpAddressRequestsPerWindow) {
        return true;
      } else {
        requestsPerIpAddress.put(ipAddress, requests + 1);
        return false;
      }
    }

    windowPerIpAddress.put(ipAddress, true);
    requestsPerIpAddress.put(ipAddress, 1);
    return false;
  }
}
