package fr.figarocms.flume.rabbitmq;

import com.google.common.base.Preconditions;
import com.google.common.base.Strings;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URI;

import static com.rabbitmq.client.MessageProperties.PERSISTENT_TEXT_PLAIN;
import static java.lang.String.format;

/**
 * <p> Base implementation of a RabbitMQ producer sending durable message to an exchange or directly to a queue if no
 * exchange is set. </p>
 */
public class SimpleProducer implements Producer {

  protected static Logger LOG = LoggerFactory.getLogger(SimpleProducer.class);

  private ConnectionFactory factory;
  private Connection connection;
  private Channel channel;
  private String exchange;
  private String type;
  private String routingKey;

  public SimpleProducer(String uri, String exchange, String type, String routingKey) {
    Preconditions.checkNotNull(uri, "Invalid configuration: 'uri' must be non-null.");
    this.exchange = Preconditions.checkNotNull(exchange, "Invalid configuration: 'exchange' must be non-null.");
    this.type = Preconditions.checkNotNull(type, "Invalid configuration: 'type' must be non-null.");
    this.routingKey = Preconditions.checkNotNull(routingKey, "Invalid configuration: 'routingKey' must be non-null.");
    factory = new ConnectionFactory();
    try {
      factory.setUri(URI.create(uri));
    } catch (Exception e) { // Catch all exception
      throw new IllegalArgumentException(
          "Invalid configuration: 'uri' is not an amqp uri.\nSee http://www.rabbitmq.com/uri-spec.html.", e);
    }
  }

  @Override
  public void open() throws IOException {
    Preconditions.checkState(connection == null, "double open not permitted");
    connection = factory.newConnection();
    channel = connection.createChannel();
    if (LOG.isDebugEnabled()) {
      LOG.debug(format("Open connection %s ", connection));
    }
    declareQueueOrExchange(channel, exchange, type, routingKey);
  }

  @Override
  public void close() throws IOException {
    channel.close();
    connection.close();
    if (LOG.isDebugEnabled()) {
      LOG.debug(format("Close connection %s ", connection));
    }
  }

  @Override
  public void publish(byte[] msg) throws IOException {
    channel.basicPublish(exchange, routingKey, PERSISTENT_TEXT_PLAIN, msg);
    if (LOG.isDebugEnabled()) {
      LOG.debug(format("Publish event %s", msg));
    }
  }

  private void declareQueueOrExchange(Channel channel, String exchange, String type, String routingKey)
      throws IOException {
    // Just declare queue if exchange is null or blank
    if (Strings.isNullOrEmpty(exchange)) {
      channel.queueDeclare(routingKey, true, false, false, null); // Durable message in queue
    } else {
      channel.exchangeDeclare(exchange, type, true, false, false, null); // Durable message in exchange
    }
  }
}
