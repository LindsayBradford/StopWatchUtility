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

/**
 * An implementation of {@link StopWatchCommand} that raises a {@link SwingStopWatchViewEvents#ToggleLedsRequested}
 * event, and adjusts the supplied {@link PersistedSwingState} to remember the decisions between invocations of the 
 * stop watch utility.
 */

final class ToggleLedsCommand implements StopWatchCommand {
  
  private SwingStopWatchViewEventRaiser eventRaiser;
  private PersistedSwingState toggleState;
  
  
  public void setSwingEventRaiser(SwingStopWatchViewEventRaiser eventRaiser) {
    this.eventRaiser = eventRaiser;
  }
  
  public void setPersistedToggleLeds(PersistedSwingState toggleState) {
    this.toggleState = toggleState;
  }

  @Override
  public void run() {
    toggleState.putAsBoolean(
        !toggleState.getAsBoolean()
    );

    eventRaiser.raise(
        SwingStopWatchViewEvents.ToggleLedsRequested
    );
  }
}
