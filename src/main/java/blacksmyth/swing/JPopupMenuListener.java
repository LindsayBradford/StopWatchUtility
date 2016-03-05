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
import java.awt.event.*;

public class JPopupMenuListener extends MouseAdapter {
  private JPopupMenu vMenu;

  public JPopupMenuListener(JPopupMenu pMenu) {
    vMenu = pMenu;    
  }

  public void mouseReleased(MouseEvent e) {
    processMouseEvent(e);
  }

  public void mousePressed(MouseEvent e) {
    processMouseEvent(e);
  }
  
  private void processMouseEvent(MouseEvent e) {
    if (e.isPopupTrigger()) {
      vMenu.show(e.getComponent(), e.getX(), e.getY());
    }
  }
}