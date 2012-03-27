package fr.figarocms.flume.formatter;

import com.cloudera.flume.core.Event;
import com.cloudera.flume.handlers.text.output.AbstractOutputFormat;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.error.YAMLException;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URL;

import fr.figarocms.flume.formatter.config.Formatter;

public abstract class ObjectFormatter extends AbstractOutputFormat {

  protected static final Logger LOG = LoggerFactory.getLogger(ObjectFormatter.class);

  private Formatter formatter;

  public ObjectFormatter(String filename) {
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
  }

  @Override
  public void format(OutputStream o, Event e) throws IOException {
    Object obj = formatter.format(e);
    format(o, obj);
  }

  public abstract void format(OutputStream o, Object obj) throws IOException;
}
