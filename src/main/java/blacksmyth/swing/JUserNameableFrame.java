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

@SuppressWarnings("serial")
public class JUserNameableFrame extends JFrame {
  private String vBaseTitle = "";
  private String vUserTitle = "";

  public JUserNameableFrame() {
    super();
  }
  
  public JUserNameableFrame(String baseFrameTitle) {
    super();
    setBaseTitle(baseFrameTitle);
  }

  public void bindPopupMenu() {
    final JPopupMenu vMenu = UserNameablePopupMenuFactory.getMenu(this);
    addMouseListener(new JPopupMenuListener(vMenu));
  }
  
  public void setBaseTitle(String pBaseTitle) {
    this.vBaseTitle = pBaseTitle.trim();
    setTitle(getUserTitle());
  }
  
  public String getBaseTitle() {
    return this.vBaseTitle;
  }
  
  public String getUserTitle() {
    return this.vUserTitle;
  }
  
  @Override
  public void setTitle(String pUserTitle) {
    String vFinalTitle;
    this.vUserTitle  = pUserTitle.trim();
    if (this.vUserTitle.equals("")) {
      vFinalTitle = this.vBaseTitle;
    }
    else {
      vFinalTitle = this.vUserTitle + " - " + this.vBaseTitle;
    }
    super.setTitle(vFinalTitle);
  }
  
  public void showTitleDialog() {
    String vNewTitle = JOptionPane.showInputDialog(this,
                                                   "Change frame title to:",
                                                   getUserTitle());
    if (vNewTitle != null) {
      setTitle(vNewTitle);
    }
  }
}

class UserNameablePopupMenuFactory {
  public static JPopupMenu getMenu(JUserNameableFrame pFrame) {
    final JPopupMenu vMenu = new JPopupMenu();
    
    vMenu.add(getSetTitleItem(pFrame));
    
    return vMenu;
  }
  
  private static JMenuItem getSetTitleItem(final JUserNameableFrame pFrame) {
    JMenuItem vSetTitleItem = new JMenuItem("Change Title...",'C');

    vSetTitleItem.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent pEvent) {
        pFrame.showTitleDialog();
      }
    });

//    vSetTitleItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_T, InputEvent.CTRL_MASK));

    return vSetTitleItem;
  }
}
