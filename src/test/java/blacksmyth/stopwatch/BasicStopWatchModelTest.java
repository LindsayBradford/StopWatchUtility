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

import blacksmyth.stopwatch.model.DefaultStopWatchModel;
import blacksmyth.stopwatch.model.Ticker;
import blacksmyth.stopwatch.model.TickRecipient;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.Test;

import java.util.Observable;
import java.util.Observer;

final public class BasicStopWatchModelTest {
  
  private static long SHORT_PAUSE_TIME = 5;
  private static long LONG_PAUSE_TIME = 10;
  
  private DefaultStopWatchModel modelBeingTested; 
  
  private MockTicker mockTicker;
  private ModelHarness modelHarness;

  private static void doShortPause() {
    pause(SHORT_PAUSE_TIME);
  }

  private static void doLongPause() {
    pause(LONG_PAUSE_TIME);
  }

  private static void pause(long pauseTime) {
    try {
      Thread.sleep(pauseTime);
    } catch (InterruptedException e) {}
  }
  
  @Before
  public void testSetup() {
    modelHarness = new ModelHarness();
    modelBeingTested = new DefaultStopWatchModel();
    mockTicker = new MockTicker();
    
    mockTicker.setRecipient(modelBeingTested);
    modelBeingTested.addObserver(modelHarness);
  }
  
  @Test
  public void Start_FromInitialState_IsRunning() {

    long firstTime = modelBeingTested.getTime();

    assertFalse(modelBeingTested.isMeasuringTime());
    assertEquals(0, firstTime);
    
    modelBeingTested.start();
    
    assertTrue(modelBeingTested.isMeasuringTime());

    doShortPause();
    
    long secondTime = modelBeingTested.getTime();
    
    assertTrue(secondTime > firstTime);
  }

  @Test
  public void Start_FromRunningState_RunningAndNotReset() {
    modelBeingTested.start();
    
    doLongPause();

    long firstTime = modelBeingTested.getTime();
    
    modelBeingTested.start();

    assertTrue(modelBeingTested.isMeasuringTime());

    doShortPause();
    
    long secondTime = modelBeingTested.getTime();
    
    assertTrue(secondTime > firstTime);
  }
  
  @Test
  public void Stop_FromInitialState_NotRunning() {

    assertFalse(modelBeingTested.isMeasuringTime());

    modelBeingTested.stop();

    long stopTime = modelBeingTested.getTime();
    
    assertFalse(modelBeingTested.isMeasuringTime());
    
    assertEquals(0, stopTime);
  }

  @Test
  public void Stop_FromRunningState_NotRunning() {

    modelBeingTested.start();

    long startTime = modelBeingTested.getTime();
    
    doShortPause();
    
    modelBeingTested.stop();

    long stopTime1 = modelBeingTested.getTime();

    assertFalse(modelBeingTested.isMeasuringTime());
    
    assertTrue(stopTime1 > startTime);
    
    doShortPause();

    long stopTime2 = modelBeingTested.getTime();

    assertEquals(stopTime1,stopTime2);
  }

  @Test
  public void Reset_FromInitialState_NotRunning() {

    long initialTime = modelBeingTested.getTime();
    
    modelBeingTested.reset();

    assertFalse(modelBeingTested.isMeasuringTime());
    
    doShortPause();

    long resetTime = modelBeingTested.getTime();

    assertEquals(initialTime,resetTime);
    assertEquals(0, resetTime);
  }
  
  @Test
  public void Reset_FromRunningState_NotRunning() {

    long initialTime = modelBeingTested.getTime();
    
    modelBeingTested.start();

    doShortPause();

    long preResetTime = modelBeingTested.getTime();
    
    modelBeingTested.reset();

    long resetTime = modelBeingTested.getTime();
    
    assertTrue(preResetTime > initialTime);
    assertEquals(initialTime,resetTime);
    assertEquals(0, resetTime);
  }

  @Test
  public void SetTime_FromInitialState_NotRunning() {
    int TEST_TIME = 100; 

    modelBeingTested.setTime(TEST_TIME);
    
    assertFalse(modelBeingTested.isMeasuringTime());
    assertEquals(TEST_TIME, modelBeingTested.getTime());
  }

  @Test
  public void SetTime_FromStoppedState_Running() {
    int TEST_TIME = 100; 

    modelBeingTested.setTime(TEST_TIME);
    modelBeingTested.start();
    
    doShortPause();
        
    assertTrue(modelBeingTested.isMeasuringTime());
    assertTrue(modelBeingTested.getTime() > TEST_TIME);
  }
  
  @Test
  public void SetTime_FromRunningState_NotRunning() {
    int TEST_TIME = 100; 
    
    modelBeingTested.start();
    
    doShortPause();
    
    modelBeingTested.setTime(TEST_TIME);
    
    assertFalse(modelBeingTested.isMeasuringTime());
    assertEquals(TEST_TIME, modelBeingTested.getTime());
  }


  @SuppressWarnings("unqualified-field-access")
  @Test
  public void update_FromRunningState_triggeredFromTicker() {

    // A StopWatchObserver should receive an initial notification of 
    // time upon subscription (1 update).

    int expectedTicks = 1;
    
    verifyModelHarnessExpectedTicks(expectedTicks);

    modelBeingTested.start();
    
    // From this point on, manual ticks should trigger an Observable.update(), 
    // and consequently report an extra update.
    
    mockTicker.doManualTick();
    
    verifyModelHarnessExpectedTicks(++expectedTicks);

    mockTicker.doManualTick();

    verifyModelHarnessExpectedTicks(++expectedTicks);

    mockTicker.doManualTick();

    verifyModelHarnessExpectedTicks(++expectedTicks);
    
    // When the model is first stopped, we expect a time update on the stop event.
    
    modelBeingTested.stop();

    verifyModelHarnessExpectedTicks(++expectedTicks);

    // However, as model state isn't changing when stopped, we don't expect to 
    // receive tick-triggered updates.
    
    mockTicker.doManualTick();

    verifyModelHarnessExpectedTicks(expectedTicks);
  }
  
  private void verifyModelHarnessExpectedTicks(int expectedTicks) {
    assertEquals(expectedTicks, modelHarness.getUpdatesReceived());
  }
  
  private class MockTicker implements Ticker {
    private TickRecipient tickRecipient;
    
    public MockTicker() {
      // TODO Auto-generated constructor stub
    }

    @Override
    public void setRecipient(TickRecipient recipient) {
      tickRecipient = recipient;
    }

    @Override
    public void setMillisecondsBetweenTicks(int milliseconds) {}

    @Override
    public void startTicking() {}
    
    public void doManualTick() {
      tickRecipient.receiveTick();
    }
  }
  
  private class ModelHarness implements Observer {

    private int updatesReceived;
    
    public ModelHarness() {
      updatesReceived = 0;
    }
    
    @Override
    public void update(Observable o, Object arg) {
      updatesReceived++;
    }
    
    public int getUpdatesReceived() {
      return updatesReceived;
    }
  }
}


