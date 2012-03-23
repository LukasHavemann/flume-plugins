package fr.figarocms.flume.rabbitmq;

import com.cloudera.flume.core.Event;
import com.cloudera.flume.handlers.text.output.OutputFormat;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import java.io.ByteArrayOutputStream;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.only;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class RabbitMQSinkTest {

  private RabbitMQSink sink;

  @Mock
  private Producer producer;
  @Mock
  private OutputFormat format;
  private String mediaType ="application/json";

  @Before
  public void setUp() throws Exception {
    sink = new RabbitMQSink(producer, format, mediaType);
  }

  @Test
  public void open() throws Exception {
    // When
    sink.open();

    // Then
    verify(producer, only()).open();
  }

  @Test
  public void close() throws Exception {
    // When
    sink.close();

    // Then
    verify(producer, only()).close();
  }

  @Test
  public void append() throws Exception {
    // When
    Event event = mock(Event.class);
    sink.append(event);

    // Then
    InOrder order = Mockito.inOrder(format, producer);
    ArgumentCaptor<ByteArrayOutputStream> captor = ArgumentCaptor.forClass(ByteArrayOutputStream.class);
    order.verify(format).format(captor.capture(), Mockito.eq(event));
    order.verify(producer).publish(captor.getValue().toByteArray(), mediaType);
    order.verifyNoMoreInteractions();
  }
}
