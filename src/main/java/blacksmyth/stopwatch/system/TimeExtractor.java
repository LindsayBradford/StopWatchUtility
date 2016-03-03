// (c) 2003 - Lindsay Bradford

package blacksmyth.stopwatch.system;

final class TimeExtractor {
  public static int getHours(long time) {
    return (int) (time / 3600000) % 100;
  }

  public static int getMinutes(long time) {
    return (int) (time / 60000) % 60;  
  }

  public static int getSeconds(long time) {
    return (int) (time / 1000) % 60;  
  }

  public static int getMilliseconds(long time) {
    return (int) (time % 1000);  
  }

}
