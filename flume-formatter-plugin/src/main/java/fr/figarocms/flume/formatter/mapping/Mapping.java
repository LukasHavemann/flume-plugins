package fr.figarocms.flume.formatter.mapping;

import com.google.common.base.Function;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;

import com.cloudera.flume.core.Event;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.google.common.collect.Maps.newHashMap;
import static fr.figarocms.flume.formatter.mapping.converter.Converters.convert;

public class Mapping {

  private List<AttributeMapping> attributes;

  public void setAttributes(List<AttributeMapping> attributes) {
    this.attributes = attributes;
  }

  // ~ Methods --------------------------------------------------------------------------------------------------------

  public Map<String, Object> map(Event e) {
    Map<String, Object> map = newHashMap();
    map.put("body", convert(e.getBody(), "string"));
    map.put("timestamp", e.getTimestamp());
    map.put("host", e.getHost());
    map.put("priority", e.getPriority());
    map.put("nanos", e.getNanos());

    // Map attributes
    map.putAll(mapAttributes(e, attributes));
    return map;
  }

  protected Map<String, Object> mapAttributes(Event e, List<AttributeMapping> attributes) {
    ImmutableMap<String, AttributeMapping> mappedAttributes = ImmutableMap.of();
    Map<String, Object> map = newHashMap();

    // Create an index base on attribute name
    if (attributes != null) {
      mappedAttributes = Maps.uniqueIndex(attributes, new Function<AttributeMapping, String>() {
        @Override
        public String apply(AttributeMapping mapping) {
          return mapping.getName();
        }
      });
    }

    // Convert attributes     
    Map<String, byte[]> attrs = e.getAttrs();
    for (Map.Entry<String, byte[]> s : attrs.entrySet()) {
      if (mappedAttributes.containsKey(s.getKey())) {
        AttributeMapping mapping = mappedAttributes.get(s.getKey());
        // Convert attribute
        map.put(s.getKey(), convert(s.getValue(), mapping.getType()));
      } else {
        // By default attribute are converted to string
        map.put(s.getKey(), convert(s.getValue(), "string"));
      }
    }
    return map;
  }

  // Required by Yaml Parser
  public List<AttributeMapping> getAttributes() {
    return attributes;
  }
}
