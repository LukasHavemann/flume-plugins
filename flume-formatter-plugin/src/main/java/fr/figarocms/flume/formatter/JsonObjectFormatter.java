package fr.figarocms.flume.formatter;

import com.cloudera.flume.core.Event;
import com.cloudera.flume.handlers.text.output.AbstractOutputFormat;

import org.codehaus.jackson.map.ObjectMapper;

import java.io.IOException;
import java.io.OutputStream;

import fr.figarocms.flume.formatter.config.Formatter;

public class JsonObjectFormatter extends AbstractOutputFormat {

  private ObjectMapper mapper;
  private Formatter formatter;

  public JsonObjectFormatter(Formatter formatter, ObjectMapper mapper) {
    this.formatter = formatter;
    this.mapper = mapper;
  }

  @Override
  public void format(OutputStream o, Event e) throws IOException {
    Object obj = formatter.format(e);
    mapper.writeValue(o, obj);
  }

}