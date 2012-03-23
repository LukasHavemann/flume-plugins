package fr.figarocms.flume.rabbitmq;

import com.google.common.base.Preconditions;

import com.cloudera.flume.conf.Context;
import com.cloudera.flume.conf.FlumeBuilder;
import com.cloudera.flume.conf.FlumeSpecException;
import com.cloudera.flume.conf.SinkFactory;
import com.cloudera.flume.core.BackOffFailOverSink;
import com.cloudera.flume.core.EventSink;
import com.cloudera.flume.core.FailOverSink;
import com.cloudera.flume.handlers.text.FormatFactory;
import com.cloudera.flume.handlers.text.output.DebugOutputFormat;
import com.cloudera.flume.handlers.text.output.OutputFormat;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;

import static com.google.common.base.Strings.isNullOrEmpty;


public class RabbitMQFailoverSinkBuilder extends SinkFactory.SinkBuilder {

  private static final Logger LOG = LoggerFactory.getLogger(RabbitMQFailoverSinkBuilder.class);

  @Override
  public EventSink build(Context context, String... argv) {
    Preconditions.checkArgument(argv.length >= 7 && argv.length <= 8,
                                "usage: rabbitFailover(addresses, username, password, vhost, exchange, exchange_type, routing_key[, format])");
    Preconditions.checkArgument(!isNullOrEmpty(argv[0]),
                                "Invalid configuration: 'addresses' must be non-null or empty.");
    // Define output format
    OutputFormat fmt = DebugOutputFormat.builder().create();
    if (argv.length == 8) {
      try {
        fmt = FlumeBuilder.createFormat(FormatFactory.get(), argv[7]);
      } catch (FlumeSpecException e) {
        LOG.error("Bad output format name " + argv[7], e);
        throw new IllegalArgumentException("Bad output format name "
                                           + argv[7], e);
      }
    }

    final String[] addresses = argv[0].split(",");
    return failover(addresses, argv[1], argv[2], argv[3], argv[4], argv[5], argv[6], fmt);
  }

  private EventSink failover(String[] addresses, String username, String password, String vhost, String exchange,
                             String exchange_type, String routing_key, OutputFormat format) {
    Producer
        producer =
        new SimpleProducer(addresses[0], username, password, vhost, exchange, exchange_type,
                           routing_key);
    final RabbitMQSink rabbit = new RabbitMQSink(producer, format);
    if (addresses.length == 1) {
      return rabbit;
    } else {
      addresses = Arrays.copyOfRange(addresses, 1, addresses.length);
      return new BackOffFailOverSink(rabbit,
                              failover(addresses, username, password, vhost, exchange, exchange_type, routing_key, format));
    }
  }


}
