/*
 * Copyright 2015 @author Unai Alegre 
 * 
 * This file is part of DCASE (Design for Context-Aware Systems Engineering), a module 
 * of Modelio that aids the design of a Context-Aware System (C-AS). 
 * 
 * DCASE is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * DCASE is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with DCASE. If not, see <http://www.gnu.org/licenses/>.
 * 
 */
package edu.casetools.dcase.modelio.properties.pages;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.core.runtime.AssertionFailedException;
import org.modelio.api.module.propertiesPage.IModulePropertyTable;
import org.modelio.metamodel.factory.ExtensionNotFoundException;
import org.modelio.metamodel.uml.infrastructure.ModelElement;
import org.modelio.vcore.smkernel.mapi.MObject;

import edu.casetools.dcase.modelio.properties.IPropertyContent;
import edu.casetools.dcase.module.api.DCaseProperties;
import edu.casetools.dcase.module.api.DCaseStereotypes;
import edu.casetools.dcase.module.i18n.I18nMessageService;
import edu.casetools.dcase.module.impl.DCasePeerModule;
import edu.casetools.dcase.utils.ModelioUtils;
import edu.casetools.dcase.utils.PropertiesUtils;

public class StatePropertyPage implements IPropertyContent {

    private static final Logger logger = Logger.getLogger(StatePropertyPage.class.getName());

    @Override
    public void changeProperty(ModelElement element, int row, String value) {
	try {
	    switch (row) {
	    case 1:
		element.putTagValue(DCasePeerModule.MODULE_NAME, DCaseProperties.PROPERTY_STATE_ID, value);
		break;
	    case 2:
		updateValue(element, value);
		element.setName(value);
		break;
	    case 3:
		element.putTagValue(DCasePeerModule.MODULE_NAME, DCaseProperties.PROPERTY_STATE_INDEPENDENT, value);
		break;
	    case 4:
		element.putTagValue(DCasePeerModule.MODULE_NAME, DCaseProperties.PROPERTY_STATE_INITIAL_VALUE, value);
		break;
	    default:
		break;
	    }
	} catch (ExtensionNotFoundException | AssertionFailedException e) {
	    logger.log(Level.SEVERE, e.getMessage(), e);
	}

    }

    private void updateValue(ModelElement element, String value) {
	String currentName = element.getName();
	List<MObject> elements = ModelioUtils.getInstance().getAllElements();
	for (MObject aux : elements) {
	    if (aux instanceof ModelElement) {
		boolean isAntecedent = ((ModelElement) aux).isStereotyped(DCasePeerModule.MODULE_NAME,
			DCaseStereotypes.STEREOTYPE_ANTECEDENT);
		boolean isConsequent = ((ModelElement) aux).isStereotyped(DCasePeerModule.MODULE_NAME,
			DCaseStereotypes.STEREOTYPE_CONSEQUENT);
		boolean isPastOperator = ((ModelElement) aux).isStereotyped(DCasePeerModule.MODULE_NAME,
			DCaseStereotypes.STEREOTYPE_PAST_OPERATOR);

		if (isAntecedent) {
		    updateAntecedent(value, currentName, aux);
		} else if (isConsequent) {
		    updateConsequent(value, currentName, aux);
		} else if (isPastOperator) {
		    updatePastOperator(value, currentName, aux);

		}
	    }
	}

    }

    private void updatePastOperator(String value, String currentName, MObject aux) {
	if (currentName.equals(((ModelElement) aux).getTagValue(DCasePeerModule.MODULE_NAME,
		DCaseProperties.PROPERTY_PAST_OPERATOR_STATE_NAME))) {
	    PropertiesUtils.getInstance().findAndAddValue(DCasePeerModule.MODULE_NAME,
		    DCaseProperties.PROPERTY_PAST_OPERATOR_STATE_NAME, value, (ModelElement) aux);
	}
    }

    private void updateConsequent(String value, String currentName, MObject aux) {
	if (currentName.equals(((ModelElement) aux).getTagValue(DCasePeerModule.MODULE_NAME,
		DCaseProperties.PROPERTY_CONSEQUENT_STATE_NAME))) {
	    PropertiesUtils.getInstance().findAndAddValue(DCasePeerModule.MODULE_NAME,
		    DCaseProperties.PROPERTY_CONSEQUENT_STATE_NAME, value, (ModelElement) aux);
	}
    }

    private void updateAntecedent(String value, String currentName, MObject aux) {
	if (currentName.equals(((ModelElement) aux).getTagValue(DCasePeerModule.MODULE_NAME,
		DCaseProperties.PROPERTY_ANTECEDENT_STATE_NAME))) {
	    PropertiesUtils.getInstance().findAndAddValue(DCasePeerModule.MODULE_NAME,
		    DCaseProperties.PROPERTY_ANTECEDENT_STATE_NAME, value, (ModelElement) aux);
	}
    }

    @Override
    public void update(ModelElement element, IModulePropertyTable table) {
	String property;

	// TagId
	String string = PropertiesUtils.getInstance().getTaggedValue(DCaseProperties.PROPERTY_STATE_ID, element);
	table.addProperty(I18nMessageService.getString("Ui.State.Property.TagId"), string);

	// State Name
	table.addProperty(I18nMessageService.getString("Ui.State.Property.TagName"), element.getName());

	// Independent
	property = element.getTagValue(DCasePeerModule.MODULE_NAME, DCaseProperties.PROPERTY_STATE_INDEPENDENT);
	table.addProperty(I18nMessageService.getString("Ui.State.Property.TagIndependent"), property,
		new String[] { I18nMessageService.getString("Ui.State.Property.TagIndependent.False"),
			I18nMessageService.getString("Ui.State.Property.TagIndependent.True") });

	// Initial Value
	property = element.getTagValue(DCasePeerModule.MODULE_NAME, DCaseProperties.PROPERTY_STATE_INITIAL_VALUE);
	table.addProperty(I18nMessageService.getString("Ui.State.Property.TagInitialValue"), property,
		new String[] { I18nMessageService.getString("Ui.State.Property.TagInitialValue.False"),
			I18nMessageService.getString("Ui.State.Property.TagInitialValue.True") });
    }

}
