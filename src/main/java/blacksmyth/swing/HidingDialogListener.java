package blacksmyth.swing;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JDialog;

final public class HidingDialogListener implements ActionListener {
  private JDialog dialog;

  public HidingDialogListener(JDialog dialog) {
    this.dialog = dialog;
  }

  public void actionPerformed(ActionEvent event) {
    dialog.pack();
    dialog.setVisible(true);
  }
}
