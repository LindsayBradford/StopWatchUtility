package blacksmyth.stopwatch;

import java.awt.BorderLayout;
import java.awt.Color;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import java.util.prefs.Preferences;

import javax.swing.JApplet;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JFrame;
import javax.swing.JPopupMenu;

import javax.swing.UIManager;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.EtchedBorder;

import blacksmyth.stopwatch.system.StopWatchController;

import blacksmyth.swing.JPopupMenuListener;
import blacksmyth.swing.JUserNameableFrame;
import blacksmyth.swing.JFrameFactory;
import blacksmyth.swing.JUtilities;

import blacksmyth.utilities.ResourceLoader;

@SuppressWarnings("serial")
public class StopWatchUtility extends JApplet {
  static Preferences myPreferences;
  static JCheckBoxMenuItem showMillisecondsItem;
  static JCheckBoxMenuItem showLedsItem;

  static StopWatchController controller;
  static StopWatchPopup stopWatchPopup;
  
  public static void main(String argv[]) {
    try {
      UIManager.setLookAndFeel(
          "javax.swing.plaf.metal.MetalLookAndFeel"
        );
    } catch (Exception e) {}
    
    myPreferences = Preferences.userNodeForPackage(StopWatchUtility.class);

    JUserNameableFrame frame = new JUserNameableFrame(getFrameTitle());
    
    JFrameFactory.makeCloseableJFrame(frame);
    frame.setIconImage(
      ResourceLoader.loadImageAsIcon(
          "/resources/gnome-set-time.gif"
      ).getImage()
    );

    controller = new StopWatchController();
    frame.getContentPane().add(controller, BorderLayout.CENTER);
    controller.setStartStopButtonAsDefault(frame);
    frame.setJMenuBar(getMenuBar(frame));

    frame.pack();
    frame.setResizable(false);
    JUtilities.centerWindow(frame);

    getFramePreferences(frame);
    saveStateOnClose(frame);

    frame.setVisible(true);
  }
  
  public void init() {
    
    try {
      UIManager.setLookAndFeel(
          "javax.swing.plaf.metal.MetalLookAndFeel"
        );
    } catch (Exception e) {}

    try {
      Color foreground = Color.decode(getParameterColor("foreground"));
      Color background = Color.decode(getParameterColor("background"));
      
      Color borderForeground = Color.decode(getParameterColor("borderForeground"));
      Color borderBackground = Color.decode(getParameterColor("borderBackground"));
      
      Color counterForeground = Color.decode(getParameterColor("counterForeground"));
      Color counterBackground = Color.decode(getParameterColor("counterBackground"));

      // TODO: clean this mess up.
      
      UIManager.put("Button.foreground", foreground);
      UIManager.put("ToggleButton.foreground", foreground);
      UIManager.put("Panel.foreground", foreground);
      UIManager.put("Border.foreground", borderForeground);
      UIManager.put("Label.foreground", counterForeground);
      UIManager.put("TableHeader.foreground", foreground);
      UIManager.put("TitledBorder.titleColor", foreground);
      UIManager.put("ScrollBar.foreground", foreground);
      UIManager.put("OptionPane.messageForeground", foreground);
      UIManager.put("TabbedPane.foreground", foreground);
      UIManager.put("menuText", foreground);

      UIManager.put("Button.background", background);
      UIManager.put("ToggleButton.background", background);
      UIManager.put("Panel.background", background);
      UIManager.put("Border.background", borderBackground);
      UIManager.put("Label.background", counterBackground);
      UIManager.put("TableHeader.background", background);
      UIManager.put("ScrollBar.background", background);
      UIManager.put("ScrollButton.background", background);
      UIManager.put("OptionPane.background", background);
      UIManager.put("TabbedPane.background", background);
      UIManager.put("TabbedPane.tabAreaBackground", background);
      UIManager.put("TabbedPane.selected", background);
      
      UIManager.put("window", background);
      
    } catch (Exception e) {}

    
    controller = new StopWatchController();

    controller.setCounterForeground(getCounterForegroundParameter());
    controller.setCounterBackground(getCounterBackgroundParameter());

    controller.setCounterBorderColors(
        getCounterBorderForegroundParameter(),
        getCounterBorderBackgroundParameter()
    );
    
    stopWatchPopup = new StopWatchPopup(controller);
    
    stopWatchPopup.setForeground(getForegroundParameter());
    stopWatchPopup.setBackground(getBackgroundParameter());
    
    addMouseListener(new JPopupMenuListener(stopWatchPopup));

    controller.setBorder(
        new CompoundBorder(
          new EmptyBorder(3,2,2,2),            
          new CompoundBorder(
              new EtchedBorder(),
              new EmptyBorder(12,11,11,11)
          )
        )
    );
    
    getContentPane().add(controller,BorderLayout.CENTER);
  }

