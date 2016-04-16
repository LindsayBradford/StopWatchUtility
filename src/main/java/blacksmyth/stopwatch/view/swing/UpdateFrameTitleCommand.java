package blacksmyth.stopwatch.view.swing;

import blacksmyth.swing.JUserNameableFrame;

public class UpdateFrameTitleCommand implements StopWatchCommand {
  
  private JUserNameableFrame frame;
  
  public void setFrame(JUserNameableFrame frame) {
    this.frame = frame;
  }

  @Override
  public void run() {
    frame.showTitleDialog();
  }

}
