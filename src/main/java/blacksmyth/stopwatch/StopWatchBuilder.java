package blacksmyth.stopwatch;

import blacksmyth.stopwatch.model.*;

public class StopWatchBuilder {
  
  private static int MILLISECONDS_BETWEEN_TICKS = 50;
  
  public static SimpleStopWatch buildModel() {
    
    Ticker threadTicker = new SleepingThreadTicker();
    threadTicker.setMillisecondsBetweenTicks(MILLISECONDS_BETWEEN_TICKS);
    
    SimpleStopWatch stopwatch = new SimpleStopWatch();
    
    threadTicker.setRecipient(stopwatch);
    threadTicker.startTicking();
    
    return stopwatch;
  }

}
