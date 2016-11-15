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

import java.util.ArrayList;
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
import edu.casetools.dcase.utils.PropertiesUtils;
import edu.casetools.dcase.utils.tables.TableUtils;

public class AntecedentPropertyPage implements IPropertyContent {

    private static final Logger logger = Logger.getLogger(AntecedentPropertyPage.class.getName());

    // TODO Reduce the complexity of the switch case
    @Override
    public void changeProperty(ModelElement element, int row, String value) {
	try {
	    switch (row) {
	    case 1:
		PropertiesUtils.getInstance().findAndAddValue(DCasePeerModule.MODULE_NAME,
			DCaseProperties.PROPERTY_ANTECEDENT_STATE_NAME, value, element);
		break;
	    case 2:
		element.putTagValue(DCasePeerModule.MODULE_NAME, DCaseProperties.PROPERTY_ANTECEDENT_VALUE, value);
		break;
	    default:
		break;
	    }
	} catch (ExtensionNotFoundException | AssertionFailedException e) {
	    logger.log(Level.SEVERE, e.getMessage(), e);
	}

    }

    @Override
    public void update(ModelElement element, IModulePropertyTable table) {
	String property;

	// TagStateName
	property = element.getTagValue(DCasePeerModule.MODULE_NAME, DCaseProperties.PROPERTY_ANTECEDENT_STATE_NAME);
	table.addProperty(I18nMessageService.getString("Ui.Antecedent.Property.TagStateName"), property,
		getAllStates());

	// TagStateValue
	property = element.getTagValue(DCasePeerModule.MODULE_NAME, DCaseProperties.PROPERTY_ANTECEDENT_VALUE);
	table.addProperty(I18nMessageService.getString("Ui.Antecedent.Property.TagStateValue"), property,
		new String[] { I18nMessageService.getString("Ui.Antecedent.Property.TagStateValue.True"),
			I18nMessageService.getString("Ui.Antecedent.Property.TagStateValue.False") });

    }

    private String[] getAllStates() {

	MObject state;
	ArrayList<MObject> stateList = new ArrayList<>();

	stateList = (ArrayList<MObject>) TableUtils.getInstance().getAllElementsStereotypedAs(stateList,
		DCasePeerModule.MODULE_NAME, DCaseStereotypes.STEREOTYPE_STATE);
	String[] stateNames = new String[stateList.size() + 1];
	stateNames[0] = new String(I18nMessageService.getString("Ui.Antecedent.Property.StateName.None"));
	for (int i = 0; i < stateList.size(); i++) {
	    state = stateList.get(i);
	    stateNames[i + 1] = state.getName();
	}
	return stateNames;
    }

}
