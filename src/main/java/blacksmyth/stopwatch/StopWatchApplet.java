// (c) 2003 - Lindsay Bradford -

package blacksmyth.stopwatch;

import java.awt.BorderLayout;
import java.awt.Color;

import javax.swing.JApplet;

import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.EtchedBorder;

import blacksmyth.stopwatch.system.StopWatchController;
import blacksmyth.swing.JPopupMenuListener;

@SuppressWarnings("serial")
public class StopWatchApplet extends JApplet {

  private final StopWatchController stopWatch = new StopWatchController();
  private StopWatchPopup stopWatchPopup;

  public String getAppletInfo() {
    return "A stopwatch applet.";
  }

  public void init() {

    getContentPane().add(stopWatch,BorderLayout.CENTER);

    if (getShowMillisecondsFlag() != true) {
      stopWatch.toggleShowMilliseconds();
    }

    if (getShowLedsFlag() != true) {
      stopWatch.toggleShowLeds();
    }

    stopWatchPopup = new StopWatchPopup(stopWatch);

    addMouseListener(new JPopupMenuListener(stopWatchPopup));

    stopWatch.setBorder(new CompoundBorder(new EtchedBorder(getBorderForegroundParameter(),
	                                                    getBorderBackgroundParameter()),
	                                   new EmptyBorder(12,11,11,11)));

    stopWatch.setForeground(getForegroundParameter());
    stopWatch.setBackground(getBackgroundParameter());

    stopWatchPopup.setForeground(getForegroundParameter());
    stopWatchPopup.setBackground(getBackgroundParameter());

    stopWatch.setCounterForeground(getCounterForegroundParameter());
    stopWatch.setCounterBackground(getCounterBackgroundParameter());

    stopWatch.setCounterBorderColors(getCounterBorderForegroundParameter(),
                                     getCounterBorderBackgroundParameter());

  }

  private boolean getShowMillisecondsFlag() {
    String flag;
    try {
      flag = getParameter("showMilliseconds");
      if (flag.equals("true")) {
        return true;
      }
    } catch (Exception e) {}
    return false;
  }
  
  private boolean getShowLedsFlag() {
    String flag;
    try {
      flag = getParameter("showLeds");
      if (flag.equals("true")) {
        return true;
      }
    } catch (Exception e) {}
    return false;
  }

  private Color getForegroundParameter() {
    return getParameterColor("foreground", Color.BLACK);
  }


  private Color getBackgroundParameter() {
    return getParameterColor("background", Color.LIGHT_GRAY);
  }

  private Color getCounterForegroundParameter() {
    return getParameterColor("counterForeground", Color.BLACK);
  }

  private Color getCounterBackgroundParameter() {
    return getParameterColor("counterBackground", Color.WHITE);
  }

  private Color getCounterBorderForegroundParameter() {
    return getParameterColor("counterBorderForeground", Color.LIGHT_GRAY);
  }

  private Color getCounterBorderBackgroundParameter() {
    return getParameterColor("counterBorderBackground", Color.GRAY);
  }

  private Color getBorderForegroundParameter() {
    return getParameterColor("borderForeground", Color.WHITE);
  }

  private Color getBorderBackgroundParameter() {
    return getParameterColor("borderBackground", Color.BLACK);
  }

  private Color getParameterColor(String parameter, Color defaultColor) {
    String colorParameter = "0x" + getParameter(parameter);
    Color color;
    try {
      color = Color.decode(colorParameter); 
    } catch (NumberFormatException nfe) {
      color = defaultColor;
    }
    return color;
  }
}
