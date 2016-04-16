package blacksmyth.stopwatch.view.swing;

public class SwingStopWatchEventDelegator implements SwingStopWatchViewEventRaiser {
  
  private SwingStopWatchViewEventRaiser delegate;
  
  public void setDelegate(SwingStopWatchViewEventRaiser delegate) {
    this.delegate = delegate;
  }

  @Override
  public void raise(SwingStopWatchViewEvents event) {
    delegate.raise(event);
  }

}
