package fr.figarocms.flume.formatter.config;

import java.util.List;
import java.util.Map;

public class MappingConfiguration {

  private Mapping body;
  private Mapping priority;
  private Mapping host;
  private List<Mapping> attributes;

  public Mapping getBody() {
    return body;
  }

  public void setBody(Mapping body) {
    this.body = body;
  }
  
  public Mapping getPriority() {
    return priority;
  }

  public void setPriority(Mapping priority) {
    this.priority = priority;
  }

  public Mapping getHost() {
    return host;
  }

  public void setHost(Mapping host) {
    this.host = host;
  }

  public List<Mapping> getAttributes() {
    return attributes;
  }

  public void setAttributes(List<Mapping> attributes) {
    this.attributes = attributes;
  }

}
