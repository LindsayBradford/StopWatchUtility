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

import java.util.Observable;

import blacksmyth.stopwatch.model.StopWatchModel;
import blacksmyth.stopwatch.view.StopWatchView;
import blacksmyth.stopwatch.view.StopWatchViewEvent;

/**
 * A simple implementation of the 'Presenter' part of the MVP pattern, allowing a 
 * StopWatchView and StopWatchModelto operate with no no direct coupling between them.
 * @see StopWatchPresenter
 * @see StopWatchView
 * @see StopWatchModel
 */
public final class DefaultStopWatchPresenter implements StopWatchPresenter {

  private StopWatchModel model;
  private StopWatchView  view;
  
  @Override
  public void update(Observable eventSource, Object eventDetail) {
    if (eventSource == this.model) {
      processModelEvent();  
    } 
    
    if (eventSource == this.view) {
      processViewEvent(
          (StopWatchViewEvent) eventDetail
      );
    }
  }

  @Override
  public void setModel(StopWatchModel model) {
    this.model = model;
  }

  @Override
  public void setView(StopWatchView view) {
    assert this.model != null;  // Set model before view so we can issue commands to model based on view state.
    
    this.view = view;
    
    // If the view remembers a time that it held on its last die event, we spoof a TimeSetRequested event here
    // to trigger the model synchronsing to that time..
    
    processViewEvent(
        StopWatchViewEvent.TimeSetRequested
    );
  }

  private void processModelEvent() {
    // The only events issued by a StopWatchModel are notifications that elapsed time has changed. 
    // All we need do here it update the view with the new time.
    
    if (this.view == null) return;
    
    this.view.setTime(
        this.model.getTime()
    );
  }
  
  private void processViewEvent(StopWatchViewEvent viewEvent) {
    switch (viewEvent) {
      case ResetRequested:
        this.model.reset();
        break;
      case StartRequested:
        this.model.start();
        break;
      case StopRequested:
        this.model.stop();
        break;
      case TimeSetRequested:
        this.model.setTime(
            this.view.getRequestedSetTime()
        );
        break;
      case DeathRequested:
        this.model.die();
        break;
    }
  }
}
