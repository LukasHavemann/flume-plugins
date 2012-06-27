package fr.figarocms.flume.haproxy.processor;

import com.cloudera.flume.core.Event;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import static org.mockito.Mockito.verify;
import static org.mockito.MockitoAnnotations.initMocks;

public class HeaderProcessorTest {

  HeaderProcessor processor;

  @Mock
  Event e;


  @Before
  public void setUp() throws Exception {
    processor = new HeaderProcessor();
    initMocks(this);
  }

  @Test
  public void testProcess() throws Exception {
    processor.doProcess(e, "test", "blah|plo p|pi f");
    verify(e).set("haproxy.test.0", "blah".getBytes());
    verify(e).set("haproxy.test.1", "plo p".getBytes());
    verify(e).set("haproxy.test.2", "pi f".getBytes());
  }
}
