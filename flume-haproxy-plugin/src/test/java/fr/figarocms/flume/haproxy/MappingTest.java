package fr.figarocms.flume.haproxy;

import com.cloudera.flume.core.Event;
import com.cloudera.flume.core.EventImpl;
import fr.figarocms.flume.haproxy.processor.HeaderProcessor;
import fr.figarocms.flume.haproxy.processor.SimpleProcessor;
import org.junit.Test;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertSame;

public class MappingTest {

    @Test
    public void testProcess() throws Exception {
        Event e = new EventImpl("Jul  1 17:22:59 ip6-localhost haproxy[32574]: 192.168.4.45:59901 [01/Jul/2011:17:22:59.083] myfrontend mybackend/myserverbackend 34/0/0/0/35 404 454 - - ---- 1/1/0/1/0 0/0 {myhost|http://www.explorimmo.com/annonce-22510491-1.html|Mozilla/5.0 (Windows NT 6.1; WOW64; rv:5.0) Gecko/20100101 Firefox/5.0} \"GET /index.php/foo?bar=1 HTTP/1.1\"".getBytes());
        Mapping map = new Mapping();

        map.addEntry("clientIp");
        map.process(e);
        assertEquals("192.168.4.45", new String(e.get("haproxy.clientIp")));
    }

    @Test
    public void testAddEntry() throws Exception {

        Mapping map = new Mapping();

        SimpleProcessor p1 = new SimpleProcessor();
        HeaderProcessor p2 = new HeaderProcessor();

        map.addEntry("toto")
                .addEntry("tata", p2);

        assertEquals(map.getList().size(), 2);

        assertEquals(map.getList().get(0).getProperty(), "toto");
        assertEquals(map.getList().get(0).getProcessor().getClass(), p1.getClass());

        assertEquals(map.getList().get(1).getProperty(), "tata");
        assertSame(map.getList().get(1).getProcessor().getClass(), p2.getClass());
    }

}
