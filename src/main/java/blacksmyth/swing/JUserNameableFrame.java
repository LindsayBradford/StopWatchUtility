// (c) 2002 - Lindsay Bradford -

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
    vBaseTitle = pBaseTitle.trim();
    setTitle(getUserTitle());
  }
  
  public String getBaseTitle() {
    return vBaseTitle;
  }
  
  public String getUserTitle() {
    return vUserTitle;
  }
  
  public void setTitle(String pUserTitle) {
    String vFinalTitle;
    vUserTitle  = pUserTitle.trim();
    if (vUserTitle.equals("")) {
      vFinalTitle = vBaseTitle;
    }
    else {
      vFinalTitle = vUserTitle + " - " + vBaseTitle;
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
      public void actionPerformed(ActionEvent pEvent) {
        pFrame.showTitleDialog();
      }
    });

//    vSetTitleItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_T, InputEvent.CTRL_MASK));

    return vSetTitleItem;
  }
}
