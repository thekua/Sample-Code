# logback-spike

Logback and SLF4j unit testing.

Original idea from [thekua's blog](https://www.thekua.com/atwork/2011/11/testing-logging-with-logback/), forked from [his repo](https://github.com/thekua/Sample-Code/tree/master/java/logback-spike) in order to provide it conveniently with good reuse as maven dependency.

## Usage 

Add the following maven repository to your POM.xml

```xml
<repositories>
    <repository>
        <id>jitpack.io</id>
        <url>https://jitpack.io</url>
    </repository>
</repositories>
```

Then add the actual dependency

```xml
<dependency>
    <groupId>com.github.schnatterer</groupId>
    <artifactId>logback-spike</artifactId>
    <version>1.0.0</version>
    <scope>test</scope>
</dependency>
```

From here it's really simple to use:
````java
// Given
LogbackCapturingAppender capturing = LogbackCapturingAppender.weaveInto(OurDomainWithLogger.LOG);

// when
new OurDomainWithLogger().logInfo("This should be logged{}", "!");

// then
assertThat(capturing.getCapturedLogMessages().get(0), is("This should be logged!"));
````
See [LogbackCapturingAppender's javadoc](src/main/java/com/thekua/spikes/LogbackCapturingAppender.java) and [its unit test](src/test/java/com/thekua/spikes/LogbackCapturingAppenderTest.java) for more insights.