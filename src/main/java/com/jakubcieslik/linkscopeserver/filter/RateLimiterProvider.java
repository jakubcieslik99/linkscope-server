package com.jakubcieslik.linkscopeserver.filter;

import com.jakubcieslik.linkscopeserver.common.AuthEntryPoint;
import com.jakubcieslik.linkscopeserver.common.Constants;
import com.jakubcieslik.linkscopeserver.error.AppError;
import io.github.bucket4j.Bucket;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

@Component
public class RateLimiterProvider {

  private final Bucket rateBucket;
  private final AuthEntryPoint authEntryPoint;

  public RateLimiterProvider(Bucket rateBucket, AuthEntryPoint authEntryPoint) {
    this.rateBucket = rateBucket;
    this.authEntryPoint = authEntryPoint;
  }

  public void checkRate() {
    if (!rateBucket.tryConsume(1)) {
      authEntryPoint.setHttpStatusError(HttpStatus.TOO_MANY_REQUESTS, Constants.TOO_MANY_REQUESTS);
      throw new AppError(Constants.TOO_MANY_REQUESTS, HttpStatus.TOO_MANY_REQUESTS);
    }
  }
}
