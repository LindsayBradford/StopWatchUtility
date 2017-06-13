/**
 * Copyright (c) 2017, Lindsay Bradford and other Contributors.
 * All rights reserved.
 * 
 * This program and the accompanying materials  are made available 
 * under the terms of the BSD 3-Clause licence  which accompanies 
 * this distribution, and is available at
 * http://opensource.org/licenses/BSD-3-Clause
 */

package blacksmyth.stopwatch.view;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import blacksmyth.stopwatch.view.swing.SwingViewBuilder;

@Configuration
@ComponentScan("blacksmyth.stopwatch.view")
public class DefaultViewConfig {
  @Bean
  public StopWatchView view() {
    return SwingViewBuilder.build();  // TODO: Wire view up via spring too.
  }
}
