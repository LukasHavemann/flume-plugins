package fr.figarocms.flume.filter.predicate;

import com.google.common.base.Objects;
import com.google.common.base.Predicate;

import com.cloudera.flume.core.Event;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Predicates.and;
import static com.google.common.base.Predicates.compose;
import static com.google.common.base.Predicates.notNull;

/**
 * A set of {@link com.google.common.base.Predicate} on Flume {@link Event}s.
 */
public class EventPredicates {

  // ~ Static methods ------------------------------------------------------------------------------------------------


  public static Predicate<Event> hasHost(Predicate<? super String> predicate) {
    return and(HasHost.INSTANCE, compose(predicate, EventFunctions.getHost()));
  }

  public static Predicate<Event> hasPriority(Predicate<? super String> predicate) {
    return and(HasPriority.INSTANCE, compose(predicate, EventFunctions.getPriority()));
  }

  public static Predicate<Event> hasBody(Predicate<? super String> predicate) {
    return and(HasBody.INSTANCE, compose(predicate, EventFunctions.getBody()));
  }

  public static Predicate<Event> containsAttribute(String attribute, Predicate<? super String> predicate) {
    return and(HasAttribute.hasAttribute(attribute),
               compose(and(notNull(), predicate), EventFunctions.getAttribute(attribute)));
  }

  // ~ Predicates ----------------------------------------------------------------------------------------------------

  /**
   * Check if host exists in {@link Event}.
   */
  private static enum HasHost implements Predicate<Event> {

    INSTANCE;

    @Override
    public boolean apply(Event event) {
      return event.getHost() != null;
    }


    @Override
    public String toString() {
      return getClass().getSimpleName();
    }
  }

  /**
   * Check if priority exists in {@link Event}.
   */
  private static enum HasPriority implements Predicate<Event> {

    INSTANCE;

    @Override
    public boolean apply(Event event) {
      return event.getPriority() != null;
    }

    @Override
    public String toString() {
      return getClass().getSimpleName();
    }
  }

  /**
   * Check if an event body exists in {@link Event}.
   */
  private static enum HasBody implements Predicate<Event> {

    INSTANCE;

    @Override
    public boolean apply(Event event) {
      return event.getBody() != null && event.getBody().length > 0;
    }

    @Override
    public String toString() {
      return getClass().getSimpleName();
    }
  }

  /**
   * Check if an attribute exists in {@link Event}.
   */
  public static class HasAttribute implements Predicate<Event> {

    private final String attribute;

    public HasAttribute(String attribute) {
      this.attribute = checkNotNull(attribute);
    }

    public static Predicate<? super Event> hasAttribute(final String attribute) {
      return new HasAttribute(attribute);
    }

    @Override
    public boolean apply(Event event) {
      return event.getAttrs().containsKey(attribute);
    }

    @Override
    public boolean equals(Object o) {
      if (this == o) {
        return true;
      }
      if (o == null || getClass() != o.getClass()) {
        return false;
      }

      HasAttribute that = (HasAttribute) o;

      return Objects.equal(this.attribute, that.attribute);
    }

    @Override
    public int hashCode() {
      return Objects.hashCode(attribute);
    }

    @Override
    public String toString() {
      return Objects.toStringHelper(this)
          .addValue(attribute)
          .toString();
    }
  }

  private EventPredicates() {
  }

}
