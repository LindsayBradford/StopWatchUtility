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

  JButton startStopButton;
  private JButton resetButton;
  
  private boolean running = false;

  private GridBagLayout gbl      = new GridBagLayout();
  private GridBagConstraints gbc = new GridBagConstraints();

  StopWatchViewEventRaiser eventRaiser;

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
    this.ledPanel = new JGoStopPanel();
    add(this.ledPanel, BorderLayout.WEST);
  }

  private void addControls() {
    this.controlPanel = getControlPanel();
    add(this.controlPanel, BorderLayout.SOUTH);
  }
  
  @Override
  public void setForeground(Color color) {
    if (this.controlPanel != null) {
      this.controlPanel.setForeground(color);
      this.ledPanel.setForeground(color);
      this.startStopButton.setForeground(color);
      this.resetButton.setForeground(color);
    }
    super.setForeground(color);
  }

  @Override
  public void setBackground(Color color) {
    if (this.controlPanel != null) {
      this.controlPanel.setBackground(color);
      this.ledPanel.setBackground(color);
      this.startStopButton.setBackground(color);
      this.resetButton.setBackground(color);
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
    return this.stopWatchPanel;
  }

  /**
   * Allows builders to specify that the start/stop button of this control panel be the default button for the 
   * supplied {@link JFrame}.
   * @param frame
   */
  public void setStartStopButtonAsDefault(JFrame frame) {
    frame.getRootPane().setDefaultButton(this.startStopButton);
  }

  private JPanel getControlPanel() {
    JPanel panel = new JPanel(this.gbl);

    int buttonCol = 0;

    this.gbc.gridwidth    = 1;
    this.gbc.gridheight   = 1;
    this.gbc.gridx        = buttonCol++;
    this.gbc.gridy        = 0;
    this.gbc.ipadx        = 11;
    this.gbc.ipady        = 3;
    this.gbc.weightx      = 1;
    this.gbc.weighty      = 1;
    this.gbc.fill         = GridBagConstraints.BOTH;

    this.gbc.insets = new Insets(17,0,0,0);

    panel.add(new JLabel(""), this.gbc);

    this.gbc.insets = new Insets(17,0,0,5);

    this.gbc.gridx        = buttonCol++;
    this.gbc.weightx = 0;
    this.gbc.fill         = GridBagConstraints.VERTICAL;

    panel.add(getStartStopButton(),this.gbc);

    this.gbc.insets = new Insets(17,0,0,0);

    this.gbc.gridx        = buttonCol++;

    panel.add(getResetButton(), this.gbc);

    this.gbc.gridx        = buttonCol++;
    this.gbc.weightx      = 1;
    this.gbc.fill         = GridBagConstraints.BOTH;

    panel.add(new JLabel(""), this.gbc);

    alignButtonSizes();

    return panel;
  }

  private JButton getStartStopButton() {
    this.startStopButton = new JButton("Start"); 

    this.startStopButton.setMnemonic('S');

    this.startStopButton.addActionListener(new ActionListener(){
      @Override
      public void actionPerformed(ActionEvent ae) {
        switch (JStopWatchControlPanel.this.startStopButton.getText()) {
          case "Start": 
            JStopWatchControlPanel.this.eventRaiser.raise(
                StopWatchViewEvent.StartRequested
            );
            break;
          case "Stop": 
            JStopWatchControlPanel.this.eventRaiser.raise(
                StopWatchViewEvent.StopRequested
            );
            break;
        }
      }
    });

    return this.startStopButton;
  }

  private JButton getResetButton() {
    this.resetButton = new JButton();

    this.resetButton.setText("Reset");
    this.resetButton.setMnemonic('R');

    this.resetButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent ae) {
        JStopWatchControlPanel.this.eventRaiser.raise(
            StopWatchViewEvent.ResetRequested
        );
      }
    });

    return this.resetButton;
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
    if (this.running) {
      return;
    }

    this.running = true;
    playClickSound();
    this.startStopButton.setText("Stop");
    this.ledPanel.activate();
  }

  private void stop() {
    if (!this.running) {
      return;
    }
    
    this.running = false;
    playClickSound();
    this.ledPanel.deactivate();
    this.startStopButton.setText("Start");
  }
  
  private void reset() {
    stop();
  }
  
  private void alignButtonSizes() {
    LinkedList<JComponent> buttons = new LinkedList<JComponent>();

    buttons.add(this.startStopButton);
    buttons.add(this.resetButton);

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
    this.ledPanel.setVisible(ledsVisible);
  }
}
