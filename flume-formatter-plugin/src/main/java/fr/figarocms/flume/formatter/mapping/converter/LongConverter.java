package fr.figarocms.flume.formatter.mapping.converter;


import java.nio.ByteBuffer;

public class LongConverter implements Converter<Long> {

  @Override
  public Long convert(byte[] source) {
    ByteBuffer buffer = ByteBuffer.wrap(source);
    return buffer.getLong();
  }

}
