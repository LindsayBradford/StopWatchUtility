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

/**
 * An interface that any name-value pair persistence mechanism should implement. As the lowest common demonimator
 * for NVP storage is for name and value to be be strings, the interface assumes this, and supplies helper methods
 * to allow users to convert to and from other base types as needed. 
 */
public interface PersistedNvpState {

  public String getAsString();
  public int getAsInt();
  public long getAsLong();
  public double getAsDouble();
  public boolean getAsBoolean();

  public void putAsString(String value);
  public void putAsInt(int value);
  public void putAsLong(long value);
  public void putAsDouble(double value);
  public void putAsBoolean(boolean value);
}
