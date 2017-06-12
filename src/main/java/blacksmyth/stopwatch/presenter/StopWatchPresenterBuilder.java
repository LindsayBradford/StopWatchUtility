/**
 * Copyright (c) 2016, Lindsay Bradford and other Contributors.
 * All rights reserved.
 * 
 * This program and the accompanying materials  are made available 
 * under the terms of the BSD 3-Clause licence  which accompanies 
 * this distribution, and is available at
 * http://opensource.org/licenses/BSD-3-Clause
 */

package blacksmyth.stopwatch.presenter;

/**
 * A class based on the Builder design pattern for the construction of a Swing-based {@link StopWatchPresenter} via
 * dependency injection. 
 */

public final class StopWatchPresenterBuilder {

  public static StopWatchPresenter build() {
    return new DefaultStopWatchPresenter();
  }
}
