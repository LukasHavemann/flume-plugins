# Flume URI Param Extractor

Event Sink Decorator which extract URI Parameters from an Event attribute

## Installation :

 * Add the jar to the FLUME_CLASSPATH
 * Add the MaxMind database file in a location accessible form FLUME_CLASSPATH or specify full path in configuration
 * Add the plugin to your flume-config.xml

```
  <property>
    <name>flume.plugin.classes</name>                                         
    <value>fr.figarocms.flume.uriparams.URIParamsExtractor</value>
    <description>Comma separated list of plugins</description>
  </property>
```

## Usage :

```
uriparams(field,[prefix])
```

  * field : the URI event field
  * prefix: the prefix for fields corresponding to uri parameters (as prefix.param1, prefix.param2). Default is the field name.


### Concrete example :

Assuming there is a attribute "uri" in event with value "/uri?param1=foo&param2=bar" for example.

The following Flume configuration :

```
uriparams("uri", "params")
```

should add for example the following attributes to the event :


| **name**           | **value**                     |
|--------------------|-------------------------------|
| params.param1      | "foo"                         |
| params.param1      | "bar"                         |

### Download :

You can download jar at https://oss.sonatype.org/ searching for "flume-uriparams-plugin" :-)
