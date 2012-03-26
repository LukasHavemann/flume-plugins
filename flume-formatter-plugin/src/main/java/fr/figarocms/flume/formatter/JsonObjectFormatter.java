package fr.figarocms.flume.formatter;

import com.google.common.base.Preconditions;

import com.cloudera.flume.handlers.text.FormatFactory;
import com.cloudera.flume.handlers.text.output.OutputFormat;

import org.codehaus.jackson.map.ObjectMapper;

import java.io.IOException;
import java.io.OutputStream;

public class JsonObjectFormatter extends ObjectFormatter {

  private static final String NAME = "json_object";
  private ObjectMapper mapper;

  public JsonObjectFormatter(String filename) {
    super(filename);
    mapper = new ObjectMapper();
  }

  @Override
  public void format(OutputStream o, Object obj) throws IOException {
    mapper.writeValue(o, obj);
  }

  public static FormatFactory.OutputFormatBuilder builder() {
    return new FormatFactory.OutputFormatBuilder() {

      @Override
      public OutputFormat build(String... args) {
        Preconditions.checkArgument(args.length >= 0 && args.length <= 1, "usage: json_object([filename])");
        OutputFormat format = new JsonObjectFormatter(args.length == 1 ? args[0]: null);
        format.setBuilder(this);
        return format;
      }

      @Override
      public String getName() {
        return NAME;
      }

    };
  }
}