package fr.figarocms.flume.geoip.processor;

import com.cloudera.flume.core.Event;

public class SimpleProcessor extends AbstractProcessor {

    @Override
    public void doProcess(Event e, String s, String token) {
        if (null != token) {
            e.set(prefix + s, token.getBytes());
        }
    }
}
