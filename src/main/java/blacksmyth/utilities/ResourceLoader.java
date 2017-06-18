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

import java.io.InputStream;
import java.applet.Applet;
import java.applet.AudioClip;
import javax.swing.ImageIcon;
import javax.swing.JLabel;

/**
 * A utility class for retrieving resources that have been packed into 
 * an accompanying JAR/WAR.
 */
public final class ResourceLoader {

  public static JLabel loadImageAsJLabel(String imageFile) {
    return new JLabel(loadImageAsIcon(imageFile));
  }

  public static ImageIcon loadImageAsIcon(String imageFile) {
    try (InputStream resourceStream = ResourceLoader.class.getResourceAsStream(imageFile)) {
      final byte[] imageByteArray = ByteArrayLoadManager.newInstance().loadFromStream(resourceStream);
      return (imageByteArray == null ? null : new ImageIcon(imageByteArray));
    } catch (Exception e) {
      e.printStackTrace();
      return null;
    }
  }

  public static AudioClip loadAudioClip(String audioClipFile) {
    return Applet.newAudioClip(ResourceLoader.class.getResource(audioClipFile));
  }

  
  public static String loadText(String textFile) {
    try (InputStream resourceStream = ResourceLoader.class.getResourceAsStream(textFile)) {
      final byte[] textByteArray = ByteArrayLoadManager.newInstance().loadFromStream(resourceStream);
      return new String(textByteArray);
    } catch (Exception e) {
      e.printStackTrace();
      return null;
    }
  }
  
  private static final class ByteArrayLoadManager {
    final static int MAX_ARRAY_SIZE = 131072;
    
    private byte[] byteArray;
    private int    totalBytesRead;
    
    public static ByteArrayLoadManager newInstance() {
      return new ByteArrayLoadManager();
    }
    
    public byte[] loadFromStream(final InputStream stream) {
      resetByteArray();
      attemptReadFromStream(stream);
      trimByteArrayToNumberRead();
      return byteArray;
    }
    
    private void resetByteArray() {
      byteArray = new byte[MAX_ARRAY_SIZE];
      totalBytesRead = 0;
    }
    
    private void attemptReadFromStream(final InputStream streamOfBytes) {
      try {
        int amountRead = 0;
        while ((amountRead = streamOfBytes.read(byteArray, totalBytesRead, MAX_ARRAY_SIZE - totalBytesRead)) >= 0) {
          totalBytesRead += amountRead;
        }
      } catch (Exception e) {
        e.printStackTrace();
        resetByteArray();
      }
    }
    
    private void trimByteArrayToNumberRead() {
      byte[] trimmedCopy = new byte[totalBytesRead];
      System.arraycopy(byteArray, 0, trimmedCopy, 0, totalBytesRead);
      byteArray = trimmedCopy;
    }
  }
}
