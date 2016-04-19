/**
 * Copyright (c) 2016, Lindsay Bradford and other Contributors.
 * All rights reserved.
 * 
 * This program and the accompanying materials  are made available 
 * under the terms of the BSD 3-Clause licence  which accompanies 
 * this distribution, and is available at
 * http://opensource.org/licenses/BSD-3-Clause
 */

package blacksmyth.stopwatch.model;

/**
 * A class based on the Builder design pattern for the construction of a Swing-based {@link StopWatchModel} via
 * dependency injection. 
 */

public final class StopWatchModelBuilder {
  
  public static StopWatchModel build() {
    final int MILLISECONDS_BETWEEN_TICKS = 50;
    
    Ticker ticker = new SleepingThreadTicker();
    ticker.setMillisecondsBetweenTicks(MILLISECONDS_BETWEEN_TICKS);
    
    DefaultStopWatchModel stopwatch = new DefaultStopWatchModel();
    
    ticker.setRecipient(stopwatch);
    ticker.startTicking();
    
    return stopwatch;
  }
}
