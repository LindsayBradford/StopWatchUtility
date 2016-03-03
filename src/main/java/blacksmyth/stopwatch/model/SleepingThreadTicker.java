// (c) 2016 Lindsay Bradford

package blacksmyth.stopwatch.model;

/**
 * An implementation of the Ticker interface that is essentially 
 * A thread, configured to sleep between ticks.
 */
public class SleepingThreadTicker extends Thread implements Ticker {

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
