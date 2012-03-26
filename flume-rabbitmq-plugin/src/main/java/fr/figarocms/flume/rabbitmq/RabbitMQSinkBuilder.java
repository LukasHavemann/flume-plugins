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
  public EventSink build(Context context, String... argv) {
    Preconditions.checkArgument(argv.length >= 7 && argv.length <= 9,
                                "usage: rabbit(address, username, password, vhost, exchange, exchange_type, routing_key[, format, media_type])");

    // Create Producer
    Producer producer = new SimpleProducer(argv[0], argv[1], argv[2], argv[3], argv[4], argv[5], argv[6]);
    String mediaType = null;
    // Define message media type
    OutputFormat fmt = DebugOutputFormat.builder().create();
    if (argv.length == 9) {
      mediaType = argv[8];
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
