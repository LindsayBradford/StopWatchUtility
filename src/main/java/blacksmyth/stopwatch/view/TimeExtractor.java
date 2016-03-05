/**
 * Copyright (c) 2016, Lindsay Bradford and other Contributors.
 * All rights reserved.
 * 
 * This program and the accompanying materials  are made available 
 * under the terms of the BSD 3-Clause licence  which accompanies 
 * this distribution, and is available at
 * http://opensource.org/licenses/BSD-3-Clause
 */

package blacksmyth.stopwatch.view;

/**
 * A simple class to extract time elements from an elapsed time value
 * expressed as number of elapsed milliseconds. 
 */
final class TimeExtractor {
  public static int getHours(long time) {
    return (int) (time / 3600000) % 100;
  }

  public static int getMinutes(long time) {
    return (int) (time / 60000) % 60;  
  }

  public static int getSeconds(long time) {
    return (int) (time / 1000) % 60;  
  }

  public static int getMilliseconds(long time) {
    return (int) (time % 1000);  
  }
}
