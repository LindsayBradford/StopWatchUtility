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

import blacksmyth.stopwatch.model.StopWatchModel;
import blacksmyth.stopwatch.model.DefaultStopWatchModel;
import blacksmyth.stopwatch.model.Ticker;
import blacksmyth.stopwatch.model.SleepingThreadTicker;
import blacksmyth.stopwatch.presenter.StopWatchPresenter;
import blacksmyth.stopwatch.presenter.DefaultStopWatchPresenter;
import blacksmyth.stopwatch.view.StopWatchView;
import blacksmyth.stopwatch.view.swing.SwingViewBuilder;

/**
 * An implementation of the Builder pattern that uses the MVP pattern and Dependency Injection
 * to create a fully functional StopWatch application.
 */

public final class StopWatchBuilder {
  
  private static int MILLISECONDS_BETWEEN_TICKS = 50;

  public static StopWatchView buildUtility()  {
    StopWatchView view = buildView();
    StopWatchModel model = buildModel();
    
    StopWatchPresenter presenter = new DefaultStopWatchPresenter();

    presenter.setModel(model);
    model.addObserver(presenter);
    
    presenter.setView(view);
    view.addObserver(presenter);
    
    return view;
  }
  
  private static StopWatchModel buildModel() {
    
    Ticker ticker = new SleepingThreadTicker();
    ticker.setMillisecondsBetweenTicks(MILLISECONDS_BETWEEN_TICKS);
    
    DefaultStopWatchModel stopwatch = new DefaultStopWatchModel();
    
    ticker.setRecipient(stopwatch);
    ticker.startTicking();
    
    return stopwatch;
  }

  private static StopWatchView buildView() {
    return SwingViewBuilder.buildView();
  }

}