/**
 * Copyright (c) 2017, Lindsay Bradford and other Contributors.
 */

package blacksmyth.stopwatch.presenter.commands;

public final class DieCommand extends AbstractPresenterCommand {
  @Override
  protected void runCommand() {
    getModel().die();
  }
}
