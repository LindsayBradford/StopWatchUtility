package blacksmyth.stopwatch.view.swing;

import java.util.prefs.Preferences;

public class ToggleLedsCommand implements StopWatchCommand {
  
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
    
    boolean showLeds = preferences.getBoolean("showLeds",true);
    showLeds = !showLeds;
    preferences.putBoolean("showLeds", showLeds);
    
    eventRaiser.raise(
        SwingStopWatchViewEvents.ToggleLedsRequested
    );
  }


}
