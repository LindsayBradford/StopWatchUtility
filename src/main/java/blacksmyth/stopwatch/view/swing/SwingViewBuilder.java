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

import java.awt.BorderLayout;

import java.util.prefs.Preferences;

import javax.swing.JFrame;
import javax.swing.UIManager;

import blacksmyth.stopwatch.view.StopWatchEventDelegator;
import blacksmyth.stopwatch.view.StopWatchView;
import blacksmyth.swing.JFrameFactory;
import blacksmyth.utilities.ResourceLoader;

/**
 * A class based on the Builder design pattern for the construction of a Swing-based {@link StopWatchView} via
 * dependency injection. 
 */

public final class SwingViewBuilder {

  private static Preferences getViewPreferences() {
    return Preferences.userNodeForPackage(SwingStopWatchView.class);
  }
  
  private static final StopWatchEventDelegator stopWatchEventDeletagor = new StopWatchEventDelegator();
  
  private static StopWatchEventDelegator getStopWatchEventDelegator() {
    return stopWatchEventDeletagor;
  }

  private static final SwingStopWatchEventDelegator swingStopWatchEventDeletagor = new SwingStopWatchEventDelegator();
  
  private static SwingStopWatchEventDelegator getSwingEventDelegator() {
    return swingStopWatchEventDeletagor;
  }
  
  private static final JStopWatchControlPanel controlPanel = new JStopWatchControlPanel();
  
  private static JStopWatchControlPanel getControlPanel() {
    return controlPanel;
  }
  
  public static StopWatchView buildView() {

    try {
      UIManager.setLookAndFeel(
          "javax.swing.plaf.metal.MetalLookAndFeel"
        );
    } catch (Exception e) {}
    
    SwingStopWatchView view = new SwingStopWatchView();
    
    view.setPreferences(
        getViewPreferences()
    );
    
    getStopWatchEventDelegator().setDelegate(view);
    getSwingEventDelegator().setDelegate(view);
    
    view.setFrame(
        buildFrame()
    );
    
    JStopWatchControlPanel controlPanel = getControlPanel();

    controlPanel.setEventRaiser(
        getStopWatchEventDelegator()
    );

    view.setControlPanel(
        controlPanel
   );
    
    return view;
  }
  
  
  private static JFrame buildFrame() {
    JStopWatchFrame frame = new JStopWatchFrame();

    frame.setPreferences(
        getViewPreferences()
    );

    frame.setEventRaiser(
        getStopWatchEventDelegator()
    );
    
    JFrameFactory.makeCloseableJFrame(frame);

    frame.setIconImage(
        ResourceLoader.loadImageAsIcon(
            "/resources/gnome-set-time.gif"
        ).getImage()
    );
    
    JStopWatchMenuBar menu = new JStopWatchMenuBar();
    
    UpdateFrameTitleCommand titleCommand = new UpdateFrameTitleCommand();
    titleCommand.setFrame(frame);
    menu.setUpdateTitleCommand(titleCommand);
    menu.setPreferences(
        getViewPreferences()
    );
    
    JTimerUpdateDialog updateDialog = new JTimerUpdateDialog(frame);
    
    UpdateStopWatchTimeCommand timeCommand = new UpdateStopWatchTimeCommand();
    timeCommand.setDialog(updateDialog);
    timeCommand.setPreferences(
        getViewPreferences()
    );
    timeCommand.setEventRaiser(
        getStopWatchEventDelegator()
    );
    menu.setUpdateTimeCommand(timeCommand);

    ToggleMillisecondsCommand toggleMillisecondsCommand = new ToggleMillisecondsCommand();
    toggleMillisecondsCommand.setPreferences(
        getViewPreferences()
    );
    toggleMillisecondsCommand.setSwingEventRaiser(
        getSwingEventDelegator()
    );
    menu.setToggleShowMillisecondsCommand(toggleMillisecondsCommand);

    ToggleLedsCommand toggleLedsCommand = new ToggleLedsCommand();
    toggleLedsCommand.setPreferences(
        getViewPreferences()
    );
    toggleLedsCommand.setSwingEventRaiser(
        getSwingEventDelegator()
    );
    menu.setToggleShowLedsCommand(toggleLedsCommand);
    
    frame.setJMenuBar(menu);
    
    JStopWatchControlPanel controlPanel = getControlPanel();
    controlPanel.setPreferences(
        getViewPreferences()
    );

    
    frame.getContentPane().add(controlPanel, BorderLayout.CENTER);
    controlPanel.setStartStopButtonAsDefault(frame);
    
    frame.pack();
    frame.setResizable(false);

    return frame;
  }
}
