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
import blacksmyth.stopwatch.view.*;

/**
 * A Spring Java annotation implementation of Dependency Injection
 * to create a fully functional StopWatch application.
 */

@Configuration
public class StopWatchConfig {
  
   @Bean
   public StopWatchPresenter presenter() {
     StopWatchPresenter presenter = new BasicStopWatchPresenter();

     presenter.setModel(model());
     model().addObserver(presenter);
     
     presenter.setView(view());
     view().addObserver(presenter);
     
     return presenter;
   }

   @Bean
   public StopWatchView view() {
     return new SwingStopWatchView();
   }
   
   @Bean
   public  StopWatchModel model() {
     return new BasicStopWatchModel();
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