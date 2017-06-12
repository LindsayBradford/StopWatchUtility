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
import java.awt.Component;

import blacksmyth.swing.JOkCancelDialog;

/**
 * A {@link JOkCancelDialog} that prompts a user for an elapsed-time amount,
 * and allows callers to retrieve the time specified as a long value.
 */

@SuppressWarnings("serial")
final class JTimerUpdateDialog extends JOkCancelDialog {

  private final JTimerPanel timerPanel = new JTimerPanel();

  private Component owner;

  long time;

  public JTimerUpdateDialog(Component owner) {
    super(null, "Set Time");
    this.owner = owner;
    setContent(timerPanel);
    setLocationRelativeTo(owner);
  }

  @Override
  public void setForeground(Color color) {
    if (timerPanel != null) {
      timerPanel.setForeground(color);
    }
    super.setForeground(color);
  }

  @Override
  public void setVisible(boolean b) {
    setLocationRelativeTo(this.owner);
    super.setVisible(b);
  }

  @Override
  public void setBackground(Color color) {
    if (timerPanel != null) {
      timerPanel.setBackground(color);
    }
    super.setBackground(color);
  }

  @Override
  protected void doOkAction() {
    this.time = timerPanel.getTime(); 
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
