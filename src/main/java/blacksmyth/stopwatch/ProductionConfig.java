/**
 * Copyright (c) 2016, Lindsay Bradford and other Contributors.
 * All rights reserved.
 * 
 * This program and the accompanying materials  are made available 
 * under the terms of the BSD 3-Clause licence  which accompanies 
 * this distribution, and is available at
 * http://opensource.org/licenses/BSD-3-Clause
 */

package blacksmyth.stopwatch;

import org.springframework.context.annotation.*;

import blacksmyth.stopwatch.model.*;
import blacksmyth.stopwatch.presenter.*;
import blacksmyth.stopwatch.presenter.commands.DieCommand;
import blacksmyth.stopwatch.presenter.commands.ResetCommand;
import blacksmyth.stopwatch.presenter.commands.StartCommand;
import blacksmyth.stopwatch.presenter.commands.StopCommand;
import blacksmyth.stopwatch.presenter.commands.TimeSetCommand;
import blacksmyth.stopwatch.view.*;
import blacksmyth.stopwatch.view.swing.SwingViewBuilder;

import static blacksmyth.stopwatch.view.StopWatchViewEvent.DeathRequested;
import static blacksmyth.stopwatch.view.StopWatchViewEvent.ResetRequested;
import static blacksmyth.stopwatch.view.StopWatchViewEvent.StartRequested;
import static blacksmyth.stopwatch.view.StopWatchViewEvent.StopRequested;
import static blacksmyth.stopwatch.view.StopWatchViewEvent.TimeSetRequested;

/**
 * A Spring Java annotation implementation of Dependency Injection
 * to create a fully functional StopWatch application.
 */

@Configuration
@ComponentScan("blacksmyth.stopwatch")
public class ProductionConfig {

   @Bean
   public StopWatchPresenter presenter() {
     StopWatchPresenter presenter = new DefaultStopWatchPresenter();

     presenter.setModel(model());
     model().addObserver(presenter);

     presenter.setView(view());
     view().addObserver(presenter);
     
     presenter.addEventCommand(ResetRequested, new ResetCommand());
     presenter.addEventCommand(StartRequested, new StartCommand());
     presenter.addEventCommand(StopRequested, new StopCommand());
     presenter.addEventCommand(TimeSetRequested, new TimeSetCommand());
     presenter.addEventCommand(DeathRequested, new DieCommand());
     
     return presenter;
   }
   
   @Bean
   public StopWatchView view() {
     return SwingViewBuilder.build();
   }
   
   @Bean
   public StopWatchModel model() {
     return new DefaultStopWatchModel();
   }
   
   @Bean
   public Ticker ticker() {
     final int MILLISECONDS_BETWEEN_TICKS = 50;

     Ticker ticker = new SleepingThreadTicker();
     ticker.setMillisecondsBetweenTicks(MILLISECONDS_BETWEEN_TICKS);
     
     ticker.setRecipient(model());
     ticker.startTicking();
     
     return ticker;
   }
}