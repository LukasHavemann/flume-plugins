package fr.figarocms.flume.filter.predicate;

import com.google.common.base.Functions;
import com.google.common.base.Predicate;
import com.google.common.base.Predicates;

import java.net.URI;

import javax.annotation.Nullable;

public class URIPredicates {

  // ~ Static methods ------------------------------------------------------------------------------------------------

  public static Predicate<String> containsParameter(String name, Predicate<? super String> predicate) {
    return Predicates.and(isURI(),
                          Predicates.compose(
                              predicate,
                              Functions.compose(URIFunctions.getValue(name), URIFunctions.uriAsString())
                          )
    );
  }

  public static Predicate<String> isURI() {
    return IsURI.INSTANCE;
  }

  // ~ Predicates ----------------------------------------------------------------------------------------------------

  // enum singleton pattern
  private static enum IsURI implements Predicate<String> {
    INSTANCE;

    @Override
    public boolean apply(@Nullable String input) {
      try {
        URI.create(input);
      } catch (Exception e) {
        return false;
      }
      return true;
    }

    @Override
    public String toString() {
      return getClass().getSimpleName();
    }

  }

  private URIPredicates() {
  }
}
