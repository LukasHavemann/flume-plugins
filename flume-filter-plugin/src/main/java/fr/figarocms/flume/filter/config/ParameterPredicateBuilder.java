package fr.figarocms.flume.filter.config;

import com.google.common.base.Preconditions;
import com.google.common.base.Predicate;

import java.util.ArrayList;
import java.util.List;

import fr.figarocms.flume.filter.predicate.URIPredicates;

import static com.google.common.base.Predicates.and;
import static com.google.common.base.Predicates.containsPattern;
import static com.google.common.base.Predicates.equalTo;
import static com.google.common.base.Predicates.or;
import static com.google.common.base.Strings.isNullOrEmpty;
import static com.google.common.base.Strings.nullToEmpty;
import static com.google.common.collect.Lists.newArrayList;
import static fr.figarocms.flume.filter.predicate.DateTimePredicates.matchesDateFormat;

public class ParameterPredicateBuilder {

  private String name;
  private List<Predicate<String>> predicates;

  public ParameterPredicateBuilder() {
    this.predicates = newArrayList();
  }

  // ~ Setters ---------------------------------------------------------------------------------------------------------

  public void setName(String name) {
    Preconditions.checkState(!isNullOrEmpty(nullToEmpty(name).trim()), "A parameter name is required");
    this.name = name;
  }

  public void setValue(String value) {
    Preconditions.checkState(!isNullOrEmpty(nullToEmpty(name).trim()), "A parameter name is required");
    this.predicates.add(URIPredicates.containsParameter(name, equalTo(value)));
  }

  public void setPattern(String pattern) {
    Preconditions.checkState(!isNullOrEmpty(nullToEmpty(name).trim()), "A parameter name is required");
    this.predicates.add(URIPredicates.containsParameter(name, containsPattern(pattern)));
  }

  public void setValues(List<String> values) {
    Preconditions.checkState(!isNullOrEmpty(nullToEmpty(name).trim()), "A parameter name is required");
    List<Predicate<String>> predicates = new ArrayList<Predicate<String>>();
    if (values != null && !values.isEmpty()) {
      for (String s : values) {
        predicates.add(equalTo(s));
      }
      this.predicates.add(URIPredicates.containsParameter(name, or(predicates)));
    }
  }

  public void setPatterns(List<String> patterns) {
    Preconditions.checkState(!isNullOrEmpty(nullToEmpty(name).trim()), "A parameter name is required");
    List<Predicate<? super String>> predicates = new ArrayList<Predicate<? super String>>();
    if (patterns != null && !patterns.isEmpty()) {
      for (String s : patterns) {
        predicates.add(containsPattern(s));
      }
      this.predicates.add(URIPredicates.containsParameter(name, or(predicates)));
    }
  }

  public void setDateFormat(String pattern) {
    Preconditions.checkState(!isNullOrEmpty(nullToEmpty(name).trim()), "A parameter name is required");
    this.predicates.add(URIPredicates.containsParameter(name, matchesDateFormat(pattern)));
  }

  public void setDateFormats(List<String> patterns) {
    Preconditions.checkState(!isNullOrEmpty(nullToEmpty(name).trim()), "A parameter name is required");
    List<Predicate<? super String>> predicates = new ArrayList<Predicate<? super String>>();
    if (patterns != null && !patterns.isEmpty()) {
      for (String s : patterns) {
        predicates.add(matchesDateFormat(s));
      }
      this.predicates.add(URIPredicates.containsParameter(name, or(predicates)));
    }
  }

  // ~ Build Predicate -------------------------------------------------------------------------------------------------


  public Predicate<String> build() {
    if (this.predicates.size() == 1) {
      return predicates.get(0);
    }
    return and(this.predicates);
  }


}
