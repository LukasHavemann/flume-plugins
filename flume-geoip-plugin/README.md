# Flume Geoip Extractor

Event Sink Decorator which augments Flume Event with IP localisation based on [MaxMind GeoIP](http://www.maxmind.com/app/ip-location)

## Installation :

 * Add the jar to the FLUME_CLASSPATH
 * Add the MaxMind database file in a location accessible form FLUME_CLASSPATH or specify full path in configuration
 * Add the plugin to your flume-config.xml

```
  <property>
    <name>flume.plugin.classes</name>                                         
    <value>fr.figarocms.flume.geoip.GeoIPExtractor</value>
    <description>Comma separated list of plugins</description>
  </property>
```

## Usage :

```
geoip(<filename>,[field],[prefix])
```

  * filename : is the database file from MaxMind full path or accessible via classpath.
  * field : the event field where is store the IP address to be geolocalised. Default is "ip".
  * prefix: the prefix for fieldname where will be store geolocalisation information (as prefix.city, prefix.countryName, prefix.countryCode, prefix.latitude and prefix.longitude). Default is "geoip".


### Concrete example :

Assuming there is a attribute "ip" in event with value "213.52.50.8" for example.

The following Flume configuration :

```
geoip("city.dat", "ip")
```

should add for example the following attributes to the event :


| **name**           | **value**                     |
|--------------------|-------------------------------|
| geoip.city | "Oslo" |
| geoip.countryName | "Norway" |
| geoip.countryCode | "NW" |
| geoip.longitude | 10 (Bytes array of Float representation) |
| geoip.latitude | 62 (Bytes array of Float representation) |


### Download :

You can download jar at https://oss.sonatype.org/ searching for "flume-geoip-plugin" :-)

