/**
 * Copyright (c) 2016, Lindsay Bradford and other Contributors.
 * All rights reserved.
 * 
 * This program and the accompanying materials  are made available 
 * under the terms of the BSD 3-Clause licence  which accompanies 
 * this distribution, and is available at
 * http://opensource.org/licenses/BSD-3-Clause
 */

package blacksmyth.stopwatch.view.swing;

import blacksmyth.swing.JUserNameableFrame;

/**
 * An implementation of {@link StopWatchCommand} that allows the user to update the title text of 
 * a {@link JUserNameableFrame}.
 */

final class UpdateFrameTitleCommand implements StopWatchCommand {
  
  private JUserNameableFrame frame;
  
  public void setFrame(JUserNameableFrame frame) {
    this.frame = frame;
  }

  @Override
  public void run() {
    this.frame.showTitleDialog();
  }

}
