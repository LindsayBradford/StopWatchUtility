
package blacksmyth.swing;

import javax.swing.*;
import java.awt.event.*;

public class TestJUserNameableFrame {

  public static void main(String args[]) {

    JUserNameableFrame vFrame  = new JUserNameableFrame();

    vFrame.addWindowListener(new WindowAdapter() {
      public void windowClosing(WindowEvent we) {
        System.exit(0);
      }
    });


    vFrame.setBaseTitle("Immutable Bit");   
    
    vFrame.setTitle("Mutable Bit");

    vFrame.setContentPane(getContentPane());

    vFrame.pack();
    vFrame.setResizable(false);

    /* center frame on screen */

//    JUtilities.centerWindow(vFrame);

    vFrame.setVisible(true);

  }

  private static JPanel getContentPane() {
    JPanel vPanel = new JPanel();
    vPanel.add(new JLabel("This is a really, really, really, really long label!"));
    return vPanel;
  }

}