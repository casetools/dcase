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

public class StationarySensorPropertyPage implements IPropertyContent {

    private static final Logger LOGGER = Logger.getLogger(StationarySensorPropertyPage.class.getName());

    // TODO Reduce the complexity of the switch case
    @Override
    public void changeProperty(ModelElement element, int row, String value) {
	try {
	    switch (row-1) {
	    case 1:
		PropertiesUtils.getInstance().findAndAddValue(DCaseModule.getInstance(), DCasePeerModule.MODULE_NAME,
			DCaseProperties.PROPERTY_STATIONARY_SENSOR_VERA_ID, value, element);
		break;
	    case 2:
		PropertiesUtils.getInstance().findAndAddValue(DCaseModule.getInstance(), DCasePeerModule.MODULE_NAME,
			DCaseProperties.PROPERTY_STATIONARY_SENSOR_MODEL_NAME, value, element);
		break;
	    case 3:
		PropertiesUtils.getInstance().findAndAddValue(DCaseModule.getInstance(), DCasePeerModule.MODULE_NAME,
			DCaseProperties.PROPERTY_STATIONARY_SENSOR_PHYSICAL_LOCATION, value, element);
		break;
	    case 4:
		PropertiesUtils.getInstance().findAndAddValue(DCaseModule.getInstance(), DCasePeerModule.MODULE_NAME,
			DCaseProperties.PROPERTY_STATIONARY_SENSOR_MIN_VALUE, value, element);
		break;
	    case 5:
			element.putTagValue(DCasePeerModule.MODULE_NAME, DCaseProperties.PROPERTY_STATIONARY_SENSOR_MAX_VALUE,
					value);
		break;
	    case 6:
		element.putTagValue(DCasePeerModule.MODULE_NAME, DCaseProperties.PROPERTY_STATIONARY_SENSOR_IS_BOOLEAN,
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
	
		String string = PropertiesUtils.getInstance().getTaggedValue(DCaseProperties.PROPERTY_STATIONARY_SENSOR_VERA_ID, element);
		table.addProperty(I18nMessageService.getString("Ui.StationarySensorPropertyPage.Property.VeraId"), string);

		string = PropertiesUtils.getInstance().getTaggedValue(DCaseProperties.PROPERTY_STATIONARY_SENSOR_MODEL_NAME, element);
		table.addProperty(I18nMessageService.getString("Ui.PreferenceSensorPropertyPage.Property.ModelName"), string);
		
		string = PropertiesUtils.getInstance().getTaggedValue(DCaseProperties.PROPERTY_STATIONARY_SENSOR_PHYSICAL_LOCATION, element);
		table.addProperty(I18nMessageService.getString("Ui.PreferenceSensorPropertyPage.Property.PhysicalLocation"), string);
		
		string = PropertiesUtils.getInstance().getTaggedValue(DCaseProperties.PROPERTY_STATIONARY_SENSOR_MIN_VALUE, element);
		table.addProperty(I18nMessageService.getString("Ui.PreferenceSensorPropertyPage.Property.MinValue"), string);
		
		string = PropertiesUtils.getInstance().getTaggedValue(DCaseProperties.PROPERTY_STATIONARY_SENSOR_MAX_VALUE, element);
		table.addProperty(I18nMessageService.getString("Ui.PreferenceSensorPropertyPage.Property.MaxValue"), string);

	
		property = element.getTagValue(DCasePeerModule.MODULE_NAME, DCaseProperties.PROPERTY_STATIONARY_SENSOR_IS_BOOLEAN);
		table.addProperty(I18nMessageService.getString("Ui.PreferenceSensorPropertyPage.Property.IsBoolean"),
			property,
			new String[] {
				I18nMessageService.getString("Ui.PreferenceSensorPropertyPage.Property.IsBoolean.True"),
				I18nMessageService.getString("Ui.PreferenceSensorPropertyPage.Property.IsBoolean.False") });

    }

}
