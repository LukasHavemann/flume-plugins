package fr.figarocms.flume.geoip;

import com.cloudera.flume.core.Event;
import com.cloudera.flume.core.EventImpl;
import com.cloudera.flume.handlers.debug.MemorySinkSource;

import org.junit.Test;

import java.io.IOException;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;

public class HaproxyLogExtractorTest {


  @Test
  public void parseHAProxyLog1_3() throws IOException, InterruptedException {
    // Given
    String body = "Mar 18 23:04:17 geoip[9859]: 81.57.246.202:62938 "
                  + "[18/Mar/2012:23:04:17.937] squid squid/adencache02 6/0/1/2/42 204 352 - "
                  + "- ---- 166/32/131/119/0 0/0 "
                  + "{sample.domain.com|http://sample.domain.com/foo|Mozilla/5.0 "
                  + "(Windows NT 6.1; WOW64) AppleWebKit/535.11 (KHTML, like Gecko) "
                  + "Chrome/17.0.963.79 Safari/535.11} \"GET "
                  + "/foo?bar=baz "
                  + "HTTP/1.1\"";

    // When
    Event e = runEvent(body);

    // Then
    assertEquals("81.57.246.202", new String(e.get("geoip.clientIp")));
    assertEquals("62938", new String(e.get("geoip.clientPort")));
    assertEquals("2012-03-18T23:04:17.937+01:00", new String(e.get("geoip.timestamp")));
    assertEquals("squid", new String(e.get("geoip.frontend")));
    assertEquals("squid", new String(e.get("geoip.backend")));
    assertEquals("adencache02", new String(e.get("geoip.host")));
    assertEquals("6/0/1/2/42", new String(e.get("geoip.times")));
    assertEquals("204", new String(e.get("geoip.statusCode")));
    assertEquals("352", new String(e.get("geoip.byteRead")));
    assertEquals("-", new String(e.get("geoip.capturedRequestCookie")));
    assertEquals("-", new String(e.get("geoip.capturedResponseCookie")));
    assertEquals("----", new String(e.get("geoip.terminationState")));
    assertEquals("166/32/131/119/0", new String(e.get("geoip.connStates")));
    assertEquals("0/0", new String(e.get("geoip.queuesStates")));
    assertEquals("sample.domain.com", new String(e.get("geoip.capturedRequestHeaders.0")));
    assertEquals("http://sample.domain.com/foo",
                 new String(e.get("geoip.capturedRequestHeaders.1")));
    assertEquals(
        "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/535.11 (KHTML, like Gecko) Chrome/17.0.963.79 Safari/535.11",
        new String(e.get("geoip.capturedRequestHeaders.2")));
    assertEquals("GET", new String(e.get("geoip.verb")));
    assertEquals("/foo?bar=baz", new String(e.get("geoip.uri")));
    assertEquals("HTTP/1.1", new String(e.get("geoip.version")));
  }


  @Test
  public void parseHAProxyLog1_4() throws IOException, InterruptedException {

    String
        body =
        "Jul  1 17:22:59 ip6-localhost geoip[32574]: 192.168.4.45:59901 [01/Jul/2011:17:22:59.083] myfrontend mybackend/myserverbackend 34/0/0/0/35 404 454 - - ---- 1/1/0/1/0 0/0 {myhost|http://www.explorimmo.com/annonce-22510491-1.html|Mozilla/5.0 (Windows NT 6.1; WOW64; rv:5.0) Gecko/20100101 Firefox/5.0} \"GET /index.php/foo?bar=1 HTTP/1.1\"";
    Event
        e = runEvent(body);

    assertEquals("192.168.4.45", new String(e.get("geoip.clientIp")));
    assertEquals("59901", new String(e.get("geoip.clientPort")));
    assertEquals("2011-07-01T17:22:59.083+02:00", new String(e.get("geoip.timestamp")));
    assertEquals("mybackend", new String(e.get("geoip.backend")));
    assertEquals("myfrontend", new String(e.get("geoip.frontend")));
    assertEquals("myserverbackend", new String(e.get("geoip.host")));
    assertEquals("34/0/0/0/35", new String(e.get("geoip.times")));
    assertEquals("404", new String(e.get("geoip.statusCode")));
    assertEquals("454", new String(e.get("geoip.byteRead")));
    assertEquals("-", new String(e.get("geoip.capturedRequestCookie")));
    assertEquals("-", new String(e.get("geoip.capturedResponseCookie")));
    assertEquals("----", new String(e.get("geoip.terminationState")));
    assertEquals("1/1/0/1/0", new String(e.get("geoip.connStates")));
    assertEquals("0/0", new String(e.get("geoip.queuesStates")));
    assertEquals("myhost", new String(e.get("geoip.capturedRequestHeaders.0")));
    assertEquals("http://www.explorimmo.com/annonce-22510491-1.html",
                 new String(e.get("geoip.capturedRequestHeaders.1")));
    assertEquals("Mozilla/5.0 (Windows NT 6.1; WOW64; rv:5.0) Gecko/20100101 Firefox/5.0",
                 new String(e.get("geoip.capturedRequestHeaders.2")));
    assertEquals("GET", new String(e.get("geoip.verb")));
    assertEquals("/index.php/foo?bar=1", new String(e.get("geoip.uri")));
    assertEquals("HTTP/1.1", new String(e.get("geoip.version")));
  }


  @Test
  public void parseBodyWithoutHeaders() throws IOException, InterruptedException {

    Event
        e =
        runEvent(
            "Jul  1 17:22:59 ip6-localhost geoip[32574]: 192.168.4.45:59901 [01/Jul/2011:17:22:59.083] myfrontend mybackend/myserverbackend 34/0/0/0/35 404 454 - - ---- 1/1/0/1/0 0/0 {} {} \"GET /index.php/foo?bar=1 HTTP/1.1\"");

    assertEquals("uri was properly parsed", "/index.php/foo?bar=1", new String(e.get("geoip.uri")));
    assertFalse("referrer was not added to event", e.getAttrs().containsKey("geoip.referrer"));
  }


  @Test
  public void parseInvalidBody() throws IOException, InterruptedException {

    MemorySinkSource mem = new MemorySinkSource();

    HaproxyLogExtractor extractor = new HaproxyLogExtractor(mem);

    Event
        e =
        runEvent(
            "Jul  1 17:22:59 ip6-localhost geoip[32574]: 192.168.4.45:59901 [] myfrontend mybackend/pimbft02 34/0/0/1/35 404 454 - - ---- 1/1/0/1/0 0/0 {myhost||Mozilla/5.0 (Windows NT 6.1; WOW64; rv:5.0) Gecko/20100101 Firefox/5.0} {} \"GET /index.php/foo?bar=1 HTTP/1.1\"");
  }

  /**
   * @param body : body of the event is geoip syslog
   * @return a Flume Event
   */
  private EventImpl runEvent(String body) throws IOException, InterruptedException {

    MemorySinkSource mem = new MemorySinkSource();

    HaproxyLogExtractor extractor = new HaproxyLogExtractor(mem);

    EventImpl e = new EventImpl(body.getBytes());
    extractor.open();
    extractor.append(e);
    extractor.close();

    return e;
  }
}
