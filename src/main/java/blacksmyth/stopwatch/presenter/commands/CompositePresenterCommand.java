package blacksmyth.stopwatch.presenter.commands;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import blacksmyth.stopwatch.model.StopWatchModel;
import blacksmyth.stopwatch.view.StopWatchView;

public class CompositePresenterCommand extends AbstractPresenterCommand {

  private List<PresenterCommand> subCommands = new ArrayList<>();
  
  public void addSubCommands(PresenterCommand... commands) {
    this.subCommands.addAll(
        Arrays.asList(commands)
    );
  }
  
  @Override
  public void setModelAndView(StopWatchModel model, StopWatchView view) {
    for (PresenterCommand subCommand : subCommands) {
      subCommand.setModelAndView(model, view);
    }
  }
  
  @Override
  public void execute() {
    for (PresenterCommand subCommand : subCommands) {
      subCommand.execute();
    }
  }
  
  @Override
  protected void runCommand() {}
}
