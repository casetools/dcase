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
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Modelio. If not, see <http://www.gnu.org/licenses/>.
 * 
 */
package edu.casetools.dcase.modelio.properties.pages.acqmod;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.core.runtime.AssertionFailedException;
import org.modelio.api.module.propertiesPage.IModulePropertyTable;
import org.modelio.metamodel.mmextensions.infrastructure.ExtensionNotFoundException;
import org.modelio.metamodel.uml.infrastructure.ModelElement;

import edu.casetools.dcase.module.api.DCaseProperties;
import edu.casetools.dcase.module.i18n.I18nMessageService;
import edu.casetools.dcase.module.impl.DCaseModule;
import edu.casetools.dcase.module.impl.DCasePeerModule;
import edu.casetools.rcase.modelio.properties.IPropertyContent;
import edu.casetools.rcase.utils.PropertiesUtils;

public class SensorPropertyPage implements IPropertyContent {

    private static final Logger LOGGER = Logger.getLogger(SensorPropertyPage.class.getName());

    // TODO Reduce the complexity of the switch case
    @Override
    public void changeProperty(ModelElement element, int row, String value) {
	try {
	    switch (row) {
	    case 1:
		PropertiesUtils.getInstance().findAndAddValue(DCaseModule.getInstance(), DCasePeerModule.MODULE_NAME,
			DCaseProperties.PROPERTY_SENSOR_ID, value, element);
		break;
	    case 2:
		element.setName(value);
		break;
	    case 3:
		element.putTagValue(DCasePeerModule.MODULE_NAME, DCaseProperties.PROPERTY_SENSOR_VALUE_TYPE,
			value);
		break;
	    case 4:
		element.putTagValue(DCasePeerModule.MODULE_NAME, DCaseProperties.PROPERTY_SENSOR_TYPE,
			value);
		break;
	    case 5:
		element.putTagValue(DCasePeerModule.MODULE_NAME, DCaseProperties.PROPERTY_SENSOR_RESPONSIBILITY,
			value);
		break;
	    case 6:
		element.putTagValue(DCasePeerModule.MODULE_NAME, DCaseProperties.PROPERTY_SENSOR_REGULARITY,
			value);
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

		// TagId
		String string = PropertiesUtils.getInstance().getTaggedValue(DCaseProperties.PROPERTY_SENSOR_ID, element);
		table.addProperty(I18nMessageService.getString("Ui.ACLContext.Property.TagId"), string);
	
		// Name
		table.addProperty(DCaseProperties.PROPERTY_NAME, element.getName());
	
		// TagDataType
		property = element.getTagValue(DCasePeerModule.MODULE_NAME, DCaseProperties.PROPERTY_SENSOR_VALUE_TYPE);
		table.addProperty(I18nMessageService.getString("Ui.SensorPropertyPage.Property.TagValueType"),
			property,
			new String[] {
				I18nMessageService.getString("Ui.SensorPropertyPage.Property.TagValueType.Boolean"),
				I18nMessageService.getString("Ui.SensorPropertyPage.Property.TagValueType.Byte"),
				I18nMessageService.getString("Ui.SensorPropertyPage.Property.TagValueType.Char"),
				I18nMessageService.getString("Ui.SensorPropertyPage.Property.TagValueType.Short"),
				I18nMessageService.getString("Ui.SensorPropertyPage.Property.TagValueType.Integer"),
				I18nMessageService.getString("Ui.SensorPropertyPage.Property.TagValueType.Long"),
				I18nMessageService.getString("Ui.SensorPropertyPage.Property.TagValueType.Float"),
				I18nMessageService.getString("Ui.SensorPropertyPage.Property.TagValueType.Double"),
				I18nMessageService.getString("Ui.SensorPropertyPage.Property.TagValueType.String") });

		property = element.getTagValue(DCasePeerModule.MODULE_NAME, DCaseProperties.PROPERTY_SENSOR_TYPE);
		table.addProperty(I18nMessageService.getString("Ui.SensorPropertyPage.Property.TagType"),
			property,
			new String[] {
				I18nMessageService.getString("Ui.SensorPropertyPage.Property.TagType.Physical"),
				I18nMessageService.getString("Ui.SensorPropertyPage.Property.TagType.Virtual"),
				I18nMessageService.getString("Ui.SensorPropertyPage.Property.TagType.Logical") });
		

		property = element.getTagValue(DCasePeerModule.MODULE_NAME, DCaseProperties.PROPERTY_SENSOR_RESPONSIBILITY);
		table.addProperty(I18nMessageService.getString("Ui.SensorPropertyPage.Property.TagResponsibility"),
			property,
			new String[] {
				I18nMessageService.getString("Ui.SensorPropertyPage.Property.TagResponsibility.Pull"),
				I18nMessageService.getString("Ui.SensorPropertyPage.Property.TagResponsibility.Push")});
		
		property = element.getTagValue(DCasePeerModule.MODULE_NAME, DCaseProperties.PROPERTY_SENSOR_REGULARITY);
		table.addProperty(I18nMessageService.getString("Ui.SensorPropertyPage.Property.TagRegularity"),
			property,
			new String[] {
				I18nMessageService.getString("Ui.SensorPropertyPage.Property.TagRegularity.Instant"),
				I18nMessageService.getString("Ui.SensorPropertyPage.Property.TagRegularity.Interval")});

    }

}
