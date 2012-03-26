package fr.figarocms.flume.formatter.mapping.converter;

public interface Converter<T> {

	public T convert(byte[] source);

}