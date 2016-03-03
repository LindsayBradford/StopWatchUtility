// (c) 2002 - Lindsay Bradford -

package blacksmyth.swing;

import java.text.*;
import java.text.SimpleDateFormat;
import java.util.*;
import javax.swing.*;
import javax.swing.text.*;

public class JFormattedDateField extends JFormattedSelectField {
  protected Date vDateValue;
  protected BoundVerifier pVerifier;

  public JFormattedDateField(String pFormat, int pColumns) {
    super(getDateFormatter(pFormat));
    initialise(pFormat, new Date(), pColumns);
  }

  public JFormattedDateField(String pFormat, Date pDate, int pColumns) {
    super(getDateFormatter(pFormat));
    initialise(pFormat, pDate, pColumns);
  }
  
  private void initialise(String pFormat, Date pDate, int pColumns) {
    assert isDateFormat(pFormat) : "Format String parameter is not a date format";
    
    pVerifier = new BoundVerifier();
    setInputVerifier(pVerifier);
    setValue(pDate);
    setColumns(pColumns);
  }
  
  public Date getDate() {
    return (Date) getValue();
  }

  public void setDate(Date pDate) {
    setValue(pDate);
  }

  
  private static boolean isDateFormat(String pFormat) {
    return true;
  }

  private static DateFormatter getDateFormatter(String pFormat) {
    SimpleDateFormat vDateFormat = new SimpleDateFormat(pFormat);
    vDateFormat.set2DigitYearStart(new Date(0));

    StrictDateFormatter vDateFormatter = new StrictDateFormatter(vDateFormat);

    return vDateFormatter;
  }
  
  
  public void setLowerBound(Date pLowerBound) {
    pVerifier.setLowerBound(pLowerBound);
  }

  public void setUpperBound(Date pUpperBound) {
    pVerifier.setUpperBound(pUpperBound);
  }

  public void setBounds(Date pLowerBound, Date pUpperBound) {
    pVerifier.setBounds(pLowerBound, pUpperBound);
  }

  
  class BoundVerifier extends InputVerifier {
    protected Date vLowerBound;
    protected Date vUpperBound;

    public BoundVerifier() {
      super();
      setBounds(new Date(Long.MIN_VALUE), new Date(Long.MAX_VALUE));
    }

    public BoundVerifier(Date pLowerBound, Date pUpperBound) {
      super();
      setBounds(pLowerBound, pUpperBound);
    }

    public boolean verify(JComponent pComponent) {
      Date vValue;

      assert pComponent instanceof JFormattedDateField;

      JFormattedDateField vField = (JFormattedDateField) pComponent;

      String vValueAsText;
      
      try {
         vValueAsText = vField.getText();
         DateFormatter vDateFormatter = (DateFormatter) vField.getFormatter();
         SimpleDateFormat vDateFormat = (SimpleDateFormat) vDateFormatter.getFormat();
         vValue = vDateFormat.parse(vValueAsText);
       } catch (ParseException npe) {
           return false;
       }
       return verifyBounds(vValue);
    }

    protected boolean verifyBounds(Date pDate) {
      if (pDate.before(vLowerBound)) {
        return false;
      }
      if (pDate.after(vUpperBound)) {
        return false;
      }
      return true;
    }

    public boolean shouldYieldFocus(JComponent pComponent) {
      boolean vValid = verify(pComponent);
      if (vValid == false) {
        JFormattedDateField vField = (JFormattedDateField) pComponent;
        vField.invalidEdit();
      }
      return vValid;
    }

    public void setLowerBound(Date pLowerBound) {
      vLowerBound = pLowerBound;
    }

    public void setUpperBound(Date pUpperBound) {
      vUpperBound = pUpperBound;
    }

    public void setBounds(Date pLowerBound, Date pUpperBound) {
      setLowerBound(pLowerBound);
      setUpperBound(pUpperBound);
    }
  }
}


class StrictDateFormatter extends DateFormatter {
  private static final DateFilter vFilter = new DateFilter();

  public StrictDateFormatter(SimpleDateFormat pFormat) {
    super(pFormat);
  }

  protected DocumentFilter getDocumentFilter() {
    return  vFilter;
  }
}


class DateFilter extends DocumentFilter {

  public void replace(DocumentFilter.FilterBypass pBypass,
                      int pOffset,
                      int pLength,
                      String pString,
                      AttributeSet pAttribs) throws BadLocationException {
    if (isValidText(pString))
      super.replace(pBypass,pOffset,pLength,pString,pAttribs);
  }

  protected boolean isValidText(String pString) {
    return validateText("0123456789/", pString);
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
