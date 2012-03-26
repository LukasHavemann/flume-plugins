package fr.figarocms.flume.formatter.mapping.converter;


import java.nio.ByteBuffer;

public class IntegerConverter implements Converter<Integer> {

  @Override
  public Integer convert(byte[] source) {
    ByteBuffer buffer = ByteBuffer.wrap(source);
    return buffer.getInt();
  }

}
