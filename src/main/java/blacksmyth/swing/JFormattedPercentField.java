// (c) 2002 - Lindsay Bradford -

package blacksmyth.swing;

public class JFormattedPercentField extends JFormattedNumField {

  public JFormattedPercentField(String pFormat, double pValue, int pColumns) {
    super(pFormat, pValue, pColumns);

    assert isPercentFormat(pFormat) : "Format String parameter is not a percentage format";
  }

  public String getText() {
    String vText = super.getText();
    if (vText.charAt(vText.length() - 1) != '%') {
      vText = vText + '%';
      setText(vText);
    }
    return vText;
  }
}