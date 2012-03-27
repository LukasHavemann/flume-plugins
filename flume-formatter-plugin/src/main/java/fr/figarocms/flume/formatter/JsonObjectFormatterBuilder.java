package fr.figarocms.flume.formatter;

import com.google.common.base.Preconditions;

import com.cloudera.flume.handlers.text.FormatFactory;
import com.cloudera.flume.handlers.text.output.OutputFormat;

import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.error.YAMLException;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.net.URL;

import fr.figarocms.flume.formatter.config.Formatter;

public class JsonObjectFormatterBuilder extends FormatFactory.OutputFormatBuilder {

  private static final Logger LOG = LoggerFactory.getLogger(JsonObjectFormatterBuilder.class);

  private static final String NAME = "json_object";
  private Formatter formatter;

  @Override
  public OutputFormat build(String... args) {
    Preconditions.checkArgument(args.length >= 0 && args.length <= 1, "usage: json_object([filename])");

    String filename = args.length == 1 ? args[0] : null;

    // Default Formatter
    if (filename == null) {
      formatter = new Formatter();
      if (LOG.isDebugEnabled()) {
        LOG.debug("Default formatter initialized");
      }
    }
    // Formatter based on configuration file
    else {
      ClassLoader loader = ClassLoader.getSystemClassLoader();
      URL url = loader.getResource(filename);

      try {
        Yaml yaml = new Yaml();
        File file = new File(url == null ? filename : url.getFile());
        formatter = yaml.loadAs(new FileInputStream(file), Formatter.class);
        if (LOG.isDebugEnabled()) {
          LOG.debug("Formatter initialized from " + filename);
        }
      } catch (FileNotFoundException f) {
        throw new IllegalArgumentException("File '" + filename + "' does not exist", f);
      } catch (YAMLException y) {
        throw new IllegalArgumentException("File '" + filename + "' is not a valid yml", y);
      }
    }
    OutputFormat format = new JsonObjectFormatter(formatter, new ObjectMapper());
    format.setBuilder(this);
    return format;
  }

  @Override
  public String getName() {
    return NAME;
  }
}
