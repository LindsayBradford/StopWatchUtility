package blacksmyth.stopwatch.view;

public class StopWatchEventDelegator implements StopWatchViewEventRaiser {
  
  private StopWatchViewEventRaiser delegate;
  
  public void setDelegate(StopWatchViewEventRaiser delegate) {
    this.delegate = delegate;
  }

  @Override
  public void raise(StopWatchViewEvents event) {
    delegate.raise(event);
  }

}
