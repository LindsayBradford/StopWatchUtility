package blacksmyth.stopwatch.view.swing;

import java.util.prefs.Preferences;

public class ToggleMillisecondsCommand implements StopWatchCommand {
  
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
