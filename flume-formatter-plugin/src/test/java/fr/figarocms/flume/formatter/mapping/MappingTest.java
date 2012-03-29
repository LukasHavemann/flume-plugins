package fr.figarocms.flume.formatter.mapping;

import com.cloudera.flume.core.Event;
import com.cloudera.flume.core.EventImpl;

import org.junit.Before;
import org.junit.Test;

import java.nio.ByteBuffer;
import java.util.List;
import java.util.Map;

import static com.google.common.collect.Lists.newArrayList;
import static org.junit.Assert.assertEquals;

public class MappingTest {

  private Mapping mapping;
  private Event event;

  @Before
  public void setUp() throws Exception {
    mapping = new Mapping();
    event = nominalEvent();
  }

  @Test
  public void mapWithoutAttributes() throws Exception {
    // Given

    // When
    Map<String, Object> objectMap = mapping.map(event);

    // Then
    assertEquals("body", objectMap.get("body"));
    assertEquals(123L, objectMap.get("timestamp"));
    assertEquals(456L, objectMap.get("nanos"));
    assertEquals(Event.Priority.INFO, objectMap.get("priority"));
    assertEquals("server1", objectMap.get("host"));
  }


  @Test
  public void mapAttributes() throws Exception {
    // Given
    List<AttributeMapping> attributeMappings = newArrayList();
    attributeMappings.add(attributeMapping("attr_string", "string attribute"));
    attributeMappings.add(attributeMapping("attr_float", "float"));

    // When
    Map<String, Object> objectMap = mapping.mapAttributes(event, attributeMappings);

    // Then
    assertEquals("string attribute", objectMap.get("attr_string"));
    assertEquals(new Float(1.232), objectMap.get("attr_float"));
  }

  @Test
  public void noAttributeMapping() throws Exception {
    // Given

    // When
    Map<String, Object> objectMap = mapping.mapAttributes(event, null);

    // Then
    assertEquals("string attribute", objectMap.get("attr_string"));
    assertEquals(new String(event.get("attr_float")), objectMap.get("attr_float"));
  }

  private AttributeMapping attributeMapping(String name, String type) {
    final AttributeMapping attributeMapping = new AttributeMapping();
    attributeMapping.setName(name);
    attributeMapping.setType(type);
    return attributeMapping;
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
