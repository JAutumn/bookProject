package com.jautumn.utils;

import org.apache.commons.lang3.time.StopWatch;

public class TimeWatch {
    private StopWatch stopWatch;
    private String timeLeftMessage;

    public TimeWatch(String timeLeftMessage) {
        this.stopWatch = new StopWatch();
        this.timeLeftMessage = timeLeftMessage;
    }

    public void start() {
        stopWatch.start();
    }

    public void stop() {
        stopWatch.stop();
    }

    public String getTimeLeft() {
        return timeLeftMessage + " " + stopWatch;
    }
}
