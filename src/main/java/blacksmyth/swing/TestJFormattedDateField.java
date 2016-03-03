
package blacksmyth.swing;

import javax.swing.*;
import java.util.*;
import java.awt.event.*;

public class TestJFormattedDateField {

  public static void main(String args[]) {

    JFrame vFrame = new JFrame();
     
    vFrame.addWindowListener(new WindowAdapter() {
      public void windowClosing(WindowEvent we) {
        System.exit(0);
      }
    });

    vFrame.setContentPane(getContentPane());
    vFrame.pack();
//    vFrame.setResizable(false);

    vFrame.setVisible(true);

  }

  private static JPanel getContentPane() {
    JPanel vPanel = new JPanel();
    vPanel.add(new JLabel("Date Field One"));
    vPanel.add(new JFormattedDateField("dd/MM/yy", new Date(), 8));
    vPanel.add(new JLabel("Date Field Two"));
    vPanel.add(new JFormattedDateField("MM/dd/yyyy", new Date(), 8));
    return vPanel;
  }

}