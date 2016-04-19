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

import static org.junit.Assert.*;

import java.util.Observable;
import java.util.Observer;

import org.junit.Before;
import org.junit.Test;

import blacksmyth.stopwatch.model.DefaultStopWatchModel;
import blacksmyth.stopwatch.model.TickRecipient;
import blacksmyth.stopwatch.model.Ticker;

final public class BasicStopWatchModelTest {
  
  private static long SHORT_PAUSE_TIME = 5;
  private static long LONG_PAUSE_TIME = 10;
  
  private DefaultStopWatchModel testModel; 
  
  private MockTicker mockTicker;
  private ModelHarness modelHarness;

  private static void shortPause() {
    pause(SHORT_PAUSE_TIME);
  }

  private static void longPause() {
    pause(LONG_PAUSE_TIME);
  }

  private static void pause(long pauseTime) {
    try {
      Thread.sleep(pauseTime);
    } catch (@SuppressWarnings("unused") InterruptedException e) {}
  }
  
  @Before
  public void testSetup() {
    this.modelHarness = new ModelHarness();
    this.testModel = new DefaultStopWatchModel();
    this.mockTicker = new MockTicker();
    
    this.mockTicker.setRecipient(this.testModel);
    this.testModel.addObserver(this.modelHarness);
  }
  
  @Test
  public void Start_FromInitialState_IsRunning() {

    long firstTime = this.testModel.getTime();

    assertFalse(
        this.testModel.isRunning()
    );
    
    assertTrue(firstTime == 0);
    
    this.testModel.start();
    
    assertTrue(
        this.testModel.isRunning()
    );

    shortPause();
    
    long secondTime = this.testModel.getTime();
    
    assertTrue(secondTime > firstTime);
  }

  @Test
  public void Start_FromRunningState_RunningAndNotReset() {
    this.testModel.start();
    
    longPause();

    long firstTime = this.testModel.getTime();
    
    this.testModel.start();

    assertTrue(
        this.testModel.isRunning()
    );

    shortPause();
    
    long secondTime = this.testModel.getTime();
    
    assertTrue(secondTime > firstTime);
  }
  
  @Test
  public void Stop_FromInitialState_NotRunning() {

    assertFalse(
        this.testModel.isRunning()
    );

    this.testModel.stop();

    long stopTime = this.testModel.getTime();
    
    assertFalse(
        this.testModel.isRunning()
    );
    
    assertTrue(stopTime == 0);
  }

  @Test
  public void Stop_FromRunningState_NotRunning() {

    this.testModel.start();

    long startTime = this.testModel.getTime();
    
    shortPause();
    
    this.testModel.stop();

    long stopTime1 = this.testModel.getTime();

    assertFalse(
        this.testModel.isRunning()
    );
    
    assertTrue(stopTime1 > startTime);
    
    shortPause();

    long stopTime2 = this.testModel.getTime();

    assertTrue(stopTime1 == stopTime2);
  }

  @Test
  public void Reset_FromInitialState_NotRunning() {

    long initialTime = this.testModel.getTime();
    
    this.testModel.reset();

    assertFalse(
        this.testModel.isRunning()
    );
    
    shortPause();

    long resetTime = this.testModel.getTime();

    assertTrue(initialTime == resetTime);
    assertTrue(resetTime == 0);
  }
  
  @Test
  public void Reset_FromRunningState_NotRunning() {

    long initialTime = this.testModel.getTime();
    
    this.testModel.start();

    shortPause();

    long preResetTime = this.testModel.getTime();
    
    this.testModel.reset();

    long resetTime = this.testModel.getTime();
    
    assertTrue(preResetTime > initialTime);
    assertTrue(initialTime == resetTime);
    assertTrue(resetTime == 0);
  }

  @Test
  public void SetTime_FromInitialState_NotRunning() {
    int TEST_TIME = 100; 

    this.testModel.setTime(TEST_TIME);
    
    assertFalse(
        this.testModel.isRunning()
    );
    
    assertTrue(this.testModel.getTime() == TEST_TIME);
  }

  @Test
  public void SetTime_FromRunningState_NotRunning() {
    int TEST_TIME = 100; 
    
    this.testModel.start();
    
    shortPause();
    
    this.testModel.setTime(TEST_TIME);
    
    assertFalse(
        this.testModel.isRunning()
    );
    
    assertTrue(this.testModel.getTime() == TEST_TIME);
  }

  @SuppressWarnings("unqualified-field-access")
  @Test
  public void update_FromRunningState_triggeredFromTicker() {

    // A StopWatchObserver should receive an initial notification of 
    // time upon subscription (1 update).

    assertTrue(
        modelHarness.getUpdatesReceived() == 1
    );

    testModel.start();
    
    // From this point on, manual ticks should trigger an Observable.update(), 
    // and consequently report an extra update.
    
    this.mockTicker.doManualTick();
    
    assertTrue(
        modelHarness.getUpdatesReceived() == 2
    );

    this.mockTicker.doManualTick();
    
    assertTrue(
        modelHarness.getUpdatesReceived() == 3
    );

    this.mockTicker.doManualTick();
    
    assertTrue(
        modelHarness.getUpdatesReceived() == 4
    );
    
    // When the model is first stopped, we expect a time update on the stop event.
    
    testModel.stop();

    assertTrue(
        modelHarness.getUpdatesReceived() == 5
    );

    // However, as model state isn't changing when stopped, we don't expect to 
    // receive tick-triggered updates.
    
    this.mockTicker.doManualTick();

    assertTrue(
        modelHarness.getUpdatesReceived() == 5
    );
  }
  
  private class MockTicker implements Ticker {
    private TickRecipient recipient;
    
    public MockTicker() {
      // TODO Auto-generated constructor stub
    }

    @Override
    public void setRecipient(TickRecipient recipient) {
      this.recipient = recipient;
    }

    @Override
    public void setMillisecondsBetweenTicks(int milliseconds) {}

    @Override
    public void startTicking() {}
    
    public void doManualTick() {
      this.recipient.receiveTick();
    }
  }
  
  private class ModelHarness implements Observer {

    private int updatesReceived;
    
    public ModelHarness() {
      this.updatesReceived = 0;
    }
    
    @Override
    public void update(Observable o, Object arg) {
      this.updatesReceived++;
    }
    
    public int getUpdatesReceived() {
      return this.updatesReceived;
    }
  }
}


