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
public final class DefaultStopWatchModel extends Observable implements StopWatchModel, TickRecipient {
  
  private enum ModelState {
    Stopped, // Currently not measuring elapsed time (paused) 
    Running  // Currently measuring elapsed time
  }
  
  private long             startTime           = 0;
  private long             previousElapsedTime = 0;

  private ModelState       status              = ModelState.Stopped;

  private boolean          needsTicks          = true;
  
  public DefaultStopWatchModel() {
    super();
  }

  @Override
  public void start() {
    if (this.status == ModelState.Running) {
      return;
    }
    this.startTime = System.currentTimeMillis();
    this.status = ModelState.Running;
  }

  @Override
  public void stop() {
    if (this.status == ModelState.Stopped) {
      return;
    }
    long stopTime = System.currentTimeMillis();
    this.status = ModelState.Stopped;
    this.previousElapsedTime += (stopTime - this.startTime);
    publishElapsedTime();
  }

  @Override
  public void reset() {
    this.status = ModelState.Stopped;
    this.startTime = 0;
    this.previousElapsedTime = 0;
    publishElapsedTime();
  }

  @Override
  public boolean isRunning() {
    return (this.status == ModelState.Running);
  }

  @Override
  public long getTime() {
    if (isRunning()) {
      return getElapsedTime();
    }
    return this.previousElapsedTime;
  }

  private long getElapsedTime() {
    long rightNow = System.currentTimeMillis();
    return this.previousElapsedTime + rightNow - this.startTime;
  }
  
  @Override
  public void setTime(long time) {
    this.status = ModelState.Stopped;
    this.startTime = 0;
    this.previousElapsedTime = time;
    publishElapsedTime();
  }
  
  @Override
  public void addObserver(Observer o) {
    super.addObserver(o);
    publishElapsedTime();
  }

  private void publishElapsedTime() {
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
