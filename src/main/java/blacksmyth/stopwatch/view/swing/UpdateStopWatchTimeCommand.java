package blacksmyth.stopwatch.view.swing;

import java.util.prefs.Preferences;

import blacksmyth.stopwatch.view.StopWatchViewEventRaiser;
import blacksmyth.stopwatch.view.StopWatchViewEvents;

public class UpdateStopWatchTimeCommand implements StopWatchCommand {
  
  private JTimerUpdateDialog dialog;
  private Preferences preferences;
  private StopWatchViewEventRaiser eventRaiser;
  
  
  public void setDialog(JTimerUpdateDialog dialog) {
    this.dialog = dialog;
  }
  
  public void setEventRaiser(StopWatchViewEventRaiser eventRaiser) {
    this.eventRaiser = eventRaiser;
  }
  
  public void setPreferences(Preferences preferences) {
    this.preferences = preferences;
  }
  
  @Override
  public void run() {
    eventRaiser.raise(
        StopWatchViewEvents.StopRequested
    );
    
    dialog.setTime(
        preferences.getLong(
            "elapsedTime",0
        )
    );
    
    dialog.setVisible(true);
    
    preferences.putLong(
        "elapsedTime",
        dialog.getTime()
    );
    
    eventRaiser.raise(
        StopWatchViewEvents.TimeSetRequested
    );
  }

}
