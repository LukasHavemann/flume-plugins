package fr.figarocms.flume.haproxy;

import com.cloudera.flume.core.Event;
import fr.figarocms.flume.haproxy.exception.ProcessorException;
import fr.figarocms.flume.haproxy.processor.Processor;
import fr.figarocms.flume.haproxy.processor.SimpleProcessor;
import jregex.Matcher;
import jregex.Pattern;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class Mapping {

    static final Logger LOG = LoggerFactory.getLogger(Mapping.class);

    private List<MappingEntry> list;
    private Pattern syslogpattern;

    /**
     * Mapping initialise list of fields entry
     */
    public Mapping() {
        this.list = new ArrayList<MappingEntry>();

        //named regex parsing haproxy syslog format access log
        String rx = "^(Jan|Feb|Mar|Apr|May|Jun|Jul|Aug|Sep|Oct|Nov|Dec) +\\d{1,2} \\d{2}:\\d{2}:\\d{2} ([-\\w]+ )?\\w+\\[\\d+\\]: "
                + "({clientIp}\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}):"
                + "({clientPort}\\d+) "
                + "\\[({timestamp}\\d{2}/(Jan|Feb|Mar|Apr|May|Jun|Jul|Aug|Sep|Oct|Nov|Dec)/\\d{4}:\\d{2}:\\d{2}:\\d{2}.\\d{3})\\] "
                + "({frontend}\\w+) "
                + "({backend}\\w+)/"
                + "({host}\\w+) "
                + "({times}\\d+/\\d+/\\d+/\\d+/\\d+) "
                + "({statusCode}\\d{3}) "
                + "({byteRead}\\d+) "
                + "({capturedRequestCookie}\\S+) "
                + "({capturedResponseCookie}\\S+) "
                + "({terminationState}\\S+) "
                + "({connStates}\\S+) "
                + "({queuesStates}\\w/\\w) "
                + "(\\{({capturedRequestHeaders}.*)\\} )?"
                + "(\\{({capturedResponseHeaders}.*)\\} )?"
                + "\"({verb}\\S+) "
                + "({uri}\\S+) "
                + "({version}\\S+)\"$";
        syslogpattern = new Pattern(rx);
    }

    public List<MappingEntry> getList() {
        return list;
    }

    /**
     * augment event with qualified values form body
     *
     * @param e : event from Flume with haproxy syslog body
     */
    public void process(Event e) {


        String s = new String(e.getBody());

        Matcher m = syslogpattern.matcher(s);
        if (!m.matches()) {
            return;
        }

        for (MappingEntry entry : this.list) {
            try {
                Processor p = entry.getProcessor();
                p.doProcess(e, entry.getProperty(), m.group(entry.getProperty()));
            } catch (ProcessorException exception) {
                if (LOG.isDebugEnabled()) {
                    LOG.debug("unable to match the given event's body {}", e.toString());
                }
            }
        }
    }

    public class MappingEntry {

        /**
         * property name or prefix
         */
        String property;

        /**
         * Implementation on the adding in event
         */
        Processor processor;

        /**
         * Event field to be extracted form body
         *
         * @param property : Field name
         */
        public MappingEntry(String property) {
            this.property = property;
            this.processor = new SimpleProcessor();
        }

        /**
         * Event field to be extracted form body
         *
         * @param property  : Field name
         * @param processor : value processor for this field
         */
        public MappingEntry(String property, Processor processor) {
            this.property = property;
            this.processor = processor;
        }


        /**
         * getter for property
         *
         * @return property name
         */
        public String getProperty() {
            return property;
        }

        /**
         * setter for property
         *
         * @param property : property name
         */
        public void setProperty(String property) {
            this.property = property;
        }

        /**
         * getter for processor
         *
         * @return value processor for current property
         */
        public Processor getProcessor() {
            return processor;
        }

        /**
         * setter for processor
         *
         * @param processor : value processor for current property
         */
        public void setProcessor(Processor processor) {
            this.processor = processor;
        }

    }

    /**
     * add new property to be extracted from body with custom value processor
     *
     * @param property  : property name to be added to event
     * @param processor : value processor for the field
     * @return the list of properties to be added to event
     */
    public Mapping addEntry(String property, Processor processor) {

        MappingEntry me = new MappingEntry(property);

        me.setProcessor(processor);

        this.list.add(me);

        return this;
    }


    /**
     * add new property to be extracted from body if default processor
     *
     * @param property : property name to be added to event
     * @return the list of properties to be added to event
     */
    public Mapping addEntry(String property) {

        this.list.add(new MappingEntry(property));

        return this;
    }


}
