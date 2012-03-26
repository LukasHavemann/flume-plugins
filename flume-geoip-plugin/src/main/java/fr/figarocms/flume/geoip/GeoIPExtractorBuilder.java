package fr.figarocms.flume.geoip;

import com.google.common.base.Preconditions;

import com.cloudera.flume.conf.Context;
import com.cloudera.flume.conf.SinkFactory;
import com.cloudera.flume.core.EventSink;
import com.maxmind.geoip.LookupService;

import java.io.File;
import java.io.IOException;
import java.net.URL;


public class GeoIPExtractorBuilder extends SinkFactory.SinkDecoBuilder {

  /**
   * @param context : Flume configuration
   * @param argv    : arguments list from Flume configuration <GeoIP database file name>,[field name],[prefix]
   * @return a GeoIPExtractor EventSink ready for event treat
   */
  @Override
  public GeoIPExtractor<EventSink> build(Context context, String... argv) {
    Preconditions.checkArgument(argv.length >= 1 && argv.length <= 3, "usage: geoip(filename, field, prefix)");
    ClassLoader loader = ClassLoader.getSystemClassLoader();
    URL url = loader.getResource(argv[0]);

    File database = new File(url == null ? argv[0] : url.getFile());
    if (!database.exists()) {
       throw new IllegalArgumentException("File '" + argv[0] + "' does not exist");
    }

    try {
      LookupService lookupService = new LookupService(database, LookupService.GEOIP_MEMORY_CACHE);
      GeoIPExtractor<EventSink> g = new GeoIPExtractor<EventSink>(null, lookupService);
      switch (argv.length) {
        case 2: //if field name is passed in parameter use it.
          g.setAttributeName(argv[1]);
          break;
        case 3: //if field name and field prefix are passed in parameter use it.
          g.setAttributeName(argv[1]);
          g.setDstPrefix(argv[2]);
          break;
        default:
          break;
      }
      return g;
    } catch (IOException e) {
      throw new IllegalArgumentException("File '" + database.getAbsolutePath() + "' is not readable");
    }
  }


}
