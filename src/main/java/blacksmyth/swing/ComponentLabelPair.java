package blacksmyth.swing;

import javax.swing.*;

public class ComponentLabelPair {
  private final JComponent vComponent;
  private final JLabel     vLabel;
  
  public ComponentLabelPair(JComponent pComponent, JLabel pLabel) {
    vComponent = pComponent;
    vLabel     = pLabel;
    bindPair();
  }

  private void bindPair() {
    getLabel().setLabelFor(getComponent());
  }
    
  public JComponent getComponent() {
    return vComponent;
  }

  public JLabel getLabel() {
    return vLabel;
  }
}