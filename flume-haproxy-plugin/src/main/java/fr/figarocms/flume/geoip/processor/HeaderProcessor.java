package fr.figarocms.flume.geoip.processor;

import com.cloudera.flume.core.Event;
import fr.figarocms.flume.geoip.exception.ProcessorException;

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
