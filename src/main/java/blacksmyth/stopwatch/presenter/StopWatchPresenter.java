package blacksmyth.stopwatch.presenter;

import java.util.Observer;

import blacksmyth.stopwatch.model.StopWatchModel;
import blacksmyth.stopwatch.view.StopWatchView;

public interface StopWatchPresenter extends Observer {
  public void setModel(StopWatchModel model);
  public void setView(StopWatchView view);
}
