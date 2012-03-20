package fr.figarocms.flume.haproxy.processor;


import com.cloudera.flume.core.Event;
import fr.figarocms.flume.haproxy.exception.ProcessorException;

public interface Processor {
    public void doProcess(Event e, String s, String token) throws ProcessorException;
}