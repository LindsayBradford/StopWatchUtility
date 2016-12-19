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

import java.text.DecimalFormat;
import java.text.ParsePosition;
import javax.swing.JComponent;
import javax.swing.InputVerifier;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;
import javax.swing.text.NumberFormatter;

@SuppressWarnings("serial")
public class JFormattedNumField extends JFormattedSelectField {
  protected double doubleValue;
  protected BoundVerifier verifier;

  public JFormattedNumField(String format, double value, int columns) {
    /*
      replace the DefaultFormatter with our own.  Inside it, we supply a
      home-grown DocumentFilter that only allows keystrokes that could
      go into forming a valid decimal number.
      Had to use inline if validation to switch which formatter to use on
      the super() call as super() has to be the first command.

      Specifying the AbstractFormatter via setAbstractFormatter() later
      instead seems to ignore the formatting in DecimalFormat().
      Bug in 1.4.0 ?
    */

    super (
      isPercentFormat(format) ? (AbstractFormatter) new PercentFormatter(new DecimalFormat(format)) :
                                (AbstractFormatter) new DecimalFormatter(new DecimalFormat(format))
    );

    setInputVerifier(new BoundVerifier());

    setDouble(value);
    setColumns(columns);
  }
  
  public void setDouble(double value) {
    setValue(new Double(value));
  }

  public double getDouble() throws NullPointerException {
  /*
    Some notes: there is a subtle coupling between this and BoundVerifier.
    BoundVerifier supplies an opinion on whether the current text of the
    field can be parsed, and therefore whether focus can leave.  The
    assumption is that the call to getDouble() will only occur at times
    where we know the contents of the field can be validly parsed
    (ie - after BoundVerfiier returns 'happy').

    Two items of interest that influenced this approach:
     * When FocusLost and ActionPerformed Events fire they have not yet
       updated the Double Object containing the underlying value. BUT...
       they should only fire if BoundVerifier is happy.
     * Especially for formats that round the decimal input to a displayed
       integer or number of decimal places, the field's text contains the
       actual value the user would expect to be used, not the Double value
       input.
  */
    double value;
    try {
      value = DoubleParser.toDouble(getText());
    } catch (IllegalArgumentException iae) {
      return 0;
    }
    return value;
  }

  public void setLowerBound(double lowerBound) {
    verifier.setLowerBound(lowerBound);
  }

  public void setUpperBound(double upperBound) {
    verifier.setUpperBound(upperBound);
  }

  public void setBounds(double lowerBound, double upperBound) {
    verifier.setBounds(lowerBound, upperBound);
  }

  public static final boolean isPercentFormat(final String format) {
    String trimmedFormat = format.trim();
    if (trimmedFormat.charAt(trimmedFormat.length() - 1) == '%') {
      return true;
    }
    return false;
  }

  class BoundVerifier extends InputVerifier {
    protected double lowerBound;
    protected double upperBound;

    public BoundVerifier() {
      super();
      setBounds(Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY);
    }

    public BoundVerifier(double lowerBound, double upperBound) {
      super();
      setBounds(lowerBound, upperBound);
    }
    
    @Override
    public boolean verify(JComponent component) {
      double value;

      assert component instanceof JFormattedNumField;

      JFormattedNumField field = (JFormattedNumField) component;

      String valueAsText;

      try {
         valueAsText = field.getText();
         value = DoubleParser.toDouble(valueAsText);
       } catch (IllegalArgumentException iae) {
           return false;
       }
       return verifyBounds(value);
    }

    protected boolean verifyBounds(double number) {
      if (number < lowerBound) {
        return false;
      }
      if (number > upperBound) {
        return false;
      }
      return true;
    }

    @Override
    public boolean shouldYieldFocus(JComponent component) {
      boolean isValid = verify(component);
      if (isValid == false) {
        JFormattedNumField field = (JFormattedNumField) component;
        field.invalidEdit();
      }
      return isValid;
    }

    public void setLowerBound(double lowerBound) {
      this.lowerBound = lowerBound;
    }

    public void setUpperBound(double upperBound) {
      this.upperBound = upperBound;
    }

    public void setBounds(double lowerBound, double upperBound) {
      setLowerBound(lowerBound);
      setUpperBound(upperBound);
    }
  }
}


class DoubleParser {
/*
  This is a down and dirty hack around DecimalFormat to correctly parse
  percentage strings to their double equivalent + the standard parsing for
  double strings that typically works very well. Left to its own devices
  DecimalFormat.parse() ignores the percentage sign, effectively
  multiplying the number returned by 100.
*/

  public static double toDouble(String textToConvert) throws IllegalArgumentException {
    
    textToConvert = checkTextHasContentAfterTrimming(textToConvert);
    
    boolean isPercentString = false;

    if (textStartsWithPercentCharacter(textToConvert)) {
      isPercentString = true;
      textToConvert = removePrecentCharacterFromText(textToConvert);
    }

    DecimalFormat format = new DecimalFormat();

    Number valueAsNumber = format.parse(textToConvert, new ParsePosition(0));

    double valueAsDouble = valueAsNumber.doubleValue();

    if (isPercentString == true) {
      valueAsDouble = valueAsDouble / 100;
    }
    return valueAsDouble;
  }
  
  private static String checkTextHasContentAfterTrimming(String textToConvert) throws IllegalArgumentException {
    if (textToConvert == null) {
      throw new IllegalArgumentException();
    }

    textToConvert = textToConvert.trim();

    if (textToConvert.length() == 0) {
      throw new IllegalArgumentException();
    }
    
    return textToConvert;
  }
  
  private static boolean textStartsWithPercentCharacter(String text) {
    return (text.charAt(text.length() - 1) == '%');
  }
  
  private static String removePrecentCharacterFromText(String text) {
    text = text.replace('%',' ');
    text = text.trim();
    return text;
  }
}

@SuppressWarnings("serial")
class DecimalFormatter extends NumberFormatter {
  private DecimalFilter filter;

  public DecimalFormatter(DecimalFormat format) {
    super(format);
    filter = new DecimalFilter();
  }

  @Override
  protected DocumentFilter getDocumentFilter() {
    return filter;
  }
}

class DecimalFilter extends DocumentFilter {

  @Override
  public void replace(DocumentFilter.FilterBypass pBypass,
                      int pOffset,
                      int pLength,
                      String pString,
                      AttributeSet pAttribs) throws BadLocationException {
    if (isValidText(pString))
      super.replace(pBypass,pOffset,pLength,pString,pAttribs);
  }

  @SuppressWarnings("static-method")
  protected boolean isValidText(String text) {
    return validateText("-0123456789,.", text);
  } 

  protected static boolean validateText(String validChars, String text) {
    // separated into another method so I can switch in a new one for differing
    // valid character sets.
    for (int i = 0; i < text.length(); i++) {
      if (validChars.indexOf(text.charAt(i)) == -1) {
        return false;
      }
    }
    return true;
  }
}

@SuppressWarnings("serial")
class PercentFormatter extends NumberFormatter {
  private PercentFilter filter;

  public PercentFormatter(DecimalFormat format) {
    super(format);
    filter = new PercentFilter();
  }

  @Override
  protected DocumentFilter getDocumentFilter() {
    return filter;
  }
}

class PercentFilter extends DecimalFilter {
  @Override
  protected boolean isValidText(String text) {
    return validateText("-0123456789,.%", text);
  }
}