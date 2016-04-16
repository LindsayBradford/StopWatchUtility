package blacksmyth.stopwatch.view.swing;

import java.awt.Color;
import java.awt.Component;

import blacksmyth.swing.JOkCancelDialog;

@SuppressWarnings("serial")
public class JTimerUpdateDialog extends JOkCancelDialog {

  private final JTimerPanel timerPanel = new JTimerPanel();

  private Component owner;

  long time;

  public JTimerUpdateDialog(Component owner) {
    super(null, "Set Time", true);
    this.owner = owner;
    setContent(timerPanel);
    setLocationRelativeTo(owner);
  }

  public void setForeground(Color color) {
    if (timerPanel != null) {
      timerPanel.setForeground(color);
    }
    super.setForeground(color);
  }

  public void setVisible(boolean b) {
    setLocationRelativeTo(owner);
    super.setVisible(b);
  }

  public void setBackground(Color color) {
    if (timerPanel != null) {
      timerPanel.setBackground(color);
    }
    super.setBackground(color);
  }

  protected void doOkAction() {
    time = timerPanel.getTime(); 
    super.doOkAction();
  }

  public long getTime() {
    return time;
  }

  public void setTime(long time) {
    this.time = time;
    timerPanel.setTime(time);
  }
}
