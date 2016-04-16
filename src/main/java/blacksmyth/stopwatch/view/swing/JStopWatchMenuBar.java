package blacksmyth.stopwatch.view.swing;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.prefs.Preferences;

import javax.swing.JCheckBoxMenuItem;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

@SuppressWarnings("serial")
public class JStopWatchMenuBar extends JMenuBar {

  private StopWatchCommand updateTitleCommand;
  private StopWatchCommand updateTimeCommand;
  private StopWatchCommand toggleShowMillisecondsCommand;
  private StopWatchCommand toggleShowLedsCommand;
  
  private JCheckBoxMenuItem showMillisecondsItem;
  private JCheckBoxMenuItem showLedsItem;
  
  public JStopWatchMenuBar() {
    super();
    add(buildStopwatchMenu());
    add(buildViewMenu());
  }
 
  private JMenu buildStopwatchMenu() {
    JMenu stopwatchMenu  = new JMenu("Stopwatch");
    stopwatchMenu.setMnemonic(KeyEvent.VK_S);

    stopwatchMenu.add(buildSetTimeItem());

    return stopwatchMenu;
  }

  private JMenuItem buildSetTimeItem() {
    JMenuItem item = new JMenuItem("Set Time...");
    item.setMnemonic(KeyEvent.VK_S);

    item.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent pEvent) {
        updateTimeCommand.run();
      }
    });
    return item;
  }
  
  public void setPreferences(Preferences preferences) {
    this.showMillisecondsItem.setSelected(
        preferences.getBoolean("showMilliseconds", true)
    );
    
    this.showLedsItem.setSelected(
        preferences.getBoolean("showLeds", true)
    );
  }
  
  public void setUpdateTitleCommand(StopWatchCommand updateTitleCommand) {
    this.updateTitleCommand = updateTitleCommand;
  }

  public void setUpdateTimeCommand(StopWatchCommand updateTimeCommand) {
    this.updateTimeCommand = updateTimeCommand;
  }
  
  public void setToggleShowMillisecondsCommand(StopWatchCommand toggleShowMillisecondsCommand) {
    this.toggleShowMillisecondsCommand = toggleShowMillisecondsCommand;
  }

  public void setToggleShowLedsCommand(StopWatchCommand toggleShowLedsCommand) {
    this.toggleShowLedsCommand = toggleShowLedsCommand;
  }
  
  private JMenu buildViewMenu() {
    JMenu viewMenu  = new JMenu("View");
    viewMenu.setMnemonic(KeyEvent.VK_V);

    viewMenu.add(buildChangeTitleItem());
    viewMenu.add(buildShowMillisecondsItem());
    viewMenu.add(buildShowLedsItem());

    return viewMenu;
  }

  private JMenuItem buildChangeTitleItem() {
    JMenuItem item = new JMenuItem("Change Title...");
    item.setMnemonic(KeyEvent.VK_C);

    item.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent pEvent) {
        updateTitleCommand.run();
      }
    });
    return item;
  }
  
  private JCheckBoxMenuItem buildShowMillisecondsItem() {
    showMillisecondsItem = new JCheckBoxMenuItem("Show Milliseconds",true);
    showMillisecondsItem.setMnemonic(KeyEvent.VK_S);

    showMillisecondsItem.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent pEvent) {
        toggleShowMillisecondsCommand.run();
      }
    });
    return showMillisecondsItem;
  }

  private JCheckBoxMenuItem buildShowLedsItem() {
    showLedsItem = new JCheckBoxMenuItem("Show Leds",true);
    showLedsItem.setMnemonic(KeyEvent.VK_L);

    showLedsItem.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent pEvent) {
        toggleShowLedsCommand.run();
      }
    });
    return showLedsItem;
  }

}

