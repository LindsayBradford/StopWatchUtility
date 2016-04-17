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

/**
 * An enumeration of events that may be produced from an implementation of the {@link StopWatchView} interface.
 */
public enum StopWatchViewEvent {
  /**
   * An event requesting that a stopwatch begin running.
   */
  StartRequested,
  
  /**
   * An event requesting that a stopwatch stop running (pause).
   */
  StopRequested,
  
  /**
   * An event requesting that a stopwatch stop, and haver its timer reset.
   */
  ResetRequested,
  
  /**
   * An event requesting that a stopwatch update its time to match one held by the view.
   */
  TimeSetRequested,
  
  /**
   * An event requsting that the stopwatch die (release any running resources it has). 
   */
  DeathRequested;
}