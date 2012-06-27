package fr.figarocms.flume.filter;

import com.cloudera.flume.conf.SinkFactory;
import com.cloudera.flume.core.Event;
import com.cloudera.flume.core.EventSink;
import com.cloudera.flume.core.EventSinkDecorator;
import com.cloudera.util.Pair;
import com.google.common.base.Predicate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static java.lang.String.format;

public class EventFilter<S extends EventSink> extends EventSinkDecorator<S> {

  protected static Logger LOG = LoggerFactory.getLogger(EventFilter.class);
  private Predicate<Event> predicate;

  public EventFilter(S s, Predicate<Event> predicate) {
    super(s);
    this.predicate = predicate;
  }

  @Override
  public void append(Event e) throws IOException, InterruptedException {
    if (predicate.apply(e)) {
      LOG.debug(format("Event %s satisfies filter predicates. Well done !!", e));
      super.append(e);
    } else {
      LOG.debug(format("Event %s doesn't satisfies filter predicates", e));
    }
  }

  /**
   * @return a Sink decorator for EventFilter
   */
  public static SinkFactory.SinkDecoBuilder builder() {
    return new EventFilterBuilder();
  }

  /**
   * This is a special function used by the SourceFactory to pull in this class as a filter decorator.
   *
   * @return a list of EventFilter
   */
  public static List<Pair<String, SinkFactory.SinkDecoBuilder>> getDecoratorBuilders() {
    List<Pair<String, SinkFactory.SinkDecoBuilder>> builders =
        new ArrayList<Pair<String, SinkFactory.SinkDecoBuilder>>();
    builders.add(new Pair<String, SinkFactory.SinkDecoBuilder>("filter",
                                                               builder()));
    return builders;
  }
}

