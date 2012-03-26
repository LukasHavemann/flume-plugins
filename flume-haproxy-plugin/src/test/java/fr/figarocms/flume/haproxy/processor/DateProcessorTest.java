package fr.figarocms.flume.haproxy.processor;

import com.cloudera.flume.core.Event;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import static org.mockito.Mockito.verify;
import static org.mockito.MockitoAnnotations.initMocks;

public class DateProcessorTest {

  DateProcessor processor;

  @Mock
  Event e;


  @Before
  public void setUp() throws Exception {
    processor = new DateProcessor();
    initMocks(this);
  }

  @Test
  public void testProcess() throws Exception {
    processor.doProcess(e, "test", "01/Jul/2011:17:22:59.083");
    DateTime dt = new DateTime("2011-07-01T17:22:59.083").withZone(DateTimeZone.getDefault());
    verify(e).set("haproxy.test", dt.toString().getBytes());
  }
}
