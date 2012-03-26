package fr.figarocms.flume.formatter.mapping.converter;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class StringConverterTest {

	StringConverter converter;

	@Before
	public void setUp() throws Exception {
		converter = new StringConverter();
	}

	@Test
	public void convert() throws Exception {
		// When
		String expected = "2.11113134679764";
		String value = converter.convert(expected.getBytes());

		// Then
		assertEquals(expected, value);
	}

}
