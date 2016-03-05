// (c) 2016 Lindsay Bradford

package blacksmyth.stopwatch.view;

/**
 * An enumeration of events that may be produced from an implementation of the StopWatchView interface.
 */
public enum StopWatchViewEvents {
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
  DeathRequested
}