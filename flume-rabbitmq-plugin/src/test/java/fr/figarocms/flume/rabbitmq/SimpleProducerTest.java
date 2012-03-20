package fr.figarocms.flume.rabbitmq;

import org.junit.Test;

import static org.junit.Assert.assertNotNull;

public class SimpleProducerTest {

  private SimpleProducer producer;

  @Test(expected = NullPointerException.class)
  public void uriNull() throws Exception {
    // When
    new SimpleProducer(null, null, null, null);
  }

  @Test(expected = NullPointerException.class)
  public void exchangeNull() throws Exception {
    // When
    new SimpleProducer("", null, null, null);
  }

  @Test(expected = NullPointerException.class)
  public void typeNull() throws Exception {
    // When
    new SimpleProducer("", "", null, null);
  }

  @Test(expected = NullPointerException.class)
  public void routingKeyNull() throws Exception {
    // When
    new SimpleProducer("", "", "", null);
  }

  @Test(expected = IllegalArgumentException.class)
  public void notAnUri() throws Exception {
    // When
    new SimpleProducer("", "", "", "");
  }

  @Test(expected = IllegalArgumentException.class)
  public void notAnAMQPUri() throws Exception {
    // When
    new SimpleProducer("http://test", "", "", "");
  }

  @Test
  public void anAMQPUri() throws Exception {
    // When
    producer = new SimpleProducer("amqp://test", "", "", "");

    // Then
    assertNotNull(producer);
  }


}
