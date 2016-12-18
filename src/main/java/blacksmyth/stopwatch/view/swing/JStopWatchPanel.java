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

import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.BevelBorder;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.SoftBevelBorder;

import blacksmyth.stopwatch.view.DigitStringFactory;
import blacksmyth.stopwatch.view.TimeExtractor;

/**
 * A sub-class of {@link JPanel} that renders an elapsed time (supplied as a long) in a user-readable format of 
 * hh:mm:ss, with the optional display of milliseconds.  
 */

@SuppressWarnings("serial")
final class JStopWatchPanel extends JPanel {
  private final Font  BASE_TIME_FONT    = new Font("Monospaced", Font.BOLD, 20);
  private final Font  MILLISECOND_FONT  = new Font("Monospaced", Font.BOLD, 15);

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

  public JStopWatchPanel() {
    super();
    setLayout(gbl);
    resetBorder(new SoftBevelBorder(BevelBorder.LOWERED));

    drawPanel();
    setTimeFieldsInChangeFrequencyOrder(0);
    isInitializing = false;
  }

  @Override
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

  @Override
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
    millisecondLabel.setText(
        DigitStringFactory.getThreeDigitNumber(
            TimeExtractor.getMilliseconds(time)
        )
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

  private static void setLabelAlignment(JLabel label) {
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

    add(getHourLabel(), gbc);

    gbc.gridx        = labelCol++;

    add(minuteSeparatorLabel, gbc);

    gbc.gridx        = labelCol++;

    add(getMinuteLabel(), gbc);

    gbc.gridx        = labelCol++;

    add(secondSeparatorLabel, gbc);

    gbc.gridx        = labelCol++;

    add(getSecondLabel(), gbc);

    gbc.gridx        = labelCol++;

    gbc.insets = new Insets(0,0,0,0);

    add(getMillisecondTimeSeparator(), gbc);

    gbc.gridx        = labelCol++;

    add(getMillisecondLabel(), gbc);

    gbc.gridx        = labelCol++;
    gbc.weightx      = 1;
    gbc.fill         = GridBagConstraints.HORIZONTAL;

    add(new JLabel(""), gbc);
  }
  
  public void setTime(long time) {
    setTimeFieldsInChangeFrequencyOrder(time);
  }

  public void setTimeFieldsInChangeFrequencyOrder(long time) {
    updateMillisecondsFieldIfNecessary(time);  
    updateSecondsFieldIfNecessary(time);
    updateMinutesFieldIfNecessary(time);
    updateHoursFieldIfNecessary(time);
  }

  public void setMillisecondsVisible(boolean showMilliseconds) {
    this.millisecondTimeSeparatorLabel.setVisible(showMilliseconds);
    this.millisecondLabel.setVisible(showMilliseconds);
  }
}