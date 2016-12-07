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
import org.modelio.metamodel.factory.ExtensionNotFoundException;
import org.modelio.metamodel.uml.infrastructure.ModelElement;

import edu.casetools.dcase.modelio.properties.IPropertyContent;
import edu.casetools.dcase.module.api.DCaseProperties;
import edu.casetools.dcase.module.api.DCaseStereotypes;
import edu.casetools.dcase.module.i18n.I18nMessageService;
import edu.casetools.dcase.module.impl.DCasePeerModule;
import edu.casetools.dcase.utils.PropertiesUtils;

public class TestCasePropertyPage implements IPropertyContent {

    private static final Logger logger = Logger.getLogger(TestCasePropertyPage.class.getName());

    @Override
    public void changeProperty(ModelElement element, int row, String value) {
	try {
	    switch (row) {
	    case 2:
		element.putTagValue(DCasePeerModule.MODULE_NAME, DCaseProperties.TEST_CASE_MESSAGE_STATE_NAME, value);
		break;
	    case 3:
		element.putTagValue(DCasePeerModule.MODULE_NAME, DCaseProperties.TEST_CASE_MESSAGE_STATE_VALUE, value);
		break;
	    case 4:
		element.putTagValue(DCasePeerModule.MODULE_NAME, DCaseProperties.TEST_CASE_MESSAGE_TIME, value);
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

	// Id
	property = PropertiesUtils.getInstance().getTaggedValue(DCaseProperties.TEST_CASE_MESSAGE_ID, element);
	table.addProperty(I18nMessageService.getString("Ui.TestCase.Message.Property.TagId"), property);

	// State Name
	property = element.getTagValue(DCasePeerModule.MODULE_NAME, DCaseProperties.TEST_CASE_MESSAGE_STATE_NAME);
	table.addProperty(I18nMessageService.getString("Ui.TestCase.Message..Property.TagStateName"), property,
		PropertiesUtils.getInstance().getAllElements(DCasePeerModule.MODULE_NAME, DCaseStereotypes.STATE,
			"Ui.None"));

	// State Value
	property = element.getTagValue(DCasePeerModule.MODULE_NAME, DCaseProperties.TEST_CASE_MESSAGE_STATE_VALUE);
	table.addProperty(I18nMessageService.getString("Ui.TestCase.Message..Property.TagStateValue"), property,
		new String[] { I18nMessageService.getString("Ui.TestCase.Message..Property.TagStateValue.False"),
			I18nMessageService.getString("Ui.TestCase.Message..Property.TagStateValue.True") });

	// Time of the event
	property = element.getTagValue(DCasePeerModule.MODULE_NAME, DCaseProperties.TEST_CASE_MESSAGE_TIME);
	table.addProperty(I18nMessageService.getString("Ui.TestCase.Message..Property.TagTime"), property);
    }

}
