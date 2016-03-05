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
import java.awt.*;
import java.awt.event.*;

final public class JFrameFactory {

  public final static int BASIC = 0;
  public final static int USER_NAMEABLE = 1;
  
  public static JFrame getCloseableJFrame(String title, int type) {
    JFrame frame = getBaseFrame(title, type);
    makeCloseableJFrame(frame);
    return frame;
  }
  
  public static JFrame getSystemExitJFrame(String title, int type) {
    JFrame frame = getBaseFrame(title, type);
    makeSystemExitJFrame(frame);
    return frame;
  }

  public static void makeCloseableJFrame(JFrame frame) {
    frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
  }

  public static void makeSystemExitJFrame(JFrame frame) {
    makeCloseableJFrame(frame);
    frame.addWindowListener(new WindowAdapter() {
      public void windowClosing(WindowEvent we) {
        System.exit(0);
      }
    });
  }
  
  private static JFrame getBaseFrame(String pTitle, int pType) {
    assert pType >= BASIC && pType <= USER_NAMEABLE : "Invalid Frame Type passed";
    
    JFrame returnFrame;
    
    switch (pType) {
      case USER_NAMEABLE: {
        JUserNameableFrame frame = new JUserNameableFrame(pTitle);
        frame.bindPopupMenu();
        returnFrame = (JFrame) frame;
        break;
      }
      default: {
        returnFrame = new JFrame();
        returnFrame.setTitle(pTitle);
        break;
      }
    }
    return returnFrame;
  }

  public static void setMinSizeToCurrent(final JFrame frame) {

    final Dimension minimumSize = frame.getSize();

    frame.addComponentListener(
      new ComponentAdapter() {
          public void componentResized(ComponentEvent ce) {
            frame.setSize(Math.max(minimumSize.width,  frame.getSize().width ), 
                          Math.max(minimumSize.height, frame.getSize().height )
                         );
          }
      }
    );
  }
}
