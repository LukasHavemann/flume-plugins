package fr.figarocms.flume.formatter;

import com.cloudera.flume.core.Event;
import com.cloudera.flume.handlers.text.output.AbstractOutputFormat;

import org.apache.hadoop.thirdparty.guava.common.base.Preconditions;

import java.io.IOException;
import java.io.OutputStream;

public class Formatter extends AbstractOutputFormat {


  public Formatter(String format, String mappingFile) {
    Preconditions.checkArgument(format !=null && format.equals("json"), "Wrong output format specified");
  }

  public Formatter() {
    //To change body of created methods use File | Settings | File Templates.
  }

  @Override
  public void format(OutputStream o, Event e) throws IOException {

    //To change body of implemented methods use File | Settings | File Templates.
  }
}
