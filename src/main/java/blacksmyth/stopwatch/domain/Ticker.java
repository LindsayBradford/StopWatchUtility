// (c) 2003 Lindsay Bradford

package blacksmyth.stopwatch.domain;

class Ticker extends Thread {

  private int millisecondsToSleep;
  private StopWatch parent;
  
  private volatile boolean isRunning = false;

  public Ticker(StopWatch parent, int millisecondsToSleep) {
    this.millisecondsToSleep = millisecondsToSleep;
    this.parent              = parent;
    setName("Ticker");
  }

  public void run() {
    isRunning = true;
    while(this.isRunning) {
      try {
        sleep(millisecondsToSleep);
      } catch (Exception e) {
        e.printStackTrace();
      }
      parent.receiveTick();
    }
  }
  
  public void die() {
    isRunning = false;
  }
}
