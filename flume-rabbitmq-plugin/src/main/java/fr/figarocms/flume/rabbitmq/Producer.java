package fr.figarocms.flume.rabbitmq;

import java.io.IOException;

public interface Producer {

  void open() throws IOException;

  void close() throws IOException;

  void publish(byte[] msg, String mediaType) throws IOException;

}
