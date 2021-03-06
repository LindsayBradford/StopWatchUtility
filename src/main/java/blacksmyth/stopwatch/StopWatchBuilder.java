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
import blacksmyth.stopwatch.model.StopWatchModelBuilder;
import blacksmyth.stopwatch.presenter.StopWatchPresenter;
import blacksmyth.stopwatch.presenter.StopWatchPresenterBuilder;
import blacksmyth.stopwatch.view.StopWatchView;
import blacksmyth.stopwatch.view.swing.SwingViewBuilder;

/**
 * An implementation of the Builder pattern that uses the MVP pattern and Dependency Injection
 * to create a fully functional StopWatch application.
 */

public final class StopWatchBuilder {
  
  private static StopWatchModel model = StopWatchModelBuilder.build();
  private static StopWatchView view = SwingViewBuilder.build();;
  private static StopWatchPresenter presenter = StopWatchPresenterBuilder.build();;
  
  public static StopWatchView build()  {
    presenter.setModel(model);
    presenter.setView(view);
    return view;
  }
}