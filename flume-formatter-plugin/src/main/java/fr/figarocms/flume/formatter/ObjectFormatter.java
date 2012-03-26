package fr.figarocms.flume.formatter;

import com.cloudera.flume.core.Event;
import com.cloudera.flume.handlers.text.output.AbstractOutputFormat;

import org.apache.hadoop.thirdparty.guava.common.base.Preconditions;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.error.YAMLException;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URL;

import fr.figarocms.flume.formatter.config.Formatter;

import static com.google.common.base.Preconditions.checkArgument;

public abstract class ObjectFormatter extends AbstractOutputFormat {

  private Formatter formatter;

  public ObjectFormatter(String filename) {
    if (filename == null) {
      formatter = new Formatter();
    } else {
      URL resource = this.getClass().getResource(filename);

      // Load configuration file
      try {
        Yaml yaml = new Yaml();
        File file = new File(resource == null ? filename : resource.getFile());
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
