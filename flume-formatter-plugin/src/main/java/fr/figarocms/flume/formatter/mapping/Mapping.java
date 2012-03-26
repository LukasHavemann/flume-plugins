package fr.figarocms.flume.formatter.mapping;

import com.cloudera.flume.core.Event;

import org.apache.hadoop.thirdparty.guava.common.base.Function;
import org.apache.hadoop.thirdparty.guava.common.collect.ImmutableMap;
import org.apache.hadoop.thirdparty.guava.common.collect.Maps;

import java.util.List;
import java.util.Map;

import javax.annotation.Nullable;

import static fr.figarocms.flume.formatter.mapping.converter.Converters.convert;
import static org.apache.hadoop.thirdparty.guava.common.collect.Maps.newHashMap;

public class Mapping {

  private List<AttributeMapping> attributes;

  public void setAttributes(List<AttributeMapping> attributes) {
    this.attributes = attributes;
  }

  // TODO add test
  public Map<String, Object> map(Event e) {
    ImmutableMap<String, AttributeMapping>
        mappedAttributes =
        Maps.uniqueIndex(attributes, new Function<AttributeMapping, String>() {
          @Override
          public String apply(@Nullable AttributeMapping mapping) {
            return mapping.getName();
          }
        });
    Map<String, Object> map = newHashMap();
    Map<String, byte[]> attrs = e.getAttrs();
    for (Map.Entry<String, byte[]> s : attrs.entrySet()) {
      if (mappedAttributes.containsKey(s)) {
        AttributeMapping mapping = mappedAttributes.get(s);
        map.put(s.getKey(), convert(s.getValue(), mapping.getType(), mapping.getFormat()));
      }
      map.put(s.getKey(), convert(s.getValue(), "string", null));
    }
    return map;
  }
}
