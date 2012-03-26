package fr.figarocms.flume.filter;

import com.cloudera.flume.conf.Context;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;

@RunWith(MockitoJUnitRunner.class)
public class EventFilterBuilderTest {

  @Mock
  private Context context;
  private EventFilterBuilder builder;

  @Before
  public void setUp() throws Exception {
    builder = new EventFilterBuilder();
  }

  @Test(expected = IllegalArgumentException.class)
  public void emptyYmlFile() {
    builder.build(context, "empty_file.yml");
  }

  @Test
  public void nominalClasspathFile() {
    EventFilter filter = builder.build(mock(Context.class), "nominal.yml");
    assertNotNull(filter);
  }

  @Test
  public void nominalAbsoluteFile() {
	ClassLoader classLoader = ClassLoader.getSystemClassLoader();
	String file = classLoader.getResource("nominal.yml").getFile();
    EventFilter filter = builder.build(mock(Context.class), file);
    assertNotNull(filter);
  }

  @Test(expected = IllegalArgumentException.class)
  public void oneArgumentIsRequired() {
    builder.build(mock(Context.class));
  }

  @Test(expected = IllegalArgumentException.class)
  public void firstArgumentMustBeAnExistingFile() {
    builder.build(mock(Context.class), "unknown.yml");
  }

  @Test(expected = IllegalArgumentException.class)
  public void firstArgumentMustBeAValidYmlFile() {
    builder.build(mock(Context.class), "not_yml.yml");
  }

}
