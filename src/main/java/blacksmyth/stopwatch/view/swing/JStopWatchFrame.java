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

import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.prefs.Preferences;

import org.springframework.stereotype.Component;

import blacksmyth.stopwatch.view.StopWatchViewEventRaiser;
import blacksmyth.stopwatch.view.StopWatchViewEvent;
import blacksmyth.swing.JUserNameableFrame;

/**
 *  A sub-class of {@link JUserNameableFrame} that uses the supplied {@link Preferences} to store and retrieve
 *  frame title, size and position information, allowing the frame to retain this detail across invocations.
 *  
 *  It will also raise an {@link StopWatchViewEvent#DeathRequested} event upon the user choosing to close the 
 *  frame to allow the system to appropriately free up any resource needed before shutdow.
 */

@SuppressWarnings("serial")
@Component
final class JStopWatchFrame extends JUserNameableFrame {
  
  PersistedNvpState framePosX;
  PersistedNvpState framePosY;
  PersistedNvpState frameTitle;
  
  public JStopWatchFrame() {
    super();  
    addPreferencesEventHandler();
  }
  
  public void setPersistedFramePosX(PersistedNvpState framePosX) {
    this.framePosX = framePosX;
    setLocation(
        framePosX.getAsInt(), 
        getY()
     );
  }

  public void setPersistedFramePosY(PersistedNvpState framePosY) {
    this.framePosY = framePosY;
    setLocation(
        getX(), 
        framePosY.getAsInt()
     );
  }
  
  public void setPersistedFrameTitle(PersistedNvpState frameTitle) {
    this.frameTitle = frameTitle;
    setTitle(
        frameTitle.getAsString()
    );
  }
  
  private void addPreferencesEventHandler() {
    addComponentListener(
        new ComponentAdapter() {
          @Override
          public void componentMoved(ComponentEvent event) {
            framePosX.putAsInt(getX());
            framePosY.putAsInt(getY());
          }
    });
  }
  
  @Override
  public void setTitle(String title) {
    super.setTitle(title);
    frameTitle.putAsString(title);
  }

  public void setEventRaiser(final StopWatchViewEventRaiser eventRaiser) {
    addWindowListener(new WindowAdapter() {

      @Override
      public void windowClosing(WindowEvent we) {
        eventRaiser.raise(
            StopWatchViewEvent.DeathRequested
        );
      }

      @Override
      public void windowClosed(WindowEvent we) {
        dispose();
        System.exit(0);
      }
    });
  }
}
