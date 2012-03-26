package fr.figarocms.flume.formatter.mapping.converter;

import com.google.common.collect.ImmutableMap;
import org.joda.time.DateTime;


public class Converters {

	private Converters() {
	}


	private static ImmutableMap<String, Converter> CONVERTERS =
			new ImmutableMap.Builder<String, Converter>()
					.put("string", new StringConverter())
					.put("integer", new IntegerConverter())
					.put("long", new LongConverter())
					.put("float", new FloatConverter())
					.put("double", new DoubleConverter())
					.put("date", new DateTimeConverter())
					.build();


	public static Object convert(byte[] bytes, String type, String pattern) {
		Converter converter = CONVERTERS.get(type);
		if("date".equals(type)){
			DateTime date = (DateTime) converter.convert(bytes);
			return date.toString(pattern);
		}
		if (converter == null) {
			converter = CONVERTERS.get("string"); // By default convert to string
		}
		return converter.convert(bytes);
	}

}