  private Color getForegroundParameter() {
    return getParameterAsColor("foreground", Color.BLACK);
  }

  private Color getBackgroundParameter() {
    return getParameterAsColor("background", Color.LIGHT_GRAY);
  }
  
  private Color getCounterBorderForegroundParameter() {
    return getParameterAsColor("counterBorderForeground", Color.LIGHT_GRAY);
  }

  private Color getCounterBorderBackgroundParameter() {
    return getParameterAsColor("counterBorderBackground", Color.GRAY);
  }
  
  private Color getCounterForegroundParameter() {
    return getParameterAsColor("counterForeground", Color.BLACK);
  }

  private Color getCounterBackgroundParameter() {
    return getParameterAsColor("counterBackground", Color.WHITE);
  }

  private Color getParameterAsColor(String parameter, Color defaultColor) {
    String colorParameter = "0x" + getParameter(parameter);
    Color color;
    try {
      color = Color.decode(colorParameter); 
    } catch (NumberFormatException nfe) {
      color = defaultColor;
    }
    return color;
  }

  
  public String getAppletInfo() {
    return "A Stopwatch Utility.";
  }
  
  private static String getFrameTitle() {
    return "Stopwatch";
  }

  private static JMenuBar getMenuBar(JFrame frame) {
    JMenuBar menuBar = new JMenuBar();
    menuBar.add(getStopwatchMenu(frame));
    menuBar.add(getViewMenu(frame));
    return menuBar;
  }

  private static JMenu getStopwatchMenu(JFrame frame) {
    JMenu stopwatchMenu  = new JMenu("Stopwatch");
    stopwatchMenu.setMnemonic(KeyEvent.VK_S);

    stopwatchMenu.add(getSetTimeItem());

    return stopwatchMenu;
  }

  private static JMenuItem getSetTimeItem() {
    JMenuItem changeTimetem = new JMenuItem("Set Time...");
    changeTimetem.setMnemonic(KeyEvent.VK_S);

    changeTimetem.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent pEvent) {
	      controller.showSetTimeDialog();
      }
    });
    return changeTimetem;
  }

  private static JMenu getViewMenu(JFrame frame) {
    JMenu viewMenu  = new JMenu("View");
    viewMenu.setMnemonic(KeyEvent.VK_V);

    viewMenu.add(getChangeTitleItem((JUserNameableFrame) frame));
    viewMenu.add(getShowMillisecondsItem());
    viewMenu.add(getShowLedsItem());

    return viewMenu;
  }

  private static JCheckBoxMenuItem getShowMillisecondsItem() {
    showMillisecondsItem = new JCheckBoxMenuItem("Show Milliseconds",true);
    showMillisecondsItem.setMnemonic(KeyEvent.VK_S);

    showMillisecondsItem.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent pEvent) {
	      toggleShowMilliseconds();
      }
    });
    return showMillisecondsItem;
  }

  private static void toggleShowMilliseconds() {
    controller.toggleShowMilliseconds();
    showMillisecondsItem.setState(controller.showingMilliseconds());
  }

  private static JCheckBoxMenuItem getShowLedsItem() {
    showLedsItem = new JCheckBoxMenuItem("Show Leds",true);
    showLedsItem.setMnemonic(KeyEvent.VK_L);

    showLedsItem.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent pEvent) {
        toggleShowLeds();
      }
    });
    return showLedsItem;
  }

  private static void toggleShowLeds() {
    controller.toggleShowLeds();
    showLedsItem.setState(controller.showingLeds());
  }

  private static JMenuItem getChangeTitleItem(final JUserNameableFrame frame) {
    JMenuItem changeTitleItem = new JMenuItem("Change Title...");
    changeTitleItem.setMnemonic(KeyEvent.VK_C);

    changeTitleItem.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent pEvent) {
	      frame.showTitleDialog();
        myPreferences.put("frameTitle",frame.getUserTitle());
      }
    });
    return changeTitleItem;
  }

  private static void getFramePreferences(final JFrame frame) {
    frame.setTitle(myPreferences.get("frameTitle",""));
    toggleShowMillisecondsIfNecessary(myPreferences.getBoolean("showMilliseconds",true));
    toggleShowLedsIfNecessary(myPreferences.getBoolean("showLeds",true));
    controller.setTime(myPreferences.getLong("elapsedTime",0));

    final int posX = myPreferences.getInt("posX",10);
    final int posY = myPreferences.getInt("posY",10);
    frame.setLocation(posX, posY);
  }

  private static void toggleShowMillisecondsIfNecessary(boolean pShowMilliseconds) {
    if (pShowMilliseconds == false) {
      toggleShowMilliseconds();
    }
  }

  private static void toggleShowLedsIfNecessary(boolean showLeds) {
    if (showLeds == false) {
      toggleShowLeds();
    }
  } 

  private static void saveStateOnClose(final JFrame frame) {
    frame.addWindowListener(new WindowAdapter() {
      public void windowClosed(WindowEvent we) {
 //       frame.dispose();
 //       System.exit(0);
      }
      public void windowClosing(WindowEvent we) {
        controller.die();
        updatePreferences();
      }
    });
    frame.addComponentListener(
      new ComponentAdapter() {
        public void componentMoved(ComponentEvent event) {
          myPreferences.putInt("posX",frame.getX());
          myPreferences.putInt("posY",frame.getY());
        }
    });
  }
  
  private static void updatePreferences() {
    myPreferences.putBoolean("showMilliseconds",controller.showingMilliseconds());
    myPreferences.putBoolean("showLeds",controller.showingLeds());
    myPreferences.putLong("elapsedTime",controller.getTime());
  }
  
  private String getParameterColor(String parameter) {
    return "#" + getParameter(parameter);
  }
  
}

