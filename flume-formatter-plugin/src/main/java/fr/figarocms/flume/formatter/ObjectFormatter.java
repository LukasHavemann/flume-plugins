package fr.figarocms.flume.formatter;

import com.cloudera.flume.core.Event;
import com.cloudera.flume.handlers.text.output.AbstractOutputFormat;
import fr.figarocms.flume.formatter.config.Formatter;
import org.apache.hadoop.thirdparty.guava.common.base.Preconditions;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.ObjectMapper;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.error.YAMLException;

import java.io.*;
import java.net.URL;
import java.util.Map;

import static com.google.common.base.Preconditions.checkArgument;

//TODO add logs
public abstract class ObjectFormatter extends AbstractOutputFormat {

	private Formatter formatter;

	public ObjectFormatter(String filename) {
		Preconditions.checkArgument(filename != null, "No formatter configuration file specified");
		URL resource = this.getClass().getResource(filename);

		// Load configuration file
		try {
			Yaml yaml = new Yaml();
			File file = new File(resource == null ? filename : resource.getFile());
			formatter = yaml.loadAs(new FileInputStream(file), Formatter.class);
			checkArgument(formatter != null, "File '" + filename + "' is empty");
		} catch (FileNotFoundException f) {
			throw new IllegalArgumentException("File '" + filename + "' does not exist", f);
		} catch (YAMLException y) {
			throw new IllegalArgumentException("File '" + filename + "' is not a valid yml", y);
		}
	}

	@Override
	public void format(OutputStream o, Event e) throws IOException {
		Object obj = formatter.format(e);
		format(o, obj);
	}

	public abstract void format(OutputStream o, Object obj) throws IOException;
}
