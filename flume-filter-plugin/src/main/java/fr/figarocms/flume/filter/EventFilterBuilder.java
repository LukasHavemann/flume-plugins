package fr.figarocms.flume.filter;

import com.google.common.base.Preconditions;

import com.cloudera.flume.conf.Context;
import com.cloudera.flume.conf.SinkFactory;

import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.error.YAMLException;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.net.URL;

import fr.figarocms.flume.filter.config.EventPredicateBuilder;

public class EventFilterBuilder extends SinkFactory.SinkDecoBuilder {

  /**
   * Build an {@EventFilter}.
   *
   * @param context : Flume configuration
   * @param argv    : Arguments list from Flume configuration ([configuration file path])
   * @return an EventFilter EventSinkDecorator ready for event treat
   */

  @Override
  public EventFilter build(Context context, String... argv) {
    Preconditions.checkArgument(argv.length == 1, "usage: filter(filename)");
    ClassLoader loader = ClassLoader.getSystemClassLoader();
    URL url = loader.getResource(argv[0]);

    // Load configuration file
    try {
      Yaml yaml = new Yaml();
      File configuration = new File(url == null ? argv[0] : url.getFile());
      EventPredicateBuilder builder = yaml.loadAs(new FileInputStream(configuration), EventPredicateBuilder.class);
      Preconditions.checkArgument(builder != null, "File '" + argv[0] + "' is empty");
      return new EventFilter(null, builder.build());
    } catch (FileNotFoundException f) {
      throw new IllegalArgumentException("File '" + argv[0] + "' does not exist", f);
    } catch (YAMLException y) {
      throw new IllegalArgumentException("File '" + argv[0] + "' is not a valid yml", y);
    }
  }


}
