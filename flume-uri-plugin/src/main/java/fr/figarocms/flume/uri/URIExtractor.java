package fr.figarocms.flume.uri;

import com.cloudera.flume.conf.SinkFactory;
import com.cloudera.flume.core.Event;
import com.cloudera.flume.core.EventSink;
import com.cloudera.flume.core.EventSinkDecorator;
import com.cloudera.util.Pair;
import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URLEncodedUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import static java.nio.charset.Charset.defaultCharset;

public class URIExtractor<S extends EventSink> extends EventSinkDecorator<S> {

    protected static Logger LOG = LoggerFactory.getLogger(URIExtractor.class);
    private String attributeName;
    private String prefix;
    private static final String PARAM_ATTRIBUTE = "param";
    private static final String HOST_ATTRIBUTE = "host";
    private static final String SEPARATOR = ".";

    public URIExtractor(S s, String attributeName, String prefix) {
        super(s);
        this.attributeName = attributeName;
        this.prefix = (prefix == null ? attributeName : prefix);
    }


    @Override
    public void append(Event e) throws IOException, InterruptedException {
        if (e.getAttrs().containsKey(attributeName)) {
            try {

                String uriString = new String(e.get(attributeName));
                URI uri = URI.create(uriString);

                if (null != uri.getHost()) {
                    extractHost(e, uri);
                }

                if (null != uri.getQuery()) {
                    extractQuery(e, uri);
                }
            } catch (IllegalArgumentException ex) {
                LOG.warn("Unable to parse attribute '" + this.attributeName + "' as an URI");
            }
        } else {
            LOG.warn("Attribute '" + this.attributeName + "' not found in event");
        }

        super.append(e);
    }

    
    private void extractHost(Event e, URI uri) {
        String hostAttribute = new StringBuilder(prefix)
                .append(SEPARATOR)
                .append(HOST_ATTRIBUTE)
                .toString();

        e.set(hostAttribute, uri.getHost().getBytes());
    }

    private void extractQuery(Event e, URI uri) {
        List<NameValuePair> pairs = URLEncodedUtils.parse(uri, defaultCharset().name());

        for (NameValuePair pair : pairs) {
            final String value = pair.getValue();
            final String name = pair.getName();

            String paramAttribute = new StringBuilder(prefix)
                    .append(SEPARATOR)
                    .append(PARAM_ATTRIBUTE)
                    .append(SEPARATOR)
                    .append(name)
                    .toString();

            e.set(paramAttribute, (value != null ? value.getBytes() : null));
        }
    }

    public static SinkFactory.SinkDecoBuilder builder() {
        return new URIExtractorBuilder();
    }

    public static List<Pair<String, SinkFactory.SinkDecoBuilder>> getDecoratorBuilders() {
        List<Pair<String, SinkFactory.SinkDecoBuilder>> builders =
                new ArrayList<Pair<String, SinkFactory.SinkDecoBuilder>>();
        builders.add(new Pair<String, SinkFactory.SinkDecoBuilder>("uri",
                builder()));
        return builders;
    }
}
