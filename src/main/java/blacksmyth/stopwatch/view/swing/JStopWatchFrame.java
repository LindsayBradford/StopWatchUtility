package blacksmyth.stopwatch.view.swing;

import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.prefs.Preferences;

import blacksmyth.stopwatch.view.StopWatchViewEventRaiser;
import blacksmyth.stopwatch.view.StopWatchViewEvents;
import blacksmyth.swing.JUserNameableFrame;

@SuppressWarnings("serial")
public class JStopWatchFrame extends JUserNameableFrame {
  
  private Preferences prefs;

  public void setPreferences(Preferences prefs) {
    this.prefs = prefs;

    processPreferences();
    addPreferencesEventHandler();
  }
  
  private void addPreferencesEventHandler() {
    addComponentListener(
        new ComponentAdapter() {
          @Override
          public void componentMoved(ComponentEvent event) {
            prefs.putInt("posX",getX());
            prefs.putInt("posY",getY());
          }
    });
  }
  
  @Override
  public void setTitle(String title) {
    super.setTitle(title);

    prefs.put(
        "frameTitle",
        title
    );
  }

  private void processPreferences() {
    setTitle(
        prefs.get("frameTitle","StopWatch")
    );

    final int posX = prefs.getInt("posX",10);
    final int posY = prefs.getInt("posY",10);

    setLocation(posX, posY);
  }
  
  public void setEventRaiser(final StopWatchViewEventRaiser eventRaiser) {
    addWindowListener(new WindowAdapter() {

      @Override
      public void windowClosing(WindowEvent we) {
        eventRaiser.raise(
            StopWatchViewEvents.DeathRequested
        );
      }

      @Override
      public void windowClosed(WindowEvent we) {
        dispose();
        System.exit(0);
      }
    });
  }
}
