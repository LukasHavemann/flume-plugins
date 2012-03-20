package fr.figarocms.flume.haproxy;


import com.cloudera.flume.conf.Context;
import com.cloudera.flume.core.EventSink;
import junit.framework.Assert;
import org.junit.Test;

import static org.mockito.Mockito.mock;


public class GeoIPExtractorBuilderTest {

    @Test
    public void nominalClasspathFile() {
        GeoIPExtractorBuilder gb = new GeoIPExtractorBuilder();
        GeoIPExtractor<EventSink> g = gb.build(mock(Context.class), new String[]{"/file.dat"});
        Assert.assertNotNull(g);
    }

    @Test
    public void nominalAbsoluteFile() {
        GeoIPExtractorBuilder gb = new GeoIPExtractorBuilder();
        String file = this.getClass().getResource("/file.dat").getFile();
        GeoIPExtractor<EventSink> g = gb.build(mock(Context.class), new String[]{file});
        Assert.assertNotNull(g);
    }

    @Test(expected = IllegalArgumentException.class)
    public void oneArgumentIsRequired() {
        GeoIPExtractorBuilder gb = new GeoIPExtractorBuilder();
        GeoIPExtractor<EventSink> g = gb.build(mock(Context.class));
    }

    @Test(expected = IllegalArgumentException.class)
    public void firstArgumentMustBeAnExistingFile() {
        GeoIPExtractorBuilder gb = new GeoIPExtractorBuilder();
        GeoIPExtractor<EventSink> g = gb.build(mock(Context.class), new String[]{"nofile.dat"});
    }

    @Test
    public void ArgumentsSrcFields() {
        GeoIPExtractorBuilder gb = new GeoIPExtractorBuilder();
        String file = this.getClass().getResource("/file.dat").getFile();
        GeoIPExtractor<EventSink> g = gb.build(mock(Context.class), new String[]{file, "plop"});
        Assert.assertNotNull(g);
        Assert.assertEquals(g.getAttributeName(),"plop");
    }

    @Test
    public void ArgumentsSrcDstFields() {
        GeoIPExtractorBuilder gb = new GeoIPExtractorBuilder();
        String file = this.getClass().getResource("/file.dat").getFile();
        GeoIPExtractor<EventSink> g = gb.build(mock(Context.class), new String[]{file, "plop", "paf"});
        Assert.assertNotNull(g);
        Assert.assertEquals(g.getAttributeName(),"plop");
        Assert.assertEquals(g.getDstPrefix(),"paf");
    }

    @Test(expected = IllegalArgumentException.class)
    public void tooManyArguments() {
        GeoIPExtractorBuilder gb = new GeoIPExtractorBuilder();
        String file = this.getClass().getResource("/file.dat").getFile();
        GeoIPExtractor<EventSink> g = gb.build(mock(Context.class), new String[]{file, "plop", "paf", "pim"});
    }

    @Test(expected = IllegalArgumentException.class)
    public void fileNotReadable() {
        GeoIPExtractorBuilder gb = new GeoIPExtractorBuilder();
        String file = this.getClass().getResource("/bad.dat").getFile();
        GeoIPExtractor<EventSink> g = gb.build(mock(Context.class), new String[]{file});
    }

}
