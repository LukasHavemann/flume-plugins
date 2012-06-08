package fr.figarocms.flume.uri;

import com.cloudera.flume.conf.Context;
import com.cloudera.flume.conf.SinkFactory;
import com.cloudera.flume.core.EventSink;
import com.cloudera.flume.core.EventSinkDecorator;
import com.google.common.base.Preconditions;

public class URIExtractorBuilder extends SinkFactory.SinkDecoBuilder {

    @Override
    public EventSinkDecorator<EventSink> build(Context context, String... argv) {
        Preconditions.checkArgument(argv.length >= 1 && argv.length < 3, "usage: uri(field, prefix)");

        String field = argv[0].toString();
        String prefix = (argv.length == 2 ? argv[1].toString() : null);
        return new URIExtractor<EventSink>(null, field, prefix);
    }
}
