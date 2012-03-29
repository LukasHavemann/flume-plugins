package fr.figarocms.flume.formatter.config;

import org.apache.hadoop.thirdparty.guava.common.collect.Maps;
import org.junit.Test;

import java.util.Map;

import static org.junit.Assert.assertEquals;

public class FormatValueTransformerTest {

  private FormatValueTransformer transformer;

  @Test
  public void transform() throws Exception {
    // Given
    final Float expected = new Float("1.223232");
    Map<String, Object> objectMap = Maps.newHashMap();
    objectMap.put("test", expected);
    transformer = new FormatValueTransformer(objectMap);

    // When
    Float o = (Float) transformer.apply("%{test}");

    // Then
    assertEquals(expected, o);
  }

  @Test
  public void nullValue() throws Exception {
    // Given
    Map<String, Object> objectMap = Maps.newHashMap();
    objectMap.put("test", null);
    transformer = new FormatValueTransformer(objectMap);

    // When
    Float o = (Float) transformer.apply("%{test}");

    // Then
    assertEquals(null, o);
  }

}
