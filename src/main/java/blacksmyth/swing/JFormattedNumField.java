// (c) 2002 - Lindsay Bradford -

package blacksmyth.swing;

import javax.swing.*;
import java.text.DecimalFormat;
import java.text.*;
import javax.swing.text.*;


public class JFormattedNumField extends JFormattedSelectField {
  protected double vDoubleValue;
  protected BoundVerifier pVerifier;

  public JFormattedNumField(String pFormat, double pValue, int pColumns) {
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

    super ((isPercentFormat(pFormat) ? (AbstractFormatter) new PercentFormatter(new DecimalFormat(pFormat)) :
                                       (AbstractFormatter) new DecimalFormatter(new DecimalFormat(pFormat))) );

    pVerifier = new BoundVerifier();
    setInputVerifier(pVerifier);

    setDouble(pValue);
    setColumns(pColumns);
  }

  public void setDouble(double pValue) {
    setValue(new Double(pValue));
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
       integer or number of decimal palces, the field's text contains the
       actual value the user would expect to be used, not the Double value
       input.
  */
    double vValue;
    try {
      vValue = DoubleParser.toDouble(getText());
    } catch (NullPointerException npe) {
      return 0;
    }
    return vValue;
  }

  public void setLowerBound(double pLowerBound) {
    pVerifier.setLowerBound(pLowerBound);
  }

  public void setUpperBound(double pUpperBound) {
    pVerifier.setUpperBound(pUpperBound);
  }

  public void setBounds(double pLowerBound, double pUpperBound) {
    pVerifier.setBounds(pLowerBound, pUpperBound);
  }

  public static final boolean isPercentFormat(String pFormat) {
    String vString = pFormat.trim();
    if (vString.charAt(vString.length() - 1) == '%') {
      return true;
    }
    return false;
  }

  class BoundVerifier extends InputVerifier {
    protected double vLowerBound;
    protected double vUpperBound;

    public BoundVerifier() {
      super();
      setBounds(Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY);
    }

    public BoundVerifier(double pLowerBound, double pUpperBound) {
      super();
      setBounds(pLowerBound, pUpperBound);
    }

    public boolean verify(JComponent pComponent) {
      // pre: pComponent of type JFormattedNumField
      double vValue;

      assert pComponent instanceof JFormattedNumField;

      JFormattedNumField vField = (JFormattedNumField) pComponent;

      String vValueAsText;

      try {
         vValueAsText = vField.getText();
         vValue = DoubleParser.toDouble(vValueAsText);
       } catch (NullPointerException npe) {
           return false;
       }
       return verifyBounds(vValue);
    }

    protected boolean verifyBounds(double pNumber) {
      if (pNumber < vLowerBound) {
        return false;
      }
      if (pNumber > vUpperBound) {
        return false;
      }
      return true;
    }

    public boolean shouldYieldFocus(JComponent pComponent) {
      boolean vValid = verify(pComponent);
      if (vValid == false) {
        JFormattedNumField vField = (JFormattedNumField) pComponent;
        vField.invalidEdit();
      }
      return vValid;
    }

    public void setLowerBound(double pLowerBound) {
      vLowerBound = pLowerBound;
    }

    public void setUpperBound(double pUpperBound) {
      vUpperBound = pUpperBound;
    }

    public void setBounds(double pLowerBound, double pUpperBound) {
      setLowerBound(pLowerBound);
      setUpperBound(pUpperBound);
    }
  }
}


class DoubleParser {
/*
  This is a down and dirty hack around DecimalFormat to correctly parse
  perentage strings to their double equivalent + the standard parsing for
  double strings that typically works very well. Left to its own devices
  DecimalFormat.parse() ignores the percentage sign, effectively
  multiplying the number returned by 100.
*/

  public static double toDouble(String pText) throws NullPointerException {
    String vText = pText;
    vText = vText.trim();

    if (vText.length() == 0) {
      throw new NullPointerException();
    }

    boolean isPercentString = false;

    if (vText.charAt(vText.length() - 1) == '%') {
      isPercentString = true;
      vText = vText.replace('%',' ');
      vText = vText.trim();
    }

    DecimalFormat vFormat = new DecimalFormat();

    Number vValueObj = vFormat.parse(vText, new ParsePosition(0));

    double vValue = vValueObj.doubleValue();

    if (isPercentString == true) {
      vValue = vValue / 100;
    }
    return vValue;
  }
}

class DecimalFormatter extends NumberFormatter {
  private DecimalFilter vFilter;

  public DecimalFormatter(DecimalFormat pFormat) {
    super(pFormat);
    vFilter = new DecimalFilter();
  }

  protected DocumentFilter getDocumentFilter() {
    return  vFilter;
  }
}

class DecimalFilter extends DocumentFilter {

  public void replace(DocumentFilter.FilterBypass pBypass,
                      int pOffset,
                      int pLength,
                      String pString,
                      AttributeSet pAttribs) throws BadLocationException {
    if (isValidText(pString))
      super.replace(pBypass,pOffset,pLength,pString,pAttribs);
  }

  protected boolean isValidText(String pString) {
    return validateText("-0123456789,.", pString);
  }

  protected boolean validateText(String pValidChars, String pString) {
    // separated into another method so I can switch in a new one for differing
    // valid character sets.
    for (int i = 0; i < pString.length(); i++) {
      if (pValidChars.indexOf(pString.charAt(i)) == -1) {
        return false;
      }
    }
    return true;
  }
}

class PercentFormatter extends NumberFormatter {
  private PercentFilter vFilter;

  public PercentFormatter(DecimalFormat pFormat) {
    super(pFormat);
    vFilter = new PercentFilter();
  }

  protected DocumentFilter getDocumentFilter() {
    return  vFilter;
  }
}

class PercentFilter extends DecimalFilter {
  protected boolean isValidText(String pString) {
    return validateText("-0123456789,.%", pString);
  }
}