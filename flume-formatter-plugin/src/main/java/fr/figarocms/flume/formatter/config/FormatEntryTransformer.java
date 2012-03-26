package fr.figarocms.flume.formatter.config;

import com.google.common.collect.Maps;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.Nullable;

public class FormatEntryTransformer implements Maps.EntryTransformer<String, String, Object> {

  /**
   * These are useful to other classes which might want to search for tags in strings.
   */
  final public static String TAG_REGEX = "\\%(\\w|\\%)|\\%\\{([\\w\\.-]+)\\}";
  final public static Pattern tagPattern = Pattern.compile(TAG_REGEX);


  private Map<String, Object> objects;

  public FormatEntryTransformer(Map<String, Object> objects) {
    this.objects = objects;
  }

  @Override
  public Object transformEntry(@Nullable String key, @Nullable String value) {
    Matcher matcher = tagPattern.matcher(key);
    StringBuffer sb = new StringBuffer();
    while (matcher.find()) {
      // Group 2 is the %{...} pattern
      if (matcher.group(2) != null) {
        return objects.get(key);
      }
    }
    return null;
  }
}
