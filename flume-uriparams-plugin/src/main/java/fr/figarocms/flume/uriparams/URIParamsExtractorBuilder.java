package fr.figarocms.flume.uriparams;

import com.google.common.base.Preconditions;

import com.cloudera.flume.conf.Context;
import com.cloudera.flume.conf.SinkFactory;
import com.cloudera.flume.core.EventSink;
import com.cloudera.flume.core.EventSinkDecorator;

public class URIParamsExtractorBuilder extends SinkFactory.SinkDecoBuilder {

  @Override
  public EventSinkDecorator<EventSink> build(Context context, String... argv) {
    Preconditions.checkArgument(argv.length >= 1 && argv.length < 3, "usage: uriparams(field, prefix)");

    String field = argv[0].toString();
    String prefix = (argv.length == 2 ? argv[1].toString() : null);
    return new URIParamsExtractor<EventSink>(null, field, prefix);
  }
}
