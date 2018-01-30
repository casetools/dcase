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

import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.core.runtime.AssertionFailedException;
import org.modelio.api.module.propertiesPage.IModulePropertyTable;
import org.modelio.metamodel.mmextensions.infrastructure.ExtensionNotFoundException;
import org.modelio.metamodel.uml.infrastructure.ModelElement;

import edu.casetools.dcase.modelio.properties.IPropertyContent;
import edu.casetools.dcase.module.api.DCaseStereotypes;
import edu.casetools.dcase.module.i18n.I18nMessageService;
import edu.casetools.dcase.module.impl.DCasePeerModule;
import edu.casetools.dcase.utils.PropertiesUtils;

public class AntecedentPropertyPage implements IPropertyContent {

    private static final Logger LOGGER = Logger.getLogger(AntecedentPropertyPage.class.getName());

    private final String propertyName;
    private final String propertyValue;

    public AntecedentPropertyPage(final String propertyName, final String propertyValue) {
	this.propertyName = propertyName;
	this.propertyValue = propertyValue;
    }

    @Override
    public void changeProperty(ModelElement element, int row, String value) {
	try {
	    switch (row) {
	    case 1:
		PropertiesUtils.getInstance().findAndAddValue(DCasePeerModule.MODULE_NAME, propertyName, value,
			element);
		break;
	    case 2:
		element.putTagValue(DCasePeerModule.MODULE_NAME, propertyValue, value);
		break;
	    default:
		break;
	    }
	} catch (ExtensionNotFoundException | AssertionFailedException e) {
	    LOGGER.log(Level.SEVERE, e.getMessage(), e);
	}

    }

    @Override
    public void update(ModelElement element, IModulePropertyTable table) {
	String property;

	// TagStateName
	property = element.getTagValue(DCasePeerModule.MODULE_NAME, propertyName);
	table.addProperty(I18nMessageService.getString("Ui.Antecedent.Property.TagStateName"), property,
		PropertiesUtils.getInstance().getAllElements(DCasePeerModule.MODULE_NAME,
			DCaseStereotypes.STEREOTYPE_STATE, "Ui.None"));

	// TagStateValue
	property = element.getTagValue(DCasePeerModule.MODULE_NAME, propertyValue);
	table.addProperty(I18nMessageService.getString("Ui.Antecedent.Property.TagStateValue"), property,
		new String[] { I18nMessageService.getString("Ui.Antecedent.Property.TagStateValue.True"),
			I18nMessageService.getString("Ui.Antecedent.Property.TagStateValue.False") });

    }

}
