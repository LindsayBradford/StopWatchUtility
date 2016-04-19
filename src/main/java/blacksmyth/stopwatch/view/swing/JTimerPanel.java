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
import javax.swing.SwingConstants;
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
    setLayout(this.gbl);
    setBorder(new EmptyBorder(10,0,0,5));
    getContents();
  }

  public void setTime(long time) {
    this.time = time;
    calculateWidgetTimes(); 
  }

  @Override
  public void setForeground(Color color) {
    if (this.hourMinuteSeparator!= null) {
      this.hourMinuteSeparator.setForeground(color);
      this.minuteSecondSeparator.setForeground(color);
      this.secondMillisecondSeparator.setForeground(color);
    }
    super.setForeground(color);
  }

  @Override
  public void setBackground(Color color) {
    if (this.hourMinuteSeparator!= null) {
      this.hourMinuteSeparator.setBackground(color);
      this.minuteSecondSeparator.setBackground(color);
      this.secondMillisecondSeparator.setBackground(color);
    }
    super.setBackground(color);
  }

  private void calculateWidgetTimes() {
    this.millisecondsField.setDouble(TimeExtractor.getMilliseconds(this.time));
    this.secondsField.setDouble(TimeExtractor.getSeconds(this.time));
    this.minutesField.setDouble(TimeExtractor.getMinutes(this.time));
    this.hoursField.setDouble(TimeExtractor.getHours(this.time));
  }

  public long getTime() {
    calculateTimeFromWidgets();
    return this.time;
  }

  private void calculateTimeFromWidgets() {
    double hoursValue = this.hoursField.getDouble();
    double minutesValue = this.minutesField.getDouble();
    double secondsValue = this.secondsField.getDouble();
    double millisecondsValue = this.millisecondsField.getDouble();

    this.time = (long) millisecondsValue + 
           (long) secondsValue * 1000 +
	   (long) minutesValue * 1000 * 60 + 
	   (long) hoursValue   * 1000 * 60 * 60;
  }

  private void getContents() {
    int labelCol = 0;

    this.gbc.gridwidth    = 1;
    this.gbc.gridheight   = 1;
    this.gbc.gridx        = labelCol++;
    this.gbc.gridy        = 0;
    this.gbc.ipadx        = 0;
    this.gbc.ipady        = 0;
    this.gbc.weightx      = 1;
    this.gbc.weighty      = 0;
    this.gbc.fill         = GridBagConstraints.HORIZONTAL;

    this.gbc.insets = new Insets(0,2,0,2);

    add(new JLabel(""), this.gbc);

    this.gbc.gridx        = labelCol++;
    this.gbc.weightx = 0;
    this.gbc.anchor       = GridBagConstraints.SOUTH;

    add(getHoursField(),this.gbc);

    this.gbc.gridx        = labelCol++;

    this.hourMinuteSeparator = getTimeSeparator();
    add(this.hourMinuteSeparator,this.gbc);

    this.gbc.gridx        = labelCol++;

    add(getMinutesField(),this.gbc);

    this.gbc.gridx        = labelCol++;

    this.minuteSecondSeparator = getTimeSeparator();
    add(this.minuteSecondSeparator,this.gbc);

    this.gbc.gridx        = labelCol++;

    add(getSecondsField(),this.gbc);

    this.gbc.gridx        = labelCol++;
    this.gbc.anchor       = GridBagConstraints.NORTH;

    this.secondMillisecondSeparator = getMillisecondTimeSeparator();

    add(this.secondMillisecondSeparator,this.gbc);

    this.gbc.gridx        = labelCol++;

    add(getMillisecondsField(), this.gbc);

    this.gbc.gridx        = labelCol++;
    this.gbc.weightx      = 1;
    this.gbc.fill         = GridBagConstraints.HORIZONTAL;

    add(new JLabel(""), this.gbc);
  }

  private JFormattedNumField getHoursField() {
    this.hoursField = getBasicField();
    this.hoursField.setUpperBound(99);
    this.hoursField.setToolTipText(" Enter elapsed hours. ");
    return this.hoursField;
  }

  private JFormattedNumField getMinutesField() {
    this.minutesField = getBasicField();
    this.minutesField.setToolTipText(" Enter elapsed minutes. ");
    return this.minutesField;
  }

  private JFormattedNumField getSecondsField() {
    this.secondsField = getBasicField();
    this.secondsField.setToolTipText(" Enter elapsed seconds. ");
    return this.secondsField;
  }

  private JFormattedNumField getMillisecondsField() {
    this.millisecondsField = getField("000", 4);
    this.millisecondsField.setBounds(0,999);
    this.millisecondsField.setHorizontalAlignment(SwingConstants.CENTER);
    this.millisecondsField.setToolTipText(" Enter elapsed milliseconds. ");
    return this.millisecondsField;
  }

  private static JFormattedNumField getBasicField() {
    JFormattedNumField field = getField("00", 3); 
    field.setFont(getDoubleSizeFont(field.getFont()));
    field.setHorizontalAlignment(SwingConstants.CENTER);
    field.setBounds(0,59);
    return field;
  }

  private static JFormattedNumField getField(String format, int columns) {
    JFormattedNumField field = new JFormattedNumField(format, 0, columns);
    return field;
  }

  private static JLabel getTimeSeparator() {
    JLabel label = getMillisecondTimeSeparator();
    label.setFont(getDoubleSizeFont(label.getFont()));
    return label;
  }

  
  static private Font getDoubleSizeFont(Font font) {
    return font.deriveFont((float) font.getSize()*3/2);
  }

  private static JLabel getMillisecondTimeSeparator() {
    return new JLabel(timeSeparator);
  }
}
