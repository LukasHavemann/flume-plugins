package fr.figarocms.flume.formatter.mapping.converter;

import java.nio.ByteBuffer;

public class DoubleConverter implements Converter<Double> {

  @Override
  public Double convert(byte[] source) {
    if (source == null) {
      return null;
    }
    ByteBuffer buffer = ByteBuffer.wrap(source);
    return buffer.getDouble();
  }

}
