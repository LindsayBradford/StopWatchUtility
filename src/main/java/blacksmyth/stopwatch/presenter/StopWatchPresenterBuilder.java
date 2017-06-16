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

import static blacksmyth.stopwatch.view.StopWatchViewEvent.DeathRequested;
import static blacksmyth.stopwatch.view.StopWatchViewEvent.ResetRequested;
import static blacksmyth.stopwatch.view.StopWatchViewEvent.StartRequested;
import static blacksmyth.stopwatch.view.StopWatchViewEvent.StopRequested;
import static blacksmyth.stopwatch.view.StopWatchViewEvent.TimeSetRequested;

import blacksmyth.stopwatch.presenter.commands.DieCommand;
import blacksmyth.stopwatch.presenter.commands.ResetCommand;
import blacksmyth.stopwatch.presenter.commands.StartCommand;
import blacksmyth.stopwatch.presenter.commands.StopCommand;
import blacksmyth.stopwatch.presenter.commands.TimeSetCommand;

/**
 * A class based on the Builder design pattern for the construction of a Swing-based {@link StopWatchPresenter} via
 * dependency injection. 
 */

public final class StopWatchPresenterBuilder {
  
  private static StopWatchPresenter presenter = new DefaultStopWatchPresenter();

  public static StopWatchPresenter build() {
    bindCommands();
    return presenter;
  }
  
  private static void bindCommands() {
    presenter.addEventCommand(ResetRequested, new ResetCommand());
    presenter.addEventCommand(StartRequested, new StartCommand());
    presenter.addEventCommand(StopRequested, new StopCommand());
    presenter.addEventCommand(TimeSetRequested, new TimeSetCommand());
    presenter.addEventCommand(DeathRequested, new DieCommand());
  }
}
