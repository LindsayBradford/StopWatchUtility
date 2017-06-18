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
  StartRequested ("Start Requested"),
  
  /**
   * An event requesting that a stopwatch stop running (pause).
   */
  StopRequested("Stop Requested"),
  
  /**
   * An event requesting that a stopwatch stop, and haver its timer reset.
   */
  ResetRequested("Reset Request"),
  
  /**
   * An event requesting that a stopwatch update its time to match one held by the view.
   */
  TimeSetRequested("Time Set Requested"),
  
  /**
   * An event requsting that the stopwatch die (release any running resources it has). 
   */
  DeathRequested("Death Requested");

  private String description;

  StopWatchViewEvent(String description) {
    this.description = description;
  }
  
  public String getDescription() {
    return description;
  }
}