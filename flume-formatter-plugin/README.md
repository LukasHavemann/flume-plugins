# Flume Formatter Plugin

Collection of OutputFormat that use a mapping file to format event.

For now there's just one Json Object Formatter.

## Installation :

 * Add the jar to the FLUME_CLASSPATH
 * Add the plugin to your flume-config.xml

```
  <property>
    <name>flume.plugin.classes</name>                                         
    <value>fr.figarocms.flume.formatter.JsonObjectFormatter</value>
    <description>Comma separated list of plugins</description>
  </property>
```

## Usage :

```
json_object([filename])
```
 * filename : is the yml configuration file defining mapping and format (optional)


### Concrete example :

Assuming this configuration file in YAML format :

```
mapping:
  attributes:
    - name: 'attr_string'
      type: string

    - name: 'attr_integer'
      type: integer

    - name: 'attr_long'
      type: long

    - name: 'attr_float'
      type: float

    - name: 'attr_double'
      type: double

format:
  body: '%{body}'
  timestamp: '%{timestamp}'
  host: '%{host}'
  priority: '%{priority}'
  nanos: '%{nanos}'
  attr_1: '%{attr_string}'
  attr_2: '%{attr_integer}'
  attr_3: '%{attr_long}'
  attr_4: '%{attr_float}'
  attr_5: '%{attr_double}'
  attr_6: '%{attr_unknown}'
  attr_7: 3.2
  attr_8: null
```

and this Flume Event

```
 Event
        event =
        new EventImpl("this is a test".getBytes(), Clock.unixTime(), Event.Priority.INFO, Clock.nanos(), "server1");

    // String attribute
    event.set("attr_string", "this is a string".getBytes());

    // Integer attribute
    Object source = new Integer(13213);
    byte[] data = new byte[Integer.SIZE];
    ByteBuffer buffer = ByteBuffer.wrap(data);
    buffer.putInt((Integer)source);
    event.set("attr_integer", data);

    // Long attribute
    source = new Long(779864634);
    data = new byte[Long.SIZE];
    buffer = ByteBuffer.wrap(data);
    buffer.putLong((Long) source);
    event.set("attr_long", data);

    // Float attribute
    source = new Float(1.232);
    data = new byte[Float.SIZE];
    buffer = ByteBuffer.wrap(data);
    buffer.putFloat((Float) source);
    event.set("attr_float", data);

    // Double attribute
    source = new Double(1.232567641);
    data = new byte[Float.SIZE];
    buffer = ByteBuffer.wrap(data);
    buffer.putDouble((Double) source);
    event.set("attr_double", data);

```

The Json Object Formatter should format the event like that :

```
{
 "body":"this is a test",
 "timestamp":1332782765428,
 "host":"server1",
 "priority":"INFO",
 "nanos":34066093387185,
 "attr_1":"this is a string",
 "attr_2":13213,
 "attr_3":779864634,
 "attr_4":1.232,
 "attr_5":1.232567641,
 "attr_6":null,
 "attr_7":3.2,
 "attr_8":null
}
```

### Download :

You can download jar at https://oss.sonatype.org/ searching for "flume-formatter-plugin" :-)

