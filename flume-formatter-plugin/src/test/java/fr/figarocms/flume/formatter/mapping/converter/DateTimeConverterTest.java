package fr.figarocms.flume.formatter.mapping.converter;


import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;

import java.nio.ByteBuffer;

import static org.junit.Assert.assertEquals;

public class DateTimeConverterTest {

	private DateTimeConverter converter;

	@Before
	public void setUp() throws Exception {
		converter = new DateTimeConverter();
	}

	@Test
	public void convert() throws Exception {
		// Given
		DateTime expected = new DateTime();

		byte[] data = new byte[Long.SIZE];
		ByteBuffer buffer = ByteBuffer.wrap(data);
		buffer.putLong(expected.getMillis());

		// When
		DateTime value = converter.convert(data);

		// Then
		assertEquals(expected, value);
	}


}
