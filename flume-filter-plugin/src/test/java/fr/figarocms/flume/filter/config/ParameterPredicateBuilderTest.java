package fr.figarocms.flume.filter.config;

import com.google.common.base.Predicate;
import com.google.common.collect.Lists;

import org.junit.Before;
import org.junit.Test;

import fr.figarocms.flume.filter.predicate.URIPredicates;

import static com.google.common.base.Predicates.and;
import static com.google.common.base.Predicates.containsPattern;
import static com.google.common.base.Predicates.equalTo;
import static com.google.common.base.Predicates.or;
import static fr.figarocms.flume.filter.predicate.DateTimePredicates.matchesDateFormat;
import static org.junit.Assert.assertEquals;

public class ParameterPredicateBuilderTest {

  private ParameterPredicateBuilder builder;

  @Before
  public void setUp() throws Exception {
    builder = new ParameterPredicateBuilder();
    builder.setName("parameter1");
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
    Predicate<String> predicate = builder.build();

    // Then
    assertEquals(URIPredicates.containsParameter("parameter1", equalTo("value")), predicate);
  }

  @Test
  public void setValues() throws Exception {
    // Given
    builder.setValues(Lists.newArrayList("value 1", "value 2"));

    // When
    Predicate<String> predicate = builder.build();

    // Then
    assertEquals(URIPredicates.containsParameter("parameter1", or(equalTo("value 1"), equalTo("value 2"))),
                 predicate);
  }


  @Test
  public void setValueAndValues() throws Exception {
    // Given
    builder.setValue("value");
    builder.setValues(Lists.newArrayList("value 1", "value 2"));

    // When
    Predicate<String> predicate = builder.build();

    // Then
    assertEquals(
        and(
            URIPredicates.containsParameter("parameter1", equalTo("value")),
            URIPredicates.containsParameter("parameter1", or(equalTo("value 1"), equalTo("value 2")))
        ), predicate);
  }

  @Test
  public void setPattern() throws Exception {
    // Given
    builder.setPattern("pattern");

    // When
    Predicate<String> predicate = builder.build();

    // Then
    assertEquals(URIPredicates.containsParameter("parameter1", containsPattern("pattern")), predicate);
  }

  @Test
  public void setPatterns() throws Exception {
    // Given
    builder.setPatterns(Lists.newArrayList("pattern 1", "pattern 2"));

    // When
    Predicate<String> predicate = builder.build();

    // Then
    assertEquals(
        URIPredicates.containsParameter("parameter1", or(containsPattern("pattern 1"), containsPattern("pattern 2"))),
        predicate);
  }

  @Test
  public void setDateFormat() throws Exception {
    // Given
    builder.setDateFormat("pattern");

    // When
    Predicate<String> predicate = builder.build();

    // Then
    assertEquals(URIPredicates.containsParameter("parameter1", matchesDateFormat("pattern")), predicate);
  }

  @Test
  public void setDateFormats() throws Exception {
    // Given
    builder.setDateFormats(Lists.newArrayList("pattern 1", "pattern 2"));

    // When
    Predicate<String> predicate = builder.build();

    // Then
    assertEquals(
        URIPredicates
            .containsParameter("parameter1", or(matchesDateFormat("pattern 1"), matchesDateFormat("pattern 2"))),
        predicate);
  }


  @Test
  public void setPatternAndPatterns() throws Exception {
    // Given
    builder.setPattern("pattern");
    builder.setPatterns(Lists.newArrayList("pattern 1", "pattern 2"));

    // When
    Predicate<String> predicate = builder.build();

    // Then
    assertEquals(
        and(
            URIPredicates.containsParameter("parameter1", containsPattern("pattern")),
            URIPredicates
                .containsParameter("parameter1", or(containsPattern("pattern 1"), containsPattern("pattern 2")))
        ), predicate);

  }

  @Test
  public void setPatternAndPatternsAndValue() throws Exception {
    // Given
    builder.setPattern("pattern");
    builder.setValue("value");
    builder.setPatterns(Lists.newArrayList("pattern 1", "pattern 2"));

    // When
    Predicate<String> predicate = builder.build();

    // Then
    assertEquals(
        and(
            URIPredicates.containsParameter("parameter1", containsPattern("pattern")),
            URIPredicates.containsParameter("parameter1", equalTo("value")),
            URIPredicates
                .containsParameter("parameter1", or(containsPattern("pattern 1"), containsPattern("pattern 2")))
        ), predicate);
  }
}
