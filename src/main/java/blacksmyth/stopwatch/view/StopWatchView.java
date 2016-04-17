/**
 * Copyright (c) 2016, Lindsay Bradford and other Contributors.
 * All rights reserved.
 * 
 * This program and the accompanying materials  are made available 
 * under the terms of the BSD 3-Clause licence  which accompanies 
 * this distribution, and is available at
 * http://opensource.org/licenses/BSD-3-Clause
 */

package blacksmyth.stopwatch.view;

import java.util.Observer;

/**
 * An interface specifying the methods required for a StopWatchView to implement.  
 *
 * Objects implementing this interface are expected to sub-type {@link java.util.Observable}, 
 * allowing watching objects to receive stopwatch specific view events. 
 * @see StopWatchViewEvent
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
   * other objects to receive view events (as enumerated in {@link StopWatchViewEvent}).
   * @param o - An observer interested in receiving notification events from the StopwatchView.
   * @see Observable, StopWatchViewEvents
   */
  public void addObserver(Observer o);
}