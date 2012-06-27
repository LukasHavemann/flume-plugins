package fr.figarocms.flume.filter.config;

import com.cloudera.flume.core.Event;
import com.cloudera.flume.core.EventImpl;
import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import fr.figarocms.flume.filter.predicate.EventPredicates;
import org.junit.Before;
import org.junit.Test;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.ConstructorException;
import org.yaml.snakeyaml.error.YAMLException;

import java.io.InputStream;

import static com.google.common.base.Predicates.*;
import static fr.figarocms.flume.filter.predicate.DateTimePredicates.matchesDateFormat;
import static org.junit.Assert.*;

public class ParsingIntegrationTest {


  private Yaml yaml;

  @Before
  public void setUp() throws Exception {
    yaml = new Yaml();
  }

  @Test
  public void hostWithoutPredicate() throws Exception {
    //Given
    String document = "host:";

    // When
    EventPredicateBuilder builder = yaml.loadAs(document, EventPredicateBuilder.class);

    // Then
    assertNotNull(builder);
    final Predicate<Event> predicate = builder.build();
    assertEquals(Predicates.alwaysTrue(), predicate);
  }

  @Test
  public void hostWithAValue() throws Exception {
    //Given
    String document = "host:\n  value: value1\n";

    // When
    EventPredicateBuilder builder = yaml.loadAs(document, EventPredicateBuilder.class);

    // Then
    assertNotNull(builder);
    final Predicate<Event> predicate = builder.build();
    assertEquals(EventPredicates.hasHost(equalTo("value1")), predicate);
  }

  @Test(expected = YAMLException.class)
  public void hostWithUnknownPredicate() throws Exception {
    //Given
    String document = "host:\n  unknown: predicate";

    // When
    EventPredicateBuilder builder = yaml.loadAs(document, EventPredicateBuilder.class);
  }


  @Test
  public void priorityWithoutPredicate() throws Exception {
    //Given
    String document = "priority:";

    // When
    EventPredicateBuilder builder = yaml.loadAs(document, EventPredicateBuilder.class);

    // Then
    assertNotNull(builder);
    final Predicate<Event> predicate = builder.build();
    assertEquals(Predicates.alwaysTrue(), predicate);
  }

  @Test
  public void priorityWithAValue() throws Exception {
    //Given
    String document = "priority:\n  value: value1\n";

    // When
    EventPredicateBuilder builder = yaml.loadAs(document, EventPredicateBuilder.class);

    // Then
    assertNotNull(builder);
    final Predicate<Event> predicate = builder.build();
    assertEquals(EventPredicates.hasPriority(equalTo("value1")), predicate);
  }

  @Test(expected = YAMLException.class)
  public void priorityWithUnknownPredicate() throws Exception {
    //Given
    String document = "priority:\n  unknown: predicate";

    // When
    EventPredicateBuilder builder = yaml.loadAs(document, EventPredicateBuilder.class);
  }

  @Test
  public void bodyWithoutPredicate() throws Exception {
    //Given
    String document = "body:";
    Yaml yaml = new Yaml();

    // When
    EventPredicateBuilder builder = yaml.loadAs(document, EventPredicateBuilder.class);

    // Then
    assertNotNull(builder);
    final Predicate<Event> predicate = builder.build();
    assertEquals(Predicates.alwaysTrue(), predicate);
  }

  @Test
  public void bodyWithAValue() throws Exception {
    //Given
    String document = "body:\n  value: value1\n";

    // When
    Object o = yaml.load(document);
    EventPredicateBuilder builder = yaml.loadAs(document, EventPredicateBuilder.class);

    // Then
    assertNotNull(builder);
    final Predicate<Event> predicate = builder.build();
    assertEquals(EventPredicates.hasBody(equalTo("value1")), predicate);
  }

  @Test(expected = YAMLException.class)
  public void bodyWithUnknownPredicate() throws Exception {
    //Given
    String document = "body:\n  unknown: predicate";

    // When
    EventPredicateBuilder builder = yaml.loadAs(document, EventPredicateBuilder.class);
  }


