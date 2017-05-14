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

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;
import static org.mockito.Matchers.anyLong;

import blacksmyth.stopwatch.model.StopWatchModel;
import blacksmyth.stopwatch.view.StopWatchView;
import static blacksmyth.stopwatch.view.StopWatchViewEvent.*;

public class DefaultStopWatchPresenterTest {
  
  private DefaultStopWatchPresenter presenterBeingTested;

  @Mock
  private StopWatchView mockView;
  
  @Mock
  private StopWatchModel mockModel;
  
  @Before
  public void before() {
    presenterBeingTested = new DefaultStopWatchPresenter();

    MockitoAnnotations.initMocks(this);
    presenterBeingTested.setModel(mockModel);
    presenterBeingTested.setView(mockView);
  }

  @Test
  public void testUpdateFromView_StartRequested_TriggersModelStart() {
    presenterBeingTested.updateFromView(StartRequested);
    verify(mockModel, times(1)).start();
  }

  @Test
  public void testUpdateFromView_StopRequested_TriggersModelStop() {
    presenterBeingTested.updateFromView(StopRequested);
    verify(mockModel, times(1)).stop();
  }
  
  @Test
  public void testUpdateFromView_ResetRequested_TriggersModelReset() {
    presenterBeingTested.updateFromView(ResetRequested);
    verify(mockModel, times(1)).reset();
  }

  @Test
  public void testUpdateFromView_TimeSetRequested_TriggersModelTimeSet() {
    verify(mockModel, times(1)).setTime(anyLong());              // time set as part of initialisation.
    presenterBeingTested.updateFromView(TimeSetRequested);
    verify(mockModel, times(2)).setTime(anyLong());              // time set again as response to event.
  }
  
  @Test
  public void testUpdateFromView_DeathRequested_TriggersModelDeath() {
    presenterBeingTested.updateFromView(DeathRequested);
    verify(mockModel, times(1)).die();
  }
}
