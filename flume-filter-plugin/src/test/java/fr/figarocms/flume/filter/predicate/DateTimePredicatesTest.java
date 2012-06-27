package fr.figarocms.flume.filter.predicate;

import com.google.common.base.Predicate;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class DateTimePredicatesTest {

  @Test
  public void matchesDateFormat() throws Exception {
    // When
    Predicate<String> predicate = DateTimePredicates.matchesDateFormat("YYYY-MM-dd");

    // Then
    assertTrue(predicate.apply("2012-01-02"));
    assertFalse(predicate.apply("02-01-2012"));
  }
}
