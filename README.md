java-dogstatsd-client
==================

A statsd client library implemented in Java.  Allows for Java applications to easily communicate with statsd.

This version is forked from the upstream [java-dogstatsd-client](https://github.com/indeedeng/java-dogstatsd-client) project (which is, in turn, forked from [java-statsd-client](https://github.com/youdevise/java-statsd-client)), adding additional support for [DataDog](http://datadoghq.com/) extensions for use with [dogstatsd](http://docs.datadoghq.com/guides/dogstatsd/).

Downloads
---------
The client jar is distributed via maven central, and can be downloaded [here](http://search.maven.org/#search%7Cga%7C1%7Cg%3Acom.timgroup%20a%3Ajava-statsd-client).

```xml
<dependency>
    <groupId>com.datadog</groupId>
    <artifactId>java-dogstatsd-client</artifactId>
    <version>2.1.0</version>
</dependency>
```

Usage
-----
```java
import com.timgroup.statsd.StatsDClient;
import com.timgroup.statsd.NonBlockingStatsDClient;

public class Foo {

  private static final StatsDClient statsd = new NonBlockingStatsDClient(
    "my.prefix",                          /* prefix to any stats; may be null or empty string */
    "statsd-host",                        /* common case: localhost */
    8125,                                 /* port */
    new String[] {"tag:value"}            /* DataDog extension: Constant tags, always applied */
  );

  public static final void main(String[] args) {
    statsd.incrementCounter("foo");
    statsd.recordGaugeValue("bar", 100);
    statsd.recordGaugeValue("baz", 0.01); /* DataDog extension: support for floating-point gauges */
    statsd.recordHistogram("qux", 15)     /* DataDog extension: histograms */
    statsd.recordHistogram("qux", 15.5)   /* ...also floating-point */

    ServiceCheck sc = new ServiceCheck("my.check.name", ServiceCheck.OK);
    statsd.serviceCheck(sc); /* Datadog extension: send service check status */

    /* Compatibility note: Unlike upstream statsd, DataDog expects execution times to be a
     * floating-point value in seconds, not a millisecond value. This library
     * does the conversion from ms to fractional seconds.
     */
    statsd.recordExecutionTime("bag", 25, "cluster:foo"); /* DataDog extension: cluster tag */
  }
}
```


Change Log
----------

- 2.1.0
    - Fork from https://github.com/indeedeng/java-dogstatsd-client (2.0.7)
    - Add support for Datadog service checks
