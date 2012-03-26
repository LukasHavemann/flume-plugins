package fr.figarocms.flume.formatter.config;

import com.google.common.base.Function;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.Nullable;

public class FormatValueTransformer implements Function<Object, Object> {

  final public static String TAG_REGEX = "\\%(\\w|\\%)|\\%\\{([\\w\\.-]+)\\}";
  final public static Pattern TAG_PATTERN = Pattern.compile(TAG_REGEX);

  private Map<String, Object> objects;


  public FormatValueTransformer(Map<String, Object> objects) {
    this.objects = objects;
  }

  @Override
  public Object apply(@Nullable Object value) {
    if (value instanceof String) {
      Matcher matcher = TAG_PATTERN.matcher((String) value);
      while (matcher.find()) {
        // Group 2 is the %{...} pattern
        final String tag = matcher.group(2);
        if (tag != null) {
          return objects.get(tag);
        }
      }
    }
    return value;
  }
}
