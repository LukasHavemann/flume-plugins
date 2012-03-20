package fr.figarocms.flume.filter.predicate;

import org.junit.Test;

import static com.google.common.collect.ImmutableMap.of;
import static fr.figarocms.flume.filter.predicate.URIFunctions.uriAsString;
import static java.net.URI.create;
import static org.junit.Assert.assertEquals;

public class URIFunctionsTest {

  @Test
  public void uriFromString() throws Exception {
    // When / Then
    assertEquals(create("/foo?bar=baz&foo=bar"), uriAsString().apply("/foo?bar=baz&foo=bar"));
  }

  @Test
  public void getParameters() throws Exception {
    // When / Then
    assertEquals(of("bar", "baz", "foo", "bar"), URIFunctions.getParameters().apply(create("/foo?bar=baz&foo=bar")));
  }

  @Test
  public void getValue() throws Exception {
    // When / Then
    assertEquals("baz", URIFunctions.getValue("bar").apply(create("/foo?bar=baz&foo=bar")));
  }

}
