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
final class JStopWatchFrame extends JUserNameableFrame {
  
  private Preferences prefs;

  public void setPreferences(Preferences prefs) {
    this.prefs = prefs;

    processPreferences();
    addPreferencesEventHandler();
  }
  
  private void addPreferencesEventHandler() {
    addComponentListener(
        new ComponentAdapter() {
          @Override
          public void componentMoved(ComponentEvent event) {
            prefs.putInt("posX",getX());
            prefs.putInt("posY",getY());
          }
    });
  }
  
  @Override
  public void setTitle(String title) {
    super.setTitle(title);

    prefs.put(
        "frameTitle",
        title
    );
  }

  private void processPreferences() {
    setTitle(
        prefs.get("frameTitle","StopWatch")
    );

    final int posX = prefs.getInt("posX",10);
    final int posY = prefs.getInt("posY",10);

    setLocation(posX, posY);
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
