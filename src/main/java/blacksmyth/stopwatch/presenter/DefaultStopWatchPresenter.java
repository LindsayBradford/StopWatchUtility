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


import blacksmyth.stopwatch.model.StopWatchModel;
import blacksmyth.stopwatch.view.StopWatchView;
import blacksmyth.stopwatch.view.StopWatchViewEvent;
import static blacksmyth.stopwatch.view.StopWatchViewEvent.TimeSetRequested;

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
  public void updateFromModel() {
    processModelTimeUpdateEvent();  
  }
  
  @Override
  public void updateFromView(StopWatchViewEvent eventDetail) {
    processViewEvent(eventDetail);
  }

  @Override
  public void setModel(StopWatchModel model) {
    this.model = model;
  }

  @Override
  public void setView(StopWatchView view) {
    assert model != null;  // Set model before view so we can issue commands to model based on view state.
    
    this.view = view;
    
    syncModelToCurrentTime();
  }
  
  private void syncModelToCurrentTime() {
    processViewEvent(TimeSetRequested);
  }

  private void processModelTimeUpdateEvent() {
    if (view == null) return;
    
    view.setTime(
        model.getTime()
    );
  }
  
  private void processViewEvent(StopWatchViewEvent viewEvent) {
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
