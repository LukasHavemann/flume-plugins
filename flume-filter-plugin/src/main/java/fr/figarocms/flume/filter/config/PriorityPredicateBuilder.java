package fr.figarocms.flume.filter.config;

import com.cloudera.flume.core.Event;
import com.google.common.base.Predicate;

import java.util.ArrayList;
import java.util.List;

import static com.google.common.base.Predicates.*;
import static com.google.common.collect.Lists.newArrayList;
import static fr.figarocms.flume.filter.predicate.EventPredicates.hasPriority;

public class PriorityPredicateBuilder {

  private List<Predicate<Event>> predicates;

  public PriorityPredicateBuilder() {
    this.predicates = newArrayList();
  }

  // ~ Setters ---------------------------------------------------------------------------------------------------------

  public void setValue(String value) {
    this.predicates.add(hasPriority(equalTo(value)));
  }

  public void setPattern(String pattern) {
    this.predicates.add(hasPriority(containsPattern(pattern)));
  }

  public void setValues(List<String> values) {
    List<Predicate<String>> predicates = new ArrayList<Predicate<String>>();
    if (values != null && !values.isEmpty()) {
      for (String s : values) {
        predicates.add(equalTo(s));
      }
      this.predicates.add(hasPriority(or(predicates)));
    }
  }

  public void setPatterns(List<String> patterns) {
    List<Predicate<? super String>> predicates = new ArrayList<Predicate<? super String>>();
    if (patterns != null && !patterns.isEmpty()) {
      for (String s : patterns) {
        predicates.add(containsPattern(s));
      }
      this.predicates.add(hasPriority(or(predicates)));
    }
  }

  // ~ Build Predicate -------------------------------------------------------------------------------------------------

  public Predicate<Event> build() {
    if (this.predicates.size() == 1) {
      return predicates.get(0);
    }
    return and(this.predicates);
  }
}
