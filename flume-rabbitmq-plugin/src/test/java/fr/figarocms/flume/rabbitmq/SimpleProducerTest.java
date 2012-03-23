package fr.figarocms.flume.rabbitmq;

import org.junit.Test;

import static org.junit.Assert.assertNotNull;

public class SimpleProducerTest {

  private SimpleProducer producer;

  @Test(expected = IllegalArgumentException.class)
  public void addressNull() throws Exception {
    // When
    new SimpleProducer(null, null, null, null, null, null, null);
  }

  @Test(expected = IllegalArgumentException.class)
  public void addressEmpty() throws Exception {
    // When
    new SimpleProducer("", null, null, null, null, null, null);
  }

  @Test(expected = IllegalArgumentException.class)
  public void usernameNull() throws Exception {
    // When
    new SimpleProducer("address", null, null, null, null, null, null);
  }

  @Test(expected = IllegalArgumentException.class)
  public void usernameEmpty() throws Exception {
    // When
    new SimpleProducer("address", null, null, null, null, null, null);
  }

  @Test(expected = IllegalArgumentException.class)
  public void passwordNull() throws Exception {
    // When
    new SimpleProducer("address", "username", null, null, null, null, null);
  }

  @Test(expected = IllegalArgumentException.class)
  public void passwordEmpty() throws Exception {
    // When
    new SimpleProducer("address", "username", "", null, null, null, null);
  }

  @Test(expected = IllegalArgumentException.class)
  public void vhostNull() throws Exception {
    // When
    new SimpleProducer("address", "username", "password", null, null, null, null);
  }

  @Test(expected = IllegalArgumentException.class)
  public void vhostEmpty() throws Exception {
    // When
    new SimpleProducer("address", "username", "password", null, null, null, null);
  }

  @Test(expected = IllegalArgumentException.class)
  public void exchangeNull() throws Exception {
    // When
    new SimpleProducer("address", "username", "password", "vhost", null, null, null);
  }

  @Test(expected = IllegalArgumentException.class)
  public void typeNull() throws Exception {
    // When
    new SimpleProducer("address", "username", "password", "vhost", "exchange", null, null);
  }

  @Test(expected = IllegalArgumentException.class)
  public void routingKeyNull() throws Exception {
    // When
    new SimpleProducer("address", "username", "password", "vhost", "exchange", "type", null);
  }

  @Test
  public void nominal() throws Exception {
    // When
    final
    SimpleProducer
        simpleProducer =
        new SimpleProducer("address", "username", "password", "vhost", "exchange", "type", "routing_key");

    // Then
    assertNotNull(simpleProducer);
  }

}
