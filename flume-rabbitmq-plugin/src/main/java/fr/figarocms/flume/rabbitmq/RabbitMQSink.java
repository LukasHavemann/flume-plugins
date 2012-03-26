package fr.figarocms.flume.rabbitmq;

import com.cloudera.flume.conf.SinkFactory;
import com.cloudera.flume.core.Event;
import com.cloudera.flume.core.EventSink;
import com.cloudera.flume.handlers.text.output.OutputFormat;
import com.cloudera.util.Pair;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class RabbitMQSink extends EventSink.Base {

  protected static Logger LOG = LoggerFactory.getLogger(RabbitMQSink.class);

  private Producer producer;
  private OutputFormat format;
  private String mediaType;

  public RabbitMQSink(Producer producer, OutputFormat format, String mediaType) {
    this.producer = producer;
    this.format = format;
    this.mediaType = mediaType;
  }

  @Override
  public void append(Event e) throws IOException, InterruptedException {
    ByteArrayOutputStream bos = new ByteArrayOutputStream();
    format.format(bos, e);
    producer.publish(bos.toByteArray(), mediaType);
  }

  @Override
  public void close() throws IOException, InterruptedException {
    producer.close();
  }

  @Override
  public void open() throws IOException, InterruptedException {
    producer.open();
  }

  private static SinkFactory.SinkBuilder builder() {
    return new RabbitMQSinkBuilder();
  }

  private static SinkFactory.SinkBuilder failover() {
    return new RabbitMQFailoverSinkBuilder();
  }

  /**
   * This is a special function used by the SourceFactory to pull in this class as a RabbitMQSink decorator.
   *
   * @return a list of RabbitMQSink
   */
  public static List<Pair<String, SinkFactory.SinkBuilder>> getSinkBuilders() {
    List<Pair<String, SinkFactory.SinkBuilder>> builders = new ArrayList<Pair<String, SinkFactory.SinkBuilder>>();
    builders.add(new Pair<String, SinkFactory.SinkBuilder>("rabbit", builder()));
    builders.add(new Pair<String, SinkFactory.SinkBuilder>("rabbitFailover", failover()));
    return builders;
  }

}
