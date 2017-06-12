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

import java.util.Observer;

import blacksmyth.stopwatch.presenter.StopWatchEventSource;

/**
 * An interface specifying the required methods for implementing a simple stopwatch. 
 * Allows for accumulating elapsed time over a succession of start & stop events.
 * The stopwatch may have its measured elapsed time reset or over-written by calling objects.
 * 
 * The StopWatchModel implementing this interface is expected to be a sub-type of an Observable object, allowing
 * other to receive time change events.
 * @see Observable
 * @see Observer
 */
public interface StopWatchModel extends TickRecipient, StopWatchEventSource {

  /**
   * Resets the stopwatch timer to {@code milliseconds}, and stops the timer running if necessary.
   */
  public void setTime(long milliseconds);
  
  /**
   * @return The total amount of elapsed time in milliseconds accumulated so far in running the stopwatch.
   */
  public long getTime();

  /**
   *   Starts the stopwatch timer running, adding more elapsed tom to the overall timer value. 
   */
  public void start();

  /**
   * Stops the stopwatch timer from running whilst retaining the amount of time it was run for.  
   */
  public void stop();

  /**
   * Resets the stopwatch timer to 0, and stops the timer running if necessary.
   */
  public void reset();

  /**
   * Returns whether the stopwatch is currently incrementing its timer (true), or paused/reset (false).
   * @return
   */
  public boolean isMeasuringTime();  
  
  /**
   * Instructs the stopwatch to die, releasing all running resources in preparation for its garbage collection.
   */
  public void die();
  
  /**
   * A method allow interested object to register for time update notifications.
   * @param o - An observer interested in receiving notification events from the Stopwatch.
   * @see Observable
   */
  public void addObserver(Observer o);
}
