/**
 * Copyright (c) 2017, Lindsay Bradford and other Contributors.
 * All rights reserved.
 * 
 * This program and the accompanying materials  are made available 
 * under the terms of the BSD 3-Clause licence  which accompanies 
 * this distribution, and is available at
 * http://opensource.org/licenses/BSD-3-Clause
 */

package blacksmyth.stopwatch;

import org.springframework.context.annotation.*;

import blacksmyth.stopwatch.model.DefaultModelConfig;
import blacksmyth.stopwatch.model.StopWatchModel;
import blacksmyth.stopwatch.view.StopWatchView;
import blacksmyth.stopwatch.view.swing.SwingViewConfig;
import blacksmyth.stopwatch.presenter.DefaultPresenterConfig;
import blacksmyth.stopwatch.presenter.StopWatchPresenter;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

/**
 * A Spring Java annotation implementation of Dependency Injection
 * to create a fully functional StopWatch application.
 */

@Configuration
@Import({ DefaultModelConfig.class, SwingViewConfig.class, DefaultPresenterConfig.class})
public class DefaultConfig {
  
  @Resource
  private StopWatchModel model;
  
  @Resource
  private StopWatchView view;

  @Resource
  private StopWatchPresenter presenter;
  
  @PostConstruct
  private void bindModelViewAndPresenter() {
    presenter.setModel(model);
    presenter.setView(view);
  }
}