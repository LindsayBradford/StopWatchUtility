/**
 * Copyright (c) 2016, Lindsay Bradford and other Contributors.
 * All rights reserved.
 * 
 * This program and the accompanying materials  are made available 
 * under the terms of the BSD 3-Clause licence  which accompanies 
 * this distribution, and is available at
 * http://opensource.org/licenses/BSD-3-Clause
 */

package blacksmyth.stopwatch.presenter;

import org.springframework.beans.factory.annotation.Autowired;

import java.util.Observable;

import blacksmyth.stopwatch.model.StopWatchModel;
import blacksmyth.stopwatch.view.StopWatchView;
import blacksmyth.stopwatch.view.StopWatchViewEvents;

/**
 * A simple implementation of the 'Presenter' part of the MVP pattern, allowing a 
 * StopWatchView and StopWatchModelto operate with no no direct coupling between them.
 * @see StopWatchPresenter
 * @see StopWatchView
 * @see StopWatchModel
 */
public final class BasicStopWatchPresenter implements StopWatchPresenter {

  private StopWatchModel model;
  private StopWatchView  view;
  
  @Override
  public void update(Observable eventSource, Object eventDetail) {
    if (eventSource == model) {
      processModelEvent();  
    } 
    
    if (eventSource == view) {
      processViewEvent(
          (StopWatchViewEvents) eventDetail
      );
    }
  }

  @Override
  @Autowired
  public void setModel(StopWatchModel model) {
    this.model = model;
    model.addObserver(this);
  }

  @Override
  @Autowired
  public void setView(StopWatchView view) {
    assert this.model != null;  // Set model before view so we can issue commands to model based on view state.

    view.addObserver(this);
    this.view = view;
    
    // If the view remembers a time that it held on its last die event, we spoof a TimeSetRequested event here
    // to trigger the model synchronsing to that time..
    
    processViewEvent(
        StopWatchViewEvents.TimeSetRequested
    );
  }

  private void processModelEvent() {
    // The only events issued by a StopWatchModel are notifications that elapsed time has changed. 
    // All we need do here it update the view with the new time.
    
    if (view == null) return;
    
    view.setTime(
        model.getTime()
    );
  }
  
  private void processViewEvent(StopWatchViewEvents viewEvent) {
    switch (viewEvent) {
      case ResetRequested:
        model.reset();
        break;
      case StartRequested:
        model.start();
        break;
      case StopRequested:
        model.stop();
        break;
      case TimeSetRequested:
        model.setTime(
            view.getRequestedSetTime()
        );
        break;
      case DeathRequested:
        model.die();
        break;
    }
  }
}
