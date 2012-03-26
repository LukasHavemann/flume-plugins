package fr.figarocms.flume.formatter.mapping.converter;

import org.junit.Before;
import org.junit.Test;

import java.nio.ByteBuffer;

import static org.junit.Assert.assertEquals;

public class LongConverterTest {

	private LongConverter converter;

	@Before
	public void setUp() throws Exception {
		converter = new LongConverter();
	}

	@Test
	public void convert() throws Exception {
		// Given
		Long expected = new Long("3456784554649878979");
		byte[] data = new byte[Long.SIZE];
		ByteBuffer buffer = ByteBuffer.wrap(data);
		buffer.putLong(expected);

		// When
		Long value = converter.convert(data);

		// Then
		assertEquals(expected, value);
	}
}
