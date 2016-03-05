// (c) 2016 - Lindsay Bradford -

package blacksmyth.stopwatch.view;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JPanel;
import javax.swing.JLabel;

import blacksmyth.utilities.ResourceLoader;

/**
 * A simple JPanel implementation showing a red and green led,  where the "lit" led can be toggled. 
 */
@SuppressWarnings("serial")
final class JGoStopPanel extends JPanel {

  private static final GridBagLayout gbl      = new GridBagLayout();
  private static final GridBagConstraints gbc = new GridBagConstraints();

  private JLedPanel greenLed;
  private JLedPanel redLed;

  public JGoStopPanel() {
    super(gbl);
    setBorder(null);
    buildPanel();
    deactivate();
  }

  public void setForeground(Color color) {
    if (greenLed != null) {
      greenLed.setForeground(color);
      redLed.setForeground(color);
    }
    super.setForeground(color);
  }

  public void setBackground(Color color) {
    if (greenLed != null) {
      greenLed.setBackground(color);
      redLed.setBackground(color);
    }
    super.setBackground(color);
  }

  private void buildPanel() {
    int ledRow = 0;

    gbc.insets = new Insets(0,0,0,5);

    gbc.gridwidth    = 1;
    gbc.gridheight   = 1;
    gbc.gridx        = 0;
    gbc.gridy        = ledRow++;
    gbc.weightx      = 0;
    gbc.weighty      = 0.2;
    gbc.anchor       = GridBagConstraints.SOUTH;

    greenLed = new JGreenLed();
    add(greenLed, gbc);

    gbc.gridy        = ledRow;
    gbc.anchor       = GridBagConstraints.NORTH;

    redLed = new JRedLed();
    add(redLed,gbc);
  }

  public void activate() {
    redLed.deactivate();
    greenLed.activate();
  }

  public void deactivate() {
    greenLed.deactivate();
    redLed.activate();
  }
}

@SuppressWarnings("serial")
final class JRedLed extends JLedPanel {
  public JRedLed() {
    super("/resources/LitRedLed.gif",
	  "/resources/RedLed.gif");
  }
}

@SuppressWarnings("serial")
final class JGreenLed extends JLedPanel {
  public JGreenLed() {
    super("/resources/LitGreenLed.gif",
	  "/resources/GreenLed.gif");
  }
}

@SuppressWarnings("serial")
abstract class JLedPanel extends JPanel {

  private JLabel onGif;
  private JLabel offGif;

  public JLedPanel(String onGifPath, String offGifPath) {
    super();
    setBorder(null);
    onGif  = ResourceLoader.loadImageAsJLabel(onGifPath);
    offGif = ResourceLoader.loadImageAsJLabel(offGifPath);
    deactivate();
  }

  public void deactivate() {
    remove(onGif);
    add(offGif);
    repaint();
  }

  public void activate() {
    remove(offGif);
    add(onGif);
    repaint();
  }
}
