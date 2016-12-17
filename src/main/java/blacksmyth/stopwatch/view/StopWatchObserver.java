/**
 * Copyright (c) 2016, Lindsay Bradford and other Contributors.
 * All rights reserved.
 * 
 * This program and the accompanying materials  are made available 
 * under the terms of the BSD 3-Clause licence  which accompanies 
 * this distribution, and is available at
 * http://opensource.org/licenses/BSD-3-Clause
 */

package blacksmyth.stopwatch.view;

import java.util.Observable;
import java.util.Observer;

import blacksmyth.stopwatch.model.StopWatchModel;
import blacksmyth.stopwatch.presenter.StopWatchEventSource;

/**
 * A wrapper interface around Observer to enforce stricter type casting upon Observer.update() calls
 * for Observers of Stopwatch events.
 */

public interface StopWatchObserver extends Observer {
  
  default void update(Observable o, Object arg) {
    if (isStopWatchModel(o)) {
      updateFromModel();
    }
    if (isStopWatchView(o)) {
      updateFromView((StopWatchViewEvent) arg);
    }
  }
  
  default boolean isStopWatchModel(Observable o) {
    return (StopWatchModel.class.isAssignableFrom(o.getClass()));
  }
  
  default boolean isStopWatchView(Observable o) {
    return (StopWatchView.class.isAssignableFrom(o.getClass()));
  }

  void updateFromView(StopWatchViewEvent event);
  
  void updateFromModel(); 
}
