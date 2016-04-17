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
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import javax.swing.border.EmptyBorder;

import blacksmyth.stopwatch.view.TimeExtractor;
import blacksmyth.swing.JFormattedNumField;

/**
 * A {@link JPanel} that displays a long value of elapsed milliseconds in a
 * more human-friendly manner (hours, minutes, seconds, etc), and allows the user
 * to input a different time in this format.
 */

@SuppressWarnings("serial")
final class JTimerPanel extends JPanel {
  private JFormattedNumField  hoursField;
  private JFormattedNumField  minutesField;
  private JFormattedNumField  secondsField;
  private JFormattedNumField  millisecondsField;

  private JLabel              hourMinuteSeparator;
  private JLabel              minuteSecondSeparator;
  private JLabel              secondMillisecondSeparator;

  private GridBagLayout gbl      = new GridBagLayout();
  private GridBagConstraints gbc = new GridBagConstraints();

  private long time;

  private static final String timeSeparator = ":";

  public JTimerPanel() {
    super();
    setLayout(gbl);
    setBorder(new EmptyBorder(10,0,0,5));
    getContents();
  }

  public void setTime(long time) {
    this.time = time;
    calculateWidgetTimes(); 
  }

  public void setForeground(Color color) {
    if (hourMinuteSeparator!= null) {
      hourMinuteSeparator.setForeground(color);
      minuteSecondSeparator.setForeground(color);
      secondMillisecondSeparator.setForeground(color);
    }
    super.setForeground(color);
  }

  public void setBackground(Color color) {
    if (hourMinuteSeparator!= null) {
      hourMinuteSeparator.setBackground(color);
      minuteSecondSeparator.setBackground(color);
      secondMillisecondSeparator.setBackground(color);
    }
    super.setBackground(color);
  }

  private void calculateWidgetTimes() {
    millisecondsField.setDouble(TimeExtractor.getMilliseconds(time));
    secondsField.setDouble(TimeExtractor.getSeconds(time));
    minutesField.setDouble(TimeExtractor.getMinutes(time));
    hoursField.setDouble(TimeExtractor.getHours(time));
  }

  public long getTime() {
    calculateTimeFromWidgets();
    return time;
  }

  private void calculateTimeFromWidgets() {
    double hoursValue = hoursField.getDouble();
    double minutesValue = minutesField.getDouble();
    double secondsValue = secondsField.getDouble();
    double millisecondsValue = millisecondsField.getDouble();

    time = (long) millisecondsValue + 
           (long) secondsValue * 1000 +
	   (long) minutesValue * 1000 * 60 + 
	   (long) hoursValue   * 1000 * 60 * 60;
  }

  private void getContents() {
    int labelCol = 0;

    gbc.gridwidth    = 1;
    gbc.gridheight   = 1;
    gbc.gridx        = labelCol++;
    gbc.gridy        = 0;
    gbc.ipadx        = 0;
    gbc.ipady        = 0;
    gbc.weightx      = 1;
    gbc.weighty      = 0;
    gbc.fill         = GridBagConstraints.HORIZONTAL;

    gbc.insets = new Insets(0,2,0,2);

    add(new JLabel(""), gbc);

    gbc.gridx        = labelCol++;
    gbc.weightx = 0;
    gbc.anchor       = GridBagConstraints.SOUTH;

    add(getHoursField(),gbc);

    gbc.gridx        = labelCol++;

    hourMinuteSeparator = getTimeSeparator();
    add(hourMinuteSeparator,gbc);

    gbc.gridx        = labelCol++;

    add(getMinutesField(),gbc);

    gbc.gridx        = labelCol++;

    minuteSecondSeparator = getTimeSeparator();
    add(minuteSecondSeparator,gbc);

    gbc.gridx        = labelCol++;

    add(getSecondsField(),gbc);

    gbc.gridx        = labelCol++;
    gbc.anchor       = GridBagConstraints.NORTH;

    secondMillisecondSeparator = getMillisecondTimeSeparator();

    add(secondMillisecondSeparator,gbc);

    gbc.gridx        = labelCol++;

    add(getMillisecondsField(), gbc);

    gbc.gridx        = labelCol++;
    gbc.weightx      = 1;
    gbc.fill         = GridBagConstraints.HORIZONTAL;

    add(new JLabel(""), gbc);
  }

  private JFormattedNumField getHoursField() {
    hoursField = getBasicField();
    hoursField.setUpperBound(99);
    hoursField.setToolTipText(" Enter elapsed hours. ");
    return hoursField;
  }

  private JFormattedNumField getMinutesField() {
    minutesField = getBasicField();
    minutesField.setToolTipText(" Enter elapsed minutes. ");
    return minutesField;
  }

  private JFormattedNumField getSecondsField() {
    secondsField = getBasicField();
    secondsField.setToolTipText(" Enter elapsed seconds. ");
    return secondsField;
  }

  private JFormattedNumField getMillisecondsField() {
    millisecondsField = getField("000", 4);
    millisecondsField.setBounds(0,999);
    millisecondsField.setHorizontalAlignment(JTextField.CENTER);
    millisecondsField.setToolTipText(" Enter elapsed milliseconds. ");
    return millisecondsField;
  }

  private JFormattedNumField getBasicField() {
    JFormattedNumField field = getField("00", 3); 
    field.setFont(getDoubleSizeFont(field.getFont()));
    field.setHorizontalAlignment(JTextField.CENTER);
    field.setBounds(0,59);
    return field;
  }

  private JFormattedNumField getField(String format, int columns) {
    JFormattedNumField field = new JFormattedNumField(format, 0, columns);
    return field;

  }

  private JLabel getTimeSeparator() {
    JLabel label = getMillisecondTimeSeparator();
    label.setFont(getDoubleSizeFont(label.getFont()));
    return label;
  }

  private Font getDoubleSizeFont(Font font) {
    return font.deriveFont((float) font.getSize()*3/2);
  }

  private JLabel getMillisecondTimeSeparator() {
    return new JLabel(timeSeparator);
  }
}
