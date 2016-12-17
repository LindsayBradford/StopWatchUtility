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
    return bind(
    		buildStopWatchModel(),
    		buildTicker()
    );
  }

  private static StopWatchModel buildStopWatchModel() {
    return new DefaultStopWatchModel();
  }	    
  
  private static Ticker buildTicker() {
    final int millisecondsBetweenTicks = 50;

    Ticker ticker = new SleepingThreadTicker();
    ticker.setMillisecondsBetweenTicks(millisecondsBetweenTicks);
    return ticker;	
  }
  
  private static StopWatchModel bind(StopWatchModel model, Ticker ticker) {
    ticker.setRecipient(model);
    ticker.startTicking();
	  
    return model;
  }
}
