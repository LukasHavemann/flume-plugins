package fr.figarocms.flume.formatter.mapping.converter;


import java.nio.ByteBuffer;

public class FloatConverter implements Converter<Float> {

	@Override
	public Float convert(byte[] source) {
		ByteBuffer buffer = ByteBuffer.wrap(source);
		return buffer.getFloat();
	}
}
