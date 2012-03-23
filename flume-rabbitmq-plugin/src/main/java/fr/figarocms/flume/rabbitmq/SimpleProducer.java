package fr.figarocms.flume.rabbitmq;

import com.google.common.base.Preconditions;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Address;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

import static com.google.common.base.Strings.isNullOrEmpty;
import static com.rabbitmq.client.Address.parseAddress;
import static java.lang.String.format;


/**
 * <p> Base implementation of a RabbitMQ producer sending durable message to an exchange or directly to a queue if no
 * exchange is set. </p>
 */
public class SimpleProducer implements Producer {

  protected static Logger LOG = LoggerFactory.getLogger(SimpleProducer.class);

  private static int DEFAULT_PORT = 5672;

  private ConnectionFactory factory;
  private Connection connection;
  private Channel channel;
  private String exchange;
  private String type;
  private String routingKey;

  public SimpleProducer(String address, String username, String password, String vhost, String exchange, String type,
                        String routingKey) {
    checkPreconditions(address, username, password, vhost, exchange, routingKey);
    Address addr = parseAddress(address);
    factory = new ConnectionFactory();
    factory.setUsername(username);
    factory.setPassword(password);
    factory.setVirtualHost(vhost);
    factory.setHost(addr.getHost());
    factory.setPort(addr.getPort() == -1 ? DEFAULT_PORT : addr.getPort());
    this.exchange = exchange;
    this.type = type;
    this.routingKey = routingKey;
  }


  @Override
  public void open() throws IOException {
    try {
      Preconditions.checkState(connection == null || !connection.isOpen(), "connection is already open");
      connection = factory.newConnection();
      channel = connection.createChannel();
      if (LOG.isInfoEnabled()) {
        LOG.info(format("Open connection %s ", connection));
      }
      declareQueueOrExchange(channel, exchange, type, routingKey);
    } catch (Exception e) {
      if (LOG.isErrorEnabled()) {
        LOG.error(format("Error occurred when opening connection %s ", connection), e);
      }
      throw new IOException(e);
    }
  }

  @Override
  public void close() throws IOException {
    try {
      if (channel != null && channel.isOpen()) {
        channel.close();
        if (LOG.isInfoEnabled()) {
          LOG.info(format("Close channel %s ", channel));
        }
      }
      if (connection != null && connection.isOpen()) {
        connection.close();
        if (LOG.isInfoEnabled()) {
          LOG.info(format("Close connection %s ", connection));
        }
      }
    } catch (Exception e) {
      if (LOG.isErrorEnabled()) {
        LOG.error(format("Error occurred when closing connection %s ", connection), e);
      }
      throw new IOException(e);
    }
  }

  @Override
  public void publish(byte[] msg, String mediaType) throws IOException {
    try {
      final AMQP.BasicProperties properties = persistentMediaTypeProperties(mediaType);
      channel.basicPublish(exchange, routingKey, properties, msg);
      if (LOG.isDebugEnabled()) {
        LOG.debug(format("Publish event %s", msg));
      }
    } catch (Exception e) {
      if (LOG.isErrorEnabled()) {
        LOG.error(format("Error occurred when publishing event %s ", msg), e);
      }
      throw new IOException(e);
    }
  }

  private AMQP.BasicProperties persistentMediaTypeProperties(String mediaType) {
    return new AMQP.BasicProperties((isNullOrEmpty(mediaType) ? "application/octet-stream" : mediaType),
                          null,
                          null,
                          2,
                          0, null, null, null,
                          null, null, null, null,
                          null, null);
  }

  private void declareQueueOrExchange(Channel channel, String exchange, String type, String routingKey)
      throws IOException {
    // Just declare queue if exchange is null or blank
    if (isNullOrEmpty(exchange)) {
      channel.queueDeclare(routingKey, true, false, false, null); // Durable message in queue
    } else {
      channel.exchangeDeclare(exchange, type, true, false, false, null); // Durable message in exchange
    }
  }


  private void checkPreconditions(String address, String username, String password, String vhost, String exchange,
                                  String routingKey) {
    Preconditions.checkArgument(!isNullOrEmpty(address),
                                "Invalid configuration: 'address' must be non-null or empty.");
    Preconditions.checkArgument(!isNullOrEmpty(username),
                                "Invalid configuration: 'username' must be non-null or empty.");
    Preconditions.checkArgument(!isNullOrEmpty(password),
                                "Invalid configuration: 'password' must be non-null or empty.");
    Preconditions.checkArgument(!isNullOrEmpty(vhost),
                                "Invalid configuration: 'vhost' must be non-null or empty.");
    Preconditions.checkArgument(exchange != null, "Invalid configuration: 'exchange' must be non-null.");
    Preconditions.checkArgument(routingKey != null, "Invalid configuration: 'routingKey' must be non-null.");
  }
}
