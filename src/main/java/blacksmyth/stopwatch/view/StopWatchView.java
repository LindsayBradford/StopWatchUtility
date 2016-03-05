package blacksmyth.stopwatch.view;

import java.util.Observer;

public interface StopWatchView {
  public void show();
  
  public void setTime(long time);
  public long getRequestedSetTime();

  /**
   * The StopWatchView implementing this interface is expected to be a sub-type of an Observable object, allowing
   * other objects to receive view events (as enumerate in StopWatchViewEvents).
   * @param o - An observer interested in receiving notification events from the StopwatchView.
   * @see Observable, StopWatchViewEvents
   */
  public void addObserver(Observer o);
}