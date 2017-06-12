/**
 * Copyright (c) 2017, Lindsay Bradford and other Contributors.
 */

package blacksmyth.stopwatch.presenter.commands;

import blacksmyth.stopwatch.model.StopWatchModel;
import blacksmyth.stopwatch.view.StopWatchView;

/**
 * An abstract implementation of {@link PresenterCommand} where all that's required to complete
 * the class is to supply the behaviour to invoke via the run command.
 */

public abstract class AbstractPresenterCommand implements PresenterCommand {
  
  private StopWatchModel model;
  private StopWatchView  view;
  
  @Override
  public void setModelAndView(StopWatchModel model, StopWatchView view) {
    this.model = model;
    this.view = view;
  }
  
  protected StopWatchModel getModel() {
    return model;
  }

  protected StopWatchView getView() {
    return view;
  }
  
  @Override
  public void execute() {
    if (model == null || view == null) { return; }
    runCommand();
  }
  
  abstract protected void runCommand();
}
