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

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import blacksmyth.stopwatch.view.StopWatchView;

public final class StopWatchUtility {

  private static ApplicationContext appContext;
 
  public static void main(String argv[]) {
    appContext = new AnnotationConfigApplicationContext(DefaultConfig.class);
    getView().show();
  }
  
  private static StopWatchView getView() {
    return appContext.getBean(StopWatchView.class);
  }
}