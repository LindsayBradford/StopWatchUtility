package blacksmyth.stopwatch.view.swing;

import java.util.Observable;
import java.util.prefs.Preferences;

import javax.swing.JFrame;

import blacksmyth.stopwatch.view.StopWatchView;
import blacksmyth.stopwatch.view.StopWatchViewEventRaiser;
import blacksmyth.stopwatch.view.StopWatchViewEvents;

public class SwingStopWatchView extends Observable implements 
    StopWatchView, StopWatchViewEventRaiser, SwingStopWatchViewEventRaiser  {
  
  private JFrame frame;
  private Preferences prefs;
  private JStopWatchControlPanel controlPanel;

  public void setFrame(JFrame frame) {
    this.frame = frame;
  }
  
  public void setPreferences(Preferences preferences) {
    this.prefs = preferences;
  }

  public void setControlPanel(JStopWatchControlPanel controlPanel) {
    this.controlPanel = controlPanel;
  }

  @Override
  public void show() {
    frame.setVisible(true);
  }

  @Override
  public void setTime(long time) {
    prefs.putLong(
        "elapsedTime",
        time
    );
    controlPanel.setTime(time);
  }

  @Override
  public long getRequestedSetTime() {
    return prefs.getLong("elapsedTime",0);
  }

  @Override
  public void raise(StopWatchViewEvents event) {
    processStopWatchEvent(event);
    setChanged();
    notifyObservers(event);
  }
  
  private void processStopWatchEvent(StopWatchViewEvents event) {
    controlPanel.processStopWatchEvent(event);
  }

  @Override
  public void raise(SwingStopWatchViewEvents event) {
    controlPanel.processSwingEvent(event);
  }
}
