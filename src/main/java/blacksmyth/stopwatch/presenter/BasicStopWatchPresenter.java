package blacksmyth.stopwatch.presenter;

import java.util.Observable;

import blacksmyth.stopwatch.model.StopWatchModel;
import blacksmyth.stopwatch.view.StopWatchView;
import blacksmyth.stopwatch.view.StopWatchViewEvents;

public class BasicStopWatchPresenter implements StopWatchPresenter {

  private StopWatchModel model;
  private StopWatchView  view;
  
  @Override
  public void update(Observable eventSource, Object eventDetail) {
    if (eventSource == model) {
      processModelEvent();  
    } else {
      processViewEvent(
          (StopWatchViewEvents) eventDetail
      );
    }
  }

  private void processViewEvent(StopWatchViewEvents viewEvent) {
    switch (viewEvent) {
      case ResetRequested:
        model.reset();
        break;
      case StartRequested:
        model.start();
        break;
      case StopRequested:
        model.stop();
        break;
      case TimeSetRequested:
        model.setTime(
            view.getRequestedSetTime()
        );
        break;
      case DeathRequested:
        model.die();
        break;
    }
  }

  private void processModelEvent() {
    // The only events issued by a StopWatchModel are notifications that elapsed time has changed. 
    // All we need do here it update the view with the new time.
    
    if (view == null) return;
    
    view.setTime(
        model.getTime()
    );
  }

  @Override
  public void setModel(StopWatchModel model) {
    this.model = model;
  }

  @Override
  public void setView(StopWatchView view) {
    assert this.model != null;
    
    this.view = view;
    
    // If the view remembers a time that it held on its last die event, we spoof a TimeSetRequested event here
    // to trigger the model synchronsing to that time..
    
    processViewEvent(
        StopWatchViewEvents.TimeSetRequested
    );
  }
}
