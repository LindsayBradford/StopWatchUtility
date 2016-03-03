// (c) 2016 Lindsay Bradford

package blacksmyth.stopwatch.model;

/**
 * An interface that specifies methods expected on an object 
 * that requires a regular heart-beat (tick) update without necessarily knowing
 * the source of that heart-beat.
 */
public interface TickRecipient {
  
  /**
   * A callback method used to tell the TickRecipient that a new tick event has occurred.
   */
  public void receiveTick();
  
  /**
   * Allows the TickRecipient to signal whether it needs a tick heart-beat service or not. 
   * @return true if the recipient requires a heart-beat service, false otherwise.
   */
  public boolean needsTicks();
}
