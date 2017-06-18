package blacksmyth.stopwatch.presenter;

import static blacksmyth.stopwatch.view.StopWatchViewEvent.DeathRequested;
import static blacksmyth.stopwatch.view.StopWatchViewEvent.ResetRequested;
import static blacksmyth.stopwatch.view.StopWatchViewEvent.StartRequested;
import static blacksmyth.stopwatch.view.StopWatchViewEvent.StopRequested;
import static blacksmyth.stopwatch.view.StopWatchViewEvent.TimeSetRequested;

import blacksmyth.stopwatch.presenter.commands.CompositePresenterCommand;
import blacksmyth.stopwatch.presenter.commands.DieCommand;
import blacksmyth.stopwatch.presenter.commands.LoggingCommand;
import blacksmyth.stopwatch.presenter.commands.PresenterCommand;
import blacksmyth.stopwatch.presenter.commands.ResetCommand;
import blacksmyth.stopwatch.presenter.commands.StartCommand;
import blacksmyth.stopwatch.presenter.commands.StopCommand;
import blacksmyth.stopwatch.presenter.commands.TimeSetCommand;
import blacksmyth.stopwatch.view.StopWatchViewEvent;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import org.apache.logging.log4j.core.config.Configurator;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan("blacksmyth.stopwatch.presenter")
public class DefaultPresenterConfig {
  
  @Resource
  private StopWatchPresenter presenter;
  
  @PostConstruct
  private void configurePresenter() {
    configureLogging();
    bindCommands();
  }
  
  private void configureLogging() {
    Configurator.initialize("default", "resources/log4j2config.xml");
  }
  
  private void bindCommands() {
    bindCommand(ResetRequested, new ResetCommand());
    bindCommand(StartRequested, new StartCommand());
    bindCommand(StopRequested, new StopCommand());
    bindCommand(TimeSetRequested, new TimeSetCommand());
    bindCommand(DeathRequested, new DieCommand());
  }
  
  private void bindCommand(StopWatchViewEvent event, PresenterCommand coreCommand) {
    CompositePresenterCommand loggedCommand = new CompositePresenterCommand();
    loggedCommand.addSubCommands(coreCommand, buildLoggingCommand(event));
    
    presenter.addEventCommand(event, loggedCommand);
  }
  
  private LoggingCommand buildLoggingCommand(StopWatchViewEvent event) {
    LoggingCommand loggingCommand = new LoggingCommand();
    loggingCommand.setViewEvent(event);
    return loggingCommand;
  }
}
