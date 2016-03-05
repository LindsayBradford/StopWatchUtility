// (c) 2016 Lindsay Bradford

package blacksmyth.stopwatch.presenter;

import java.util.Observer;

import blacksmyth.stopwatch.model.StopWatchModel;
import blacksmyth.stopwatch.view.StopWatchView;

/**
 * An interface specifying the necessary methods required for implementations to successfully
 * act as Presenter glue between stopwatch models and views as per the MVP pattern.
 * @see <a href="https://en.wikipedia.org/wiki/Model%E2%80%93view%E2%80%93presenter">Wikipedia discussion on MVP</a>
 * @see StopWatchModel
 * @see StopWatchView
 */
public interface StopWatchPresenter extends Observer {
  /**
   * Method specifies the model this presenter must observe for updates, and issue commands to. 
   * @param model
   */
  public void setModel(StopWatchModel model);
  
  /**
   * Method specifies the view this presenter must observe for updates, and issue commands to. 
   * @param model
   */
  public void setView(StopWatchView view);
}
