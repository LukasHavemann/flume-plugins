package fr.figarocms.flume.filter.config;

import com.google.common.base.Predicate;

import com.cloudera.flume.core.Event;

import java.util.ArrayList;
import java.util.List;

import static com.google.common.base.Predicates.and;
import static com.google.common.base.Predicates.containsPattern;
import static com.google.common.base.Predicates.equalTo;
import static com.google.common.base.Predicates.or;
import static com.google.common.collect.Lists.newArrayList;
import static fr.figarocms.flume.filter.predicate.EventPredicates.hasHost;

public class HostPredicateBuilder {

  private List<Predicate<Event>> predicates;

  public HostPredicateBuilder() {
    this.predicates = newArrayList();
  }

  // ~ Setters ---------------------------------------------------------------------------------------------------------

  public void setValue(String value) {
    this.predicates.add(hasHost(equalTo(value)));
  }

  public void setPattern(String pattern) {
    this.predicates.add(hasHost(containsPattern(pattern)));
  }

  public void setValues(List<String> values) {
    List<Predicate<String>> predicates = new ArrayList<Predicate<String>>();
    if (values != null && !values.isEmpty()) {
      for (String s : values) {
        predicates.add(equalTo(s));
      }
      this.predicates.add(hasHost(or(predicates)));
    }
  }

  public void setPatterns(List<String> patterns) {
    List<Predicate<? super String>> predicates = new ArrayList<Predicate<? super String>>();
    if (patterns != null && !patterns.isEmpty()) {
      for (String s : patterns) {
        predicates.add(containsPattern(s));
      }
      this.predicates.add(hasHost(or(predicates)));
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
