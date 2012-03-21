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

import fr.figarocms.flume.formatter.config.MappingConfiguration;

import static com.google.common.base.Preconditions.checkArgument;

public class Formatter extends AbstractOutputFormat {

  private File mappingFile;

  public Formatter(String format, String mappingFile) {
    Preconditions.checkArgument(format != null && format.equals("json"), "Wrong output format specified");
    Preconditions.checkArgument(mappingFile != null, "No mapping file specified");
    URL resource = this.getClass().getResource(mappingFile);

    // Load configuration file
    try {
      Yaml yaml = new Yaml();
      this.mappingFile = new File(resource == null ? mappingFile : resource.getFile());
      final
	  MappingConfiguration
          configuration =
          yaml.loadAs(new FileInputStream(this.mappingFile), MappingConfiguration.class);
      checkArgument(configuration != null, "File '" + mappingFile + "' is empty");
    } catch (FileNotFoundException f) {
      throw new IllegalArgumentException("File '" + mappingFile + "' does not exist", f);
    } catch (YAMLException y) {
      throw new IllegalArgumentException("File '" + mappingFile + "' is not a valid yml", y);
    }
  }

  @Override
  public void format(OutputStream o, Event e) throws IOException {

    //To change body of implemented methods use File | Settings | File Templates.
  }

  public File getMappingFile() {
    return mappingFile;
  }
}
