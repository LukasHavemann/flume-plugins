package fr.figarocms.flume.formatter.mapping.converter;

import org.joda.time.DateTime;
import org.junit.Test;

import java.nio.ByteBuffer;

import static org.junit.Assert.assertEquals;


public class ConvertersTest {

  @Test
  public void convertString() throws Exception {
    // Given / When
    String value = (String) Converters.convert("test".getBytes(), "string", null);

    // Then
    assertEquals("test", value);
  }

  @Test
  public void convertDate() throws Exception {
    // Given
    DateTime expected = new DateTime();
    byte[] data = new byte[Long.SIZE];
    ByteBuffer buffer = ByteBuffer.wrap(data);
    buffer.putLong(expected.getMillis());

    // When
    String value = (String) Converters.convert(data, "date", null);

    assertEquals(expected.toString(), value);
  }

  @Test
  public void convertDateWithFormat() throws Exception {
    // Given
    DateTime expected = new DateTime();
    byte[] data = new byte[Long.SIZE];
    ByteBuffer buffer = ByteBuffer.wrap(data);
    buffer.putLong(expected.getMillis());

    // When
    String value = (String) Converters.convert(data, "date", "dd/MM/yyyy");

    assertEquals(expected.toString("dd/MM/yyyy"), value);
  }
}
