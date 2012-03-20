package fr.figarocms.flume.filter.predicate;

import com.google.common.base.Objects;
import com.google.common.base.Predicate;

import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import javax.annotation.Nullable;

public class DateTimePredicates {

  // ~ Static Methods --------------------------------------------------------------------------------------------------

  public static Predicate<String> matchesDateFormat(String pattern) {
    return new MatchDateFormat(pattern);
  }

  // ~ Predicates ------------------------------------------------------------------------------------------------------

  private static class MatchDateFormat implements Predicate<String> {

    private String pattern;

    private MatchDateFormat(String pattern) {
      this.pattern = pattern;
    }

    @Override
    public boolean apply(@Nullable String date) {
      try {
        DateTimeFormatter parser = DateTimeFormat.forPattern(pattern);
        parser.parseDateTime(date);
        return true;
      } catch (Exception e) {
        return false;
      }
    }

    @Override
    public boolean equals(Object o) {
      if (this == o) {
        return true;
      }
      if (o == null || getClass() != o.getClass()) {
        return false;
      }

      MatchDateFormat that = (MatchDateFormat) o;

      return Objects.equal(this.pattern, that.pattern);
    }

    @Override
    public int hashCode() {
      return Objects.hashCode(pattern);
    }

    @Override
    public String toString() {
      return Objects.toStringHelper(this)
          .addValue(pattern)
          .toString();
    }
  }
}
