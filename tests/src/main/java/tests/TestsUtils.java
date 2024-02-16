package tests;

public class TestsUtils {

  private TestsUtils() {}

  public static String createObjectId(String id) {
    if (id.length() > 24) {
      throw new IllegalArgumentException("Id length must be 24 characters or less.");
    }
    StringBuilder sb = new StringBuilder(id);
    while (sb.length() < 24) {
      sb.insert(0, "0");
    }
    return sb.toString();
  }
}
