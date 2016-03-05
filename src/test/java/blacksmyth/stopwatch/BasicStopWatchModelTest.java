// (c) 2016 Lindsay Bradford

package blacksmyth.stopwatch;

import static org.junit.Assert.*;

import java.util.Observable;
import java.util.Observer;

import org.junit.Before;
import org.junit.Test;

import blacksmyth.stopwatch.model.BasicStopWatchModel;
import blacksmyth.stopwatch.model.TickRecipient;
import blacksmyth.stopwatch.model.Ticker;

public class BasicStopWatchModelTest {
  
  private static long SHORT_PAUSE_TIME = 5;
  private static long LONG_PAUSE_TIME = 10;
  
  private BasicStopWatchModel testModel; 
  
  private MockTicker mockTicker;
  private ModelHarness modelHarness;

  private void shortPause() {
    pause(SHORT_PAUSE_TIME);
  }

  private void longPause() {
    pause(LONG_PAUSE_TIME);
  }

  private void pause(long pauseTime) {
    try {
      Thread.sleep(pauseTime);
    } catch (InterruptedException e) {}
  }
  
  @SuppressWarnings({"cast"})
  @Before
  public void testSetup() {
    modelHarness = new ModelHarness();
    testModel = new BasicStopWatchModel();
    mockTicker = new MockTicker();
    
    mockTicker.setRecipient(testModel);
    testModel.addObserver(modelHarness);
  }
  
  @Test
  public void Start_FromInitialState_IsRunning() {

    long firstTime = testModel.getTime();

    assertFalse(
        testModel.isRunning()
    );
    
    assertTrue(firstTime == 0);
    
    testModel.start();
    
    assertTrue(
        testModel.isRunning()
    );

    shortPause();
    
    long secondTime = testModel.getTime();
    
    assertTrue(secondTime > firstTime);
  }

  @Test
  public void Start_FromRunningState_RunningAndNotReset() {
    testModel.start();
    
    longPause();

    long firstTime = testModel.getTime();
    
    testModel.start();

    assertTrue(
        testModel.isRunning()
    );

    shortPause();
    
    long secondTime = testModel.getTime();
    
    assertTrue(secondTime > firstTime);
  }
  
  @Test
  public void Stop_FromInitialState_NotRunning() {

    assertFalse(
        testModel.isRunning()
    );

    testModel.stop();

    long stopTime = testModel.getTime();
    
    assertFalse(
        testModel.isRunning()
    );
    
    assertTrue(stopTime == 0);
  }

  @Test
  public void Stop_FromRunningState_NotRunning() {

    testModel.start();

    long startTime = testModel.getTime();
    
    shortPause();
    
    testModel.stop();

    long stopTime1 = testModel.getTime();

    assertFalse(
        testModel.isRunning()
    );
    
    assertTrue(stopTime1 > startTime);
    
    shortPause();

    long stopTime2 = testModel.getTime();

    assertTrue(stopTime1 == stopTime2);
  }

  @Test
  public void Reset_FromInitialState_NotRunning() {

    long initialTime = testModel.getTime();
    
    testModel.reset();

    assertFalse(
        testModel.isRunning()
    );
    
    shortPause();

    long resetTime = testModel.getTime();

    assertTrue(initialTime == resetTime);
    assertTrue(resetTime == 0);
  }
  
  @Test
  public void Reset_FromRunningState_NotRunning() {

    long initialTime = testModel.getTime();
    
    testModel.start();

    shortPause();

    long preResetTime = testModel.getTime();
    
    testModel.reset();

    long resetTime = testModel.getTime();
    
    assertTrue(preResetTime > initialTime);
    assertTrue(initialTime == resetTime);
    assertTrue(resetTime == 0);
  }

  @Test
  public void SetTime_FromInitialState_NotRunning() {
    int TEST_TIME = 100; 

    testModel.setTime(TEST_TIME);
    
    assertFalse(
        testModel.isRunning()
    );
    
    assertTrue(testModel.getTime() == TEST_TIME);
  }

  @Test
  public void SetTime_FromRunningState_NotRunning() {
    int TEST_TIME = 100; 
    
    testModel.start();
    
    shortPause();
    
    testModel.setTime(TEST_TIME);
    
    assertFalse(
        testModel.isRunning()
    );
    
    assertTrue(testModel.getTime() == TEST_TIME);
  }

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
    
    mockTicker.doManualTick();
    
    assertTrue(
        modelHarness.getUpdatesReceived() == 2
    );

    mockTicker.doManualTick();
    
    assertTrue(
        modelHarness.getUpdatesReceived() == 3
    );

    mockTicker.doManualTick();
    
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
    
    mockTicker.doManualTick();

    assertTrue(
        modelHarness.getUpdatesReceived() == 5
    );
  }
  
  private class MockTicker implements Ticker {
    private TickRecipient recipient;
    
    @Override
    public void setRecipient(TickRecipient recipient) {
      this.recipient = recipient;
    }

    @Override
    public void setMillisecondsBetweenTicks(int milliseconds) {}

    @Override
    public void startTicking() {}
    
    public void doManualTick() {
      recipient.receiveTick();
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


