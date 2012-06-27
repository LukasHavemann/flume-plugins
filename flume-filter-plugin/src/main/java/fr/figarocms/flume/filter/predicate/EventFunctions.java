package fr.figarocms.flume.filter.predicate;

import com.cloudera.flume.core.Event;
import com.google.common.base.Function;
import com.google.common.base.Objects;

import static com.google.common.base.Preconditions.checkNotNull;

public class EventFunctions {

  public static Function<Event, String> getHost() {
    return GetHost.INSTANCE;
  }

  public static Function<Event, String> getPriority() {
    return GetPriority.INSTANCE;
  }

  public static Function<Event, String> getBody() {
    return GetBody.INSTANCE;
  }

  public static Function<Event, String> getAttribute(String attribute) {
    return new GetAttribute(attribute);
  }

  // ~ Functions  ------------------------------------------------------------------------------------------------------

  /**
   * Return {@link Event} host.
   */
  private static enum GetHost implements Function<Event, String> {

    INSTANCE;

    @Override
    public String apply(Event event) {
      return event.getHost();
    }


    @Override
    public String toString() {
      return getClass().getSimpleName();
    }
  }


  /**
   * Return {@link Event} host.
   */
  private static enum GetPriority implements Function<Event, String> {

    INSTANCE;

    @Override
    public String apply(Event event) {
      return event.getPriority().toString();
    }

    @Override
    public String toString() {
      return getClass().getSimpleName();
    }
  }

  /**
   * Return {@link Event} host.
   */
  private static enum GetBody implements Function<Event, String> {

    INSTANCE;

    @Override
    public String apply(Event event) {
      return new String(event.getBody());
    }

    @Override
    public String toString() {
      return getClass().getSimpleName();
    }
  }

  /**
   * Return {@link Event} attribute if exists, null otherwise.
   */
  private static class GetAttribute implements Function<Event, String> {

    private final String attribute;

    public GetAttribute(String attribute) {
      this.attribute = checkNotNull(attribute);
    }

    @Override
    public String apply(Event event) {
      return (event.get(attribute) != null ? new String(event.get(attribute)) : null);
    }

    @Override
    public boolean equals(Object o) {
      if (this == o) {
        return true;
      }
      if (o == null || getClass() != o.getClass()) {
        return false;
      }

      GetAttribute that = (GetAttribute) o;

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

  private EventFunctions() {
  }

}
