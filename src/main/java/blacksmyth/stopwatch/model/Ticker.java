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

/**
 * An interface specifying the methods needed for a Heart-beat (Ticker) service, allowing the Ticker to
 * update a TickRecipient with a regular heart-beat, whilst also allowing the TickRecipient to remain unaware
 * of the Ticker itself.
 * @see TickRecipient
 */
public interface Ticker {
  /**
   * Tells the Ticker to inform recipient of its heart-beat ticks.
   * @param recipient
   */
  public void setRecipient(TickRecipient recipient);

  /**
   * At construction time, allows the specification of the tick frequency needed in milliseconds.
   * @param milliseconds
   */
  public void setMillisecondsBetweenTicks(int milliseconds);
  
  /**
   * At construction time, allows the caller to start the heart-beat process. This permits construction
   * sequence to best choose when the heart-beat service should begin. 
   * Calls to this method assume a pre-requisite that setRecipient() has already been called.
   */
  public void startTicking();
}
