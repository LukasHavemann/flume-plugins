package fr.figarocms.flume.filter.config;

import com.google.common.base.Predicate;

import com.cloudera.flume.core.Event;

import java.util.List;

import static com.google.common.base.Predicates.alwaysTrue;
import static com.google.common.base.Predicates.and;
import static com.google.common.collect.Lists.newArrayList;

public class EventPredicateBuilder {

  private List<Predicate<Event>> predicates;
//  private List<AttributePredicateBuilder> attributes;


  public EventPredicateBuilder() {
    this.predicates = newArrayList();
  }

  // ~ Setters ---------------------------------------------------------------------------------------------------------

  public void setHost(HostPredicateBuilder builder) {
    if (builder != null) {
      this.predicates.add(builder.build());
    }
  }

  public void setPriority(PriorityPredicateBuilder builder) {
    if (builder != null) {
      this.predicates.add(builder.build());
    }
  }

  public void setBody(BodyPredicateBuilder builder) {
    if (builder != null) {
      this.predicates.add(builder.build());
    }
  }

  public void setAttributes(List<AttributePredicateBuilder> builders) {
    if (builders != null && !builders.isEmpty()) {
      for (AttributePredicateBuilder builder : builders) {
        this.predicates.add(builder.build());
      }
    }
  }

  // ~ Build Predicate -------------------------------------------------------------------------------------------------

  public Predicate<Event> build() {
    if (this.predicates.isEmpty()) {
      return alwaysTrue(); // No filter
    }
    if (this.predicates.size() == 1) {
      return predicates.get(0);
    }
    return and(this.predicates); // All predicates must be satisfied
  }

  // Required by YAML Parser
  public List<AttributePredicateBuilder> getAttributes() {
    throw new RuntimeException("This getter is just defined for YAML Parser");
  }
}
