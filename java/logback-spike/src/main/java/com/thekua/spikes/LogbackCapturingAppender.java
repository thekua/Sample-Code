package com.thekua.spikes;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.Appender;
import ch.qos.logback.core.AppenderBase;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class LogbackCapturingAppender extends AppenderBase<ILoggingEvent> {
    public static class Factory {
        private static final List<LogbackCapturingAppender> ALL = new ArrayList<LogbackCapturingAppender>();

        private Factory() {}

        public static LogbackCapturingAppender weaveInto(org.slf4j.Logger sl4jLogger) {
            LogbackCapturingAppender appender = new LogbackCapturingAppender(sl4jLogger);
            ALL.add(appender);
            return appender;
        }

        public static void cleanUp() {
            for (LogbackCapturingAppender appender : ALL) {
                appender.cleanUp();
            }
        }
    }

    private final Logger logger;
    private List<ILoggingEvent> capturedEvents = new LinkedList<ILoggingEvent>();

    public LogbackCapturingAppender(org.slf4j.Logger sl4jLogger) {
        this.logger = (Logger) sl4jLogger;
        connect(logger);
        detachDefaultConsoleAppender();
    }

    private void detachDefaultConsoleAppender() {
        Logger rootLogger = getRootLogger();
        Appender<ILoggingEvent> consoleAppender = rootLogger.getAppender("console");
        rootLogger.detachAppender(consoleAppender);
    }

    private Logger getRootLogger() {
        return logger.getLoggerContext().getLogger("ROOT");
    }

    private void connect(Logger logger) {
        logger.setLevel(Level.ALL);
        logger.addAppender(this);
        this.start();
    }

    public List<ILoggingEvent> getCapturedEvents() {
        return capturedEvents;
    }

    public List<String> getCapturedLogMessages() {
        List<String> capturedMessages = new LinkedList<String>();
        for (ILoggingEvent capturedEvent : capturedEvents) {
            capturedMessages.add(capturedEvent.getFormattedMessage());
        }
        return capturedMessages;
    }

    @Override
    protected void append(ILoggingEvent iLoggingEvent) {
        capturedEvents.add(iLoggingEvent);
    }

    private void cleanUp() {
        logger.detachAppender(this);

    }
}
