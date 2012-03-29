package fr.figarocms.flume.uriparams;

import com.cloudera.flume.core.Event;
import com.cloudera.flume.core.EventImpl;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.slf4j.Logger;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.verify;

@RunWith(value = MockitoJUnitRunner.class)
public class URIParamsExtractorTest {

  private URIParamsExtractor extractor;
  private Event event;

  @Mock
  private Logger LOG;

  @Before
  public void setUp() throws Exception {
    event = new EventImpl("test".getBytes());

  }

  @Test
  public void nominal() throws Exception {
    // Given
    extractor = new URIParamsExtractor(null, "uri", "param");
    event.set("uri", "/uri?param1=foo&param2=bar".getBytes());

    // When
    extractor.append(event);

    // Then
    assertEquals(new String(event.get("param.param1")), "foo");
    assertEquals(new String(event.get("param.param2")), "bar");
  }


  @Test
  public void withoutPrefix() throws Exception {
    // Given
    extractor = new URIParamsExtractor(null, "uri", null);
    event.set("uri", "/uri?param1=foo&param2=bar".getBytes());

    // When
    extractor.append(event);

    // Then
    assertEquals(new String(event.get("uri.param1")), "foo");
    assertEquals(new String(event.get("uri.param2")), "bar");
  }

  @Test
  public void withoutParams() throws Exception {
    // Given
    extractor = new URIParamsExtractor(null, "uri", null);
    extractor.LOG = LOG;
    event.set("uri", "/uri".getBytes());

    // When
    extractor.append(event);

    // Then
    verify(LOG).warn("Attribute 'uri' is an URI without parameters");
  }

  @Test
  public void withoutParameterValue() throws Exception {
    // Given
    extractor = new URIParamsExtractor(null, "uri", null);
    event.set("uri", "/uri?param1".getBytes());

    // When
    extractor.append(event);

    // Then
    assertEquals(event.get("uri.param1"), null);
  }


  @Test
  public void notAnURI() throws Exception {
    // Given
    extractor = new URIParamsExtractor(null, "uri", null);
    extractor.LOG = LOG;
    event.set("uri", "not an uri".getBytes());

    // When
    extractor.append(event);

    // Then
    verify(LOG).warn("Unable to parse attribute 'uri' as an URI");
  }

  @Test
  public void argumentNotFound() throws Exception {
    // Given
    extractor = new URIParamsExtractor(null, "uri", null);
    extractor.LOG = LOG;
    event.set("test", "/uri?param1=foo&param2=bar".getBytes());

    // When
    extractor.append(event);

    // Then
    verify(LOG).warn("Attribute 'uri' not found in event");
  }
}
