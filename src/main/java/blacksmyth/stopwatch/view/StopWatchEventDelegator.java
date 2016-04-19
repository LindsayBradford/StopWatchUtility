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
 *  An implementation of {@link StopWatchEventRaiser} that forwards the raising of an 
 *  {@link StopWatchViewEvent} to a specified delegate.
 *  
 *  Used to trigger the raising of a event locally, but where the event itself needs ultimately to be consumed with a 
 *  different event source reference.
 */

public class StopWatchEventDelegator implements StopWatchViewEventRaiser {
  
  private StopWatchViewEventRaiser delegate;
  
  /**
   * Specifies the delegate which should raise events on behalf of this object.
   * @param delegate
   */
  
  public void setDelegate(StopWatchViewEventRaiser delegate) {
    this.delegate = delegate;
  }

  /**
   * an implementation of {@link StopWatchViewEventRaiser#raise(StopWatchViewEvent)} that 
   * does no locally raise the event specified, but forwards it onto the delegate specified via
   * {@link #setDelegate(StopWatchViewEventRaiser)}.
   */
  
  @Override
  public void raise(StopWatchViewEvent event) {
    this.delegate.raise(event);
  }

}
