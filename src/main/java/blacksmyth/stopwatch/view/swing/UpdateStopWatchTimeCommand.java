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

import blacksmyth.stopwatch.view.StopWatchViewEventRaiser;
import blacksmyth.stopwatch.view.StopWatchViewEvent;

/**
 * An implementation of {@link StopWatchCommand} that raises a 
 * {@link StopWatchViewEvents#StopRequested} event to stop a running stop watch, prompt the user for a new 
 * elapsed time via the command's {@link JTimerUpdateDialog}, and informs listeners of a time-change via a
 * {@link StopWatchViewEvents#TimeSetRequested} event. 
 * Finally, it adjusts the supplied {@link PersistedNvpState} to remember the updated time between invocations of the 
 * stop watch utility.
 */

final class UpdateStopWatchTimeCommand implements StopWatchCommand {
  
  private JTimerUpdateDialog dialog;

  private StopWatchViewEventRaiser eventRaiser;

  private PersistedNvpState elapsedTimeState;
  
  public void setDialog(JTimerUpdateDialog dialog) {
    this.dialog = dialog;
  }
  
  public void setEventRaiser(StopWatchViewEventRaiser eventRaiser) {
    this.eventRaiser = eventRaiser;
  }
  
  public void setPersistedElapsedTime(PersistedNvpState elapsedTimeState) {
    this.elapsedTimeState = elapsedTimeState;
  }
  
  @Override
  public void run() {
    eventRaiser.raise(
        StopWatchViewEvent.StopRequested
    );
    
    dialog.setTime(
        elapsedTimeState.getAsLong()
    );
    
    dialog.setVisible(true);
    
    elapsedTimeState.putAsLong(
        dialog.getTime()
    );
    
    eventRaiser.raise(
        StopWatchViewEvent.TimeSetRequested
    );
  }

}
