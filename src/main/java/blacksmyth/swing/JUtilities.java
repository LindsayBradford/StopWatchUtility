/**
 * Copyright (c) 2016, Lindsay Bradford and other Contributors.
 * All rights reserved.
 * 
 * This program and the accompanying materials  are made available 
 * under the terms of the BSD 3-Clause licence  which accompanies 
 * this distribution, and is available at
 * http://opensource.org/licenses/BSD-3-Clause
 */

package blacksmyth.swing;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.Window;
import java.util.List;
import javax.swing.JComponent;

public class JUtilities {

  public static void centerWindow(Window window) {
    final Point screenCentre = getScreenCenterPoint();
    final Dimension windowSize = window.getSize();
    window.setLocation(
        (int) screenCentre.getX() - (windowSize.width  / 2),
        (int) screenCentre.getY() - (windowSize.height / 2)
    );
  }

  private static Dimension getScreenSize() {
    return Toolkit.getDefaultToolkit().getScreenSize();
  }
  
  private static Point getScreenCenterPoint() {
    final Dimension screenSize = getScreenSize();
    return new Point(screenSize.width  / 2, screenSize.height / 2);
  }

  public static void equalizeComponentSizes(List<JComponent> components) {
    resizeAllComponentsToSize(
        components,
        findMaxComponentSize(components)
    );    
  }

  private static void resizeAllComponentsToSize(List<JComponent> components, Dimension newSize) {
    components.stream().forEach(
        (component) -> resize(component, newSize)
    );
  }
  
  private static void resize(JComponent component, Dimension size) {
    component.setPreferredSize(size);
    component.setMaximumSize(size);
    component.setMinimumSize(size);
  }
  
  private static Dimension findMaxComponentSize(List<JComponent> components) {
    return new Dimension(
        getMaxComponentWidth(components), 
        getMaxComponentHeight(components)
    );
  }

  private static int getMaxComponentWidth(List<JComponent> components) {
    return components.stream()
        .max( (c1, c2) -> Integer.compare(c1.getPreferredSize().width, c2.getPreferredSize().width) )
        .get().getPreferredSize().width;
  }
  
  private static int getMaxComponentHeight(List<JComponent> components) {
    return components.stream()
            .max( (c1, c2) -> Integer.compare(c1.getPreferredSize().height, c2.getPreferredSize().height) )
            .get().getPreferredSize().height;
  }
}