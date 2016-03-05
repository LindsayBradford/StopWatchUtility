/**
 * Copyright (c) 2016, Lindsay Bradford and other Contributors.
 * All rights reserved.
 * 
 * This program and the accompanying materials  are made available 
 * under the terms of the BSD 3-Clause licence  which accompanies 
 * this distribution, and is available at
 * http://opensource.org/licenses/BSD-3-Clause
 */

package blacksmyth.stopwatch.model;

import java.util.Observable;
import java.util.Observer;

/**
 * A basic implementation of a {@link StopWatchModel}, and {@link TickRecipient}.
 */
public final class BasicStopWatchModel extends Observable implements StopWatchModel, TickRecipient {
  
  private enum ModelState {
    Stopped, // Currently not measuring elapsed time (paused) 
    Running  // Currently measuring elapsed time
  }
  
  private long             startTime           = 0;
  private long             previousElapsedTime = 0;

  private ModelState       status              = ModelState.Stopped;

  private boolean          needsTicks          = true;
  
  public BasicStopWatchModel() {
    super();
  }

  @Override
  public void start() {
    if (status == ModelState.Running) {
      return;
    }
    startTime = System.currentTimeMillis();
    status = ModelState.Running;
  }

  @Override
  public void stop() {
    if (status == ModelState.Stopped) {
      return;
    }
    long stopTime = System.currentTimeMillis();
    status = ModelState.Stopped;
    previousElapsedTime += (stopTime - startTime);
    publishTime(previousElapsedTime);
  }

  @Override
  public void reset() {
    status = ModelState.Stopped;
    startTime = 0;
    previousElapsedTime = 0;
    publishTime(0);
  }

  @Override
  public boolean isRunning() {
    return (status == ModelState.Running);
  }

  @Override
  public long getTime() {
    if (isRunning()) {
      return getElapsedTime();
    }
    return previousElapsedTime;
  }

  private long getElapsedTime() {
    long rightNow = System.currentTimeMillis();
    return previousElapsedTime + rightNow - startTime;
  }
  
  @Override
  public void setTime(long time) {
    status = ModelState.Stopped;
    startTime = 0;
    previousElapsedTime = time;
    publishElapsedTime();
  }
  
  @Override
  public void addObserver(Observer o) {
    super.addObserver(o);
    publishElapsedTime();
  }

  private void publishElapsedTime() {
    publishTime(getElapsedTime());
  }

  private void publishTime(long time) {
    setChanged();
    notifyObservers();
  }
  
  @Override
  public void die() {
    this.needsTicks = false;
  }

  @Override
  public void receiveTick() {
    if (isRunning()) {
      publishElapsedTime();
    }
  }

  @Override
  public boolean needsTicks() {
    return this.needsTicks;
  }
}
