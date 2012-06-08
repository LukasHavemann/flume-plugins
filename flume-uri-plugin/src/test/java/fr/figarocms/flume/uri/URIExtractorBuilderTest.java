package fr.figarocms.flume.uri;

import com.cloudera.flume.conf.Context;
import com.cloudera.flume.core.EventSink;
import com.cloudera.flume.core.EventSinkDecorator;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;

public class URIExtractorBuilderTest {

    URIExtractorBuilder builder;

    @Before
    public void setUp() throws Exception {
        builder = new URIExtractorBuilder();
    }

    @Test(expected = IllegalArgumentException.class)
    public void buildWithoutArgument() throws Exception {
        // When
        builder.build(mock(Context.class));

        // Then throw new IllegalArgumentException
    }

    @Test
    public void buildOneArgument() throws Exception {
        // When
        final EventSinkDecorator<EventSink> decorator = builder.build(mock(Context.class), "field");

        // Then
        assertNotNull(decorator);
    }

    @Test
    public void buildTwoArgument() throws Exception {
        // When
        final EventSinkDecorator<EventSink> decorator = builder.build(mock(Context.class), "field", "prefix");

        // Then
        assertNotNull(decorator);
    }

    @Test(expected = IllegalArgumentException.class)
    public void buildMoreThanTwoArgument() throws Exception {
        // When
        builder.build(mock(Context.class), "field", "prefix", "other");

        // Then throw new IllegalArgumentException
    }
}
