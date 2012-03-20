package fr.figarocms.flume.geoip.processor;


import com.cloudera.flume.core.Event;
import fr.figarocms.flume.geoip.exception.ProcessorException;

public interface Processor {
    public void doProcess(Event e, String s, String token) throws ProcessorException;
}