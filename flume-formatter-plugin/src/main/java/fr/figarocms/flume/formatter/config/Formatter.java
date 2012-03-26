package fr.figarocms.flume.formatter.config;

import com.cloudera.flume.core.Event;
import com.google.common.collect.Maps;
import fr.figarocms.flume.formatter.mapping.Mapping;

import java.util.Map;

public class Formatter {

	private Mapping mapping;
	private Map format;

	public void setMapping(Mapping mapping) {
		this.mapping = mapping;
	}

	public void setFormat(Map format) {
		this.format = format;
	}

	public Object format(Event e) {
		Map<String, Object> objectMap = mapping.map(e);
		Maps.EntryTransformer transformer = new FormatEntryTransformer(objectMap);
		return Maps.transformEntries(format, transformer);
	}
}
