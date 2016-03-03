// (c) 2002 - Lindsay Bradford -

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