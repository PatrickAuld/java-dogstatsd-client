package com.timgroup.statsd;

import java.net.SocketException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.Locale;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;

public class NonBlockingStatsDClientTest {

    private static final int STATSD_SERVER_PORT = 17254;
    private final NonBlockingStatsDClient client = new NonBlockingStatsDClient("my.prefix", "localhost", STATSD_SERVER_PORT);
    private DummyStatsDServer server;

    @Before
    public void start() throws SocketException {
        server = new DummyStatsDServer(STATSD_SERVER_PORT);
    }

    @After
    public void stop() throws Exception {
        client.stop();
        server.close();
    }

    @Test(timeout=5000L) public void
    sends_counter_value_to_statsd() throws Exception {


        client.count("mycount", 24);
        server.waitForMessage();

        assertThat(server.messagesReceived(), contains("my.prefix.mycount:24|c"));
    }

    @Test(timeout=5000L) public void
    sends_counter_value_to_statsd_with_null_tags() throws Exception {


        client.count("mycount", 24, (java.lang.String[]) null);
        server.waitForMessage();

        assertThat(server.messagesReceived(), contains("my.prefix.mycount:24|c"));
    }

    @Test(timeout=5000L) public void
    sends_counter_value_to_statsd_with_empty_tags() throws Exception {


        client.count("mycount", 24);
        server.waitForMessage();

        assertThat(server.messagesReceived(), contains("my.prefix.mycount:24|c"));
    }

    @Test(timeout=5000L) public void
    sends_counter_value_to_statsd_with_tags() throws Exception {


        client.count("mycount", 24, "foo:bar", "baz");
        server.waitForMessage();

        assertThat(server.messagesReceived(), contains("my.prefix.mycount:24|c|#baz,foo:bar"));
    }

    @Test(timeout=5000L) public void
    sends_counter_increment_to_statsd() throws Exception {


        client.incrementCounter("myinc");
        server.waitForMessage();

        assertThat(server.messagesReceived(), contains("my.prefix.myinc:1|c"));
    }

    @Test(timeout=5000L) public void
    sends_counter_increment_to_statsd_with_tags() throws Exception {


        client.incrementCounter("myinc", "foo:bar", "baz");
        server.waitForMessage();

        assertThat(server.messagesReceived(), contains("my.prefix.myinc:1|c|#baz,foo:bar"));
    }

    @Test(timeout=5000L) public void
    sends_counter_decrement_to_statsd() throws Exception {


        client.decrementCounter("mydec");
        server.waitForMessage();

        assertThat(server.messagesReceived(), contains("my.prefix.mydec:-1|c"));
    }

    @Test(timeout=5000L) public void
    sends_counter_decrement_to_statsd_with_tags() throws Exception {


        client.decrementCounter("mydec", "foo:bar", "baz");
        server.waitForMessage();

        assertThat(server.messagesReceived(), contains("my.prefix.mydec:-1|c|#baz,foo:bar"));
    }

    @Test(timeout=5000L) public void
    sends_gauge_to_statsd() throws Exception {


        client.recordGaugeValue("mygauge", 423);
        server.waitForMessage();

        assertThat(server.messagesReceived(), contains("my.prefix.mygauge:423|g"));
    }

    @Test(timeout=5000L) public void
    sends_large_double_gauge_to_statsd() throws Exception {


        client.recordGaugeValue("mygauge", 123456789012345.67890);
        server.waitForMessage();

        assertThat(server.messagesReceived(), contains("my.prefix.mygauge:123456789012345.67|g"));
    }

    @Test(timeout=5000L) public void
    sends_exact_double_gauge_to_statsd() throws Exception {


        client.recordGaugeValue("mygauge", 123.45678901234567890);
        server.waitForMessage();

        assertThat(server.messagesReceived(), contains("my.prefix.mygauge:123.456789|g"));
    }

    @Test(timeout=5000L) public void
    sends_double_gauge_to_statsd() throws Exception {


        client.recordGaugeValue("mygauge", 0.423);
        server.waitForMessage();

        assertThat(server.messagesReceived(), contains("my.prefix.mygauge:0.423|g"));
    }

    @Test(timeout=5000L) public void
    sends_gauge_to_statsd_with_tags() throws Exception {


        client.recordGaugeValue("mygauge", 423, "foo:bar", "baz");
        server.waitForMessage();

        assertThat(server.messagesReceived(), contains("my.prefix.mygauge:423|g|#baz,foo:bar"));
    }

    @Test(timeout=5000L) public void
    sends_double_gauge_to_statsd_with_tags() throws Exception {


        client.recordGaugeValue("mygauge", 0.423, "foo:bar", "baz");
        server.waitForMessage();

        assertThat(server.messagesReceived(), contains("my.prefix.mygauge:0.423|g|#baz,foo:bar"));
    }

    @Test(timeout=5000L) public void
    sends_histogram_to_statsd() throws Exception {


        client.recordHistogramValue("myhistogram", 423);
        server.waitForMessage();

        assertThat(server.messagesReceived(), contains("my.prefix.myhistogram:423|h"));
    }

