package blacksmyth.stopwatch;

import blacksmyth.stopwatch.view.StopWatchView;

public class StopWatchUtility {
  public static void main(String argv[]) {
    StopWatchView view = StopWatchBuilder.buildUtility();
    view.show();
  }
}