package fr.figarocms.flume.formatter;

import com.cloudera.flume.handlers.text.FormatFactory;
import com.cloudera.flume.handlers.text.output.OutputFormat;


import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertNotNull;

public class JsonObjectFormatterTest {

  private FormatFactory.OutputFormatBuilder builder;

  @Before
  public void setUp() throws Exception {
    builder = JsonObjectFormatter.builder();
  }

  @Test
  public void buildWithoutArgs() throws Exception {
    // When
    OutputFormat format = builder.build();

    // Then
    assertNotNull(format);
  }

  @Test
  public void buildWithOneArg() throws Exception {
    // When
    OutputFormat format = builder.build("/formatter.yml");
    // Then
    assertNotNull(format);
  }

  @Test(expected = IllegalArgumentException.class)
  public void buildWithMoreThanOneArgs() throws Exception {
    // When
    OutputFormat format = builder.build("/formatter.yml", "other", "args");

    // Then IllegalArgumentException is thrown

  }
}
