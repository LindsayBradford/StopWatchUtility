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
  private String baseFrameTitle = "";
  private String userExtensionToFrameTitle = "";

  public JUserNameableFrame() {
    super();
  }
  
  public JUserNameableFrame(String title) {
    super();
    setBaseFrameTitle(title);
  }

  public void bindPopupMenu() {
    final JPopupMenu vMenu = UserNameablePopupMenuFactory.getMenu(this);
    addMouseListener(new JPopupMenuListener(vMenu));
  }
  
  public void setBaseFrameTitle(String title) {
    baseFrameTitle = title.trim();
    setTitle(getUserExtensionToFrameTitle());
  }
  
  public String getBaseTitle() {
    return baseFrameTitle;
  }
  
  public String getUserExtensionToFrameTitle() {
    return userExtensionToFrameTitle;
  }
  
  @Override
  public void setTitle(String userExtension) {
    userExtensionToFrameTitle = userExtension == null ? null : userExtension.trim();
    
    super.setTitle(
        buildDisplayTitle()
    );
  }
  
  private String buildDisplayTitle() {
    if (userExtensionToFrameTitle == null || userExtensionToFrameTitle.equals("")) {
      return  baseFrameTitle;
    }

    return userExtensionToFrameTitle + " - " + baseFrameTitle;
  }
  
  public void showTitleDialog() {
    String userExtensionToFrameTitle = 
        JOptionPane.showInputDialog(
            this,
            "Change frame title to:",
            getUserExtensionToFrameTitle()
        );
    setTitle(userExtensionToFrameTitle);
  }
}

class UserNameablePopupMenuFactory {
  public static JPopupMenu getMenu(JUserNameableFrame frame) {
    final JPopupMenu menu = new JPopupMenu();
    
    menu.add(getSetTitleItem(frame));
    
    return menu;
  }
  
  private static JMenuItem getSetTitleItem(final JUserNameableFrame frame) {
    JMenuItem changeTitleMenuItem = new JMenuItem("Change Title...",'C');

    changeTitleMenuItem.addActionListener(
        new ActionListener() {
          @Override
          public void actionPerformed(ActionEvent pEvent) {
            frame.showTitleDialog();
          }
        }
    );

    return changeTitleMenuItem;
  }
}
