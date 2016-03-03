package blacksmyth.swing;

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;

@SuppressWarnings("serial")
abstract public class JOkCancelDialog extends JDialog {

  private JButton okButton;
  private JButton cancelButton;
  private JPanel  buttonPanel;

  public JOkCancelDialog(JFrame frame, String title, boolean modal) {
    super(frame, title, true);
    setLocationRelativeTo(frame);
    setResizable(false);
  }

  public void setContent(JComponent component) {
    addComponent(component);
    pack();
  }

  public void setForeground(Color color) {
    if (okButton != null) {
      getContentPane().setForeground(color);
      buttonPanel.setForeground(color);
      okButton.setForeground(color);
      cancelButton.setForeground(color);
    }
    super.setForeground(color);
  }

  public void setBackground(Color color) {
    if (okButton != null) {
      getContentPane().setBackground(color);
      buttonPanel.setBackground(color);
      okButton.setBackground(color);
      cancelButton.setBackground(color);
    }
    super.setBackground(color);
  }

  private void addComponent(JComponent component) {

    Container contentPane = getContentPane();

    GridBagLayout      gbl = new GridBagLayout();
    GridBagConstraints gbc = new GridBagConstraints();

    contentPane.setLayout(gbl);

    gbc.insets = new Insets(11,11,0,11);

    gbc.gridwidth    = GridBagConstraints.REMAINDER;
    gbc.gridheight   = 1;
    gbc.gridx        = 0;
    gbc.gridy        = 0;
    gbc.weightx      = 0;
    gbc.anchor       = GridBagConstraints.CENTER;
    contentPane.add(component, gbc);

    gbc.insets = new Insets(17,12,11,11);

    gbc.gridy++;
    gbc.weightx      = 1;
    gbc.anchor       = GridBagConstraints.EAST;
    buttonPanel = getButtonPanel();
    contentPane.add(buttonPanel, gbc);
  }

  protected void doOkAction() {
    setVisible(false);
  }

  protected void doCancelAction() {
    setVisible(false);
  }

  private JPanel getButtonPanel() {

    JPanel buttonPanel = new JPanel();

    GridBagLayout      gbl = new GridBagLayout();
    GridBagConstraints gbc = new GridBagConstraints();

    buttonPanel.setLayout(gbl);

    gbc.insets = new Insets(0,5,0,0);

    gbc.gridwidth    = 1;
    gbc.gridheight   = 1;
    gbc.gridx        = 0;
    gbc.gridy        = 0;
    gbc.ipadx        = 6;
    gbc.weightx      = 1; 
    gbc.gridx        = 0;
    gbc.anchor       = GridBagConstraints.CENTER;

    buttonPanel.add(getOkButton(), gbc);

    gbc.gridx        = 1;

    buttonPanel.add(getCancelButton(), gbc);

    getRootPane().setDefaultButton(okButton);

    Vector<JComponent> buttons = new Vector<JComponent>();

    buttons.add(okButton);
    buttons.add(cancelButton);

    JUtilities.equalizeComponentSizes(buttons);

    buttons = null;

    return buttonPanel;
  }

  private JButton getCancelButton() {
    cancelButton = new JButton("Cancel");
    cancelButton.setMnemonic(KeyEvent.VK_C);
    cancelButton.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent pEvent) {
	doCancelAction();
      }
    });
    return cancelButton;
  }

  private JButton getOkButton() {
    okButton = new JButton("Ok");
    okButton.setMnemonic(KeyEvent.VK_O);
    okButton.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent pEvent) {
	doOkAction();
      }
    });
    return okButton;
  }
}
