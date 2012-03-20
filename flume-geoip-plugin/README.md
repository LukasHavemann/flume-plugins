# Flume Geoip Extractor

Augments event with IP localisation based on MaxMind GeoIPhttp://www.maxmind.com/app/ip-location

## Installation :

 * Add the jar to the FLUME_CLASSPATH
 * Add the MaxMind database file in a location accessible form FLUME_CLASSPATH or specify full path in configuration
 * Add the plugin to your flume-config.xml

```
  <property>
    <name>flume.plugin.classes</name>                                         
    <value>fr.figarocms.flume.extractor.GeoIPExtractor</value>
    <description>Comma separated list of plugins</description>
  </property>
```

## Usage :
Assuming there is a attribute "ip" in event with value "213.52.50.8" for example.
The following FLume configuration :
GeoIPExtractor("/city.dat")

or specify the event field where the IP adress is stored :

```
geoip(<filename>,[field],[prefix])
```

  * filename : is the database file from MaxMind full path or accessible via classpath.
  * field : the event field where is store the IP address to be geolocalised. Default is "ip".
  * prefix: the prefix for fieldname where will be store geolocalisation information (as prefix.city, prefix.countryName, prefix.countryCode, prefix.latitude and prefix.longitude). Default is "geoip".

Will eventually add the following attributes to the event

| **name**           | **value**                     |
|--------------------|-------------------------------|
| geoip.city | "Oslo" |
| geoip.countryName | "Norway" |
| geoip.countryCode | "NW" |
| geoip.longitude | 10 (Bytes array of Float representation) |
| geoip.latitude | 62 (Bytes array of Float representation) |

You have to add a MaxMind binary database file your CLASSPATH (named GeoLiteCity.dat).

Concrete example :

echo "test of IP 90.16.13.14 for geolocalisation" | \
$> flume node_nowatch -1 -n me -c 'me:console|regex("\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}",0,"ip") GeoIPExtractor("/GeoLiteCity.dat") console;'

desktop [INFO <DATE>] { geoip.city : Saint-sauvant } { geoip.countryCode : FR } { geoip.countryName : France } { geoip.latitude : B6�� } { geoip.longitude : � } { ip : 90.16.13.14 } test of IP 90.16.13.14 for geolocalisation

Or specifying the event field where is stored the IP address

$> echo "test of IP 90.16.13.14 for geolocalisation" | flume node_nowatch -1 -n me -c 'me:console|regex("\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}",0,"meta.ip") GeoIPExtractor("/GeoLiteCity.dat","meta.ip") console;'

desktop [INFO <DATE>] { geoip.city : Saint-sauvant } { geoip.countryCode : FR } { geoip.countryName : France } { geoip.latitude : B6�� } { geoip.longitude : � } { meta.ip : 90.16.13.14 } test of IP 90.16.13.14 for geolocalisation

You can download jar at https://oss.sonatype.org/ searching for "geoip" :)

