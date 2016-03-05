// (c) 2016 Lindsay Bradford

package blacksmyth.stopwatch;

import blacksmyth.stopwatch.model.*;
import blacksmyth.stopwatch.presenter.*;
import blacksmyth.stopwatch.view.*;

/**
 * An implementation of the Builder pattern that uses the MVP pattern and Dependency Injection
 * to create a fully functional StopWatch application.
 */

// TODO: Replace this with Spring or Guice.

public final class StopWatchBuilder {
  
  private static int MILLISECONDS_BETWEEN_TICKS = 50;

  public static StopWatchView buildUtility()  {
    StopWatchView view = buildView();
    StopWatchModel model = buildModel();
    
    StopWatchPresenter presenter = new BasicStopWatchPresenter();

    presenter.setModel(model);
    model.addObserver(presenter);
    
    presenter.setView(view);
    view.addObserver(presenter);
    
    return view;
  }
  
  private static StopWatchModel buildModel() {
    
    Ticker ticker = new SleepingThreadTicker();
    ticker.setMillisecondsBetweenTicks(MILLISECONDS_BETWEEN_TICKS);
    
    BasicStopWatchModel stopwatch = new BasicStopWatchModel();
    
    ticker.setRecipient(stopwatch);
    ticker.startTicking();
    
    return stopwatch;
  }
  
  private static StopWatchView buildView() {
    SwingStopWatchView view = new SwingStopWatchView();
    return view;
  }
}