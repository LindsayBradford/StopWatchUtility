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

import javax.swing.UIManager;

import blacksmyth.stopwatch.view.StopWatchEventDelegator;
import static blacksmyth.stopwatch.view.swing.PersistedSwingState.*;
import blacksmyth.stopwatch.view.StopWatchView;
import blacksmyth.swing.JFrameFactory;
import blacksmyth.utilities.ResourceLoader;

/**
 * A class based on the Builder design pattern for the construction of a Swing-based {@link StopWatchView} via
 * dependency injection. 
 */

public final class SwingViewBuilder {

  private static final SwingStopWatchView view = new SwingStopWatchView();
  
  private static final StopWatchEventDelegator eventDelegator = new StopWatchEventDelegator();
  
  private static final SwingStopWatchEventDelegator swingEventDelegator = new SwingStopWatchEventDelegator();
  
  private static final JStopWatchFrame frame = new JStopWatchFrame();
  
  private static final JStopWatchControlPanel controlPanel = new JStopWatchControlPanel();
  
  private static final JStopWatchMenuBar menu = new JStopWatchMenuBar();
  
  public static StopWatchView build() {
    assignLookAndFeel();
    view.setPersistedElapsedTime(ELAPSED_TIME);
    bindEventDelegates();
    configureSubComponents();
    return view;
  }
  
  private static void bindEventDelegates() {
    eventDelegator.setDelegate(view);
    swingEventDelegator.setDelegate(view);
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
  
  private static void configureSubComponents() {
    configureFrame();
    view.setFrame(frame);
  }
  
  private static void configureFrame() {
    setFramePersistedState();
    frame.setEventRaiser(eventDelegator);
    configureFrameContent();
  }
  
  private static void setFramePersistedState() {
    frame.setPersistedFrameTitle(FRAME_TITLE);
    frame.setBaseFrameTitle("Stopwatch");    // TODO: Why does this have to happen after setting persisted title?
    frame.setPersistedFramePosX(FRAME_X_POS);
    frame.setPersistedFramePosY(FRAME_Y_POS);
  }
  
  private static void configureFrameContent() {
    JFrameFactory.makeCloseableJFrame(frame);

    frame.setIconImage(
        ResourceLoader.loadImageAsIcon(
            "/resources/gnome-set-time.gif"
        ).getImage()
    );
    
    bindControlPanel();
    bindMenu();
    
    frame.pack();
    frame.setResizable(false);
  }
  
  private static void bindControlPanel() {
    configureControlPanel();
    frame.getContentPane().add(
        controlPanel, 
        BorderLayout.CENTER
    );
    controlPanel.setStartStopButtonAsDefault(frame);
  }
  
  private static void configureControlPanel() {
    controlPanel.setEventRaiser(eventDelegator);
    controlPanel.setPersistedToggleMilliseconds(TOGGLE_MILLISECONDS);
    controlPanel.setPersistedToggleLeds(TOGGLE_LEDS);
  }

  private static void bindMenu() {
    configureMenu();
    frame.setJMenuBar(menu);
  }

  private static void configureMenu() {
    setMenuPersistedState();
    buildAndBindMenuCommands();
  }
  
  private static void setMenuPersistedState() {
    menu.setPersistedToggleMilliseconds(TOGGLE_MILLISECONDS);
    menu.setPersistedToggleLeds(TOGGLE_LEDS);
  }

  private static void buildAndBindMenuCommands() {
    menu.setUpdateTitleCommand(
        buildUpdateTitleCmd()
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
  
  private static UpdateFrameTitleCommand buildUpdateTitleCmd() {
    UpdateFrameTitleCommand cmd = new UpdateFrameTitleCommand();
    cmd.setFrame(frame);
    return cmd;
  }
  
  private static UpdateStopWatchTimeCommand buildUpdateTimeCmd(JTimerUpdateDialog dialog) {
    UpdateStopWatchTimeCommand cmd = new UpdateStopWatchTimeCommand();

    cmd.setDialog(dialog);
    cmd.setPersistedElapsedTime(ELAPSED_TIME);
    cmd.setEventRaiser(eventDelegator);
    
    return cmd;
  }
  
  private static ToggleMillisecondsCommand buildToggleMillisecondsCmd() {
    ToggleMillisecondsCommand cmd = new ToggleMillisecondsCommand();

    cmd.setPersistedToggleMilliseconds(TOGGLE_MILLISECONDS);
    cmd.setSwingEventRaiser(swingEventDelegator);
    
    return cmd;
  }
  
  private static ToggleLedsCommand buildToggleLedsCmd() {
    ToggleLedsCommand cmd = new ToggleLedsCommand();
    
    cmd.setPersistedToggleLeds(TOGGLE_LEDS);
    cmd.setSwingEventRaiser(swingEventDelegator);
    
    return cmd;
  }
}
