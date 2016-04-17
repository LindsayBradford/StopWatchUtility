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
 * An interface that any objects capable of raising a {@link StopWatchViewEvent} should implement.
 */
public interface StopWatchViewEventRaiser {
  
  /**
   * Raises the specified {@link StopWatchViewEvent} for consumption by any observers of a {@link StopWatchView}.
   * @param event
   */

  public void raise(StopWatchViewEvent event);
}
