/**
 * Copyright (c) 2017, Lindsay Bradford and other Contributors.
 */

package blacksmyth.stopwatch.presenter.commands;

import blacksmyth.stopwatch.model.StopWatchModel;
import blacksmyth.stopwatch.presenter.StopWatchPresenter;
import blacksmyth.stopwatch.view.StopWatchView;

/**
 * An interface specifying a command that can be invoked by a {@link StopWatchPresenter}. The command is generally 
 * expected to trigger side-effects in the presenter's model and view.  
 */

public interface PresenterCommand {
  void setModelAndView(StopWatchModel model, StopWatchView view);

  /**
   * A wrapper method to {@link Runnable#run()} that ensures run is called only when 
   * a model and view are known.
   */
  void execute();
}
