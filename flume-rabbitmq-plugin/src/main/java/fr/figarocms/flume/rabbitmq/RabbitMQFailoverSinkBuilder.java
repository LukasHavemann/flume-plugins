package fr.figarocms.flume.rabbitmq;

import com.cloudera.flume.conf.Context;
import com.cloudera.flume.conf.FlumeBuilder;
import com.cloudera.flume.conf.FlumeSpecException;
import com.cloudera.flume.conf.SinkFactory;
import com.cloudera.flume.core.BackOffFailOverSink;
import com.cloudera.flume.core.EventSink;
import com.cloudera.flume.handlers.text.FormatFactory;
import com.cloudera.flume.handlers.text.output.DebugOutputFormat;
import com.cloudera.flume.handlers.text.output.OutputFormat;
import com.google.common.base.Preconditions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;

import static com.google.common.base.Strings.isNullOrEmpty;


public class RabbitMQFailoverSinkBuilder extends SinkFactory.SinkBuilder {

  private static final Logger LOG = LoggerFactory.getLogger(RabbitMQFailoverSinkBuilder.class);

  @Override
  @Deprecated
  public EventSink build(Context context, String... argv) {
    // updated interface calls build(Context,Object...) instead
    throw new RuntimeException(
        "Old sink builder for RabbitMQ sink should not be exercised");
  }

  @Override
  public EventSink create(Context context, Object... argv) {
    Preconditions.checkArgument(argv.length >= 7 && argv.length <= 9,
                                "usage: rabbitFailover(address, username, password, vhost, exchange, exchange_type, routing_key[, format, media_type])");
    Preconditions.checkArgument(!isNullOrEmpty(argv[0].toString()),
                                "Invalid configuration: 'addresses' must be non-null or empty.");
    String mediaType = null;
    // Define message media type
    OutputFormat fmt = DebugOutputFormat.builder().create();
    if (argv.length == 9) {
      mediaType = argv[8].toString();
    }
    // Define output format
    if (argv.length >= 8) {
      try {
        fmt = FlumeBuilder.createFormat(FormatFactory.get(), argv[7]);
      } catch (FlumeSpecException e) {
        LOG.error("Bad output format name " + argv[7], e);
        throw new IllegalArgumentException("Bad output format name "
                                           + argv[7], e);
      }
    }

    final String[] addresses = argv[0].toString().split(",");
    return failover(addresses, argv[1].toString(), argv[2].toString(), argv[3].toString(), argv[4].toString(),
                    argv[5].toString(), argv[6].toString(), fmt, mediaType);
  }


  private EventSink failover(String[] addresses, String username, String password, String vhost, String exchange,
                             String exchange_type, String routing_key, OutputFormat format, String mediaType) {
    Producer
        producer =
        new SimpleProducer(addresses[0], username, password, vhost, exchange, exchange_type,
                           routing_key);
    final RabbitMQSink rabbit = new RabbitMQSink(producer, format, mediaType);
    if (addresses.length == 1) {
      return rabbit;
    } else {
      addresses = Arrays.copyOfRange(addresses, 1, addresses.length);
      return new BackOffFailOverSink(rabbit,
                                     failover(addresses, username, password, vhost, exchange, exchange_type,
                                              routing_key, format, mediaType));
    }
  }


}
