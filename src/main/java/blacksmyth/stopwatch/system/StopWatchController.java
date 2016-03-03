// (c) 2003 - Lindsay Bradford -

package blacksmyth.stopwatch.system;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

import java.util.LinkedList;
import java.util.Observable;
import java.applet.AudioClip;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import blacksmyth.swing.JOkCancelDialog;
import blacksmyth.swing.JUtilities;

import blacksmyth.utilities.ResourceLoader;

import blacksmyth.stopwatch.domain.TimerObserver;

@SuppressWarnings("serial")
public class StopWatchController extends JPanel {

  private final StopWatchPanel stopWatchPanel = new StopWatchPanel();
  private JPanel controlPanel;
  private JGoStopPanel ledPanel;

  private JButton startStopButton;
  private ResetButton resetButton;

  private GridBagLayout gbl      = new GridBagLayout();
  private GridBagConstraints gbc = new GridBagConstraints();

  private final JTimerUpdateDialog setTimeDialog = 
          new JTimerUpdateDialog(this);

  private static final AudioClip soundClip = 
    ResourceLoader.loadAudioClip("/resources/stopwatch.wav");

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

  public long getTime() {
    return getStopWatchPanel().getTime();
  }

  public void setTime(long time) {
    getStopWatchPanel().setTime(time);
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

  private JButton getResetButton(){
    resetButton = new ResetButton(this);
    return resetButton;
  }

  public void toggle() {
    if (getStopWatchPanel().running()) {
      stop();
    } else {
      start();
    }
  }

  public void start() {
    playClickSound();
    startStopButton.setText("Stop");
    ledPanel.activate();
    getStopWatchPanel().start();
  }

  public void stop() {
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
    getStopWatchPanel().stop();
    getStopWatchPanel().die();
  }

  private void alignButtonSizes() {
    LinkedList<JComponent> buttons = new LinkedList<JComponent>();

    buttons.add(startStopButton);
    buttons.add(resetButton);

    JUtilities.equalizeComponentSizes(buttons);

    buttons.clear();
  }

  private static void playClickSound() {
	  // TODO: Bug: pulseAudio thread is created to play the clip, but continues running once done.
	  //       Looks like this: https://bugs.openjdk.java.net/browse/JDK-8077019

	  soundClip.play();
      soundClip.stop();  // <-- shouldn't be needed, but is to stop hangs. Stops audio from playing. 
  }

  public void toggleShowMilliseconds() {
    getStopWatchPanel().toggleShowMilliseconds();
  }

  public void toggleShowLeds() {
    ledPanel.setVisible(!ledPanel.isVisible());
  }

  public void showSetTimeDialog() {
    if (getStopWatchPanel().running()) {
      stop();
    }
    setTimeDialog.setTime(getTime());
    setTimeDialog.setVisible(true);
    setTime(setTimeDialog.getTime());
  }
  
}

@SuppressWarnings("serial")
class ResetButton extends JButton implements TimerObserver  {
  
  public ResetButton(final StopWatchController controller) {
    super("Reset");
    setMnemonic('R');

    addActionListener(new ActionListener(){
      public void actionPerformed(ActionEvent ae) {
        controller.reset();
      }
    });
    
    controller.getStopWatchPanel().subscribeToStopWatch(this);
  }

  @Override
  public void update(Observable timer, Object newTimeAsObject) {
    long newTime = ((Long) newTimeAsObject).longValue();

    if (newTime == 0) {
      setEnabled(false);
    } else if (!isEnabled()) {
      setEnabled(true);
    }
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

