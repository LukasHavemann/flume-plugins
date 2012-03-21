package fr.figarocms.flume.formatter;

import org.junit.Before;
import org.junit.Test;

public class FormatterTest {

  Formatter formatter;

  @Before
  public void setUp() throws Exception {
    formatter = new Formatter();
  }

  @Test(expected = IllegalArgumentException.class)
  public void noFormatSpecified() throws Exception {
    // Given
    formatter = new Formatter(null, null);
  }

  @Test(expected = IllegalArgumentException.class)
  public void wrongFormatSpecified() throws Exception {
    // Given
    formatter = new Formatter("foo", null);
  }
}
