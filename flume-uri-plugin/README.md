# Flume URI Extractor

Event Sink Decorator which extract URI parts from an Event attribute

## Installation :

 * Add the jar to the FLUME_CLASSPATH
 * Add the MaxMind database file in a location accessible form FLUME_CLASSPATH or specify full path in configuration
 * Add the plugin to your flume-config.xml

```
  <property>
    <name>flume.plugin.classes</name>                                         
    <value>fr.figarocms.flume.uri.URIExtractor</value>
    <description>Comma separated list of plugins</description>
  </property>
```

## Usage :

```
uri(field,[prefix])
```

  * field : the URI event field
  * prefix: the prefix for fields corresponding to uri parameters (as prefix.host, prefix.param.param1, prefix.param.param2). Default is the field name.


### Concrete example :

Assuming there is a attribute "uri" in event with value "http://www.foobar.fr/uri?param1=foo&param2=bar" for example.

The following Flume configuration :

```
uri("uri", "prefix")
```

should add for example the following attributes to the event :


| **name**            | **value**                     |
|---------------------|-------------------------------|
| prefix.param.param1 | "foo"                         |
| prefix.param.param1 | "bar"                         |
| prefix.host         | "www.foobar.fr"               |

### Download :

You can download jar at https://oss.sonatype.org/ searching for "flume-uri-plugin" :-)
