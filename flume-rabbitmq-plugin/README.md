# Flume RabbitMQ Plugin

Event Sink to send events to RabbitMQ

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
rabbit(address, username, password, vhost, exchange, exchange_type, routing_key[, format, media_type])

```

 * address: an address to create connection to an AMQP broker (For example : 'hostname1:portnumber1')
 * username: the username used to create connections to an AMQP broker
 * password: the password used to create connections to an AMQP broker
 * vhost: the virtual host used to create connections tto an AMQP broker
 * exchange: exchange where messages are sent
 * exchange_type: type of the exchange (direct, topic, fanout, headers)
 * routing_key : routing_key or queue name if no exchange are defined (see http://www.rabbitmq.com/tutorials/tutorial-four-java.html)
 * format : output format supported by flume (see http://archive.cloudera.com/cdh/3/flume/UserGuide/#_output_format) (optional, debug as default)
 * media_type : media type declaration for messages formatted with the output format (optional, 'application/octet-stream by default)

or

```
rabbitFailover(addresses, username, password, vhost, exchange, exchange_type, routing_key[, format, media_type])"

```

 * addresses: an address list to create connection to an AMQP broker (For example : 'hostname1:portnumber1,hostname2:portnumber2')
 * username: the username used to create connections to an AMQP broker
 * password: the password used to create connections to an AMQP broker
 * vhost: the virtual host used to create connections tto an AMQP broker
 * exchange: exchange where messages are sent
 * exchange_type: type of the exchange (direct, topic, fanout, headers)
 * routing_key : routing_key or queue name if no exchange are defined (see http://www.rabbitmq.com/tutorials/tutorial-four-java.html)
 * format : output format supported by flume (see http://archive.cloudera.com/cdh/3/flume/UserGuide/#_output_format) (optional, debug as default)
 * media_type : media type declaration for messages formatted with the output format (optional, 'application/octet-stream by default)

### Concrete example :

Assume the following declaration :

```
rabbit("host:port", "userName", "password", "/", "sample", "topic", "*.foo.*", "json", "application/json");
```

This sink declaration will send events with the json format to the topic "sample" with routing key "*.foo.*".
The media type used to send messages is "application/json".


### Download :

You can download jar at https://oss.sonatype.org/ searching for "flume-rabbitmq-plugin" :-)
