package fr.figarocms.flume.formatter.config;

import com.google.common.collect.Maps;

import com.cloudera.flume.core.Event;

import java.util.Map;

import fr.figarocms.flume.formatter.mapping.Mapping;

public class Formatter {

  private Mapping mapping;
  private Map format;

  public void setMapping(Mapping mapping) {
    this.mapping = mapping;
  }

  public void setFormat(Map format) {
    this.format = format;
  }

  public Map<String, Object> format(Event e) {
    if (mapping == null) {
      mapping = new Mapping();
    }

    Map<String, Object> objectMap = mapping.map(e);
    if (format == null) {
      return objectMap;
    }
    return Maps.transformValues(format, new FormatValueTransformer(objectMap));
  }
}
