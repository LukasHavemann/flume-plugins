package fr.figarocms.flume.filter.config;

import com.cloudera.flume.core.Event;
import com.google.common.base.Predicate;
import com.google.common.collect.Lists;
import fr.figarocms.flume.filter.predicate.EventPredicates;
import org.junit.Before;
import org.junit.Test;

import static com.google.common.base.Predicates.*;
import static fr.figarocms.flume.filter.predicate.DateTimePredicates.matchesDateFormat;
import static org.junit.Assert.assertEquals;

public class AttributePredicateBuilderTest {

  private AttributePredicateBuilder builder;

  @Before
  public void setUp() throws Exception {
    builder = new AttributePredicateBuilder();
    builder.setName("attribute1");
  }

  @Test(expected = IllegalStateException.class)
  public void nameMustNotBeNull() throws Exception {
    // When
    builder.setName(null);
  }

  @Test(expected = IllegalStateException.class)
  public void nameMustNotBeEmpty() throws Exception {
    // When
    builder.setName("   ");
  }

  @Test
  public void setValue() throws Exception {
    // Given
    builder.setValue("value");

    // When
    Predicate<Event> predicate = builder.build();

    // Then
    assertEquals(EventPredicates.containsAttribute("attribute1", equalTo("value")), predicate);
  }

  @Test
  public void setValues() throws Exception {
    // Given
    builder.setValues(Lists.newArrayList("value 1", "value 2"));

    // When
    Predicate<Event> predicate = builder.build();

    // Then
    assertEquals(EventPredicates.containsAttribute("attribute1", or(equalTo("value 1"), equalTo("value 2"))),
                 predicate);
  }


  @Test
  public void setValueAndValues() throws Exception {
    // Given
    builder.setValue("value");
    builder.setValues(Lists.newArrayList("value 1", "value 2"));

    // When
    Predicate<Event> predicate = builder.build();

    // Then
    assertEquals(
        and(
            EventPredicates.containsAttribute("attribute1", equalTo("value")),
            EventPredicates.containsAttribute("attribute1", or(equalTo("value 1"), equalTo("value 2")))
        ), predicate);
  }

  @Test
  public void setPattern() throws Exception {
    // Given
    builder.setPattern("pattern");

    // When
    Predicate<Event> predicate = builder.build();

    // Then
    assertEquals(EventPredicates.containsAttribute("attribute1", containsPattern("pattern")), predicate);
  }

  @Test
  public void setPatterns() throws Exception {
    // Given
    builder.setPatterns(Lists.newArrayList("pattern 1", "pattern 2"));

    // When
    Predicate<Event> predicate = builder.build();

    // Then
    assertEquals(
        EventPredicates.containsAttribute("attribute1", or(containsPattern("pattern 1"), containsPattern("pattern 2"))),
        predicate);
  }

  @Test
  public void setDateFormat() throws Exception {
    // Given
    builder.setDateFormat("pattern");

    // When
    Predicate<Event> predicate = builder.build();

    // Then
    assertEquals(EventPredicates.containsAttribute("attribute1", matchesDateFormat("pattern")), predicate);
  }

  @Test
  public void setDateFormats() throws Exception {
    // Given
    builder.setDateFormats(Lists.newArrayList("pattern 1", "pattern 2"));

    // When
    Predicate<Event> predicate = builder.build();

    // Then
    assertEquals(
        EventPredicates.containsAttribute("attribute1", or(
            matchesDateFormat("pattern 1"), matchesDateFormat("pattern 2"))),
        predicate);
  }


  @Test
  public void setPatternAndPatterns() throws Exception {
    // Given
    builder.setPattern("pattern");
    builder.setPatterns(Lists.newArrayList("pattern 1", "pattern 2"));

    // When
    Predicate<Event> predicate = builder.build();

    // Then
    assertEquals(
        and(
            EventPredicates.containsAttribute("attribute1", containsPattern("pattern")),
            EventPredicates
                .containsAttribute("attribute1", or(containsPattern("pattern 1"), containsPattern("pattern 2")))
        ), predicate);

  }

  @Test
  public void setPatternAndPatternsAndValue() throws Exception {
    // Given
    builder.setPattern("pattern");
    builder.setValue("value");
    builder.setPatterns(Lists.newArrayList("pattern 1", "pattern 2"));

    // When
    Predicate<Event> predicate = builder.build();

    // Then
    assertEquals(
        and(
            EventPredicates.containsAttribute("attribute1", containsPattern("pattern")),
            EventPredicates.containsAttribute("attribute1", equalTo("value")),
            EventPredicates
                .containsAttribute("attribute1", or(containsPattern("pattern 1"), containsPattern("pattern 2")))
        ), predicate);
  }

}
