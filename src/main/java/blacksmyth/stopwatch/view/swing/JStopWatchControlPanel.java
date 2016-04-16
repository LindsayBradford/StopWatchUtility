package blacksmyth.stopwatch.view.swing;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.LinkedList;
import java.util.prefs.Preferences;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import blacksmyth.stopwatch.view.StopWatchViewEventRaiser;
import blacksmyth.stopwatch.view.StopWatchViewEvents;
import blacksmyth.stopwatch.view.swing.JStopWatchPanel;
import blacksmyth.swing.JUtilities;

@SuppressWarnings("serial")
public class JStopWatchControlPanel extends JPanel {

  private JPanel controlPanel;
  
  private final JStopWatchPanel stopWatchPanel = new JStopWatchPanel();

  private JGoStopPanel ledPanel;

  private JButton startStopButton;
  private JButton resetButton;
  
  private boolean running = false;

  private GridBagLayout gbl      = new GridBagLayout();
  private GridBagConstraints gbc = new GridBagConstraints();

  private StopWatchViewEventRaiser eventRaiser;

  private Preferences preferences;

//  private static final AudioClip soundClip = 
//    ResourceLoader.loadAudioClip("/resources/stopwatch.wav");

  public JStopWatchControlPanel() {
    super(new BorderLayout());
    initialise();
  }

  public void setEventRaiser(StopWatchViewEventRaiser eventRaiser) {
    this.eventRaiser = eventRaiser;
  }
  
  public void setPreferences(Preferences preferences) {
    this.preferences = preferences;
    showMillisecondsAsPerPreferences();
    showLedsAsPerPreferences();
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
    }
    super.setForeground(color);
  }

  public void setBackground(Color color) {
    if (controlPanel != null) {
      controlPanel.setBackground(color);
      ledPanel.setBackground(color);
      startStopButton.setBackground(color);
      resetButton.setBackground(color);
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

  public boolean showingLeds() {
    return ledPanel.isVisible();
  }

  public void setTime(long time) {
    getStopWatchPanel().setTime(time);
  }
  
  public JStopWatchPanel getStopWatchPanel() {
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

    startStopButton.addActionListener(new ActionListener(){
      public void actionPerformed(ActionEvent ae) {
        switch (startStopButton.getText()) {
          case "Start": 
            eventRaiser.raise(
                StopWatchViewEvents.StartRequested
            );
            break;
          case "Stop": 
            eventRaiser.raise(
                StopWatchViewEvents.StopRequested
            );
            break;
        }
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
        eventRaiser.raise(
            StopWatchViewEvents.ResetRequested
        );
      }
    });

    return resetButton;
  }
  
  public void processStopWatchEvent(StopWatchViewEvents event) {
    switch (event) {
      case StopRequested:
        stop();
        break;
      case StartRequested:
        start();
        break;
      case ResetRequested:
        reset();
        break;
      default: // nothing to do with other events. 
    }
  }
  
  public void processSwingEvent(SwingStopWatchViewEvents event) {
    switch (event) {
      case ToggleMillisecondsRequested:
        showMillisecondsAsPerPreferences();
        break;
      case ToggleLedsRequested:
        showLedsAsPerPreferences();
        break;
    }
  }

  private void start() {
    if (running) {
      return;
    }

    running = true;
    playClickSound();
    startStopButton.setText("Stop");
    ledPanel.activate();
  }

  private void stop() {
    if (!running) {
      return;
    }
    
    running = false;
    playClickSound();
    ledPanel.deactivate();
    startStopButton.setText("Start");
  }
  
  private void reset() {
    stop();
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
  
  private void showMillisecondsAsPerPreferences() {
    setMillisecondsVisible(
        preferences.getBoolean("showMilliseconds", true)
    );
  }

  private void setMillisecondsVisible(boolean millisecondsVisible) {
    getStopWatchPanel().setMillisecondsVisible(millisecondsVisible);
  }
  
  private void showLedsAsPerPreferences() {
    setLedsVisible(
        preferences.getBoolean("showLeds", true)
    );
  }
  
  private void setLedsVisible(boolean ledsVisible) {
    ledPanel.setVisible(ledsVisible);
  }
}
