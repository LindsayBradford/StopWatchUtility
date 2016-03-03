// (c) 2003 - Lindsay Bradford

package blacksmyth.stopwatch.system;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import javax.swing.border.BevelBorder;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.SoftBevelBorder;

import blacksmyth.stopwatch.StopWatchBuilder;
import blacksmyth.stopwatch.model.SimpleStopWatch;

@SuppressWarnings("serial")
class StopWatchPanel extends JPanel implements Observer {
  private static final Font  BASE_TIME_FONT    = new Font("Monospaced", Font.BOLD, 20);
  private static final Font  MILLISECOND_FONT  = new Font("Monospaced", Font.BOLD, 15);

  private SimpleStopWatch stopWatch;

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
    bindStopWatch();
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

  private void bindStopWatch() {
    stopWatch = StopWatchBuilder.buildModel();
    subscribeToStopWatch(this);
  }
  
  public void subscribeToStopWatch(Observer observer) {
    stopWatch.addObserver(observer);
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
    stopWatch.reset();
  }

  public void start() {
    stopWatch.start();
  }

  public void stop() {
    stopWatch.stop();
  }
  
  public void die() {
    stopWatch.die();
  }

  public boolean running() {
    return stopWatch.isRunning();
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

  public long getTime() {
    return stopWatch.getTime();
  }

  public void setTime(long time) {
    stopWatch.setTime(time);
    setDisplayTime(time);
  }

  @Override
  public void update(Observable o, Object timeAsObject) {
    long elapsedTime = ((Long) timeAsObject).longValue();
    setDisplayTime(elapsedTime);
  }
}
