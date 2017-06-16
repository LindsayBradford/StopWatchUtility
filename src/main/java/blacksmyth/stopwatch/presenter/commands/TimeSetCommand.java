/**
 * Copyright (c) 2017, Lindsay Bradford and other Contributors.
 */

package blacksmyth.stopwatch.presenter.commands;

import blacksmyth.stopwatch.model.StopWatchModel;
import blacksmyth.stopwatch.view.StopWatchView;

public final class TimeSetCommand extends AbstractPresenterCommand {

  @Override
  public void setModelAndView(StopWatchModel model, StopWatchView view) {
    super.setModelAndView(model, view);
    execute();  // Once bound, allow view to inform model of view's cached time.
  }
  
  @Override
  protected void runCommand() {
    getModel().setTime(
        getView().getRequestedSetTime()
    );
  }
}