    @Test(timeout=5000L) public void
    sends_double_histogram_to_statsd() throws Exception {


        client.recordHistogramValue("myhistogram", 0.423);
        server.waitForMessage();

        assertThat(server.messagesReceived(), contains("my.prefix.myhistogram:0.423|h"));
    }

    @Test(timeout=5000L) public void
    sends_histogram_to_statsd_with_tags() throws Exception {


        client.recordHistogramValue("myhistogram", 423, "foo:bar", "baz");
        server.waitForMessage();

        assertThat(server.messagesReceived(), contains("my.prefix.myhistogram:423|h|#baz,foo:bar"));
    }

    @Test(timeout=5000L) public void
    sends_double_histogram_to_statsd_with_tags() throws Exception {


        client.recordHistogramValue("myhistogram", 0.423, "foo:bar", "baz");
        server.waitForMessage();

        assertThat(server.messagesReceived(), contains("my.prefix.myhistogram:0.423|h|#baz,foo:bar"));
    }

    @Test(timeout=5000L) public void
    sends_timer_to_statsd() throws Exception {


        client.recordExecutionTime("mytime", 123);
        server.waitForMessage();

        assertThat(server.messagesReceived(), contains("my.prefix.mytime:123|ms"));
    }

    /**
     * A regression test for <a href="https://github.com/indeedeng/java-dogstatsd-client/issues/3">this i18n number formatting bug</a>
     * @throws Exception
     */
    @Test public void
    sends_timer_to_statsd_from_locale_with_unamerican_number_formatting() throws Exception {

        Locale originalDefaultLocale = Locale.getDefault();

        // change the default Locale to one that uses something other than a '.' as the decimal separator (Germany uses a comma)
        Locale.setDefault(Locale.GERMANY);

        try {


            client.recordExecutionTime("mytime", 123, "foo:bar", "baz");
            server.waitForMessage();

            assertThat(server.messagesReceived(), contains("my.prefix.mytime:123|ms|#baz,foo:bar"));
        } finally {
            // reset the default Locale in case changing it has side-effects
            Locale.setDefault(originalDefaultLocale);
        }
    }


    @Test(timeout=5000L) public void
    sends_timer_to_statsd_with_tags() throws Exception {


        client.recordExecutionTime("mytime", 123, "foo:bar", "baz");
        server.waitForMessage();

        assertThat(server.messagesReceived(), contains("my.prefix.mytime:123|ms|#baz,foo:bar"));
    }


    @Test(timeout=5000L) public void
    sends_gauge_mixed_tags() throws Exception {

        final NonBlockingStatsDClient empty_prefix_client = new NonBlockingStatsDClient("my.prefix", "localhost", STATSD_SERVER_PORT, new String[] {"instance:foo", "app:bar"});
        empty_prefix_client.gauge("value", 423, "baz");
        server.waitForMessage();

        assertThat(server.messagesReceived(), contains("my.prefix.value:423|g|#app:bar,instance:foo,baz"));
    }

    @Test(timeout=5000L) public void
    sends_gauge_constant_tags_only() throws Exception {

        final NonBlockingStatsDClient empty_prefix_client = new NonBlockingStatsDClient("my.prefix", "localhost", STATSD_SERVER_PORT, new String[] {"instance:foo", "app:bar"});
        empty_prefix_client.gauge("value", 423);
        server.waitForMessage();

        assertThat(server.messagesReceived(), contains("my.prefix.value:423|g|#app:bar,instance:foo"));
    }

    @Test(timeout=5000L) public void
    sends_gauge_empty_prefix() throws Exception {

        final NonBlockingStatsDClient empty_prefix_client = new NonBlockingStatsDClient("", "localhost", STATSD_SERVER_PORT);
        empty_prefix_client.gauge("top.level.value", 423);
        server.waitForMessage();

        assertThat(server.messagesReceived(), contains("top.level.value:423|g"));
    }

    @Test(timeout=5000L) public void
    sends_gauge_null_prefix() throws Exception {

        final NonBlockingStatsDClient null_prefix_client = new NonBlockingStatsDClient(null, "localhost", STATSD_SERVER_PORT);
        null_prefix_client.gauge("top.level.value", 423);
        server.waitForMessage();

        assertThat(server.messagesReceived(), contains("top.level.value:423|g"));
    }

    @Test public void
    sends_service_check() throws Exception {
        String[] tags = {"key1:val1", "key2:val2"};
        ServiceCheck sc = new ServiceCheck("my_check.name", ServiceCheck.WARNING, "♬ †øU \n†øU ¥ºu|m: T0µ ♪", "i-abcd1234", tags);
        sc.setTimestamp(1420740000);

        client.serviceCheck(sc);
        server.waitForMessage();

        assertThat(server.messagesReceived(), contains(String.format("_sc|my_check.name|1|d:1420740000|h:i-abcd1234|#key2:val2,key1:val1|m:%s",
                "♬ †øU \\n†øU ¥ºu|m\\: T0µ ♪")));
    }
}
