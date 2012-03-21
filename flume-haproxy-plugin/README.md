# Flume HAProxy Plugin

Event Sink Decorator which parse [HAProxy log format](http://code.google.com/p/haproxy-docs/wiki/HTTPLogFormat) in order to augment Flume Event attributes

## Installation :

 * Add the jar to the FLUME_CLASSPATH
 * depends on joda-time (http://joda-time.sourceforge.net/) and jregex (http://jregex.sourceforge.net/) Java library, they should be available from CLASSPATH too
 * Add the plugin to your flume-config.xml

```
  <property>
    <name>flume.plugin.classes</name>                                         
    <value>fr.figarocms.flume.extractor.HAProxyLogExtractor</value>
    <description>Comma separated list of plugins</description>
  </property>
```

## Usage :

Assuming the following log line :

```
Jul  1 17:22:59 ip6-localhost haproxy[32574]: 192.168.4.45:59901 [01/Jul/2011:17:22:59.083] myfrontend mybackend/mybackendserver 34/0/0/1/35 404 454 - - ---- 1/1/0/1/0 0/0 {myhostname||Mozilla/5.0 (Windows NT 6.1; WOW64; rv:5.0) Gecko/20100101 Firefox/5.0} "GET /index.php/foo?bar=1 HTTP/1.1"
```

The following configuration


```
haproxy()
```

Will add the following attributes to the event :


| **name**                   | **value**                     |
|----------------------------|-------------------------------|
| haproxy.clientIp           | 192.168.4.45                  |
| haproxy.clientPort         | 59901                         |
| haproxy.timestamp          | 2011-07-01T17:22:59.083+02:00 |
| haproxy.frontend           | myfrontend                    |
| haproxy.backend            | mybackend                     |
| haproxy.host               | mybackendserver               |
| haproxy.times              | 34/0/0/1/35                   |
| haproxy.statusCode         | 404                           |
| haproxy.byteRead           | 454                           |
| haproxy.capturedRequestCookie | -                          |
| haproxy.capturedResponseCookie | -                         |
| haproxy.terminationState   | ----                          |
| haproxy.connStates         | 1/1/0/1/0                     |
| haproxy.queuesStates       | 0/0                           |
| haproxy.capturedRequestHeaders.0 | myhostname              |
| haproxy.capturedRequestHeaders.1 |                         |
| haproxy.capturedRequestHeaders.2 | Mozilla/5.0 (Windows NT 6.1; WOW64; rv:5.0) Gecko/20100101 Firefox/5.0 |
| haproxy.verb               | GET                           |
| haproxy.uri                | /index.php/foo?bar=1          |
| haproxy.version            | HTTP/1.1                      |

Example :

```
echo 'Jul  1 17:22:59 ip6-localhost haproxy[32574]: 192.168.4.45:59901 [01/Jul/2011:17:22:59.083] myfrontend mybackend/mybackendserver 34/0/0/1/35 404 454 - - ---- 1/1/0/1/0 0/0 {myhostname||Mozilla/5.0 (Windows NT 6.1; WOW64; rv:5.0) Gecko/20100101 Firefox/5.0} "GET /index.php/foo?bar=1 HTTP/1.1"' | flume node_nowatch -1 -n me -c 'me:console|haproxy() console;'
```

```
myhost [INFO Thu Dec 01 17:43:06 CET 2011] { haproxy.backend : mybackend } { haproxy.byteRead : 454 } { haproxy.capturedRequestCookie : 45 } { haproxy.capturedRequestHeaders.0 : myhostname } { haproxy.capturedRequestHeaders.1 :  } { haproxy.capturedRequestHeaders.2 : Mozilla/5.0 (Windows NT 6.1; WOW64; rv:5.0) Gecko/20100101 Firefox/5.0 } { haproxy.capturedResponseCookie : 45 } { haproxy.clientIp : 192.168.4.45 } { haproxy.clientPort : 59901 } { haproxy.connStates : 1/1/0/1/0 } { haproxy.frontend : myfrontend } { haproxy.host : mybackendserver } { haproxy.queuesStates : 0/0 } { haproxy.uri : /index.php/foo?bar=1 } { haproxy.verb : GET } { haproxy.version : (long)5211883372140375601  (string) 'HTTP/1.1' (double)2.7670875323932447E40 } { haproxy.statusCode : 404 } { haproxy.terminationState : 757935405 '----' } { haproxy.times : 34/0/0/1/35 } { haproxy.timestamp : 2011-07-01T17:22:59.083+02:00 } Jul  1 17:22:59 ip6-localhost haproxy[32574]: 192.168.4.45:59901 [01/Jul/2011:17:22:59.083] myfrontend mybackend/mybackendserver 34/0/0/1/35 404 454 - - ---- 1/1/0/1/0 0/0 {myhostname||Mozilla/5.0 (Windows NT 6.1; WOW64; rv:5.0) Gecko/20100101 Firefox/5.0} \"GET /index.php/foo?bar=1 HTTP/1.1\"
```
### Download :

You can download jar at https://oss.sonatype.org/ searching for "flume-haproxy-plugin" :-)

