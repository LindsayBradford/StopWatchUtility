// (c) 20016 Lindsay Bradford

package blacksmyth.stopwatch.model;

import java.util.Observable;
import java.util.Observer;

public class SimpleStopWatch extends Observable implements StopWatchModel, TickRecipient {
  
  private enum ModelState {
    Stopped, // Currently not measuring elapsed time (paused) 
    Running  // Currently measuring elapsed time
  }
  
  private long             startTime           = 0;
  private long             previousElapsedTime = 0;

  private ModelState       status              = ModelState.Stopped;

  private boolean          needsTicks          = true;
  
  public SimpleStopWatch() {
    super();
  }

  @Override
  public void start() {
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
    notifyObservers(new Long(time));
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
