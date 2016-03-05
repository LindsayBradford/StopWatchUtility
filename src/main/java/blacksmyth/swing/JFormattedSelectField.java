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

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

@SuppressWarnings("serial")
public class JFormattedSelectField extends JFormattedTextField {
  private Color vNormalBackground;
  private Color vNormalForeground;

  public JFormattedSelectField(AbstractFormatter pFormatter) {
    super(pFormatter);

    setDisabledTextColor(Color.BLACK);
    setFont(new Font("Monospaced", Font.PLAIN, 11));

    vNormalBackground = getBackground();
    vNormalForeground = getForeground();
  }

  protected void processFocusEvent(FocusEvent e) {
    super.processFocusEvent(e);
    if (e.getID() == FocusEvent.FOCUS_GAINED) {
      selectAll();
    }
  }

  public void postActionEvent() {
    super.postActionEvent();
    selectAll();
  }
  
  public void setEnabled(boolean vEnabledFlag) {
    super.setEnabled(vEnabledFlag);
    if (vEnabledFlag == false) {
      setBackground(Color.LIGHT_GRAY);
    } else {
      setBackground(vNormalBackground);
    }
  }

  protected void invalidEdit() {

    super.invalidEdit();

    setBackground(Color.RED);
    setForeground(Color.WHITE);
    
    final JFormattedSelectField vField = this;
    
    ActionListener vColorChanger = new ActionListener() {
      public void actionPerformed(ActionEvent ae) {
        vField.setBackground(vNormalBackground);
        vField.setForeground(vNormalForeground);
      }
    };
    
    Timer vTimer = new Timer(75, vColorChanger);
    vTimer.setRepeats(false);
    vTimer.start();
  }
}
