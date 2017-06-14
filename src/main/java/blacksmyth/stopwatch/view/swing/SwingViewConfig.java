package blacksmyth.stopwatch.view.swing;

import java.awt.BorderLayout;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import javax.swing.UIManager;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import blacksmyth.stopwatch.view.StopWatchEventDelegator;
import static blacksmyth.stopwatch.view.swing.PersistedSwingState.*;
import blacksmyth.swing.JFrameFactory;
import blacksmyth.utilities.ResourceLoader;

@Configuration
@ComponentScan("blacksmyth.stopwatch.view")
public class SwingViewConfig {
  
  @Resource
  SwingStopWatchView view;
  
  @Resource
  StopWatchEventDelegator eventDelegator;
  
  @Resource
  SwingStopWatchEventDelegator swingEventDelegator;
  
  @Resource
  JStopWatchFrame frame;
  
  @Resource
  JStopWatchControlPanel controlPanel;
  
  @Resource
  JStopWatchMenuBar menu;
  
  @PostConstruct
  public void postConstruct() {
    assignLookAndFeel();
    view.setPersistedElapsedTime(ELAPSED_TIME);
    bindEventDelegates();
    configureSubComponents();
  }
  
  private void bindEventDelegates() {
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
  
  public void configureSubComponents() {
    configureFrame();
    view.setFrame(frame);
  }
  
  private void configureFrame() {
    setFramePersistedState();
    frame.setEventRaiser(eventDelegator);
    configureFrameContent();
  }
  
  private void setFramePersistedState() {
    frame.setPersistedFrameTitle(FRAME_TITLE);
    frame.setBaseFrameTitle("Stopwatch");    // TODO: Why does this have to happen after setting persisted title?
    frame.setPersistedFramePosX(FRAME_X_POS);
    frame.setPersistedFramePosY(FRAME_Y_POS);
  }
  
  private void configureFrameContent() {
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
  
  private void bindControlPanel() {
    configureControlPanel();
    frame.getContentPane().add(
        controlPanel, 
        BorderLayout.CENTER
    );
    controlPanel.setStartStopButtonAsDefault(frame);
  }
  
  private void configureControlPanel() {
    controlPanel.setEventRaiser(eventDelegator);
    controlPanel.setPersistedToggleMilliseconds(TOGGLE_MILLISECONDS);
    controlPanel.setPersistedToggleLeds(TOGGLE_LEDS);
  }

  private void bindMenu() {
    configureMenu();
    frame.setJMenuBar(menu);
  }

  private void configureMenu() {
    setMenuPersistedState();
    buildAndBindMenuCommands();
  }
  
  private void setMenuPersistedState() {
    menu.setPersistedToggleMilliseconds(TOGGLE_MILLISECONDS);
    menu.setPersistedToggleLeds(TOGGLE_LEDS);
  }

  private void buildAndBindMenuCommands() {
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
  
  private UpdateFrameTitleCommand buildUpdateTitleCmd() {
    UpdateFrameTitleCommand cmd = new UpdateFrameTitleCommand();
    cmd.setFrame(frame);
    return cmd;
  }
  
  private UpdateStopWatchTimeCommand buildUpdateTimeCmd(JTimerUpdateDialog dialog) {
    UpdateStopWatchTimeCommand cmd = new UpdateStopWatchTimeCommand();

    cmd.setDialog(dialog);
    cmd.setPersistedElapsedTime(ELAPSED_TIME);
    cmd.setEventRaiser(eventDelegator);
    
    return cmd;
  }
  
  private ToggleMillisecondsCommand buildToggleMillisecondsCmd() {
    ToggleMillisecondsCommand cmd = new ToggleMillisecondsCommand();

    cmd.setPersistedToggleMilliseconds(TOGGLE_MILLISECONDS);
    cmd.setSwingEventRaiser(swingEventDelegator);
    
    return cmd;
  }
  
  private ToggleLedsCommand buildToggleLedsCmd() {
    ToggleLedsCommand cmd = new ToggleLedsCommand();
    
    cmd.setPersistedToggleLeds(TOGGLE_LEDS);
    cmd.setSwingEventRaiser(swingEventDelegator);
    
    return cmd;
  }
}
