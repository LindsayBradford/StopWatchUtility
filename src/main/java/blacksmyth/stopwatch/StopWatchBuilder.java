/**
 * Copyright (c) 2016, Lindsay Bradford and other Contributors.
 * All rights reserved.
 * 
 * This program and the accompanying materials  are made available 
 * under the terms of the BSD 3-Clause licence  which accompanies 
 * this distribution, and is available at
 * http://opensource.org/licenses/BSD-3-Clause
 */

package blacksmyth.stopwatch;

import static blacksmyth.stopwatch.view.StopWatchViewEvent.DeathRequested;
import static blacksmyth.stopwatch.view.StopWatchViewEvent.ResetRequested;
import static blacksmyth.stopwatch.view.StopWatchViewEvent.StartRequested;
import static blacksmyth.stopwatch.view.StopWatchViewEvent.StopRequested;
import static blacksmyth.stopwatch.view.StopWatchViewEvent.TimeSetRequested;

import blacksmyth.stopwatch.model.StopWatchModel;
import blacksmyth.stopwatch.model.StopWatchModelBuilder;
import blacksmyth.stopwatch.presenter.StopWatchPresenter;
import blacksmyth.stopwatch.presenter.StopWatchPresenterBuilder;
import blacksmyth.stopwatch.presenter.commands.DieCommand;
import blacksmyth.stopwatch.presenter.commands.ResetCommand;
import blacksmyth.stopwatch.presenter.commands.StartCommand;
import blacksmyth.stopwatch.presenter.commands.StopCommand;
import blacksmyth.stopwatch.presenter.commands.TimeSetCommand;
import blacksmyth.stopwatch.view.StopWatchView;
import blacksmyth.stopwatch.view.swing.SwingViewBuilder;

/**
 * An implementation of the Builder pattern that uses the MVP pattern and Dependency Injection
 * to create a fully functional StopWatch application.
 */

public final class StopWatchBuilder {
  
  private StopWatchModel model;
  private StopWatchView view;
  private StopWatchPresenter presenter;
  
  public static StopWatchView build()  {
    StopWatchBuilder builder = new StopWatchBuilder();
    builder.addPresenter();
    builder.addAndBindModel();
    builder.addAndBindView();
    builder.addPresenterCommands();
    return builder.getView();
  }
  
  private void addPresenter() {
    presenter = StopWatchPresenterBuilder.build();
  }
  
  private void addAndBindModel() {
    model = StopWatchModelBuilder.build();
    bindModel();
  }
  
  private void bindModel() {
    presenter.setModel(model);
    model.addObserver(presenter);
  }
  
  private void addAndBindView() {
    view = SwingViewBuilder.build();
    bindView();
  }
  
  private void bindView() {
    presenter.setView(view);
    view.addObserver(presenter);
  }
  
  
  private void addPresenterCommands() {
    presenter.addEventCommand(ResetRequested, new ResetCommand());
    presenter.addEventCommand(StartRequested, new StartCommand());
    presenter.addEventCommand(StopRequested, new StopCommand());
    presenter.addEventCommand(TimeSetRequested, new TimeSetCommand());
    presenter.addEventCommand(DeathRequested, new DieCommand());
  }
  
  public StopWatchView getView() {
    return view;
  }
}