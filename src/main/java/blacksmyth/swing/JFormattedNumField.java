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
  protected double fieldValue;
  protected BoundVerifier boundVerifier;

  public JFormattedNumField(String fieldFormat, double fieldValue, int fieldColumns) {
    /*
      replace the DefaultFormatter with our own.  Inside it, we supply a
      home-grown DocumentFilter that only allows keystrokes that could
      go into forming a valid decimal number.
      Had to use inline if validation to switch which formatter to use on
      the super() call as super() has to be the first command *grumble*.

      Specifying the AbstractFormatter via setAbstractFormatter() later
      instead seems to ignore the formatting in DecimalFormat().
      Bug in 1.4.0 ?
    */

    super(
        isPercentFormat(fieldFormat) ? new PercentFormatter(new DecimalFormat(fieldFormat)):
                                       new DecimalFormatter(new DecimalFormat(fieldFormat))
    );

    boundVerifier = new BoundVerifier();
    setInputVerifier(boundVerifier);

    setDouble(fieldValue);
    setColumns(fieldColumns);
  }

  public void setDouble(double fieldValue) {
    setValue(new Double(fieldValue));
  }

  public double getDouble() {
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
    try {
      return getFieldValueAsDouble();
    } catch (Exception e) {
      return 0;
    }
  }
  
  private double getFieldValueAsDouble() {
    return DoubleAndPercentParser.toDouble(getText());
  }

  public void setLowerBound(double loweBound) {
    boundVerifier.setLowerBound(loweBound);
  }

  public void setUpperBound(double upperBound) {
    boundVerifier.setUpperBound(upperBound);
  }

  public void setBounds(double lowerBound, double upperBound) {
    boundVerifier.setBounds(lowerBound, upperBound);
  }

  public static boolean isPercentFormat(String fieldFormat) {
    String trimmedFieldFormat = fieldFormat.trim();
    if (lastCharOfFieldFormatIsPercentSymbol(trimmedFieldFormat)) {
      return true;
    }
    return false;
  }
  
  private static boolean lastCharOfFieldFormatIsPercentSymbol(String text) {
    return (text.charAt(text.length() - 1) == '%');
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
    public boolean verify(JComponent componentToVerify) {
      assert componentToVerify instanceof JFormattedNumField;

      JFormattedNumField fieldToVerify = (JFormattedNumField) componentToVerify;

      try {
        String fieldValueAsText = fieldToVerify.getText();
        double value = DoubleAndPercentParser.toDouble(fieldValueAsText);
        return verifyBounds(value);
      } catch (IllegalArgumentException e) {
        return false;
      }
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
      boolean componentIsVerified = verify(component);
      if (componentIsVerified == false) {
        JFormattedNumField componentAsField = (JFormattedNumField) component;
        componentAsField.invalidEdit();
      }
      return componentIsVerified;
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


class DoubleAndPercentParser {

  public static double toDouble(String textToParse) {

    String trimmedTextToParse = trimAndCheck(textToParse);
    
    boolean needToHandlePercentInText = lastCharOfFieldFormatIsPercentSymbol(trimmedTextToParse);

    if (needToHandlePercentInText) {
      trimmedTextToParse = trimmedTextToParse.replace('%',' ').trim();
    }

    double parsedValue = tryToParseTextAsDouble(trimmedTextToParse);

    if (needToHandlePercentInText) {
      parsedValue = parsedValue / 100;
    }

    return parsedValue;
  }
  
  private static String trimAndCheck(String textToParse) {
    String trimmedTextToParse = textToParse.trim();

    if (trimmedTextToParse.length() == 0) {
      throw new IllegalArgumentException("String supplied to parser is empty once trimmed.");
    }
    return trimmedTextToParse;
  }
  
  private static double tryToParseTextAsDouble(String textToParse) {
    try {
      DecimalFormat fieldFormat = new DecimalFormat();
      Number parsedValueAsNumber = fieldFormat.parse(textToParse, new ParsePosition(0));
      return parsedValueAsNumber.doubleValue();
    } catch (Exception e) {
      throw new IllegalArgumentException("Failed to parse string [" + textToParse + "] to double.", e);
    }
  }
  
  private static boolean lastCharOfFieldFormatIsPercentSymbol(String text) {
    return (text.charAt(text.length() - 1) == '%');
  }
}

@SuppressWarnings("serial")
class DecimalFormatter extends NumberFormatter {
  private DecimalFilter fieldFilter;

  public DecimalFormatter(DecimalFormat fieldFormat) {
    super(fieldFormat);
    fieldFilter = new DecimalFilter();
  }

  @Override
  protected DocumentFilter getDocumentFilter() {
    return  fieldFilter;
  }
}

class DecimalFilter extends DocumentFilter {

  @Override
  public void replace(DocumentFilter.FilterBypass bypass, int offset, int length, 
                      String text, AttributeSet attributes) throws BadLocationException {
    if (isValidText(text)) {
      super.replace(bypass,offset,length,text,attributes);
    }
  }

  @SuppressWarnings("static-method")
  protected boolean isValidText(String textToValidate) {
    return validateText("-0123456789,.", textToValidate);
  } 

  protected static boolean validateText(String validCharSet, String textToValidate) {
    return textToValidate.chars().noneMatch(
      (c) -> validCharSet.indexOf(c) == -1     
    );
  }
}

@SuppressWarnings("serial")
class PercentFormatter extends NumberFormatter {
  private PercentFilter fieldFilter;

  public PercentFormatter(DecimalFormat fieldFormat) {
    super(fieldFormat);
    fieldFilter = new PercentFilter();
  }

  @Override
  protected DocumentFilter getDocumentFilter() {
    return fieldFilter;
  }
}

class PercentFilter extends DecimalFilter {
  @Override
  protected boolean isValidText(String textToValidate) {
    return validateText("-0123456789,.%", textToValidate);
  }
}