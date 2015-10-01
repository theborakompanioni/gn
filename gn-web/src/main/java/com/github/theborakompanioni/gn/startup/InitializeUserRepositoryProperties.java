package com.github.theborakompanioni.gn.startup;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("vishy.initialize.user")
public class InitializeUserRepositoryProperties {

    private boolean enabled;
    private long delayInSeconds;

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public long getDelayInSeconds() {
        return delayInSeconds;
    }

    public void setDelayInSeconds(long delayInSeconds) {
        this.delayInSeconds = delayInSeconds;
    }
}