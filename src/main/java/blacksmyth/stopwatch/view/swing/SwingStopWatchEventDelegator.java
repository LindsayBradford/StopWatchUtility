/**
 * Copyright (c) 2016, Lindsay Bradford and other Contributors.
 * All rights reserved.
 * 
 * This program and the accompanying materials  are made available 
 * under the terms of the BSD 3-Clause licence  which accompanies 
 * this distribution, and is available at
 * http://opensource.org/licenses/BSD-3-Clause
 */

package blacksmyth.stopwatch.view.swing;

import org.springframework.stereotype.Component;

/**
 *  An implementation of {@link SwingStopWatchEventRaiser} that forwards the raising of an 
 *  {@link SwingStopWatchViewEvent} to a specified delegate.
 *  
 *  Used to trigger the raising of a event locally, but where the event itself needs ultimately to be consumed with a 
 *  different event source reference.
 */

@Component
public final class SwingStopWatchEventDelegator implements SwingStopWatchViewEventRaiser {
  
  private SwingStopWatchViewEventRaiser delegate;
  
  /**
   * Specifies the delegate which should raise events on behalf of this object.
   * @param delegate
   */

  public void setDelegate(SwingStopWatchViewEventRaiser delegate) {
    this.delegate = delegate;
  }

  /**
   * an implementation of {@link SwingStopWatchViewEventRaiser#raise(SwingStopWatchViewEvent)} that 
   * does no locally raise the event specified, but forwards it onto the delegate specified via
   * {@link #setDelegate(SwingStopWatchViewEventRaiser)}.
   */

  @Override
  public void raise(SwingStopWatchViewEvents event) {
    delegate.raise(event);
  }

}
