package fr.figarocms.flume.haproxy.processor;

import com.cloudera.flume.core.Event;

import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.Locale;

import fr.figarocms.flume.haproxy.exception.ProcessorException;

public class DateProcessor extends AbstractProcessor {

  final static String dateFormat = "dd/MMM/YYYY:HH:mm:ss.SSS";


  private DateTimeFormatter df;

  public DateProcessor() {
    // Ha-Proxy Logs use UTC timezone : "get_gmtime(s->logs.accept_date.tv_sec, &tm);"
    // cf : http://git.1wt.eu/web?p=haproxy-1.4.git;a=blob_plain;f=src/proto_http.c;hb=HEAD
    this.df = DateTimeFormat.forPattern(dateFormat).withLocale(Locale.US);
  }

  @Override
  public void doProcess(Event e, String s, String token) throws ProcessorException {
    if (null != token) {
      try {
        e.set(prefix + s, df.parseDateTime(token).toString().getBytes());
      } catch (IllegalArgumentException exception) {
        throw new ProcessorException();
      }
    }
  }
}

