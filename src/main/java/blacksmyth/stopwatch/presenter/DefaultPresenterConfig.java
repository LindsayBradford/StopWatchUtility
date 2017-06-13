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

import javax.annotation.PostConstruct;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan("blacksmyth.stopwatch.presenter")
public class DefaultPresenterConfig {
  @Bean
  public StopWatchPresenter presenter() {
    return new DefaultStopWatchPresenter();
  }
  
  @PostConstruct
  public void bindCommands() {
    presenter().addEventCommand(ResetRequested, new ResetCommand());
    presenter().addEventCommand(StartRequested, new StartCommand());
    presenter().addEventCommand(StopRequested, new StopCommand());
    presenter().addEventCommand(TimeSetRequested, new TimeSetCommand());
    presenter().addEventCommand(DeathRequested, new DieCommand());
  }
}
