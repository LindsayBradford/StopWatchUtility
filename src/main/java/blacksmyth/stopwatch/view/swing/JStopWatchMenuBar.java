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

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import javax.swing.JCheckBoxMenuItem;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
/**
 *  A sub-class of {@link JMenuBar} that builds a Swing-specific memu-bar. The processing of actions as a result
 *  of the user selecting a menu item are delegated to a number of externally supplied 
 *  {@link StopWatchCommand} objects,  allowing the menu-bar to remain unaware of implementation details outside
 *  of the supply of a menu.
 */
@SuppressWarnings("serial")
final class JStopWatchMenuBar extends JMenuBar {

  StopWatchCommand updateTitleCommand;
  StopWatchCommand updateTimeCommand;
  StopWatchCommand toggleShowMillisecondsCommand;
  StopWatchCommand toggleShowLedsCommand;
  
  private JCheckBoxMenuItem showMillisecondsItem;
  private JCheckBoxMenuItem showLedsItem;
  
  public JStopWatchMenuBar() {
    super();
    add(buildStopwatchMenu());
    add(buildViewMenu());
  }
 
  private JMenu buildStopwatchMenu() {
    JMenu stopwatchMenu  = new JMenu("Stopwatch");
    stopwatchMenu.setMnemonic(KeyEvent.VK_S);

    stopwatchMenu.add(buildSetTimeItem());

    return stopwatchMenu;
  }

  private JMenuItem buildSetTimeItem() {
    JMenuItem item = new JMenuItem("Set Time...");
    item.setMnemonic(KeyEvent.VK_S);

    item.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent pEvent) {
        JStopWatchMenuBar.this.updateTimeCommand.run();
      }
    });
    return item;
  }

  public void setPersistedToggleMilliseconds(PersistedSwingState toggleState) {
    this.showMillisecondsItem.setSelected(
        toggleState.getAsBoolean()
    );    
  }

  public void setPersistedToggleLeds(PersistedSwingState toggleLeds) {
    this.showLedsItem.setSelected(
        toggleLeds.getAsBoolean()
    );
  }
  
  public void setUpdateTitleCommand(StopWatchCommand updateTitleCommand) {
    this.updateTitleCommand = updateTitleCommand;
  }

  public void setUpdateTimeCommand(StopWatchCommand updateTimeCommand) {
    this.updateTimeCommand = updateTimeCommand;
  }
  
  public void setToggleShowMillisecondsCommand(StopWatchCommand toggleShowMillisecondsCommand) {
    this.toggleShowMillisecondsCommand = toggleShowMillisecondsCommand;
  }

  public void setToggleShowLedsCommand(StopWatchCommand toggleShowLedsCommand) {
    this.toggleShowLedsCommand = toggleShowLedsCommand;
  }
  
  private JMenu buildViewMenu() {
    JMenu viewMenu  = new JMenu("View");
    viewMenu.setMnemonic(KeyEvent.VK_V);

    viewMenu.add(buildChangeTitleItem());
    viewMenu.add(buildShowMillisecondsItem());
    viewMenu.add(buildShowLedsItem());

    return viewMenu;
  }

  private JMenuItem buildChangeTitleItem() {
    JMenuItem item = new JMenuItem("Change Title...");
    item.setMnemonic(KeyEvent.VK_C);

    item.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent pEvent) {
        JStopWatchMenuBar.this.updateTitleCommand.run();
      }
    });
    return item;
  }
  
  private JCheckBoxMenuItem buildShowMillisecondsItem() {
    this.showMillisecondsItem = new JCheckBoxMenuItem("Show Milliseconds",true);
    this.showMillisecondsItem.setMnemonic(KeyEvent.VK_S);

    this.showMillisecondsItem.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent pEvent) {
        JStopWatchMenuBar.this.toggleShowMillisecondsCommand.run();
      }
    });
    return this.showMillisecondsItem;
  }

  private JCheckBoxMenuItem buildShowLedsItem() {
    this.showLedsItem = new JCheckBoxMenuItem("Show Leds",true);
    this.showLedsItem.setMnemonic(KeyEvent.VK_L);

    this.showLedsItem.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent pEvent) {
        JStopWatchMenuBar.this.toggleShowLedsCommand.run();
      }
    });
    return this.showLedsItem;
  }

}

