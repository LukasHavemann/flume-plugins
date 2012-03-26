package fr.figarocms.flume.formatter.mapping.converter;

import org.junit.Test;

import java.nio.ByteBuffer;

import static org.junit.Assert.assertEquals;


public class ConvertersTest {

  @Test
  public void convertString() throws Exception {
    // Given / When
    String value = (String) Converters.convert("test".getBytes(), "string");

    // Then
    assertEquals("test", value);
  }

  @Test
  public void convertFloat() throws Exception {
    // Given
    Object source = new Integer(13213);
    byte[] data = new byte[Integer.SIZE];
    ByteBuffer buffer = ByteBuffer.wrap(data);
    buffer.putInt((Integer)source);
    Integer value = (Integer) Converters.convert(data, "integer");

    // Then
    assertEquals(source, value);
  }

  @Test
  public void unknownConverter() throws Exception {
    // Given / When
    String value = (String) Converters.convert("test".getBytes(), "test");

    // Then
    assertEquals("test", value);
  }


}
