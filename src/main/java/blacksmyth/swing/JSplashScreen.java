package blacksmyth.swing;

import blacksmyth.utilities.ResourceLoader;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JWindow;
import javax.swing.JProgressBar;
import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class JSplashScreen {

  private String message;
  private String imageFile;

  private JWindow splashScreen;
  private JProgressBar progressBar;

  public JSplashScreen(String imageFile, String message) {
    this.message = message;
    this.imageFile = imageFile;

    buildScreen();
  }

  public void show() {
    splashScreen.setVisible(true);
  }

  public void finish() {
    updateProgressBar(100);
    pause(200);
    splashScreen.setVisible(false);
  }

  private static void pause(long milliseconds) {
    long now = System.currentTimeMillis();
    long hideTime = now + milliseconds;
    while(now < hideTime) {
      now = System.currentTimeMillis();
    }
  }

  public void updateProgressBar(int completionValue) {
    if (completionValue < 0 || completionValue > 100) {
      return;
    }
    progressBar.setValue(completionValue);
  }

  private void buildScreen() {

    splashScreen = new JWindow();

    splashScreen.getContentPane().add(getContents(imageFile, message), 
                                      BorderLayout.CENTER);

    splashScreen.pack();

    JUtilities.centerWindow(splashScreen);
  }

  private JComponent getContents(String imageFile, String message) {

    GridBagLayout      gbl  = new GridBagLayout();
    GridBagConstraints gbc  = new GridBagConstraints();

    JPanel windowPanel = new JPanel(gbl);

    windowPanel.setBorder(
      BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(Color.black, 2),
                                         BorderFactory.createEmptyBorder(2,2,2,2)));


    int yPos = 0;

    gbc.gridwidth    = 1;
    gbc.gridheight   = 1;
    gbc.gridx        = 0;
    gbc.gridy        = yPos++;

    windowPanel.add(ResourceLoader.loadImageAsJLabel(imageFile),gbc);

    gbc.fill         = GridBagConstraints.HORIZONTAL;
    gbc.insets       = new Insets(5,5,5,5);
    gbc.gridy        = yPos++;

    windowPanel.add(getProgressBar(),gbc);

    gbc.anchor       = GridBagConstraints.CENTER;
    gbc.insets       = new Insets(5,0,5,0);
    gbc.gridy        = yPos++;

    windowPanel.add(new JLabel(message, JLabel.CENTER), gbc);
    
    return windowPanel;
  }

  private JProgressBar getProgressBar() {
    progressBar = new JProgressBar();
    progressBar.setStringPainted(true);
    progressBar.setBorder(BorderFactory.createLoweredBevelBorder());
    return progressBar;
  }
}
