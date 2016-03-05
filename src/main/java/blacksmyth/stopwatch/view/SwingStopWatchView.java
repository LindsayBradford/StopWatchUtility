/**
 * Copyright (c) 2016, Lindsay Bradford and other Contributors.
 * All rights reserved.
 * 
 * This program and the accompanying materials  are made available 
 * under the terms of the BSD 3-Clause licence  which accompanies 
 * this distribution, and is available at
 * http://opensource.org/licenses/BSD-3-Clause
 */

package blacksmyth.stopwatch.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.LinkedList;
import java.util.Observable;
import java.util.prefs.Preferences;

import javax.swing.JButton;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.border.BevelBorder;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.SoftBevelBorder;

import blacksmyth.swing.JFrameFactory;
import blacksmyth.swing.JOkCancelDialog;
import blacksmyth.swing.JUserNameableFrame;
import blacksmyth.swing.JUtilities;
import blacksmyth.utilities.ResourceLoader;

/**
 * A Swing implementation of {@link StopWatchView}.
 */
public final class SwingStopWatchView extends Observable implements StopWatchView {
  
  private Preferences myPreferences;
  private JCheckBoxMenuItem showMillisecondsItem;
  private JCheckBoxMenuItem showLedsItem;

  private StopWatchController controller;
  private JFrame frame;
  
  public SwingStopWatchView() {
    buildSwingView();
  }

  @Override
  public void show() {
    frame.setVisible(true);
  }

  @Override
  public void setTime(long time) {
    controller.setTime(time);
    myPreferences.putLong("elapsedTime",time);
  }

  @Override
  public long getRequestedSetTime() {
    return myPreferences.getLong("elapsedTime",0);
  }

  // TODO: Yes, the view's been successfully decoupled from the presenter and model, 
  // but it's still an eye-bleed inducing dog's breakfast.
  
