package fr.figarocms.flume.formatter.mapping.converter;

import java.nio.ByteBuffer;

public class DoubleConverter implements Converter<Double> {

	@Override
	public Double convert(byte[] source) {
		ByteBuffer buffer = ByteBuffer.wrap(source);
		return buffer.getDouble();
	}

}
