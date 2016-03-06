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

import org.springframework.context.*;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import blacksmyth.stopwatch.view.StopWatchView;

public final class StopWatchUtility {
  public static void main(String argv[]) {
    
    @SuppressWarnings("resource")
    ApplicationContext ctx = new AnnotationConfigApplicationContext(StopWatchConfig.class);
        
    StopWatchView utility = ctx.getBean(StopWatchView.class);
        
    utility.show();
  }
}