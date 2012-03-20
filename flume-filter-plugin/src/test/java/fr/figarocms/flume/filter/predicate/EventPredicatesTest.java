package fr.figarocms.flume.filter.predicate;

import com.cloudera.flume.core.Event;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static com.google.common.base.Predicates.equalTo;
import static java.util.Collections.singletonMap;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

@RunWith(value = MockitoJUnitRunner.class)
public class EventPredicatesTest {

  @Mock
  private Event event;


  @Test
  public void hasHost() throws Exception {
    // Given
    when(event.getHost()).thenReturn("host");

    // When / Then
    assertTrue(EventPredicates.hasHost(equalTo("host")).apply(event));
    assertFalse(EventPredicates.hasHost(equalTo("not host")).apply(event));
  }

  @Test
  public void hasPriority() throws Exception {
    // Given
    when(event.getPriority()).thenReturn(Event.Priority.INFO);

    // When / Then
    assertTrue(EventPredicates.hasPriority(equalTo(Event.Priority.INFO.toString())).apply(event));
    assertFalse(EventPredicates.hasPriority(equalTo(Event.Priority.FATAL.toString())).apply(event));
  }


  @Test
  public void hasBody() throws Exception {
    // Given
    when(event.getBody()).thenReturn("body".getBytes());

    // When / Then
    assertTrue(EventPredicates.hasBody(equalTo("body")).apply(event));
    assertFalse(EventPredicates.hasBody(equalTo("not body")).apply(event));
  }

  @Test
  public void containsAttribute() throws Exception {
    // Given
    when(event.getAttrs()).thenReturn(singletonMap("attribute name", "attribute value".getBytes()));
    when(event.get("attribute name")).thenReturn("attribute value".getBytes());

    // When / Then
    assertTrue(EventPredicates.containsAttribute("attribute name", equalTo("attribute value")).apply(event));
    assertFalse(EventPredicates.containsAttribute("attribute name", equalTo("other attribute value")).apply(event));
  }

}
