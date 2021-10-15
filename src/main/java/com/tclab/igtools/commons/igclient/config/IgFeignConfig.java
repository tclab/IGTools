package com.tclab.igtools.commons.igclient.config;

import feign.Retryer;
import java.util.concurrent.TimeUnit;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;

public class IgFeignConfig {

    @Value("${api.graph.retry.enable}")
    private boolean isRetryerEnable;

    @Value("${api.graph.retry.maxAttempts}")
    private int maxAttempts;

    @Value("${api.graph.retry.interval}")
    private long interval;

    @Bean
    public Retryer retryer() {
        if(isRetryerEnable)
            return new Retryer.Default(100L, TimeUnit.SECONDS.toMillis(interval), maxAttempts);
        else
            return Retryer.NEVER_RETRY;
    }

}
