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
    assignLookAndFeel();
    
    SwingStopWatchView view = new SwingStopWatchView();
    
    buildAndBindViewEventHandlers(view);
    buildAndBindViewSwingComponents(view);
    
    return view;
  }

  private static void assignLookAndFeel() {
    try {
      UIManager.setLookAndFeel(
          "javax.swing.plaf.metal.MetalLookAndFeel"
        );
    } catch (Exception e) {
      // Doesn't matter - whatever it defaults to will do if MetalLookAndFeel fails.
    }
  }
  
  private static void buildAndBindViewEventHandlers(SwingStopWatchView view) {
    view.setPersistedElapsedTime(
        PersistedSwingState.ELAPSED_TIME
    );
    
    getStopWatchEventDelegator().setDelegate(view);
    getSwingEventDelegator().setDelegate(view);
  }
  
  private static void buildAndBindViewSwingComponents(SwingStopWatchView view) {
    view.setFrame(
        buildFrame()
    );

    view.setControlPanel(
        buildControlPanel()
    );
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
    
    buildFramePersistedSwingState(frame);

    frame.setEventRaiser(
        getStopWatchEventDelegator()
    );
    
    buildAndBindFrameContent(frame);

    return frame;
  }
  
  private static void buildFramePersistedSwingState(JStopWatchFrame frame) {
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
  }
  
  private static void buildAndBindFrameContent(JStopWatchFrame frame) {
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
  }
  
  private static JStopWatchMenuBar buildMenuBar(JStopWatchFrame frame) {
    JStopWatchMenuBar menu = new JStopWatchMenuBar();
    
    buildAndBindMenuPersistedSwingState(menu);
    buildAndBindMenuCommands(menu, frame);
    
    return menu;
  }
  
  private static void buildAndBindMenuPersistedSwingState(JStopWatchMenuBar menu) {
    menu.setPersistedToggleMilliseconds(
        PersistedSwingState.TOGGLE_MILLISECONDS
    );

    menu.setPersistedToggleLeds(
        PersistedSwingState.TOGGLE_LEDS
    );
  }

  private static void buildAndBindMenuCommands(JStopWatchMenuBar menu, JStopWatchFrame frame) {
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
