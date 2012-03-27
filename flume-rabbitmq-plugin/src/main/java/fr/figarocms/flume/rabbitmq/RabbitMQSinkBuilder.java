package fr.figarocms.flume.rabbitmq;

import com.google.common.base.Preconditions;

import com.cloudera.flume.conf.Context;
import com.cloudera.flume.conf.FlumeBuilder;
import com.cloudera.flume.conf.FlumeSpecException;
import com.cloudera.flume.conf.SinkFactory;
import com.cloudera.flume.core.EventSink;
import com.cloudera.flume.handlers.text.FormatFactory;
import com.cloudera.flume.handlers.text.output.DebugOutputFormat;
import com.cloudera.flume.handlers.text.output.OutputFormat;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class RabbitMQSinkBuilder extends SinkFactory.SinkBuilder {

  private static final Logger LOG = LoggerFactory.getLogger(RabbitMQSinkBuilder.class);

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
                                "usage: rabbit(address, username, password, vhost, exchange, exchange_type, routing_key[, format, media_type])");

    // Create Producer
    Producer
        producer =
        new SimpleProducer(argv[0].toString(), argv[1].toString(), argv[2].toString(), argv[3].toString(),
                           argv[4].toString(), argv[5].toString(), argv[6].toString());
    String mediaType = null;
    OutputFormat fmt = DebugOutputFormat.builder().create();
    // Define message media type
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

    return new RabbitMQSink(producer, fmt, mediaType);
  }
}
