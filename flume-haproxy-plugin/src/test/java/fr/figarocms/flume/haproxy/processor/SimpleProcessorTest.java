package fr.figarocms.flume.haproxy.processor;

import com.cloudera.flume.core.Event;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import static org.mockito.Mockito.verify;
import static org.mockito.MockitoAnnotations.initMocks;

public class SimpleProcessorTest {

  SimpleProcessor processor;

  @Mock
  Event e;


  @Before
  public void setUp() throws Exception {
    processor = new SimpleProcessor();
    initMocks(this);
  }

  @Test
  public void testProcess() throws Exception {
    processor.doProcess(e, "test", "plop paf");
    verify(e).set("haproxy.test", "plop paf".getBytes());
  }
}
