package fr.figarocms.flume.rabbitmq;

import com.cloudera.flume.conf.Context;
import com.cloudera.flume.core.Event;
import com.cloudera.flume.core.EventImpl;
import com.cloudera.flume.core.EventSink;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
@Ignore
public class IntegrationTest {

  @Mock
  private Context context;

  private RabbitMQFailoverSinkBuilder builder;


  @Test
  public void failover() throws Exception {
    // Given
    Event event = new EventImpl("this is a test".getBytes(), 132L, Event.Priority.INFO, 159L, "server1");
    event.set("attribute1", "200".getBytes());
    event.set("attribute2", "2012-02-15".getBytes());
    event.set("attribute3", "pattern/example?parameter1=value1&parameter2=value2&parameter3=2012-02".getBytes());
    builder = new RabbitMQFailoverSinkBuilder();

    // When
    final EventSink sink = builder.build(context, "iamqp01.int.adencf.local,iamqp02.int.adencf.local","guest", "guest", "/", "eventTracker","topic","#","json" );
    sink.open();
    sink.append(event);
    sink.close();

    // Then

  }
}
