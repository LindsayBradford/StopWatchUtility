/*
 * Created on 18/10/2003
 *
 */
package blacksmyth.swing;

import java.awt.BorderLayout;

import java.awt.Color;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.BorderFactory;


/**
 * @author Lindsay Bradford
 */
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

  public String getStatus() {
    return statusLabel.getText() ;
  }

  public void setStatus(String message) {
    statusLabel.setText(message);
  }
}


