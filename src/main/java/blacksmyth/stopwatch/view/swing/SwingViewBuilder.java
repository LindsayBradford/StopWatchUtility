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
  
  public static StopWatchView build() {

    try {
      UIManager.setLookAndFeel(
          "javax.swing.plaf.metal.MetalLookAndFeel"
        );
    } catch (Exception e) {
      // Doesn't matter - whatever it defaults to will do if MetalLookAndFeel fails.
    }
    
    SwingStopWatchView view = new SwingStopWatchView();
    
    view.setPersistedElapsedTime(
        PersistedSwingState.ELAPSED_TIME
    );
    
    getStopWatchEventDelegator().setDelegate(view);
    getSwingEventDelegator().setDelegate(view);
    
    view.setFrame(
        buildFrame()
    );

    view.setControlPanel(
        buildControlPanel()
   );
    
    return view;
  }
  
  private static JStopWatchControlPanel buildControlPanel() {
    JStopWatchControlPanel controlPanel = getControlPanel();

    controlPanel.setEventRaiser(
        getStopWatchEventDelegator()
    );
    
    controlPanel.setPersistedToggleMilliseconds(
        PersistedSwingState.TOGGLE_MILLISECONDS
    );

    controlPanel.setPersistedToggleLeds(
        PersistedSwingState.TOGGLE_LEDS
    );
    
    return controlPanel;
  }
  
  private static JFrame buildFrame() {
    JStopWatchFrame frame = new JStopWatchFrame();
    
    frame.setPersistedFrameTitle(
        PersistedSwingState.FRAME_TITLE
    );
    
    frame.setBaseFrameTitle("Stopwatch");

    frame.setPersistedFramePosX(
        PersistedSwingState.FRAME_X_POS
    );

    frame.setPersistedFramePosY(
        PersistedSwingState.FRAME_Y_POS
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
    
    frame.setJMenuBar(
        buildMenuBar(frame)
    );
    
    frame.getContentPane().add(
        getControlPanel(), 
        BorderLayout.CENTER
    );

    getControlPanel().setStartStopButtonAsDefault(frame);
    
    frame.pack();
    frame.setResizable(false);

    return frame;
  }
  
  private static JStopWatchMenuBar buildMenuBar(JStopWatchFrame frame) {
    JStopWatchMenuBar menu = new JStopWatchMenuBar();
    
    menu.setPersistedToggleMilliseconds(
        PersistedSwingState.TOGGLE_MILLISECONDS
    );

    menu.setPersistedToggleLeds(
        PersistedSwingState.TOGGLE_LEDS
    );
    
    menu.setUpdateTitleCommand(
        buildUpdateTitleCmd(frame)
    );
    
    menu.setUpdateTimeCommand(
        buildUpdateTimeCmd(
            new JTimerUpdateDialog(frame)    
        )
    );
    
    menu.setToggleShowMillisecondsCommand(
        buildToggleMillisecondsCmd()
    );
    
    menu.setToggleShowLedsCommand(
        buildToggleLedsCmd()
    );
    
    return menu;
  }

  private static UpdateFrameTitleCommand buildUpdateTitleCmd(JStopWatchFrame frame) {
    UpdateFrameTitleCommand cmd = new UpdateFrameTitleCommand();
    cmd.setFrame(frame);
    return cmd;
  }
  
  private static UpdateStopWatchTimeCommand buildUpdateTimeCmd(JTimerUpdateDialog dialog) {
    UpdateStopWatchTimeCommand cmd = new UpdateStopWatchTimeCommand();
    cmd.setDialog(dialog);
    
    cmd.setPersistedElapsedTime(
        PersistedSwingState.ELAPSED_TIME
    );
    
    cmd.setEventRaiser(
        getStopWatchEventDelegator()
    );
    
    return cmd;
  }
  
  private static ToggleMillisecondsCommand buildToggleMillisecondsCmd() {
    ToggleMillisecondsCommand cmd = new ToggleMillisecondsCommand();

    cmd.setPersistedToggleMilliseconds(
        PersistedSwingState.TOGGLE_MILLISECONDS
    );

    cmd.setSwingEventRaiser(
        getSwingEventDelegator()
    );
    
    return cmd;
  }
  
  private static ToggleLedsCommand buildToggleLedsCmd() {
    ToggleLedsCommand cmd = new ToggleLedsCommand();
    
    cmd.setPersistedToggleLeds(
        PersistedSwingState.TOGGLE_LEDS
    );
    
    cmd.setSwingEventRaiser(
        getSwingEventDelegator()
    );
    
    return cmd;
  }
}
