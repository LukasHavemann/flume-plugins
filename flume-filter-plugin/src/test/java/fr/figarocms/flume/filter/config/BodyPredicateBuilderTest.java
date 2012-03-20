package fr.figarocms.flume.filter.config;

import com.google.common.base.Predicate;
import com.google.common.collect.Lists;

import com.cloudera.flume.core.Event;

import org.junit.Before;
import org.junit.Test;

import fr.figarocms.flume.filter.predicate.EventPredicates;

import static com.google.common.base.Predicates.and;
import static com.google.common.base.Predicates.containsPattern;
import static com.google.common.base.Predicates.equalTo;
import static com.google.common.base.Predicates.or;
import static org.junit.Assert.assertEquals;

public class BodyPredicateBuilderTest {

  private BodyPredicateBuilder builder;

  @Before
  public void setUp() throws Exception {
    builder = new BodyPredicateBuilder();
  }

  @Test
  public void setValue() throws Exception {
    // Given
    builder.setValue("value");

    // When
    Predicate<Event> predicate = builder.build();

    // Then
    assertEquals(EventPredicates.hasBody(equalTo("value")), predicate);
  }

  @Test
  public void setValues() throws Exception {
    // Given
    builder.setValues(Lists.newArrayList("value 1", "value 2"));

    // When
    Predicate<Event> predicate = builder.build();

    // Then
    assertEquals(EventPredicates.hasBody(or(equalTo("value 1"), equalTo("value 2"))), predicate);
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
            EventPredicates.hasBody(equalTo("value")),
            EventPredicates.hasBody(or(equalTo("value 1"), equalTo("value 2")))
        ), predicate);
  }

  @Test
  public void setPattern() throws Exception {
    // Given
    builder.setPattern("pattern");

    // When
    Predicate<Event> predicate = builder.build();

    // Then
    assertEquals(EventPredicates.hasBody(containsPattern("pattern")), predicate);
  }

  @Test
  public void setPatterns() throws Exception {
    // Given
    builder.setPatterns(Lists.newArrayList("pattern 1", "pattern 2"));

    // When
    Predicate<Event> predicate = builder.build();

    // Then
    assertEquals(EventPredicates.hasBody(or(containsPattern("pattern 1"), containsPattern("pattern 2"))), predicate);
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
            EventPredicates.hasBody(containsPattern("pattern")),
            EventPredicates.hasBody(or(containsPattern("pattern 1"), containsPattern("pattern 2")))
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
            EventPredicates.hasBody(containsPattern("pattern")),
            EventPredicates.hasBody(equalTo("value")),
            EventPredicates.hasBody(or(containsPattern("pattern 1"), containsPattern("pattern 2")))
        ), predicate);
  }


}
