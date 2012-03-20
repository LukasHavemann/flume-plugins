package fr.figarocms.flume.geoip.processor;

import com.cloudera.flume.core.Event;
import fr.figarocms.flume.geoip.exception.ProcessorException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

abstract public class AbstractProcessor implements Processor {

    static final Logger LOG = LoggerFactory.getLogger(AbstractProcessor.class);
    static protected final String prefix = "geoip.";

    public abstract void doProcess (Event e, String s, String token) throws ProcessorException;

}
