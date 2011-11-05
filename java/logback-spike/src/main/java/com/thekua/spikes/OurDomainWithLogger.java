package com.thekua.spikes;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class OurDomainWithLogger {

    static Logger LOG = LoggerFactory.getLogger(OurDomainWithLogger.class);


    public void logThis(String message) {
        LOG.info(message);
    }

}
