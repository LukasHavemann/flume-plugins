package fr.figarocms.flume.rabbitmq;

import com.cloudera.flume.conf.Context;
import com.cloudera.flume.core.EventSink;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.junit.Assert.assertNotNull;

@RunWith(MockitoJUnitRunner.class)
public class RabbitMQSinkBuilderTest {

  @Mock
  private Context context;

  private RabbitMQSinkBuilder builder;

  @Before
  public void setUp() throws Exception {
    builder = new RabbitMQSinkBuilder();
  }

  @Test(expected = IllegalArgumentException.class)
  public void buildWithoutArgs() throws Exception {
    // When
    builder.build(context, new String[]{});

    // Then IllegalArgumentException is thrown
  }

  @Test(expected = IllegalArgumentException.class)
  public void buildWithOneArgument() throws Exception {
    // When
    builder.build(context, new String[]{"arg1"});

    // Then IllegalArgumentException is thrown
  }

  @Test(expected = IllegalArgumentException.class)
  public void buildWithTwoArgs() throws Exception {
    // When
    builder.build(context, new String[]{"arg1", "arg2"});

    // Then IllegalArgumentException is thrown
  }

  @Test(expected = IllegalArgumentException.class)
  public void buildWithThreeArgs() throws Exception {
    // When
    builder.build(context, new String[]{"arg1", "arg2", "arg3"});

    // Then IllegalArgumentException is thrown
  }

  @Test(expected = IllegalArgumentException.class)
  public void buildWithInvalidURI() throws Exception {
    // When
    builder.build(context, new String[]{"arg1", "arg2", "arg3", "arg4"});

    // Then IllegalArgumentException is thrown
  }

  @Test
  public void buildWithFourArgs() throws Exception {
    // When
    EventSink sink = builder.build(context, new String[]{"amqp://test", "arg2", "arg3", "arg4"});

    // Then
    assertNotNull(sink);
  }

  @Test(expected = IllegalArgumentException.class)
  public void buildWithInvalidOutputFormat() throws Exception {
    // When
    builder.build(context, new String[]{"amqp://test", "arg2", "arg3", "arg4", "arg5"});

    // Then IllegalArgumentException is thrown
  }

  @Test
  public void buildWithValidOutputFormat() throws Exception {
    // When
    EventSink sink = builder.build(context, new String[]{"amqp://test", "arg2", "arg3", "arg4", "json"});

    // Then
    assertNotNull(sink);
  }

}
