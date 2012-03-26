package fr.figarocms.flume.formatter.mapping.converter;

import org.junit.Before;
import org.junit.Test;

import java.nio.ByteBuffer;

import static org.junit.Assert.assertEquals;

public class DoubleConverterTest {

  private DoubleConverter converter;

  @Before
  public void setUp() throws Exception {
    converter = new DoubleConverter();
  }

  @Test
  public void convert() throws Exception {
    // Given
    Double expected = new Double("2.14434344676876");
    byte[] data = new byte[Double.SIZE];
    ByteBuffer buffer = ByteBuffer.wrap(data);
    buffer.putDouble(expected);

    // When
    Double value = converter.convert(data);

    // Then
    assertEquals(expected, value);
  }

}