  @Test
  public void bodyAndPriority() throws Exception {
    //Given
    String document = "body:\n  value: body1\npriority:\n  value: priority1\n";

    // When
    EventPredicateBuilder builder = yaml.loadAs(document, EventPredicateBuilder.class);

    // Then
    assertNotNull(builder);
    final Predicate<Event> predicate = builder.build();
    final Predicate<Event> expected = and(
        EventPredicates.hasBody(equalTo("body1")),
        EventPredicates.hasPriority(equalTo("priority1"))
    );
    assertEquals(expected, predicate);
  }

  @Test(expected = ConstructorException.class)
  public void oneAttributeWithoutName() throws Exception {
    //Given
    String document = "attributes:\n-\n  value: value1\n";

    // When
    EventPredicateBuilder builder = yaml.loadAs(document, EventPredicateBuilder.class);

    // Then
  }

  @Test
  public void oneAttributeWithValue() throws Exception {
    //Given
    String document = "attributes:\n- name: attribute1\n  value: value1\n";

    // When
    EventPredicateBuilder builder = yaml.loadAs(document, EventPredicateBuilder.class);

    // Then
    assertNotNull(builder);
    final Predicate<Event> predicate = builder.build();
    assertEquals(EventPredicates.containsAttribute("attribute1", equalTo("value1")), predicate);
  }

  @Test
  public void twoAttributesWithValues() throws Exception {
    //Given
    String
        document =
        "attributes:\n- name: attribute1\n  values:\n    - value1\n    - value2\n- name: attribute2\n  value: value3\n";

    // When
    EventPredicateBuilder builder = yaml.loadAs(document, EventPredicateBuilder.class);

    // Then
    assertNotNull(builder);
    final Predicate<Event> predicate = builder.build();
    assertEquals(
        Predicates.and(
            EventPredicates.containsAttribute("attribute1", or(equalTo("value1"), equalTo("value2"))),
            EventPredicates.containsAttribute("attribute2", equalTo("value3"))
        ), predicate);
  }

  @Test
  public void oneAttributeWithDateFormat() throws Exception {
    //Given
    String document = "attributes:\n- name: attribute1\n  dateFormat: value1\n";

    // When
    EventPredicateBuilder builder = yaml.loadAs(document, EventPredicateBuilder.class);

    // Then
    assertNotNull(builder);
    final Predicate<Event> predicate = builder.build();
    assertEquals(EventPredicates.containsAttribute("attribute1", matchesDateFormat("value1")), predicate);
  }

  @Test
  public void oneAttributeWithDateFormats() throws Exception {
    //Given
    String document = "attributes:\n- name: attribute1\n  dateFormats:\n    - value1\n    - value2\n";

    // When
    EventPredicateBuilder builder = yaml.loadAs(document, EventPredicateBuilder.class);

    // Then
    assertNotNull(builder);
    final Predicate<Event> predicate = builder.build();
    assertEquals(EventPredicates.containsAttribute("attribute1", or(
        matchesDateFormat("value1"), matchesDateFormat("value2"))), predicate);
  }

  @Test
  public void completeYmlFile() throws Exception {
    //Given
    Event event = new TestingEventImpl("this is a test".getBytes(), 132L, Event.Priority.INFO, 159L, "server1");
    event.set("attribute1", "200".getBytes());
    event.set("attribute2", "2012-02-15".getBytes());
    event.set("attribute3", "pattern/example?parameter1=value1&parameter2=value2&parameter3=2012-02".getBytes());

    final InputStream stream = this.getClass().getResourceAsStream("/nominal.yml");

    // When
    EventPredicateBuilder builder = yaml.loadAs(stream, EventPredicateBuilder.class);

    // Then
    assertNotNull(builder);
    assertTrue(builder.build().apply(event)); // This event satisfied all predicates

    event.set("attribute1", "404".getBytes()); // Now change just one attribute value
    assertFalse(builder.build().apply(event));
  }


  /**
   * An Event implementation for Test.
   */
  private static class TestingEventImpl extends EventImpl {

    public TestingEventImpl(byte[] s, long timestamp, Priority pri, long nanoTime, String host) {
      super(s, timestamp, pri, nanoTime,
            host);
    }

    @Override
    public void set(String attr, byte[] v) {
      fields.put(attr, v);
    }
  }


}
