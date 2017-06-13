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
import blacksmyth.stopwatch.presenter.commands.PresenterCommand;
import blacksmyth.stopwatch.view.StopWatchView;
import blacksmyth.stopwatch.view.StopWatchViewEvent;

import java.util.HashMap;

import org.springframework.stereotype.Component;

/**
 * A simple implementation of the 'Presenter' part of the MVP pattern, allowing a 
 * StopWatchView and StopWatchModelto operate with no no direct coupling between them.
 * @see StopWatchPresenter
 * @see StopWatchView
 * @see StopWatchModel
 */

@Component
public final class DefaultStopWatchPresenter implements StopWatchPresenter {

  private StopWatchModel model;
  private StopWatchView  view;
  
  private HashMap<StopWatchViewEvent, PresenterCommand> eventCommandMap = new HashMap<>();;
  
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
    model.addObserver(this);
    bindCommandsIfPossible();
  }

  @Override
  public void setView(StopWatchView view) {
    this.view = view;
    view.addObserver(this);
    bindCommandsIfPossible();
  }
  
  private void bindCommandsIfPossible() {
    if (canBindCommands()) {
      for(PresenterCommand command: eventCommandMap.values()) {
        command.setModelAndView(model, view);
      }
    }
  }
  
  private boolean canBindCommands() {
    return (model !=null && view != null);
  }
  
  @Override
  public void addEventCommand(StopWatchViewEvent event, PresenterCommand command) {
    bindEventToCommand(event, command);
  }
  
  private void bindEventToCommand(StopWatchViewEvent event, PresenterCommand command) {
    command.setModelAndView(model, view);
    eventCommandMap.put(event, command);
  }
  

  private void processModelTimeUpdateEvent() {
    if (view == null) return;
    
    view.setTime(
        model.getTime()
    );
  }
  
  private void processViewEvent(StopWatchViewEvent viewEvent) {
    if (canProcessEvent(viewEvent)) {
      executeCommandForEvent(viewEvent);
    }
  }
  
  private boolean canProcessEvent(StopWatchViewEvent viewEvent) {
    return eventCommandMap.keySet().contains(viewEvent);
  }
  
  private void executeCommandForEvent(StopWatchViewEvent viewEvent) {
    eventCommandMap.get(viewEvent).execute();
  }
}
