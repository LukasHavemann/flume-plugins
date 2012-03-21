package fr.figarocms.flume.formatter;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class FormatterTest {

  private Formatter formatter;

  
  @Test(expected = IllegalArgumentException.class)
  public void noFormatSpecified() throws Exception {
    // When
    formatter = new Formatter(null, null);
  }

  @Test(expected = IllegalArgumentException.class)
  public void wrongFormatSpecified() throws Exception {
    // When
    formatter = new Formatter("foo", null);
  }

  @Test(expected = IllegalArgumentException.class)
  public void noMappingFileSpecified() throws Exception {
    // When
    formatter = new Formatter("json", null);
  }

  @Test
  public void classpathMappingFile() throws Exception {
    // When
    formatter = new Formatter("json", "/formatter.yml");

    // Then
    assertNotNull(formatter);
    assertTrue(formatter.getMappingFile().exists());
  }

  @Test
  public void absoluteMappingFile() throws Exception {
    // When
    String file = getClass().getResource("/formatter.yml").getFile();

    formatter = new Formatter("json", file);

    // Then
    assertNotNull(formatter);
    assertTrue(formatter.getMappingFile().exists());
  }

  @Test(expected = IllegalArgumentException.class)
  public void nonExistingMappingFile() throws Exception {
    // When
    formatter = new Formatter("json", "/not_existing.yml");
  }

  @Test(expected = IllegalArgumentException.class)
  public void nonYmlMappingFile() throws Exception {
    // When
    formatter = new Formatter("json", "/not_yml.yml");
  }

}