  public void buildSwingView() {
    try {
      UIManager.setLookAndFeel(
          "javax.swing.plaf.metal.MetalLookAndFeel"
        );
    } catch (Exception e) {}
    
    myPreferences = Preferences.userNodeForPackage(SwingStopWatchView.class);

    frame = new JUserNameableFrame(getFrameTitle());
    
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
  
  private String getFrameTitle() {
    return "Stopwatch";
  }

  private JMenuBar getMenuBar(JFrame frame) {
    JMenuBar menuBar = new JMenuBar();
    menuBar.add(getStopwatchMenu(frame));
    menuBar.add(getViewMenu(frame));
    return menuBar;
  }

  private JMenu getStopwatchMenu(JFrame frame) {
    JMenu stopwatchMenu  = new JMenu("Stopwatch");
    stopwatchMenu.setMnemonic(KeyEvent.VK_S);

    stopwatchMenu.add(getSetTimeItem());

    return stopwatchMenu;
  }

  private JMenuItem getSetTimeItem() {
    JMenuItem changeTimetem = new JMenuItem("Set Time...");
    changeTimetem.setMnemonic(KeyEvent.VK_S);

    changeTimetem.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent pEvent) {
        controller.showSetTimeDialog();
      }
    });
    return changeTimetem;
  }

  private JMenu getViewMenu(JFrame frame) {
    JMenu viewMenu  = new JMenu("View");
    viewMenu.setMnemonic(KeyEvent.VK_V);

    viewMenu.add(getChangeTitleItem((JUserNameableFrame) frame));
    viewMenu.add(getShowMillisecondsItem());
    viewMenu.add(getShowLedsItem());

    return viewMenu;
  }

  private JCheckBoxMenuItem getShowMillisecondsItem() {
    showMillisecondsItem = new JCheckBoxMenuItem("Show Milliseconds",true);
    showMillisecondsItem.setMnemonic(KeyEvent.VK_S);

    showMillisecondsItem.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent pEvent) {
        toggleShowMilliseconds();
      }
    });
    return showMillisecondsItem;
  }

  private void toggleShowMilliseconds() {
    controller.toggleShowMilliseconds();
    showMillisecondsItem.setState(controller.showingMilliseconds());
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

  private void toggleShowLeds() {
    controller.toggleShowLeds();
    showLedsItem.setState(controller.showingLeds());
  }

  private JMenuItem getChangeTitleItem(final JUserNameableFrame frame) {
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

  private void getFramePreferences(final JFrame frame) {
    frame.setTitle(myPreferences.get("frameTitle",""));
    toggleShowMillisecondsIfNecessary(myPreferences.getBoolean("showMilliseconds",true));
    toggleShowLedsIfNecessary(myPreferences.getBoolean("showLeds",true));

    final int posX = myPreferences.getInt("posX",10);
    final int posY = myPreferences.getInt("posY",10);
    frame.setLocation(posX, posY);
  }

  private void toggleShowMillisecondsIfNecessary(boolean pShowMilliseconds) {
    if (pShowMilliseconds == false) {
      toggleShowMilliseconds();
    }
  }

  private void toggleShowLedsIfNecessary(boolean showLeds) {
    if (showLeds == false) {
      toggleShowLeds();
    }
  } 

  private void saveStateOnClose(final JFrame frame) {
    frame.addWindowListener(new WindowAdapter() {
      @Override
      public void windowClosing(WindowEvent we) {
        publishEvent(
            StopWatchViewEvents.DeathRequested
        );
        updatePreferences();
      }
      @Override
      public void windowClosed(WindowEvent we) {
        frame.dispose();
        System.exit(0);
      }
    });
    frame.addComponentListener(
      new ComponentAdapter() {
        @Override
        public void componentMoved(ComponentEvent event) {
          myPreferences.putInt("posX",frame.getX());
          myPreferences.putInt("posY",frame.getY());
        }
    });
  }
  
  private void updatePreferences() {
    myPreferences.putBoolean("showMilliseconds",controller.showingMilliseconds());
    myPreferences.putBoolean("showLeds",controller.showingLeds());
  }
  
  private void publishEvent(StopWatchViewEvents event) {
    setChanged();
    notifyObservers(event);
  }
  
  @SuppressWarnings("serial")
  class StopWatchController extends JPanel {

    private final StopWatchPanel stopWatchPanel = new StopWatchPanel();
    private JPanel controlPanel;
    private JGoStopPanel ledPanel;

    private JButton startStopButton;
    private JButton resetButton;
    
    private boolean running = false;

    private GridBagLayout gbl      = new GridBagLayout();
    private GridBagConstraints gbc = new GridBagConstraints();

    private final JTimerUpdateDialog setTimeDialog = 
            new JTimerUpdateDialog(this);

//    private static final AudioClip soundClip = 
//      ResourceLoader.loadAudioClip("/resources/stopwatch.wav");

    public StopWatchController() {
      super(new BorderLayout());
      initialise();
    }

    private void initialise() {
      setBorder(new EmptyBorder(12,11,11,11));
      drawContents();
    }

    private void drawContents() {
      addTimerView();
      addControls();
    }

    private void addTimerView() {
      add(getStopWatchPanel(), BorderLayout.CENTER);
      ledPanel = new JGoStopPanel();
      add(ledPanel, BorderLayout.WEST);
    }

    private void addControls() {
      controlPanel = getControlPanel();
      add(controlPanel, BorderLayout.SOUTH);
    }

    public void setForeground(Color color) {
      if (controlPanel != null) {
        controlPanel.setForeground(color);
        ledPanel.setForeground(color);
        startStopButton.setForeground(color);
        resetButton.setForeground(color);
        setTimeDialog.setForeground(color);
      }
      super.setForeground(color);
    }

    public void setBackground(Color color) {
      if (controlPanel != null) {
        controlPanel.setBackground(color);
        ledPanel.setBackground(color);
        startStopButton.setBackground(color);
        resetButton.setBackground(color);
        setTimeDialog.setBackground(color);
      }
      super.setBackground(color);
    }

    public void setCounterForeground(Color color) {
      getStopWatchPanel().setForeground(color);
    }

    public void setCounterBackground(Color color) {
      getStopWatchPanel().setBackground(color);
    }

    public void setCounterBorderColors(Color foreground, Color background) {
      getStopWatchPanel().setBorderColors(foreground, background);
    }

    public boolean showingMilliseconds() {
      return getStopWatchPanel().showingMilliseconds();
    }

    public boolean showingLeds() {
      return ledPanel.isVisible();
    }

    public void setTime(long time) {
      getStopWatchPanel().setTime(time);
      setTimeDialog.setTime(time);
    }
    
    public StopWatchPanel getStopWatchPanel() {
      return stopWatchPanel;
    }

    public void setStartStopButtonAsDefault(JFrame frame) {
      frame.getRootPane().setDefaultButton(startStopButton);
    }

    private JPanel getControlPanel() {
      JPanel panel = new JPanel(gbl);

      int buttonCol = 0;

      gbc.gridwidth    = 1;
      gbc.gridheight   = 1;
      gbc.gridx        = buttonCol++;
      gbc.gridy        = 0;
      gbc.ipadx        = 11;
      gbc.ipady        = 3;
      gbc.weightx      = 1;
      gbc.weighty      = 1;
      gbc.fill         = GridBagConstraints.BOTH;

      gbc.insets = new Insets(17,0,0,0);

      panel.add(new JLabel(""), gbc);

      gbc.insets = new Insets(17,0,0,5);

      gbc.gridx        = buttonCol++;
      gbc.weightx = 0;
      gbc.fill         = GridBagConstraints.VERTICAL;

      panel.add(getStartStopButton(),gbc);

      gbc.insets = new Insets(17,0,0,0);

      gbc.gridx        = buttonCol++;

      panel.add(getResetButton(), gbc);

      gbc.gridx        = buttonCol++;
      gbc.weightx      = 1;
      gbc.fill         = GridBagConstraints.BOTH;

      panel.add(new JLabel(""), gbc);

      alignButtonSizes();

      return panel;
    }

    private JButton getStartStopButton() {
      startStopButton = new JButton("Start"); 

      startStopButton.setMnemonic('S');

      final StopWatchController controller = this;

      startStopButton.addActionListener(new ActionListener(){
        public void actionPerformed(ActionEvent ae) {
            controller.toggle();
        }
      });

      return startStopButton;
    }

    private JButton getResetButton() {
      resetButton = new JButton();

      resetButton.setText("Reset");
      resetButton.setMnemonic('R');

      resetButton.addActionListener(new ActionListener() {
        public void actionPerformed(ActionEvent ae) {
          publishEvent(
              StopWatchViewEvents.ResetRequested
          );
        }
      });

      return resetButton;
    }

    public void toggle() {
      if (running) {
        stop();
      } else {
        start();
      }
    }

    public void start() {
      running = true;
      playClickSound();
      startStopButton.setText("Stop");
      ledPanel.activate();
      getStopWatchPanel().start();
    }

    public void stop() {
      running = false;
      getStopWatchPanel().stop();
      playClickSound();
      ledPanel.deactivate();
      startStopButton.setText("Start");
    }

    public void reset() {
      stop();
      getStopWatchPanel().reset();
    }
    
    public void die() {
      setTimeDialog.dispose();
    }

    private void alignButtonSizes() {
      LinkedList<JComponent> buttons = new LinkedList<JComponent>();

      buttons.add(startStopButton);
      buttons.add(resetButton);

      JUtilities.equalizeComponentSizes(buttons);

      buttons.clear();
    }

    private void playClickSound() {
      // TODO: Bug: pulseAudio thread is created to play the clip, but continues running once done.
      //       Looks like this: https://bugs.openjdk.java.net/browse/JDK-8077019

      // soundClip.play();
    }

    public void toggleShowMilliseconds() {
      getStopWatchPanel().toggleShowMilliseconds();
    }

    public void toggleShowLeds() {
      ledPanel.setVisible(!ledPanel.isVisible());
    }

    public void showSetTimeDialog() {
      if (running) {
        stop();
      }
      setTimeDialog.setVisible(true);
      
      myPreferences.putLong(
          "elapsedTime",
          setTimeDialog.getTime()
      );
      
      publishEvent(
          StopWatchViewEvents.TimeSetRequested
      );
    }
  }

  @SuppressWarnings("serial")
  class JTimerUpdateDialog extends JOkCancelDialog {

    private final JTimerPanel timerPanel = new JTimerPanel();

    private Component owner;

    long time;

    public JTimerUpdateDialog(Component owner) {
      super(null, "Set Time", true);
      this.owner = owner;
      setContent(timerPanel);
      setLocationRelativeTo(owner);
    }

    public void setForeground(Color color) {
      if (timerPanel != null) {
        timerPanel.setForeground(color);
      }
      super.setForeground(color);
    }

    public void setVisible(boolean b) {
      setLocationRelativeTo(owner);
      super.setVisible(b);
    }

    public void setBackground(Color color) {
      if (timerPanel != null) {
        timerPanel.setBackground(color);
      }
      super.setBackground(color);
    }

    protected void doOkAction() {
      time = timerPanel.getTime(); 
      super.doOkAction();
    }

    public long getTime() {
      return time;
    }

    public void setTime(long time) {
      this.time = time;
      timerPanel.setTime(time);
    }
  }
  
  @SuppressWarnings("serial")
  class StopWatchPanel extends JPanel {
    private final Font  BASE_TIME_FONT    = new Font("Monospaced", Font.BOLD, 20);
    private final Font  MILLISECOND_FONT  = new Font("Monospaced", Font.BOLD, 15);

    private boolean showMilliseconds;

    private int hours;
    private int minutes;
    private int seconds;

    private JLabel hourLabel;
    private JLabel minuteLabel;
    private JLabel secondLabel;
    private JLabel millisecondLabel;
    private JLabel minuteSeparatorLabel;
    private JLabel secondSeparatorLabel;
    private JLabel millisecondTimeSeparatorLabel;

    private GridBagLayout gbl      = new GridBagLayout();
    private GridBagConstraints gbc = new GridBagConstraints();
    
    private boolean isInitializing = true;

    public StopWatchPanel() {
      super();
      showMilliseconds = true;
      setLayout(gbl);
      resetBorder(new SoftBevelBorder(BevelBorder.LOWERED));

      drawPanel();
      setTime(0);
      isInitializing = false;
    }

    public void setForeground(Color color) {
      if (hourLabel != null) {
        hourLabel.setForeground(color);
        minuteLabel.setForeground(color);
        secondLabel.setForeground(color);
        millisecondLabel.setForeground(color);
        millisecondTimeSeparatorLabel.setForeground(color);
        minuteSeparatorLabel.setForeground(color);
        secondSeparatorLabel.setForeground(color);
      }
      super.setForeground(color);
    }

    public void setBorderColors(Color foreground, Color background) {
      resetBorder(new SoftBevelBorder(BevelBorder.LOWERED, foreground, background));
    }

    private void resetBorder(Border newBorder) {
      setBorder(new CompoundBorder(newBorder, new EmptyBorder(7,5,5,5)));
    }

    public void setBackground(Color color) {
      if (hourLabel != null) {
        hourLabel.setBackground(color);
        minuteLabel.setBackground(color);
        secondLabel.setBackground(color);
        millisecondLabel.setBackground(color);
        millisecondTimeSeparatorLabel.setBackground(color);
        minuteSeparatorLabel.setBackground(color);
        secondSeparatorLabel.setBackground(color);
      }
      super.setBackground(color);
    }

    private void setDisplayTime(final long time) {
   
      // following calls deliberately in this order to ensure
      // that the most frequently changing fields are refreshed onscreen first.

      updateMillisecondsFieldIfNecessary(time);  
      updateSecondsFieldIfNecessary(time);
      updateMinutesFieldIfNecessary(time);
      updateHoursFieldIfNecessary(time);
    }

    private void updateHoursFieldIfNecessary(long time) {
      final int newHours = TimeExtractor.getHours(time);
      if (newHours != hours || isInitializing) {
        hourLabel.setText(DigitStringFactory.getTwoDigitNumber(newHours));
        hours = newHours;
      }
    }

    private void updateMinutesFieldIfNecessary(long time) {
      final int newMinutes = TimeExtractor.getMinutes(time);
      if (newMinutes != minutes || isInitializing) {
        minuteLabel.setText(DigitStringFactory.getTwoDigitNumber(newMinutes));
        minutes = newMinutes;
      }
    }

    private void updateSecondsFieldIfNecessary(long time) {
      final int newSeconds = TimeExtractor.getSeconds(time);
      if (newSeconds != seconds || isInitializing) {
        secondLabel.setText(DigitStringFactory.getTwoDigitNumber(newSeconds));
        seconds = newSeconds;
      }
    }

    private void updateMillisecondsFieldIfNecessary(long time) {
      if (showMilliseconds) {
        millisecondLabel.setText(DigitStringFactory.getThreeDigitNumber(
         TimeExtractor.getMilliseconds(time)));
      }
    }

    public void reset() {
      publishEvent(
          StopWatchViewEvents.ResetRequested
      );
    }

    public void start() {
      publishEvent(
          StopWatchViewEvents.StartRequested
      );
    }

    public void stop() {
      publishEvent(
          StopWatchViewEvents.StopRequested
      );
    }
    
    private JLabel getHourLabel() {
      hourLabel = new JLabel();
      setBaseLabelCharacteristics(hourLabel);
      return  hourLabel;
    }

    private JLabel getMinuteLabel() {
      minuteLabel = new JLabel();
      setBaseLabelCharacteristics(minuteLabel);
      return minuteLabel;
    }

    private JLabel getSecondLabel() {
      secondLabel = new JLabel();
      setBaseLabelCharacteristics(secondLabel);
      return secondLabel;
    }


    private JLabel getMillisecondLabel() {
      millisecondLabel = new JLabel();
      setMillisecondLabelCharacteristics(millisecondLabel);
      return millisecondLabel;
    }

    private void setMillisecondLabelCharacteristics(JLabel label) {
      setLabelAlignment(label);
      label.setFont(MILLISECOND_FONT);
    }

    private void setLabelAlignment(JLabel label) {
      label.setHorizontalAlignment(SwingConstants.LEFT);
      label.setVerticalAlignment(SwingConstants.BOTTOM);
    }

    private JLabel getBaseTimeSeparator() {
      JLabel label = new JLabel(":");
      setBaseLabelCharacteristics(label);
      return label;
    }

    private void setBaseLabelCharacteristics(JLabel label) {
      setLabelAlignment(label);
      label.setFont(BASE_TIME_FONT);
    }

    private JLabel getMillisecondTimeSeparator() {
      millisecondTimeSeparatorLabel = new JLabel(":");
      setMillisecondLabelCharacteristics(millisecondTimeSeparatorLabel);
      return millisecondTimeSeparatorLabel;
    }

    private void drawPanel() {
      int labelCol = 0;

      setBackground(Color.WHITE);

      minuteSeparatorLabel = getBaseTimeSeparator();
      secondSeparatorLabel = getBaseTimeSeparator();

      gbc.gridwidth    = 1;
      gbc.gridheight   = 1;
      gbc.gridx        = labelCol++;
      gbc.gridy        = 0;
      gbc.ipadx        = 0;
      gbc.ipady        = 0;
      gbc.weightx      = 1;
      gbc.weighty      = 0;
      gbc.fill         = GridBagConstraints.HORIZONTAL;

      gbc.insets = new Insets(0,0,0,0);

      add(new JLabel(""), gbc);

      gbc.gridx        = labelCol++;
      gbc.weightx = 0;
      gbc.anchor       = GridBagConstraints.NORTH;

      add(getHourLabel(),gbc);

      gbc.gridx        = labelCol++;

      add(minuteSeparatorLabel,gbc);

      gbc.gridx        = labelCol++;

      add(getMinuteLabel(),gbc);

      gbc.gridx        = labelCol++;

      add(secondSeparatorLabel,gbc);

      gbc.gridx        = labelCol++;

      add(getSecondLabel(),gbc);

      gbc.gridx        = labelCol++;

      gbc.insets = new Insets(0,0,0,0);

      add(getMillisecondTimeSeparator(),gbc);

      gbc.gridx        = labelCol++;

      add(getMillisecondLabel(), gbc);

      gbc.gridx        = labelCol++;
      gbc.weightx      = 1;
      gbc.fill         = GridBagConstraints.HORIZONTAL;

      add(new JLabel(""), gbc);
    }

    public void toggleShowMilliseconds() {
      showMilliseconds = !showMilliseconds;
      millisecondLabel.setVisible(showMilliseconds);
      millisecondTimeSeparatorLabel.setVisible(showMilliseconds);
    }

    public boolean showingMilliseconds() {
      return showMilliseconds;
    }

    public void setTime(long time) {
      setDisplayTime(time);
    }
  }
}