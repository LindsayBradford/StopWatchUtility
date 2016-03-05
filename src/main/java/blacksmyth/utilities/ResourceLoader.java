/**
 * Copyright (c) 2016, Lindsay Bradford and other Contributors.
 * All rights reserved.
 * 
 * This program and the accompanying materials  are made available 
 * under the terms of the BSD 3-Clause licence  which accompanies 
 * this distribution, and is available at
 * http://opensource.org/licenses/BSD-3-Clause
 */

package blacksmyth.utilities;

import java.io.*;
import javax.swing.*;
import java.applet.*;

/**
 * A utility class for retrieving resources that have been packed into 
 * an accompanying JAR/WAR.
 */
public class ResourceLoader {

  public static JLabel loadImageAsJLabel(String imageFile) {
    return new JLabel(loadImageAsIcon(imageFile));
  }

  public static ImageIcon loadImageAsIcon(String imageFile) {
    try {
      InputStream in = ResourceLoader.class.getResourceAsStream(imageFile);
      final byte[] imageByteBuffer = loadAsByteArray(in);
      in.close();
      return new ImageIcon(imageByteBuffer);
    } catch (Exception e) {
      e.printStackTrace();
      return null;
    }
  }

  public static AudioClip loadAudioClip(String audioClipFile) {
    return Applet.newAudioClip(ResourceLoader.class.getResource(audioClipFile));
  }

  public static byte[] loadAsByteArray(final InputStream is) {
    final int ARRAY_SIZE = 131072;
    int read = 0;
    int totalRead = 0;

    byte[] byteArray = new byte[ARRAY_SIZE];

    try {
      while ((read = is.read(byteArray, totalRead, ARRAY_SIZE - totalRead)) >= 0) {
        totalRead += read;
      }
    } catch (Exception e) {
      return null;
    }

    byte[] finalByteArray = new byte[totalRead];
    System.arraycopy(byteArray, 0, finalByteArray, 0, totalRead);
    return finalByteArray;
  }
}
