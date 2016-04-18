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

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.LinkedList;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import blacksmyth.stopwatch.view.StopWatchViewEventRaiser;
import blacksmyth.stopwatch.view.StopWatchViewEvent;
import blacksmyth.stopwatch.view.swing.JStopWatchPanel;
import blacksmyth.swing.JUtilities;

/**
 *  An implementation of {@link JPanel} that renders a Swing StopWatch panel complete with 
 *  start/stop button, reset button, and optionally visible {@link JGoStopPanel} to indicate when the
 *  stopwatch is running.
 */

@SuppressWarnings("serial")
final class JStopWatchControlPanel extends JPanel {

  private JPanel controlPanel;
  
  private final JStopWatchPanel stopWatchPanel = new JStopWatchPanel();

  private JGoStopPanel ledPanel;

  private JButton startStopButton;
  private JButton resetButton;
  
  private boolean running = false;

  private GridBagLayout gbl      = new GridBagLayout();
  private GridBagConstraints gbc = new GridBagConstraints();

  private StopWatchViewEventRaiser eventRaiser;

  private PersistedSwingState toggleMillisecondsState;
  private PersistedSwingState toggleLedsState;

//  private static final AudioClip soundClip = 
//    ResourceLoader.loadAudioClip("/resources/stopwatch.wav");

  public JStopWatchControlPanel() {
    super(new BorderLayout());
    initialise();
  }

  public void setEventRaiser(StopWatchViewEventRaiser eventRaiser) {
    this.eventRaiser = eventRaiser;
  }

  public void setPersistedToggleMilliseconds(PersistedSwingState toggleMillisecondsState) {
    this.toggleMillisecondsState = toggleMillisecondsState;
    showMillisecondsAsPerPersistedState();

  }

  public void setPersistedToggleLeds(PersistedSwingState toggleLedsState) {
    this.toggleLedsState = toggleLedsState;
    showLedsAsPerPersistedState();
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
  
  @Override
  public void setForeground(Color color) {
    if (controlPanel != null) {
      controlPanel.setForeground(color);
      ledPanel.setForeground(color);
      startStopButton.setForeground(color);
      resetButton.setForeground(color);
    }
    super.setForeground(color);
  }

  @Override
  public void setBackground(Color color) {
    if (controlPanel != null) {
      controlPanel.setBackground(color);
      ledPanel.setBackground(color);
      startStopButton.setBackground(color);
      resetButton.setBackground(color);
    }
    super.setBackground(color);
  }
  
  /**
   * Sets the elapsed time (as a long) to display to users.
   * @param time
   */

  public void setTime(long time) {
    getStopWatchPanel().setTime(time);
  }
  
  private JStopWatchPanel getStopWatchPanel() {
    return stopWatchPanel;
  }

  /**
   * Allows builders to specify that the start/stop button of this control panel be the default button for the 
   * supplied {@link JFrame}.
   * @param frame
   */
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
                StopWatchViewEvent.StartRequested
            );
            break;
          case "Stop": 
            eventRaiser.raise(
                StopWatchViewEvent.StopRequested
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
            StopWatchViewEvent.ResetRequested
        );
      }
    });

    return resetButton;
  }
  
  /**
   * If a {@link StopWatchViewEvent} is raised, calling this method with the event raised will see the 
   * component appropriately alter its appearance to match the event. 
   * @param event
   */
  public void processStopWatchEvent(StopWatchViewEvent event) {
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

  /**
   * If a {@link SwingStopWatchViewEvent} is raised, calling this method with the event raised will see the 
   * component appropriately alter its appearance to match the event. 
   * @param event
   */

  public void processSwingEvent(SwingStopWatchViewEvents event) {
    switch (event) {
      case ToggleMillisecondsRequested:
        showMillisecondsAsPerPersistedState();
        break;
      case ToggleLedsRequested:
        showLedsAsPerPersistedState();
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
  
  private void showMillisecondsAsPerPersistedState() {
    setMillisecondsVisible(
        this.toggleMillisecondsState.getAsBoolean()
    );
  }

  private void setMillisecondsVisible(boolean millisecondsVisible) {
    getStopWatchPanel().setMillisecondsVisible(millisecondsVisible);
  }
  
  private void showLedsAsPerPersistedState() {
    setLedsVisible(
        this.toggleLedsState.getAsBoolean()
    );
  }
  
  private void setLedsVisible(boolean ledsVisible) {
    ledPanel.setVisible(ledsVisible);
  }
}
