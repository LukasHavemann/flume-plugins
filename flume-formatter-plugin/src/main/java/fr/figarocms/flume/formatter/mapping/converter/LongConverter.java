package fr.figarocms.flume.formatter.mapping.converter;


import java.nio.ByteBuffer;

public class LongConverter implements Converter<Long> {

  @Override
  public Long convert(byte[] source) {
    if (source == null) {
      return null;
    }
    ByteBuffer buffer = ByteBuffer.wrap(source);
    return buffer.getLong();
  }

}
