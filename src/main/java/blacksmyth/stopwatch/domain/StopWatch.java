// (c) 2003 Lindsay Bradford

package blacksmyth.stopwatch.domain;

public class StopWatch extends ObservableTimer {

  private static final int DEFAULT_PERIOD = 50;

  private static final int STOPPED    = 0;
  private static final int TIMING     = 1;

  private long startTime  = 0;
  private long stopTime   = 0;
  private long previousElapsedTime = 0;

  private int status = STOPPED;

  private Ticker ticker;

  public StopWatch(int millisecondsToSleep) {
    super();
    startTicker(millisecondsToSleep);
  }

  public StopWatch() {
    super();
    startTicker(DEFAULT_PERIOD);
  }

  private void startTicker(int notifyPeriod) {
    ticker = new Ticker(this, notifyPeriod);
    ticker.start();
  }

  public void start() {
    startTime = System.currentTimeMillis();
    status = TIMING;
  }

  public void stop() {
    if (status == STOPPED) {
      return;
    }
	stopTime = System.currentTimeMillis();
	status = STOPPED;
	previousElapsedTime += (stopTime - startTime);
    publishTime(previousElapsedTime);
  }
  
  public void reset() {
  	status = STOPPED;
    startTime = 0;
    stopTime = 0;
    previousElapsedTime = 0;
    publishTime(0);
  }

  public boolean isRunning() {
    return (status == TIMING);
  }
  
  public void die() {
    ticker.die();
  }

  private long getElapsedTime() {
    long rightNow = System.currentTimeMillis();
    return previousElapsedTime + rightNow - startTime;
  }

  public void receiveTick() {
    if (isRunning()) {
        publishTime(getElapsedTime());
    }
  }

  public long getTime() {
    if (isRunning()) {
      return getElapsedTime();
    } 
    return previousElapsedTime;
  }
  
  public void subscribe(final TimerObserver newObserver) {
    super.subscribe(newObserver);
    publishTime(newObserver, getTime());
  }

  public void setTime(long time) {
    previousElapsedTime = time;
    publishTime(time);
  }
}
