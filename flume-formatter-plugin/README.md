# Flume Formatter Plugin

Collection of OutputFormat that use a mapping file to format event.

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
json_object(<filename>)
```
 * filename : is the yml configuration file defining mapping and format.

