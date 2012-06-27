/**
 * Licensed to Cloudera, Inc. under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  Cloudera, Inc. licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package fr.figarocms.flume.geoip;

import com.cloudera.flume.conf.SinkFactory.SinkDecoBuilder;
import com.cloudera.flume.core.Event;
import com.cloudera.flume.core.EventSink;
import com.cloudera.flume.core.EventSinkDecorator;
import com.cloudera.util.Pair;
import com.maxmind.geoip.Location;
import com.maxmind.geoip.LookupService;
import fr.figarocms.flume.geoip.utils.Bytes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @param <S>
 */
public class GeoIPExtractor<S extends EventSink> extends EventSinkDecorator<S> {

  protected static Logger LOG = LoggerFactory.getLogger(GeoIPExtractor.class);

  private LookupService lookupService;

  private String attributeName = "ip"; //event Field where to read IP address
  private String dstPrefix = "geoip"; //prefix for Fields of geoloc information in event

  /**
   * @param s             : event Sink from Flume
   * @param lookupService : GeoIP database interface
   */
  public GeoIPExtractor(S s, LookupService lookupService) {
    super(s);

    this.lookupService = lookupService;
  }

  /**
   * @param attributeName :  field where is stored IP address
   */
  public void setAttributeName(String attributeName) {
    this.attributeName = attributeName;
  }

  /**
   * @param dstPrefix : field prefix for geotag attributes
   */
  public void setDstPrefix(String dstPrefix) {
    this.dstPrefix = dstPrefix;
  }

  /**
   * @return name of the field containing IP address
   */
  public String getAttributeName() {
    return this.attributeName;
  }

  /**
   * @return prefix for the flieds containing geotag attributes add to event
   */
  public String getDstPrefix() {
    return this.dstPrefix;
  }

  /**
   * @param e : the event Object
   */
  @Override
  public void append(Event e) throws IOException, InterruptedException {
    if (e.getAttrs().containsKey(this.attributeName)) {
      Location l = lookupService.getLocation(new String(e.get(this.attributeName)));
      if (l != null) {
        if (l.city != null) {
          e.set(this.dstPrefix + ".city", l.city.getBytes());
        }
        if (l.countryName != null) {
          e.set(this.dstPrefix + ".countryName", l.countryName.getBytes());
        }
        if (l.countryCode != null) {
          e.set(this.dstPrefix + ".countryCode", l.countryCode.getBytes());
        }
        if (l.longitude != 0) {
          e.set(this.dstPrefix + ".longitude", Bytes.toBytes(l.longitude));
        }
        if (l.latitude != 0) {
          e.set(this.dstPrefix + ".latitude", Bytes.toBytes(l.latitude));
        }
      } else {
        LOG.warn("Unable to parse attribute '" + this.attributeName + "' as an IP");
      }
    } else {
      LOG.warn("Attribute '" + this.attributeName + "' not found in event");
    }

    super.append(e);
  }

  /**
   * @return a Sink decorator for GeoIPExtractor
   */
  public static SinkDecoBuilder builder() {
    return new GeoIPExtractorBuilder();
  }

  /**
   * This is a special function used by the SourceFactory to pull in this class as a flume-geoip-plugin decorator.
   *
   * @return a list of GeoIPExtractorBuilder
   */
  public static List<Pair<String, SinkDecoBuilder>> getDecoratorBuilders() {
    List<Pair<String, SinkDecoBuilder>> builders =
        new ArrayList<Pair<String, SinkDecoBuilder>>();
    builders.add(new Pair<String, SinkDecoBuilder>("geoip",
                                                   builder()));
    return builders;
  }

}
