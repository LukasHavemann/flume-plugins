package fr.figarocms.flume.haproxy.processor;

import com.cloudera.flume.core.Event;
import fr.figarocms.flume.haproxy.exception.ProcessorException;

public class HeaderProcessor extends AbstractProcessor {

  @Override
  public void doProcess(Event e, String s, String token) throws ProcessorException {
    if (null != token) {
      String d = "\\|";
      int i = 0;

      for (String h : token.split(d)) {
        try {
          e.set(prefix + s + "." + i, h.getBytes());
          i++;
        } catch (IllegalArgumentException exception) {
          throw new ProcessorException();
        }
      }
    }
  }
}
