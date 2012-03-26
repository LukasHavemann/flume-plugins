package fr.figarocms.flume.formatter;

import com.cloudera.flume.core.Event;
import com.cloudera.flume.handlers.text.output.AbstractOutputFormat;
import fr.figarocms.flume.formatter.config.Formatter;
import org.dozer.config.BeanContainer;
import org.dozer.util.DozerClassLoader;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.error.YAMLException;

import java.io.*;
import java.net.URL;

public abstract class ObjectFormatter extends AbstractOutputFormat {

  private Formatter formatter;

  public ObjectFormatter(String filename) {
	// Default Formatter
    if (filename == null) {
      formatter = new Formatter();
    }
	// Formatter based on configuration file
	else {
  	  ClassLoader loader = ClassLoader.getSystemClassLoader();
      URL url = loader.getResource(filename);

      try {
        Yaml yaml = new Yaml();
        File file = new File(url == null ? filename : url.getFile());
        formatter = yaml.loadAs(new FileInputStream(file), Formatter.class);
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
