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
    Preconditions.checkArgument(argv.length >= 4 && argv.length <= 5,
                                "usage: rabbit(uri, exchange, exchange_type, routing_key[, format])");

    // Create Producer
    Producer producer = new SimpleProducer(argv[0], argv[1], argv[2], argv[3]);

    // Define output format
    OutputFormat fmt = DebugOutputFormat.builder().create();
    if (argv.length == 5) {
      try {
        fmt = FlumeBuilder.createFormat(FormatFactory.get(), argv[4]);
      } catch (FlumeSpecException e) {
        LOG.error("Bad output format name " + argv[4], e);
        throw new IllegalArgumentException("Bad output format name "
                                           + argv[4], e);
      }
    }

    return new RabbitMQSink(producer, fmt);
  }
}
