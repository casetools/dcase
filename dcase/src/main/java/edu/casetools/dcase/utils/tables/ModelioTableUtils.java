/*
 * Copyright 2015 @author Unai Alegre 
 * 
 * This file is part of R-CASE (Requirements for Context-Aware Systems Engineering), a module 
 * of Modelio that aids the requirements elicitation phase of a Context-Aware System (C-AS). 
 * 
 * R-CASE is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * R-CASE is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Modelio.  If not, see <http://www.gnu.org/licenses/>.
 * 
 */
package edu.casetools.dcase.utils.tables;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JLabel;

import org.modelio.metamodel.uml.infrastructure.ModelElement;
import org.modelio.metamodel.uml.infrastructure.Stereotype;
import org.modelio.vcore.smkernel.mapi.MObject;

import edu.casetools.dcase.module.api.DCaseStereotypes;
import edu.casetools.dcase.module.i18n.I18nMessageService;
import edu.casetools.dcase.module.impl.DCasePeerModule;
import edu.casetools.dcase.utils.ModelioUtils;
import edu.casetools.dcase.utils.ResourcesManager;

/**
 * The Class ModelioTableUtils .
 */
public class ModelioTableUtils {

    private static ModelioTableUtils instance = null;

    /**
     * Gets the single instance of ModelioTableUtils.
     *
     * @return single instance of ModelioTableUtils
     */
    public static ModelioTableUtils getInstance() {
	if (instance == null) {
	    instance = new ModelioTableUtils();
	}
	return instance;
    }

    /**
     * Gets the possible values.
     *
     * @return the possible values
     */
    public List<String> getResponsibilityPossibleValues() {
	String[] values = new String[] {
		I18nMessageService.getString("Ui.ContextInformationMessage.Property.TagResponsibility.Pull"),
		I18nMessageService.getString("Ui.ContextInformationMessage.Property.TagResponsibility.Push") };
	return new ArrayList<>(Arrays.asList(values));
    }

    public List<String> getRegularityPossibleValues() {
	String[] values = new String[] {
		I18nMessageService.getString("Ui.ContextInformationMessage.Property.TagRegularity.Regular"),
		I18nMessageService.getString("Ui.ContextInformationMessage.Property.TagRegularity.Irregular") };
	return new ArrayList<>(Arrays.asList(values));
    }

    public List<String> getSynchronicityPossibleValues() {
	String[] values = new String[] {
		I18nMessageService.getString("Ui.ContextInformationMessage.Property.TagSynchronicity.Synchronous"),
		I18nMessageService.getString("Ui.ContextInformationMessage.Property.TagSynchronicity.Asynchronous") };
	return new ArrayList<>(Arrays.asList(values));
    }

    /**
     * Creates the j label.
     *
     * @param elementName
     *            the element name
     * @return the j label
     */
    public JLabel createJLabel(String elementName) {
	Stereotype stereotype;
	ImageIcon icon;
	JLabel label = new JLabel();
	label.setText(elementName);
	label.setOpaque(true);

	stereotype = ModelioUtils.getInstance().getStereotypeOfElementByName(elementName);
	if (stereotype != null) {
	    icon = ResourcesManager.getInstance().createImageIcon(stereotype.getIcon());
	    label.setIcon(icon);
	}

	return label;
    }

    public List<MObject> getMessagesFromComInteraction(List<MObject> list, ModelElement element) {
	List<MObject> auxiliarList = list;
	for (MObject child : element.getCompositionChildren()) {
	    if (child instanceof ModelElement) {
		ModelElement modelElement = (ModelElement) child;
		if (!modelElement.getCompositionChildren().isEmpty())
		    auxiliarList = getMessagesFromComInteraction(auxiliarList, modelElement);
		if (modelElement.isStereotyped(DCasePeerModule.MODULE_NAME, DCaseStereotypes.STEREOTYPE_MESSAGE)) {
		    auxiliarList.add(child);
		}
	    }
	}
	return auxiliarList;
    }

}
