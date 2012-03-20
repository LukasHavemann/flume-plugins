# Flume Filter Plugin

Event Sink Decorator which filter Flume Events

## Installation :

 * Add the jar to the FLUME_CLASSPATH
 * Add the plugin to your flume-config.xml

```
  <property>
    <name>flume.plugin.classes</name>                                         
    <value>fr.figarocms.flume.filter.EventFilter</value>
    <description>Comma separated list of plugins</description>
  </property>
```

## Usage :

```
filter(<filename>)
```

 * filename : is the yml configuration file defining filter rules.


### Concrete example :

Assuming this configuration file in YAML format :

```
host:
  value: server1

priority:
  values:
    - INFO
    - ERROR

body:
  pattern: test

attributes:
- name: attribute1
  values:
    - '200'
    - '204'

- name: attribute2
  dateFormat: YYYY-MM-DD

- name: attribute3
  pattern: 'pattern/*'
  parameters:
  - name: parameter1
    value: value1
  - name: parameter2
    values:
      - value2
      - value3
  - name: parameter3
    dateFormat: YYYY-MM
```

All the events not satisfying all predicates defined in this configuration file will be filtered.

Conversely the following event satisfies all the predicates:

 ```
    Event event = new EventImpl("this is a test".getBytes(), 132L, Event.Priority.INFO, 159L, "server1");
    event.set("attribute1", "200".getBytes());
    event.set("attribute2", "2012-02-15".getBytes());
    event.set("attribute3", "pattern/example?parameter1=value1&parameter2=value2&parameter3=2012-02".getBytes());
 ```

### Download :

You can download jar at https://oss.sonatype.org/ searching for "flume-filter-plugin" :-)

