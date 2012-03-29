package fr.figarocms.flume.formatter.mapping.converter;


public class StringConverter implements Converter<String> {

  @Override
  public String convert(byte[] source) {
    if (source == null){
      return null;
    }
    return new String(source);
  }

}
