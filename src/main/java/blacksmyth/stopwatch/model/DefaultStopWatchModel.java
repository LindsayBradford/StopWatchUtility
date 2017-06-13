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

import static blacksmyth.stopwatch.model.DefaultStopWatchModel.ModelState.*;

import java.util.Observable;
import java.util.Observer;

import org.springframework.stereotype.Component;

/**
 * A basic implementation of a {@link StopWatchModel}, and {@link TickRecipient}.
 */

@Component
public final class DefaultStopWatchModel extends Observable implements StopWatchModel {
  
  enum ModelState {
    NotMeasuringElapsedTime,
    MeasuringElapsedTime
  }
  
  private long             startTime = 0;
  private long             lastElapsedTime = 0;

  private ModelState       modelState = NotMeasuringElapsedTime;

  private boolean          needsTicks = true;
  
  public DefaultStopWatchModel() {
    super();
  }

  @Override
  public void start() {
    if (isMeasuringTime()) { return; }
    modelState = MeasuringElapsedTime;
    startTime = now();
  }

  @Override
  public void stop() {
    if (modelState == NotMeasuringElapsedTime) { return; }
    lastElapsedTime = sampleElapsingTime();
    modelState = NotMeasuringElapsedTime;
    notiftObserversOfTime();
  }

  @Override
  public void reset() {
    startTime = 0;
    lastElapsedTime = 0;
    modelState = NotMeasuringElapsedTime;
    notiftObserversOfTime();
  }
  
  private long now() {
	  return System.currentTimeMillis();
  }

  @Override
  public boolean isMeasuringTime() {
    return (modelState == MeasuringElapsedTime);
  }

  @Override
  public long getTime() {
    if (isMeasuringTime()) {
      return sampleElapsingTime();
    }
    return lastElapsedTime;
  }

  private long sampleElapsingTime() {
    return lastElapsedTime - startTime + now() ;
  }
  
  @Override
  public void setTime(long time) {
    modelState = NotMeasuringElapsedTime;
    startTime = 0;
    lastElapsedTime = time;
    notiftObserversOfTime();
  }
  
  @Override
  public void addObserver(Observer o) {
    super.addObserver(o);
    notiftObserversOfTime();
  }

  private void notiftObserversOfTime() {
    setChanged();
    notifyObservers();
  }
  
  @Override
  public void die() {
    needsTicks = false;
  }

  @Override
  public void receiveTick() {
    if (isMeasuringTime()) {
      notiftObserversOfTime();
    }
  }

  @Override
  public boolean needsTicks() {
    return needsTicks;
  }
}