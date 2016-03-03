// (c) 2002 - Lindsay Bradford -

package blacksmyth.swing;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

@SuppressWarnings("serial")
public class JSimpleDialog extends JDialog {

  private JButton vCloseButton;

  public JSimpleDialog(JFrame     pOwner,
                       String     pContents,
                       String     pTitle,
                       boolean    pModal) {

    super(pOwner, pTitle, pModal);
    initialise(pOwner, getScrollPane(pContents));
  }

  public JSimpleDialog(JFrame     pOwner,
                       JComponent pContents,
                       String     pTitle,
                       boolean    pModal) {

    super(pOwner, pTitle, pModal);
    setSize(getPreferredSize()); 
    initialise(pOwner, pContents);
  }

  private JScrollPane getScrollPane(String pText) {
    JLabel vText = new JLabel(pText);
    return bindScrollPane(vText);
  }

  private JScrollPane bindScrollPane(JComponent component) {
    component.setBorder(BorderFactory.createEmptyBorder(11,11,11,11));
    return new JScrollPane(component);
  }

  private void initialise(Container pOwner, JComponent pComponent) {

    assert pComponent != null : "pComponent was null.";

    Container vContentPane = getContentPane();
    pComponent.setEnabled(false);

    JPanel vBottomPanel    = new JPanel();
    GridBagLayout gbl      = new GridBagLayout();
    GridBagConstraints gbc = new GridBagConstraints();

    vBottomPanel.setLayout(gbl);

    gbc.insets       = new Insets(17,0,11,0);
    gbc.gridwidth    = GridBagConstraints.REMAINDER;
    gbc.gridheight   = 1;
    gbc.gridx        = 0;
    gbc.gridy        = 0;
    gbc.weighty      = 1;
    gbc.ipadx        = 12;
    gbc.ipady        = 3;
    gbc.anchor       = GridBagConstraints.CENTER;

    vBottomPanel.add(getCloseButton(),gbc);

    vContentPane.add(Box.createVerticalStrut(11),   BorderLayout.NORTH);
    vContentPane.add(Box.createHorizontalStrut(11), BorderLayout.WEST);
    vContentPane.add(Box.createHorizontalStrut(11), BorderLayout.EAST);
    vContentPane.add(pComponent,  BorderLayout.CENTER);
    vContentPane.add(vBottomPanel, BorderLayout.SOUTH);

    getRootPane().setDefaultButton(getCloseButton());

    pack();

    if (pOwner != null) {
      setLocationRelativeTo(pOwner);
    } else {
      JUtilities.centerWindow(this);
    }
  }

  private JButton getCloseButton() {
    if (vCloseButton != null) {
      return vCloseButton;
    } else { 
      vCloseButton = new JButton("Close");
      vCloseButton.setMnemonic(KeyEvent.VK_C);

      vCloseButton.addActionListener(new ActionListener() {
        public void actionPerformed(ActionEvent pEvent) {
          setVisible(false);
        }
      });
      return vCloseButton;
    }
  }
}
