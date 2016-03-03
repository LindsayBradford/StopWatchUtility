// (c) 2002 - Lindsay Bradford -

package blacksmyth.swing;

import java.awt.*;
import java.util.List;
import javax.swing.*;

public class JUtilities {

  public static void centerWindow(Window window) {
    final Point screenCentre = getScreenCentre();
    final Dimension windowSize = window.getSize();
    window.setLocation((int) screenCentre.getX() - (windowSize.width  / 2),
                       (int) screenCentre.getY() - (windowSize.height / 2));
  }

  private static Point getScreenCentre() {
    final Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    return new Point(screenSize.width  / 2, screenSize.height / 2);
  }

  public static void centerWindowRandomly(Window window, double percentageArea) {

    assert percentageArea >= 0 && percentageArea <= 1 : "percentageArea not between 0 & 100%";

    Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

    double reducedWidth  = screenSize.width  * percentageArea;
    double reducedHeight = screenSize.height * percentageArea;

    Dimension windowSize = window.getSize();

    // If window is bigger than reduced screen area dimensions, just center it.

    if (reducedWidth < windowSize.width || reducedHeight < windowSize.height) {
      centerWindow(window);
      return;
    }

    double offsetX = (screenSize.width  / 2) - (reducedWidth  / 2);
    double offsetY = (screenSize.height / 2) - (reducedHeight / 2);

    // now we have the offset, reduce valid height/width range by size of window.

    reducedWidth  = reducedWidth  - windowSize.width;
    reducedHeight = reducedHeight - windowSize.height;

    double randomX = Math.random() * reducedWidth;
    double randomY = Math.random() * reducedHeight;

    randomX += offsetX;
    randomY += offsetY;

    window.setLocation((int) randomX, (int) randomY);
  }


  public static void equalizeComponentSizes(List<JComponent> components) {
    Dimension  maxComponentSize = new Dimension(0,0);
    for (int i = 0; i < components.size(); ++i) {
      setMaximumSize(maxComponentSize,(JComponent) components.get(i));
    }
    for (int i = 0; i < components.size(); ++i) {
      resize((JComponent) components.get(i), maxComponentSize);
    }
  } 

  private static void setMaximumSize(Dimension maxComponentSize, JComponent component) {
    final Dimension componentSize   = component.getPreferredSize();
    maxComponentSize.width  = Math.max(maxComponentSize.width,  (int)componentSize.getWidth());
    maxComponentSize.height = Math.max(maxComponentSize.height, (int)componentSize.getHeight());
  }

  private static void resize(JComponent component, Dimension size) {
    component.setPreferredSize(size);
    component.setMaximumSize(size);
    component.setMinimumSize(size);
  }
}
