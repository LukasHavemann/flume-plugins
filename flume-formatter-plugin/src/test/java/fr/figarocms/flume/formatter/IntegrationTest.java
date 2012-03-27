package fr.figarocms.flume.formatter;

import com.cloudera.flume.core.Event;
import com.cloudera.flume.core.EventImpl;

import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.HashMap;

import static org.junit.Assert.assertEquals;

public class IntegrationTest {

  JsonObjectFormatter formatter;
  ObjectMapper mapper;


  @Before
  public void setUp() throws Exception {
    final JsonObjectFormatterBuilder builder = new JsonObjectFormatterBuilder();
    formatter = (JsonObjectFormatter) builder.build("formatter.yml");
    mapper = new ObjectMapper();
  }

  @Test
  public void nominal() throws Exception {
    //Given
    Event event = nominalEvent();
    InputStream expected = ClassLoader.getSystemClassLoader().getResourceAsStream("expected.json");

    // When
    ByteArrayOutputStream stream = new ByteArrayOutputStream();
    formatter.format(stream, event);

    // Then
    ByteArrayInputStream actual = new ByteArrayInputStream(stream.toByteArray());
    final byte[] bytes = stream.toByteArray();
    System.out.print(new String(bytes));

    assertEquals(
        mapper.readValue(expected, new TypeReference<HashMap<String, Object>>() {
        }),
        mapper.readValue(actual, new TypeReference<HashMap<String, Object>>() {
        })
    );
  }


  public static Event nominalEvent() {
    Event
        event =
        new EventImpl("this is a test".getBytes(), 123L, Event.Priority.INFO, 456L, "server1");

    // String attribute
    event.set("attr_string", "this is a string".getBytes());

    // Integer attribute
    Object source = new Integer(13213);
    byte[] data = new byte[Integer.SIZE];
    ByteBuffer buffer = ByteBuffer.wrap(data);
    buffer.putInt((Integer) source);
    event.set("attr_integer", data);

    // Long attribute
    source = new Long(779864634);
    data = new byte[Long.SIZE];
    buffer = ByteBuffer.wrap(data);
    buffer.putLong((Long) source);
    event.set("attr_long", data);

    // Float attribute
    source = new Float(1.232);
    data = new byte[Float.SIZE];
    buffer = ByteBuffer.wrap(data);
    buffer.putFloat((Float) source);
    event.set("attr_float", data);

    // Double attribute
    source = new Double(1.232567641);
    data = new byte[Float.SIZE];
    buffer = ByteBuffer.wrap(data);
    buffer.putDouble((Double) source);
    event.set("attr_double", data);

    return event;
  }
}
