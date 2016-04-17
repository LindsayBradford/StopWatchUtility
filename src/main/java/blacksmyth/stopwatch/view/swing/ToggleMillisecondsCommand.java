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

import java.util.prefs.Preferences;

/**
 * An implementation of {@link StopWatchCommand} that raises a 
 * {@link SwingStopWatchViewEvents#ToggleMillisecondsRequested} event, and adjusts the supplied {@link Preferences} to 
 * remember the decisions between invocations of the stop watch utility.
 */

final class ToggleMillisecondsCommand implements StopWatchCommand {
  
  private Preferences preferences;
  private SwingStopWatchViewEventRaiser eventRaiser;
    
  public void setSwingEventRaiser(SwingStopWatchViewEventRaiser eventRaiser) {
    this.eventRaiser = eventRaiser;
  }
  
  public void setPreferences(Preferences preferences) {
    this.preferences = preferences;
  }

  @Override
  public void run() {
    
    boolean showMilliseconds = preferences.getBoolean("showMilliseconds",true);
    showMilliseconds = !showMilliseconds;
    preferences.putBoolean("showMilliseconds", showMilliseconds);
    
    eventRaiser.raise(
        SwingStopWatchViewEvents.ToggleMillisecondsRequested
    );
  }


}
