# Flume RabbitMQ Plugin

Event Sink to send event to RabbitMQ

## Installation :

 * Add the jar to the FLUME_CLASSPATH
 * Add the plugin to your flume-config.xml

```
  <property>
    <name>flume.plugin.classes</name>                                         
    <value>fr.figarocms.flume.rabbitmq.RabbitMQSink</value>
    <description>Comma separated list of plugins</description>
  </property>
```

## Usage :

```
rabbit(uri, exchange, exchange_type, routing_key[, format]);

```

 * uri: AMQP uri to create connection to RabbitMQ (see http://www.rabbitmq.com/uri-spec.html)
 * exchange: exchange where messages are sent
 * exchange_type: type of the exchange (direct, topic, fanout, headers)
 * routing_key : routing_key or queue name if no exchange are defined (see http://www.rabbitmq.com/tutorials/tutorial-four-java.html)
 * format : output format supported by flume (see http://archive.cloudera.com/cdh/3/flume/UserGuide/#_output_format) (optionnal, debug as default)



### Concrete example :

Assume the following declaration :

```
rabbit("amqp://userName:password@hostName:portNumber/virtualHost", "sample", "topic", "*.foo.*", "json");
```

This sink declaration will send events as json events to the topic "sample" with routing key "*.foo.*".


### Download :

You can download jar at https://oss.sonatype.org/ searching for "flume-rabbitmq-sink" :)
