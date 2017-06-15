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

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan("blacksmyth.stopwatch.model")
public class DefaultModelConfig {
  
  @Resource
  private StopWatchModel model;
  
  private Ticker ticker = new SleepingThreadTicker();

  @PostConstruct
  private void postConstruction() {
    ticker.setMillisecondsBetweenTicks(50);
    ticker.setRecipient(model);
    ticker.startTicking();
  }
}
