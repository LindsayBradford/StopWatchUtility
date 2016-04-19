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
    setLayout(this.gbl);
    resetBorder(new SoftBevelBorder(BevelBorder.LOWERED));

    drawPanel();
    setTime(0);
    this.isInitializing = false;
  }

  @Override
  public void setForeground(Color color) {
    if (this.hourLabel != null) {
      this.hourLabel.setForeground(color);
      this.minuteLabel.setForeground(color);
      this.secondLabel.setForeground(color);
      this.millisecondLabel.setForeground(color);
      this.millisecondTimeSeparatorLabel.setForeground(color);
      this.minuteSeparatorLabel.setForeground(color);
      this.secondSeparatorLabel.setForeground(color);
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
    if (this.hourLabel != null) {
      this.hourLabel.setBackground(color);
      this.minuteLabel.setBackground(color);
      this.secondLabel.setBackground(color);
      this.millisecondLabel.setBackground(color);
      this.millisecondTimeSeparatorLabel.setBackground(color);
      this.minuteSeparatorLabel.setBackground(color);
      this.secondSeparatorLabel.setBackground(color);
    }
    super.setBackground(color);
  }
 
  private void updateHoursFieldIfNecessary(long time) {
    final int newHours = TimeExtractor.getHours(time);
    if (newHours != this.hours || this.isInitializing) {
      this.hourLabel.setText(DigitStringFactory.getTwoDigitNumber(newHours));
      this.hours = newHours;
    }
  }

  private void updateMinutesFieldIfNecessary(long time) {
    final int newMinutes = TimeExtractor.getMinutes(time);
    if (newMinutes != this.minutes || this.isInitializing) {
      this.minuteLabel.setText(DigitStringFactory.getTwoDigitNumber(newMinutes));
      this.minutes = newMinutes;
    }
  }

  private void updateSecondsFieldIfNecessary(long time) {
    final int newSeconds = TimeExtractor.getSeconds(time);
    if (newSeconds != this.seconds || this.isInitializing) {
      this.secondLabel.setText(DigitStringFactory.getTwoDigitNumber(newSeconds));
      this.seconds = newSeconds;
    }
  }

  private void updateMillisecondsFieldIfNecessary(long time) {
      this.millisecondLabel.setText(DigitStringFactory.getThreeDigitNumber(
       TimeExtractor.getMilliseconds(time)));
  }
  
  private JLabel getHourLabel() {
    this.hourLabel = new JLabel();
    setBaseLabelCharacteristics(this.hourLabel);
    return  this.hourLabel;
  }

  private JLabel getMinuteLabel() {
    this.minuteLabel = new JLabel();
    setBaseLabelCharacteristics(this.minuteLabel);
    return this.minuteLabel;
  }

  private JLabel getSecondLabel() {
    this.secondLabel = new JLabel();
    setBaseLabelCharacteristics(this.secondLabel);
    return this.secondLabel;
  }


  private JLabel getMillisecondLabel() {
    this.millisecondLabel = new JLabel();
    setMillisecondLabelCharacteristics(this.millisecondLabel);
    return this.millisecondLabel;
  }

  private void setMillisecondLabelCharacteristics(JLabel label) {
    setLabelAlignment(label);
    label.setFont(this.MILLISECOND_FONT);
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
    label.setFont(this.BASE_TIME_FONT);
  }

  private JLabel getMillisecondTimeSeparator() {
    this.millisecondTimeSeparatorLabel = new JLabel(":");
    setMillisecondLabelCharacteristics(this.millisecondTimeSeparatorLabel);
    return this.millisecondTimeSeparatorLabel;
  }

  private void drawPanel() {
    int labelCol = 0;

    setBackground(Color.WHITE);

    this.minuteSeparatorLabel = getBaseTimeSeparator();
    this.secondSeparatorLabel = getBaseTimeSeparator();

    this.gbc.gridwidth    = 1;
    this.gbc.gridheight   = 1;
    this.gbc.gridx        = labelCol++;
    this.gbc.gridy        = 0;
    this.gbc.ipadx        = 0;
    this.gbc.ipady        = 0;
    this.gbc.weightx      = 1;
    this.gbc.weighty      = 0;
    this.gbc.fill         = GridBagConstraints.HORIZONTAL;

    this.gbc.insets = new Insets(0,0,0,0);

    add(new JLabel(""), this.gbc);

    this.gbc.gridx        = labelCol++;
    this.gbc.weightx = 0;
    this.gbc.anchor       = GridBagConstraints.NORTH;

    add(getHourLabel(),this.gbc);

    this.gbc.gridx        = labelCol++;

    add(this.minuteSeparatorLabel,this.gbc);

    this.gbc.gridx        = labelCol++;

    add(getMinuteLabel(),this.gbc);

    this.gbc.gridx        = labelCol++;

    add(this.secondSeparatorLabel,this.gbc);

    this.gbc.gridx        = labelCol++;

    add(getSecondLabel(),this.gbc);

    this.gbc.gridx        = labelCol++;

    this.gbc.insets = new Insets(0,0,0,0);

    add(getMillisecondTimeSeparator(),this.gbc);

    this.gbc.gridx        = labelCol++;

    add(getMillisecondLabel(), this.gbc);

    this.gbc.gridx        = labelCol++;
    this.gbc.weightx      = 1;
    this.gbc.fill         = GridBagConstraints.HORIZONTAL;

    add(new JLabel(""), this.gbc);
  }

  public void setTime(long time) {

    // following calls deliberately in this order to ensure
    // that the most frequently changing fields are refreshed onscreen first.

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