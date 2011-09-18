package me.heartmycity;

/**
 * @author Jason Axelson
 * 
 */
public class Utils {
  public static void checkNull(Object object, String description) {
    if (object == null) {
      throw new IllegalArgumentException(description + " cannot be null");
    }
  }
}