@SuppressWarnings("serial")
class StopWatchPopup extends JPopupMenu {

  private StopWatchController stopwatch;
  private JCheckBoxMenuItem showMillisecondsItem;
  private JCheckBoxMenuItem showLedsItem;
  private JMenuItem         setTimeItem;

  public StopWatchPopup(StopWatchController stopwatch) {
    this.stopwatch = stopwatch;
    buildContent();
    showMillisecondsItem.setState(stopwatch.showingMilliseconds());
    showLedsItem.setState(stopwatch.showingLeds());
  }

  private void buildContent() {
    add(getSetTimeItem());
    addSeparator();
    add(getShowMillisecondsItem());
    add(getShowLedsItem());
  }

  private JMenuItem getSetTimeItem() {
    setTimeItem = new JMenuItem("Set Time...");
    setTimeItem.setMnemonic(KeyEvent.VK_S);

    setTimeItem.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent pEvent) {
        stopwatch.showSetTimeDialog();
      }
    });
    return setTimeItem;
  }

  private JCheckBoxMenuItem getShowMillisecondsItem() {
    showMillisecondsItem = new JCheckBoxMenuItem("Show Milliseconds",true);
    showMillisecondsItem.setMnemonic(KeyEvent.VK_M);

    showMillisecondsItem.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent pEvent) {
        toggleShowMilliseconds();
      }
    });
    return showMillisecondsItem;
  }

  private JCheckBoxMenuItem getShowLedsItem() {
    showLedsItem = new JCheckBoxMenuItem("Show Leds",true);
    showLedsItem.setMnemonic(KeyEvent.VK_L);

    showLedsItem.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent pEvent) {
        toggleShowLeds();
      }
    });
    return showLedsItem;
  }

  private void toggleShowMilliseconds() {
    stopwatch.toggleShowMilliseconds();
    showMillisecondsItem.setState(stopwatch.showingMilliseconds());
  }

  private void toggleShowLeds() {
    stopwatch.toggleShowLeds();
    showLedsItem.setState(stopwatch.showingLeds());
  }

  public void setForeground(Color color) {
    super.setForeground(color);
    if (showMillisecondsItem != null) {
      setTimeItem.setForeground(color);
      showMillisecondsItem.setForeground(color);
      showLedsItem.setForeground(color);
    }
  }

  public void setBackground(Color color) {
    super.setBackground(color);
    if (showMillisecondsItem != null) {
      setTimeItem.setBackground(color);
      showMillisecondsItem.setBackground(color);
      showLedsItem.setBackground(color);
    }
  }
}
