package fr.figarocms.flume.formatter.mapping.converter;

import org.apache.hadoop.thirdparty.guava.common.base.Strings;
import org.joda.time.DateTime;

import java.nio.ByteBuffer;

public class DateTimeConverter implements Converter<DateTime> {

	@Override
	public DateTime convert(byte[] source) {
		ByteBuffer buffer = ByteBuffer.wrap(source);
		long timestamp = buffer.getLong();
		return new DateTime(timestamp);
	}
}
