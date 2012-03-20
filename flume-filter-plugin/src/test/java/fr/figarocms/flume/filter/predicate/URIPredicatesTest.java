package fr.figarocms.flume.filter.predicate;

import com.google.common.base.Predicate;
import com.google.common.base.Predicates;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

@RunWith(Suite.class)
@Suite.SuiteClasses({URIPredicatesTest.ContainsParameter.class})
public class URIPredicatesTest {

  public static class ContainsParameter {

    @Test
    public void match() throws Exception {
      Predicate<String> predicate = URIPredicates.containsParameter("p1", Predicates.equalTo("v1"));

      assertTrue(predicate.apply("http://example.com/path?p1=v1"));
    }

    @Test
    public void mismatch() throws Exception {
      Predicate<String> predicate = URIPredicates.containsParameter("p1", Predicates.equalTo("v1"));

      assertFalse(predicate.apply("not an URI"));
      assertFalse(predicate.apply("http://example.com/path"));
      assertFalse(predicate.apply("http://example.com/path?p2=v2"));
      assertFalse(predicate.apply("http://example.com/path?p1=v2"));
    }

  }
}
