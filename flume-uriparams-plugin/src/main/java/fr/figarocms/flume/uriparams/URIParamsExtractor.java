package fr.figarocms.flume.uriparams;

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
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import static java.lang.String.format;
import static java.nio.charset.Charset.defaultCharset;

public class URIParamsExtractor <S extends EventSink> extends EventSinkDecorator<S> {

  protected static Logger LOG = LoggerFactory.getLogger(URIParamsExtractor.class);
  private String attributeName;
  private String prefix;


  public URIParamsExtractor(S s, String attributeName, String prefix) {
    super(s);
    this.attributeName = attributeName;
    this.prefix = (prefix == null ? attributeName : prefix);
  }


  @Override
  public void append(Event e) throws IOException, InterruptedException {
    if(e.getAttrs().containsKey(attributeName)){
      try{
      String uriString = new String(e.get(attributeName));
      URI uri = URI.create(uriString);
      List<NameValuePair> pairs = URLEncodedUtils.parse(uri, defaultCharset().name());
      for (NameValuePair pair : pairs) {
        e.set(format("%s.%s", prefix, pair.getName()),pair.getValue().getBytes());
      }
      }catch(IllegalArgumentException ex){
        LOG.warn("Unable to parse attribute '" + this.attributeName + "' as an URI");
      }
    }else {
      LOG.warn("Attribute '" + this.attributeName + "' not found in event");
    }
  }

  public static SinkFactory.SinkDecoBuilder builder() {
    return new URIParamsExtractorBuilder();
  }

  public static List<Pair<String, SinkFactory.SinkDecoBuilder>> getDecoratorBuilders() {
    List<Pair<String, SinkFactory.SinkDecoBuilder>> builders =
        new ArrayList<Pair<String, SinkFactory.SinkDecoBuilder>>();
    builders.add(new Pair<String, SinkFactory.SinkDecoBuilder>("uriparams",
                                                   builder()));
    return builders;
  }
}
