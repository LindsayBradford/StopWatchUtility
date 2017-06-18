package blacksmyth.stopwatch.presenter.commands;

import blacksmyth.stopwatch.view.StopWatchViewEvent;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class LoggingCommand extends AbstractPresenterCommand {
  
  private static final Logger logger = LogManager.getLogger("LoggingCommand");
  
  private StopWatchViewEvent viewEvent;
  
  public void setViewEvent(StopWatchViewEvent viewEvent) {
    this.viewEvent = viewEvent;
  }
  
  @Override
  protected void runCommand() {
    logger.info("Processing event '" + viewEvent.getDescription() + "'.");
    
  }
}
