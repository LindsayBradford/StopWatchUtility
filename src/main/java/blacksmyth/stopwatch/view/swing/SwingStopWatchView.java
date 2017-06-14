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

import java.util.Observable;

import javax.swing.JFrame;

import org.springframework.stereotype.Component;

import blacksmyth.stopwatch.view.StopWatchView;
import blacksmyth.stopwatch.view.StopWatchViewEventRaiser;
import blacksmyth.stopwatch.view.StopWatchViewEvent;

@Component
public class SwingStopWatchView extends Observable implements 
    StopWatchView, StopWatchViewEventRaiser, SwingStopWatchViewEventRaiser  {
  
  private JFrame frame;
  private PersistedNvpState elapsedTimeState;

  public void setFrame(JFrame frame) {
    this.frame = frame;
  }
  
  public void setPersistedElapsedTime(PersistedNvpState elapsedTimeState) {
    this.elapsedTimeState = elapsedTimeState;
  }

  public JStopWatchControlPanel getControlPanel() {
    return (JStopWatchControlPanel) frame.getContentPane().getComponent(0);
  }

  @Override
  public void show() {
    frame.setVisible(true);
  }

  @Override
  public void setTime(long time) {
    elapsedTimeState.putAsLong(time);
    getControlPanel().setTime(time);
  }

  @Override
  public long getRequestedSetTime() {
    return elapsedTimeState.getAsInt();
  }

  @Override
  public void raise(StopWatchViewEvent event) {
    processStopWatchEvent(event);
    setChanged();
    notifyObservers(event);
  }
  
  private void processStopWatchEvent(StopWatchViewEvent event) {
    getControlPanel().processStopWatchEvent(event);
  }

  @Override
  public void raise(SwingStopWatchViewEvents event) {
    getControlPanel().processSwingEvent(event);
  }
}
