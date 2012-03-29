package fr.figarocms.flume.formatter.mapping.converter;


import org.junit.Before;
import org.junit.Test;

import java.nio.ByteBuffer;

import static org.junit.Assert.assertEquals;

public class FloatConverterTest {

  private FloatConverter converter;

  @Before
  public void setUp() throws Exception {
    converter = new FloatConverter();
  }

  @Test
  public void convert() throws Exception {
    // Given
    Float expected = new Float("2.14434344676876");
    byte[] data = new byte[Float.SIZE];
    ByteBuffer buffer = ByteBuffer.wrap(data);
    buffer.putFloat(expected);

    // When
    Float value = converter.convert(data);

    // Then
    assertEquals(expected, value);
  }

  @Test
  public void nullValue() throws Exception {
    // When
    Float value = converter.convert(null);

    // Then
    assertEquals(null, value);
  }
}
