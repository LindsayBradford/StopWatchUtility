package blacksmyth.stopwatch;

import blacksmyth.stopwatch.model.*;

public class StopWatchBuilder {
  
  private static int MILLISECONDS_BETWEEN_TICKS = 50;
  
  public static BasicStopWatchModel buildModel() {
    
    Ticker ticker = new SleepingThreadTicker();
    ticker.setMillisecondsBetweenTicks(MILLISECONDS_BETWEEN_TICKS);
    
    BasicStopWatchModel stopwatch = new BasicStopWatchModel();
    
    ticker.setRecipient(stopwatch);
    ticker.startTicking();
    
    return stopwatch;
  }

}
