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

import java.awt.BorderLayout;

import java.awt.Color;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.BorderFactory;

@SuppressWarnings("serial")
public class JStatusBar extends JPanel {
  private static JLabel statusLabel = new JLabel();

  private static JStatusBar INSTANCE = new JStatusBar();

  public static JStatusBar getInstance( ) {
    return INSTANCE;
  }  

  private JStatusBar() {
    super();
    setLayout(new BorderLayout());
    setBorder(BorderFactory.createEmptyBorder(0,2,2,2));
    statusLabel.setText("Press <F1> for a list of commands.");
    statusLabel.setForeground(Color.DARK_GRAY);
    statusLabel.setBorder(
      BorderFactory.createCompoundBorder(
        BorderFactory.createLoweredBevelBorder(),
        BorderFactory.createEmptyBorder(0,2,0,2)));
    add(statusLabel, BorderLayout.CENTER);
  }

  public static String getStatus() {
    return statusLabel.getText() ;
  }

  public static void setStatus(String message) {
    statusLabel.setText(message);
  }
}


