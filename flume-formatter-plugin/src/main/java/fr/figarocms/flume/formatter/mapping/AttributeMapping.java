package fr.figarocms.flume.formatter.mapping;

public class AttributeMapping {

  private String name;
  private String type;
  private String format;

  public void setName(String name) {
    this.name = name;
  }

  public void setType(String type) {
    this.type = type;
  }

  public void setFormat(String format) {
    this.format = format;
  }

  public String getName() {
    return name;
  }

  public String getType() {
    return type;
  }

  public String getFormat() {
    return format;
  }
}
