package fr.figarocms.flume.geoip;

import com.cloudera.flume.core.Event;
import com.cloudera.flume.core.EventImpl;
import com.cloudera.flume.handlers.debug.MemorySinkSource;
import com.maxmind.geoip.Location;
import com.maxmind.geoip.LookupService;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.slf4j.Logger;

import java.io.IOException;

import fr.figarocms.flume.geoip.utils.Bytes;

import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class GeoIPExtractorTest {

  private GeoIPExtractor<MemorySinkSource> geoIPExtractor;

  @Mock
  private Logger LOG;

  @Mock
  private LookupService lookupService;

  @Before
  public void initialize() {

    MockitoAnnotations.initMocks(this);

    geoIPExtractor = new GeoIPExtractor<MemorySinkSource>(new MemorySinkSource(), this.lookupService);
    GeoIPExtractor.LOG = LOG;
  }

  @Test
  public void testAppend() throws IOException, InterruptedException {

    Location l = new Location();
    l.countryName = "Norway";
    l.countryCode = "NW";
    l.city = "Oslo";
    l.latitude = 62;
    l.longitude = 10;

    when(lookupService.getLocation(anyString())).thenReturn(l);
    Event e = new EventImpl();

    e.set("ip", "213.52.50.8".getBytes());
    openAppendClose(e);

    Assert.assertEquals("Norway", new String(e.get("geoip.countryName")));
    Assert.assertEquals("NW", new String(e.get("geoip.countryCode")));
    Assert.assertEquals("Oslo", new String(e.get("geoip.city")));
    Assert.assertEquals(62F, Bytes.toFloat(e.get("geoip.latitude")));
    Assert.assertEquals(10F, Bytes.toFloat(e.get("geoip.longitude")));

  }

  @Test
  public void testAppendNoField() throws IOException, InterruptedException {

    when(lookupService.getLocation(anyString())).thenReturn(null);
    Event e = new EventImpl();

    openAppendClose(e);

    verify(LOG).warn("no Field 'ip' in event");
  }

  @Test
  public void testAppendNotIP() throws IOException, InterruptedException {

    when(lookupService.getLocation(anyString())).thenReturn(null);
    Event e = new EventImpl();
    e.set("ip", "toto".getBytes());

    openAppendClose(e);

    verify(LOG).warn("Unable to parse IP in 'ip'");
  }

  private void openAppendClose(Event e) throws IOException, InterruptedException {
    geoIPExtractor.open();
    geoIPExtractor.append(e);
    geoIPExtractor.close();
  }
}
