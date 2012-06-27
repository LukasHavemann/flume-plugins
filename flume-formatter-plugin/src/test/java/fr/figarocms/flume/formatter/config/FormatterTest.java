package fr.figarocms.flume.formatter.config;

import com.cloudera.flume.core.Event;
import com.cloudera.flume.core.EventImpl;
import fr.figarocms.flume.formatter.mapping.Mapping;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;

import static com.google.common.collect.Maps.newHashMap;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(value = MockitoJUnitRunner.class)
public class FormatterTest {

  private Formatter formatter;
  private Event event;

  @Before
  public void setUp() throws Exception {
    formatter = new Formatter();
    event = nominalEvent();
  }

  @Test
  public void formatAndMappingNull() throws Exception {
    // Given

    // When
    Event event = nominalEvent();
    Map<String, Object> value = formatter.format(event);

    // Then
    assertEquals("body", value.get("body"));
    assertEquals(123L, value.get("timestamp"));
    assertEquals(456L, value.get("nanos"));
    assertEquals(Event.Priority.INFO, value.get("priority"));
    assertEquals("server1", value.get("host"));
    assertEquals("string attribute", value.get("attr_string"));
    assertEquals(new String(event.get("attr_float")), value.get("attr_float"));
  }


  @Test
  public void formatNull() throws Exception {
    // Given
    final HashMap<String, Object> mapping = newHashMap();
    mapping.put("attr_string", "string attribute");
    mapping.put("attr_float", new Float(1.232));
    final Mapping mockMapping = mock(Mapping.class);
    when(mockMapping.map(event)).thenReturn(mapping);
    formatter.setMapping(mockMapping);

    // When
    Map<String, Object> value = formatter.format(event);

    // Then
    assertEquals("string attribute", value.get("attr_string"));
    assertEquals(new Float(1.232), value.get("attr_float"));
  }

  @Test
  public void mappingNull() throws Exception {
    // Given
    final HashMap<String, Object> format = newHashMap();
    format.put("string", "%{attr_string}");
    format.put("float", "%{attr_float}");
    formatter.setFormat(format); // Set format

    // When
    Map<String, Object> value = formatter.format(event);

    // Then
    final HashMap<String, Object> expected = newHashMap();
    expected.put("string", "string attribute");
    expected.put("float", new String(event.get("attr_float"))); // Default converter is string
    assertEquals(expected, value);
  }


  @Test
  public void nominal() throws Exception {
    // Given
    final HashMap<String, Object> format = newHashMap();
    format.put("string", "%{attr_string}");
    format.put("float", "%{attr_float}");
    formatter.setFormat(format); // Set format

    final HashMap<String, Object> mapping = newHashMap();
    mapping.put("attr_string", "string attribute");
    mapping.put("attr_float", new Float(1.232));
    final Mapping mockMapping = mock(Mapping.class);
    when(mockMapping.map(event)).thenReturn(mapping);
    formatter.setMapping(mockMapping);

    // When
    Map<String, Object> value = formatter.format(event);

    // Then
    final HashMap<String, Object> expected = newHashMap();
    expected.put("string", "string attribute");
    expected.put("float", new Float(1.232));
    assertEquals(expected, value);
  }

  public static Event nominalEvent() {
    Event
        event =
        new EventImpl("body".getBytes(), 123L, Event.Priority.INFO, 456L, "server1");

    // String attribute
    event.set("attr_string", "string attribute".getBytes());

    // Float attribute
    Float source = new Float(1.232);
    byte[] data = new byte[Float.SIZE];
    ByteBuffer buffer = ByteBuffer.wrap(data);
    buffer.putFloat((Float) source);
    event.set("attr_float", data);

    return event;
  }


}
