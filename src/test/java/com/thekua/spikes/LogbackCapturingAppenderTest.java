package com.thekua.spikes;


import ch.qos.logback.classic.Level;
import org.junit.After;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class LogbackCapturingAppenderTest {
    @After
    public void cleanUp() {
        LogbackCapturingAppender.Factory.cleanUp();
    }

    @Test
    public void shouldCaptureAGivenLogMessage() throws Exception {
        // Given
        LogbackCapturingAppender capturing = LogbackCapturingAppender.Factory.weaveInto(OurDomainWithLogger.LOG);
        OurDomainWithLogger domainClass = new OurDomainWithLogger();

        // when
        domainClass.logInfo("This should be logged{}", "!");

        // then
        assertThat(capturing.getCapturedLogMessages().get(0), is("This should be logged!"));
    }

    @Test
    public void shouldCaptureMultipleLogMessages() throws Exception {
        // Given, when
        LogbackCapturingAppender capturing = sendMultipleFormattedMessages();

        // then
        assertThat(capturing.getCapturedLogMessages().get(0), is("This should be logged!"));
        assertThat(capturing.getCapturedLogMessages().get(1), is("This should also be logged"));
    }

    @Test
    public void shouldCaptureMultipleLogEvents() throws Exception {
        LogbackCapturingAppender capturing = sendMultipleFormattedMessages();

        // then
        assertThat(capturing.getCapturedEvents().get(0).getLevel(), is(Level.INFO));
        assertThat(capturing.getCapturedEvents().get(1).getLevel(), is(Level.WARN));
    }


    @Test
    public void shouldNotCaptureAGiveLogAfterCleanUp() throws Exception {
        // Given
        LogbackCapturingAppender capturing = LogbackCapturingAppender.Factory.weaveInto(OurDomainWithLogger.LOG);
        OurDomainWithLogger domainClass = new OurDomainWithLogger();
        domainClass.logInfo("This should be logged at info");
        LogbackCapturingAppender.Factory.cleanUp();

        // when
        domainClass.logInfo("This should not be logged");

        // then
        assertThat(capturing.getCapturedLogMessages().get(0), is("This should be logged at info"));
    }

    private LogbackCapturingAppender sendMultipleFormattedMessages() {
        // Given
        LogbackCapturingAppender capturing = LogbackCapturingAppender.Factory.weaveInto(OurDomainWithLogger.LOG);
        OurDomainWithLogger domainClass = new OurDomainWithLogger();

        // when
        domainClass.logInfo("This should be logged{}", "!");
        domainClass.logWarn("This should also be logged");
        return capturing;
    }

}
