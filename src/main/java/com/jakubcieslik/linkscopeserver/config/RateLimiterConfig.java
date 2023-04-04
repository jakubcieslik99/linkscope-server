package com.jakubcieslik.linkscopeserver.config;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.Refill;
import io.github.bucket4j.local.SynchronizationStrategy;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;

@Configuration
public class RateLimiterConfig {

  @Bean
  public Bandwidth limitBandwidth() {
    int requestsPerBucket = 30;
    int secondsPerBucket = 30;

    return Bandwidth.classic(requestsPerBucket, Refill.intervally(requestsPerBucket, Duration.ofSeconds(secondsPerBucket)));
  }

  @Bean
  public Bucket bucket(Bandwidth limitBandwidth) {
    return Bucket.builder().addLimit(limitBandwidth).withNanosecondPrecision().withSynchronizationStrategy(SynchronizationStrategy.SYNCHRONIZED).build();
    //return Bucket.builder().addLimit(limitBandwidth).build();
  }
}
