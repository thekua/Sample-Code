package com.thekua.spikes;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class OurDomainWithLogger {

    static Logger LOG = LoggerFactory.getLogger(OurDomainWithLogger.class);


    public void logInfo(String message) {
        LOG.info(message);
    }

    public void logWarn(String message) {
        LOG.warn(message);
    }

    public void logInfo(String message, Object... args) {
        LOG.info(message, args);
    }

}
