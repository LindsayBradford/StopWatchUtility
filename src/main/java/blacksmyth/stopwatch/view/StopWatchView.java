// (c) 2016 Lindsay Bradford

package blacksmyth.stopwatch.view;

import java.util.Observer;

/**
 * An interface specifying the methods required for a StopWatchView to implement.  
 *
 * The StopWatchView implementing this interface is expected to be a sub-type of an {@link java.util.Observable} 
 * object, allowing watching objects to receive stopwatch specific view events. 
 * @see StopWatchViewEvents
 */
public interface StopWatchView {
  /**
   * This method instructs the view to be shown to an end-user. 
   */
  public void show();
  
  /**
   * A method allowing the view to receive updates to the time being reported some stopwatch implementation.
   * @param time
   */
  public void setTime(long time);
  
  /**
   * If the view publishes an StopWatchViewEvents.TimeSetRequested event, the new time requested by the view
   * is to be offered on this callback method. 
   * @return
   */
  public long getRequestedSetTime();

  /**
   * The StopWatchView implementing this interface is expected to be a sub-type of an Observable object, allowing
   * other objects to receive view events (as enumerate in StopWatchViewEvents).
   * @param o - An observer interested in receiving notification events from the StopwatchView.
   * @see Observable, StopWatchViewEvents
   */
  public void addObserver(Observer o);
}