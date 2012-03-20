package fr.figarocms.flume.filter.predicate;

import com.google.common.base.Function;

import com.cloudera.flume.core.Event;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;

@RunWith(value = MockitoJUnitRunner.class)
public class EventFunctionsTest {

  private static final String ATTRIBUTE_VALUE = "attribute value";
  private static final String ATTRIBUTE_NAME = "attribute name";

  @Mock
  private Event event;

  @Test
  public void getExistingAttribute() throws Exception {
    // Given
    when(event.get(ATTRIBUTE_NAME)).thenReturn(ATTRIBUTE_VALUE.getBytes());

    final Function<Event, String> function = EventFunctions.getAttribute(ATTRIBUTE_NAME);

    // When / Then
    assertEquals(ATTRIBUTE_VALUE, function.apply(event));
  }

  @Test
  public void getNotExistingAttribute() throws Exception {
    // Given
    final Function<Event, String> function = EventFunctions.getAttribute(ATTRIBUTE_NAME);

    // When / Then
    assertNull("attribute does not exists", function.apply(event));
  }


  @Test
  public void getNotExistingAttributeValue() throws Exception {
    // Given
    when(event.get(ATTRIBUTE_NAME)).thenReturn("other attribute value".getBytes());
    final Function<Event, String> function = EventFunctions.getAttribute(ATTRIBUTE_NAME);

    // When / Then
    assertThat(function.apply(event), not(equalTo(ATTRIBUTE_VALUE)));
  }
}
