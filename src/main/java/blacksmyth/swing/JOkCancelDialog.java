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

import java.awt.Color;
import java.awt.Container;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.KeyEvent;
import java.util.Vector;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;


@SuppressWarnings("serial")
abstract public class JOkCancelDialog extends JDialog {

  private JButton okButton;
  private JButton cancelButton;
  private JPanel  buttonPanel;

  public JOkCancelDialog(JFrame frame, String title) {
    super(frame, title, true);
    setLocationRelativeTo(frame);
    setResizable(false);
  }

  public void setContent(JComponent component) {
    addComponent(component);
    pack();
  }
  
  @Override
  public void setForeground(Color color) {
    if (this.okButton != null) {
      getContentPane().setForeground(color);
      this.buttonPanel.setForeground(color);
      this.okButton.setForeground(color);
      this.cancelButton.setForeground(color);
    }
    super.setForeground(color);
  }

  @Override
  public void setBackground(Color color) {
    if (this.okButton != null) {
      getContentPane().setBackground(color);
      this.buttonPanel.setBackground(color);
      this.okButton.setBackground(color);
      this.cancelButton.setBackground(color);
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
    this.buttonPanel = getButtonPanel();
    contentPane.add(this.buttonPanel, gbc);
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

    getRootPane().setDefaultButton(this.okButton);

    Vector<JComponent> buttons = new Vector<JComponent>();

    buttons.add(this.okButton);
    buttons.add(this.cancelButton);

    JUtilities.equalizeComponentSizes(buttons);

    buttons = null;

    return buttonPanel;
  }

  private JButton getCancelButton() {
    this.cancelButton = new JButton("Cancel");
    this.cancelButton.setMnemonic(KeyEvent.VK_C);
    this.cancelButton.addActionListener(
        (actionEvent) -> doCancelAction()
    );
    return this.cancelButton;
  }

  private JButton getOkButton() {
    this.okButton = new JButton("Ok");
    this.okButton.setMnemonic(KeyEvent.VK_O);
    this.okButton.addActionListener(
        (actionEvent) -> doOkAction()
    );
    return this.okButton;
  }
}
