/**
 * Copyright (c) 2016, Lindsay Bradford and other Contributors.
 * All rights reserved.
 * 
 * This program and the accompanying materials  are made available 
 * under the terms of the BSD 3-Clause licence  which accompanies 
 * this distribution, and is available at
 * http://opensource.org/licenses/BSD-3-Clause
 */

package blacksmyth.swing;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import javax.swing.JFormattedTextField;
import javax.swing.Timer;

@SuppressWarnings("serial")
public class JFormattedSelectField extends JFormattedTextField {
  protected Color normalBackground;
  protected Color normalForeground;

  public JFormattedSelectField(AbstractFormatter formatter) {
    super(formatter);

    setDisabledTextColor(Color.BLACK);
    setFont(new Font("Monospaced", Font.PLAIN, 11));

    this.normalBackground = getBackground();
    this.normalForeground = getForeground();
  }

  @Override
  protected void processFocusEvent(FocusEvent e) {
    super.processFocusEvent(e);
    if (e.getID() == FocusEvent.FOCUS_GAINED) {
      selectAll();
    }
  }

  @Override
  public void postActionEvent() {
    super.postActionEvent();
    selectAll();
  }
  
  @Override
  public void setEnabled(boolean enabledFlag) {
    super.setEnabled(enabledFlag);
    if (enabledFlag == false) {
      setBackground(Color.LIGHT_GRAY);
    } else {
      setBackground(this.normalBackground);
    }
  }

  @Override
  protected void invalidEdit() {

    super.invalidEdit();

    setBackground(Color.RED);
    setForeground(Color.WHITE);
    
    
    ActionListener colorChanger = 
      (actionEvent) -> {
        setBackground(JFormattedSelectField.this.normalBackground);
        setForeground(JFormattedSelectField.this.normalForeground);
      };
    
    Timer vTimer = new Timer(75, colorChanger);
    vTimer.setRepeats(false);
    vTimer.start();
  }
}
