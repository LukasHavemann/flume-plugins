package fr.figarocms.flume.formatter;

import org.junit.Test;

import java.io.IOException;
import java.io.OutputStream;

import static org.junit.Assert.assertNotNull;

public class ObjectFormatterTest {

  private ObjectFormatter formatter;

  @Test(expected = IllegalArgumentException.class)
  public void noFileSpecified() throws Exception {
    // When
    formatter = new ForTestFormatter(null);
  }

  @Test
  public void classpathFile() throws Exception {
    // When
    formatter = new ForTestFormatter("/formatter.yml");

    // Then
    assertNotNull(formatter);
  }

  @Test
  public void absoluteFile() throws Exception {
    // When
    String file = getClass().getResource("/formatter.yml").getFile();

    formatter = new ForTestFormatter(file);

    // Then
    assertNotNull(formatter);
  }

  @Test(expected = IllegalArgumentException.class)
  public void nonExistingFile() throws Exception {
    // When
    formatter = new ForTestFormatter("/not_existing.yml");
  }

  @Test(expected = IllegalArgumentException.class)
  public void nonYmlFile() throws Exception {
    // When
    formatter = new ForTestFormatter("/not_yml.yml");
  }


  private class ForTestFormatter extends ObjectFormatter {

    private ForTestFormatter(String filename) {
      super(filename);
    }

    @Override
    public void format(OutputStream o, Object obj) throws IOException {
      // Do nothing
    }
  }
}
