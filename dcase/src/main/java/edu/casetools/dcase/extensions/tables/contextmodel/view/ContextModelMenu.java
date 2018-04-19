/*
 * Copyright 2015 @author Unai Alegre 
 * 
 * This file is part of RCASE (Requirements for Context-Aware Systems Engineering), a module 
 * of Modelio that aids the requirements elicitation stage of a Context-Aware System (C-AS). 
 * 
 * RCASE is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * RCASE is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with RCASE.  If not, see <http://www.gnu.org/licenses/>.
 * 
 */
package edu.casetools.dcase.extensions.tables.contextmodel.view;

import java.awt.FlowLayout;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

import edu.casetools.dcase.extensions.tables.contextmodel.ContextModelTable;
import edu.casetools.dcase.extensions.tables.contextmodel.control.ComboBoxListener;
import edu.casetools.dcase.extensions.tables.contextmodel.control.ContextModelMenuListener;
import edu.casetools.dcase.module.i18n.I18nMessageService;

// TODO: Auto-generated Javadoc
/**
 * The Class MatrixMenu.
 */
public class ContextModelMenu extends JPanel {

    private static final long serialVersionUID = 1478577245641896113L;
    private JComboBox<String> contextDependencyDiagramsComboBox;
    private JLabel linkTypeLabel;
    private String[] buttons = { I18nMessageService.getString("Menu.Matrix.Refresh") };
    private ContextModelTable main;

    /**
     * Instantiates a new matrix menu.
     *
     * @param main
     *            the main
     * @param columnDialog
     *            the column dialog
     */
    public ContextModelMenu(ContextModelTable main) {
	this.main = main;
	this.setLayout(new FlowLayout());
	createLinkType(main);
	createButtons();
    }

    private void createLinkType(ContextModelTable main) {

	linkTypeLabel = new JLabel(I18nMessageService.getString("Menu.ContextModel.Combobox.Label"));
	this.add(linkTypeLabel);
	contextDependencyDiagramsComboBox = new JComboBox<>();
	refresh();
	contextDependencyDiagramsComboBox.addActionListener(new ComboBoxListener(main));
	this.add(contextDependencyDiagramsComboBox);

    }

    private void createButtons() {
	for (int i = 0; i < buttons.length; i++) {
	    this.add(createButton(buttons[i]));
	}

    }

    private JButton createButton(String name) {
	JButton button = new JButton(name);
	button.setFocusable(false);
	button.addActionListener(new ContextModelMenuListener(this.main));
	button.setActionCommand(name);
	return button;
    }

    /**
     * Gets the button names.
     *
     * @return the button names
     */
    public String[] getButtonNames() {
	return buttons;
    }

    /**
     * Refresh.
     *
     * @return the string
     */
    public String refresh() {
	List<String> content = new ArrayList<>();
	//Collection<CommunicationInteraction> communicationInteractions;
	content.add(I18nMessageService.getString("Menu.ContextModel.Combobox.All"));
	//communicationInteractions = TableUtils.getInstance().getCommunicationInteractions(DCasePeerModule.MODULE_NAME);
//	if (null != communicationInteractions) {
//	    for (MObject element : communicationInteractions) {
//		content.add(element.getName());
//	    }
//	}
	return refreshComboBox(content);

    }

    private String refreshComboBox(List<String> content) {
	contextDependencyDiagramsComboBox.setModel(new DefaultComboBoxModel<String>(new Vector<String>(content)));
	if (contextDependencyDiagramsComboBox.getItemCount() > 0) {
	    contextDependencyDiagramsComboBox.setSelectedItem(0);
	    return (String) contextDependencyDiagramsComboBox.getSelectedItem();
	}

	return "";
    }

    /**
     * Gets the combo box.
     *
     * @return the combo box
     */
    public JComboBox<String> getComboBox() {
	return contextDependencyDiagramsComboBox;
    }

}
