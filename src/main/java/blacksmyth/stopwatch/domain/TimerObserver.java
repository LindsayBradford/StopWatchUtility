// (c) 2003 Lindsay Bradford

package blacksmyth.stopwatch.domain;

public interface TimerObserver {
  public void updateTime(ObservableTimer timer, long newTime);
}
