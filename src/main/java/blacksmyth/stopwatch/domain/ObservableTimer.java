// (c) 2003 Lindsay Bradford

package blacksmyth.stopwatch.domain;

import java.util.LinkedList;
import java.util.Observable;

public abstract class ObservableTimer extends Observable {
  private LinkedList subscribers;

  public ObservableTimer() {
    subscribers = new LinkedList();
  }

  public void subscribe(final TimerObserver newObserver) {
    subscribers.add(newObserver);
  }

  public void publishTime(final long time) {
    for(int i = 0; i < subscribers.size(); i++) {
      TimerObserver observer = (TimerObserver) subscribers.get(i);
      publishTime(observer, time);
    }
  }
  
  public void publishTime(TimerObserver observer, final long time) {
    observer.updateTime(this, time);
  }
}
