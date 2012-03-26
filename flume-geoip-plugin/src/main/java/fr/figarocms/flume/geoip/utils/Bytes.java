package fr.figarocms.flume.geoip.utils;

import java.nio.ByteBuffer;

/**
 * Simple Class to manipulate ByteArray as Float and reverse.
 */
public class Bytes {

  /**
   * @param f : float value
   * @return value as Byte Array
   */
  public static byte[] toBytes(Float f) {

    byte[] data = new byte[Float.SIZE];
    ByteBuffer buffer = ByteBuffer.wrap(data);
    buffer.putFloat(f);

    return data;
  }

  /**
   * @param b : Byte Array storing Float value
   * @return value as Float
   */
  public static Float toFloat(byte[] b) {

    ByteBuffer buffer = ByteBuffer.wrap(b);

    return buffer.getFloat();
  }
}
