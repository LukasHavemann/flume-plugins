package fr.figarocms.flume.filter;

import com.cloudera.flume.core.Event;
import com.google.common.base.Predicate;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.mockito.Mockito.*;

@RunWith(value = MockitoJUnitRunner.class)
public class EventFilterTest {

  private EventFilter filter;

  @Mock
  private Predicate<Event> predicate;
  @Mock
  private Event e;

  @Before
  public void setUp() throws Exception {
    filter = new EventFilter(null, predicate);
  }

  @Test
  public void nominal() throws Exception {
    // Given
    when(predicate.apply(e)).thenReturn(false);

    // When
    filter.append(e);

    // Then
    verify(predicate, only()).apply(e);
  }
}
