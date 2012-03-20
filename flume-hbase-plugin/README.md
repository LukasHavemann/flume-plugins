# Flume HBase Plugin

Event Sink to send events to HBase.

This plugin is a backport of https://github.com/cloudera/flume/tree/master/plugins/flume-plugin-hbasesink
for HBase version : 0.90.4-cdh3u3


## Installation :

 * Add the jar to the FLUME_CLASSPATH
 * Add the plugin to your flume-config.xml

```
  <property>
    <name>flume.plugin.classes</name>                                         
    <value>fr.figarocms.flume.hbase.HBaseSink</value>
    <description>Comma separated list of plugins</description>
  </property>
```

## Usage :


### Download :

You can download jar at https://oss.sonatype.org/ searching for "flume-hbase-plugin" :-)
