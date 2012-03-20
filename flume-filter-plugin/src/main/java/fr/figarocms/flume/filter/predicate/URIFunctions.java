package fr.figarocms.flume.filter.predicate;

import com.google.common.base.Function;
import com.google.common.base.Functions;
import com.google.common.base.Objects;
import com.google.common.base.Preconditions;

import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URLEncodedUtils;

import java.net.URI;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import static com.google.common.collect.Maps.newHashMap;
import static java.nio.charset.Charset.defaultCharset;

/**
 * Static utility methods pertaining to {@link java.net.URI} functions.
 */
public class URIFunctions {

  // ~ Static methods ------------------------------------------------------------------------------------------------

  public static Function<String, URI> uriAsString() {
    return StringToURI.INSTANCE;
  }

  public static Function<URI, Map<String, String>> getParameters() {
    return URIParameters.INSTANCE;
  }

  public static Function<URI, String> getValue(String parameter) {
    return Functions.compose(getEntryValue(parameter), getParameters());
  }

  private static Function<Map<String, String>, String> getEntryValue(String parameter) {
    return new GetEntryValue(parameter);
  }

  // ~ Functions ----------------------------------------------------------------------------------------------------

  // enum singleton pattern
  private static enum StringToURI implements Function<String, URI> {

    INSTANCE;

    @Override
    public URI apply(@Nonnull String input) {
      return URI.create(input);
    }

    @Override
    public String toString() {
      return getClass().getSimpleName();
    }

  }


  // enum singleton pattern
  private static enum URIParameters implements Function<URI, Map<String, String>> {

    INSTANCE;

    @Override
    public Map<String, String> apply(@Nonnull URI uri) {
      List<NameValuePair> pairs = URLEncodedUtils.parse(uri, defaultCharset().name());
      Map<String, String> parameters = NameValuePairsToMap.INSTANCE.apply(pairs);
      return parameters;
    }

    @Override
    public String toString() {
      return getClass().getSimpleName();
    }

    // enum singleton pattern
    private enum NameValuePairsToMap implements Function<List<NameValuePair>, Map<String, String>> {

      INSTANCE;

      @Override
      public Map<String, String> apply(@Nullable List<NameValuePair> pairs) {
        Map<String, String> map = newHashMap();
        for (NameValuePair pair : pairs) {
          map.put(pair.getName(), pair.getValue());
        }
        return map;
      }

      @Override
      public String toString() {
        return getClass().getSimpleName();
      }
    }


    private static Function<URI, Set<String>> names() {
      return Functions.compose(KeySet.INSTANCE, INSTANCE);
    }

    // enum singleton pattern
    private enum KeySet implements Function<Map<String, String>, Set<String>> {

      INSTANCE;

      @Override
      public Set<String> apply(@Nullable Map<String, String> map) {
        return map.keySet();
      }

      @Override
      public String toString() {
        return getClass().getSimpleName();
      }
    }
  }

  private static class GetEntryValue implements Function<Map<String, String>, String> {

    private final String parameter;

    public GetEntryValue(String parameter) {
      this.parameter = Preconditions.checkNotNull(parameter);
    }

    @Override
    public String apply(@Nullable Map<String, String> input) {
      return input == null ? null : input.get(parameter);
    }

    @Override
    public boolean equals(Object o) {
      if (this == o) {
        return true;
      }
      if (o == null || getClass() != o.getClass()) {
        return false;
      }

      GetEntryValue that = (GetEntryValue) o;

      return Objects.equal(this.parameter, that.parameter);
    }

    @Override
    public int hashCode() {
      return Objects.hashCode(parameter);
    }

    @Override
    public String toString() {
      return Objects.toStringHelper(this)
          .addValue(parameter)
          .toString();
    }
  }

  private URIFunctions() {
  }

}
