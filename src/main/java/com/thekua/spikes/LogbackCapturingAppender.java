package com.thekua.spikes;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.Appender;
import ch.qos.logback.core.AppenderBase;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Logback appender that simply stores all logged messages of an {@link org.slf4j.Logger} in memory and provides methods
 * to retrieve them, so they can be used in asserts of unit tests.
 * <br/>
 * Add a logger like so  <br/>
 * {@code LogbackCapturingAppender capturing = LogbackCapturingAppender.weaveInto(OurDomainWithLogger.LOG); }
 * <br/><br/>
 * Retrieve any log messages using any of the following methods:
 * <ul>
 *     <li>{@link #getCapturedEvents()}</li>
 *     <li>{@link #getCapturedLogMessages()}</li>
 * </ul>
 * <br/>
 * If necessary remove appender instance or all {@link LogbackCapturingAppender}s using
 *
 * <ul>
 *     <li>{@link #cleanUp()}</li>
 *     <li>{@link #cleanUpAll()}</li>
 * </ul>
 */
public class LogbackCapturingAppender extends AppenderBase<ILoggingEvent> {
    private static final List<LogbackCapturingAppender> ALL = new ArrayList<LogbackCapturingAppender>();

    private final Logger logger;

    private List<ILoggingEvent> capturedEvents = new LinkedList<ILoggingEvent>();

    public static LogbackCapturingAppender weaveInto(org.slf4j.Logger sl4jLogger) {
        LogbackCapturingAppender appender = new LogbackCapturingAppender(sl4jLogger);
        ALL.add(appender);
        return appender;
    }

    public static void cleanUpAll() {
        for (LogbackCapturingAppender appender : ALL) {
            appender.cleanUp();
        }
    }

    public void cleanUp() {
        logger.detachAppender(this);
    }

    /**
     * @return whole event, including log level, unformatted message, etc.
     */
    public List<ILoggingEvent> getCapturedEvents() {
        return capturedEvents;
    }

    /**
     * @return formatted message strings
     */
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

    private LogbackCapturingAppender(org.slf4j.Logger sl4jLogger) {
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
}
