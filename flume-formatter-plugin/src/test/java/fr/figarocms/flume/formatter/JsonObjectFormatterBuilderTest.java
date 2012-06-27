package fr.figarocms.flume.formatter;

import com.cloudera.flume.handlers.text.output.OutputFormat;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertNotNull;

public class JsonObjectFormatterBuilderTest {

  private JsonObjectFormatterBuilder builder;

  @Before
  public void setUp() throws Exception {
    builder = new JsonObjectFormatterBuilder();
  }

  @Test
  public void buildWithoutArgs() throws Exception {
    // When
    OutputFormat format = builder.build();

    // Then
    assertNotNull(format);
  }

  @Test
  public void buildWithAClasspathFile() throws Exception {
    // When
    OutputFormat format = builder.build("formatter.yml");
    // Then
    assertNotNull(format);
  }

  @Test
  public void buildWithAnAbsoluteFile() throws Exception {
    // When
    ClassLoader classLoader = ClassLoader.getSystemClassLoader();
    String file = classLoader.getResource("formatter.yml").getFile();
    OutputFormat format = builder.build(file);
    // Then
    assertNotNull(format);
  }

  @Test(expected = IllegalArgumentException.class)
  public void buildWithANonExistingFile() throws Exception {
    // When
    OutputFormat format = builder.build("not_existing.yml");

    // Then
    assertNotNull(format);
  }

  @Test(expected = IllegalArgumentException.class)
  public void buildWithANonYmlFile() throws Exception {
    // When
    OutputFormat format = builder.build("not_yml.yml");

    // Then
    assertNotNull(format);
  }


  @Test(expected = IllegalArgumentException.class)
  public void buildWithMoreThanOneArgs() throws Exception {
    // When
    OutputFormat format = builder.build("formatter.yml", "other", "args");

    // Then IllegalArgumentException is thrown
  }

}
