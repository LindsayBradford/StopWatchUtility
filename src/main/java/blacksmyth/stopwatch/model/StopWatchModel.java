// (c) 2016 Lindsay Bradford

package blacksmyth.stopwatch.model;

/**
 * An interface specifying the required methods for implementing a simple stopwatch. 
 * Allows for accumulating elapsed time over a succession of start & stop events.
 * The stopwatch may have its measured elapsed time reset or over-written by calling objects.
 */
public interface StopWatchModel {

  /**
   * Resets the stopwatch timer to {@code time}, and stops the timer running if necessary.
   */
  public void setTime(long milliseconds);
  
  /**
   * @return The total amount of elapsed time in milliseconds accumulated so far in running the stopwatch.
   */
  public long getTime();

  /**
   *   Starts the stopwatch timer running, adding more elapsed tom to the overal timer value. 
   */
  public void start();

  /**
   * Stops the stopwatch timer from running but retains the amount of time it was run for.  
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
  public boolean isRunning();  
  
  /**
   * Instructs the stopwatch to die, releasing all running resources in preparation for its garbage collection.
   */
  public void die(); 
}
