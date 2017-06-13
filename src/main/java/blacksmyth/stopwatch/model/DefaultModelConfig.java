/**
 * Copyright (c) 2016, Lindsay Bradford and other Contributors.
 * All rights reserved.
 * 
 * This program and the accompanying materials  are made available 
 * under the terms of the BSD 3-Clause licence  which accompanies 
 * this distribution, and is available at
 * http://opensource.org/licenses/BSD-3-Clause
 */

package blacksmyth.stopwatch.model;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan("blacksmyth.stopwatch.model")
public class DefaultModelConfig {
  
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
