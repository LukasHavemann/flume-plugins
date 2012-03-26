package fr.figarocms.flume.formatter.mapping.converter;


import org.junit.Before;
import org.junit.Test;

import java.nio.ByteBuffer;

import static org.junit.Assert.assertEquals;

public class IntegerConverterTest {

	private IntegerConverter converter;

	@Before
	public void setUp() throws Exception {
		converter = new IntegerConverter();
	}

	@Test
	public void convert() throws Exception {
		// Given
		Integer expected = new Integer("345678");
		byte[] data = new byte[Integer.SIZE];
		ByteBuffer buffer = ByteBuffer.wrap(data);
		buffer.putInt(expected);

		// When
		Integer value = converter.convert(data);

		// Then
		assertEquals(expected, value);
	}


}
