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

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JPopupMenu;

public class JPopupMenuListener extends MouseAdapter {
  private JPopupMenu popupMenu;

  public JPopupMenuListener(JPopupMenu menu) {
    popupMenu = menu;    
  }

  @Override
  public void mouseReleased(MouseEvent e) {
    processMouseEvent(e);
  }

  @Override
  public void mousePressed(MouseEvent e) {
    processMouseEvent(e);
  }
  
  private void processMouseEvent(MouseEvent e) {
    if (e.isPopupTrigger()) {
      popupMenu.show(
          e.getComponent(), 
          e.getX(), 
          e.getY()
      );
    }
  }
}