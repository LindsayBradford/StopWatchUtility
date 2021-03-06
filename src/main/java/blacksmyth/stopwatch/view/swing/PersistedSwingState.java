/**
 * Copyright (c) 2016, Lindsay Bradford and other Contributors.
 * All rights reserved.
 * 
 * This program and the accompanying materials  are made available 
 * under the terms of the BSD 3-Clause licence  which accompanies 
 * this distribution, and is available at
 * http://opensource.org/licenses/BSD-3-Clause
 */

package blacksmyth.stopwatch.view.swing;

import java.util.prefs.Preferences;
import blacksmyth.stopwatch.view.StopWatchView;

/**
 * An enumeration of all valid name-value pair (NVP) entries that are persisted across invocations 
 * of a Swing {@link StopWatchView} implementation.
 */

public enum PersistedSwingState implements PersistedNvpState {
  
  /**
   * The horizontal position of the stopwatch frame/window. 
   */
  FRAME_X_POS("frame.pos.x", "10"),
  
  /**
   * The vertical position of the stopwatch frame/window. 
   */
  
  FRAME_Y_POS("frame.pos.y", "10"),
  
  /**
   * The text title of the stopwatch frame/window
   */
  
  FRAME_TITLE("frame.title", "StopWatch"),

  /**
   *  The elapsed time to display on the stopwatch.
   */
  ELAPSED_TIME("controlPanel.elapsedTime", "0"),
  
  /**
   *  Indicates whether to show or hide milliseconds on the stopwatch display.
   */
  
  TOGGLE_MILLISECONDS("controlPanel.toggleMilliseconds", "true"),
  
  /**
   * Indicates whether to show or hide leds on the stopwatch display.
   */
  TOGGLE_LEDS("controlPanel.toggleLeds", "true");
    
  private static final Preferences preferences = 
      Preferences.userNodeForPackage(PersistedSwingState.class);
  
  private String key;
  private String defaultValue;
  
  private PersistedSwingState(String key, String defaultValue) {
    this.key = key;
    this.defaultValue = defaultValue;
  }

  @Override
  public String getAsString() {
    return preferences.get(key, defaultValue);
  }

  @Override
  public int getAsInt() {
    return preferences.getInt(key, Integer.parseInt(defaultValue));
  }

  @Override
  public long getAsLong() {
    return preferences.getLong(key, Long.parseLong(defaultValue));
  }
  
  @Override
  public double getAsDouble() {
    return preferences.getDouble(key, Double.parseDouble(defaultValue));
  }

  @Override
  public boolean getAsBoolean() {
    return preferences.getBoolean(key, Boolean.parseBoolean(defaultValue));
  }

  @Override
  public void putAsString(String value) {
    preferences.put(key, value);
  }

  @Override
  public void putAsInt(int value) {
    preferences.putInt(key, value);
  }

  @Override
  public void putAsLong(long value) {
    preferences.putLong(key, value);
  }
  
  @Override
  public void putAsDouble(double value) {
    preferences.putDouble(key, value);
  }

  @Override
  public void putAsBoolean(boolean value) {
    preferences.putBoolean(key, value);
  }
}