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
 * An implementation of the {@link Ticker} interface that is essentially 
 * a thread, configured to sleep between ticks.
 */
public final class SleepingThreadTicker extends Thread implements Ticker {

  private int millisecondsBetweenTicks;
  private TickRecipient recipient;
  
  public SleepingThreadTicker() {
    setName("ThreadTicker");
  }

  @Override
  public void setMillisecondsBetweenTicks(int milliseconds) {
    this.millisecondsBetweenTicks = milliseconds;
  }
  
  @Override
  public void setRecipient(TickRecipient recipient) {
    this.recipient = recipient;
  }
  
  @Override
  public void startTicking() {
    this.start();
  }
  
  public void run() {
    assert recipient != null;
    
    while(recipient.needsTicks()) {
      try {
        sleep(millisecondsBetweenTicks);
      } catch (Exception e) {
        e.printStackTrace();
      }
      recipient.receiveTick();
    }
  }
}
