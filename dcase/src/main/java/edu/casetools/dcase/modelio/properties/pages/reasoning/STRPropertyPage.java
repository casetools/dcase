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
package edu.casetools.dcase.modelio.properties.pages.reasoning;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.core.runtime.AssertionFailedException;
import org.modelio.api.module.propertiesPage.IModulePropertyTable;
import org.modelio.metamodel.uml.infrastructure.Dependency;
import org.modelio.metamodel.uml.infrastructure.ModelElement;

import edu.casetools.dcase.module.api.DCaseProperties;
import edu.casetools.dcase.module.api.DCaseStereotypes;
import edu.casetools.dcase.module.i18n.I18nMessageService;
import edu.casetools.dcase.module.impl.DCaseModule;
import edu.casetools.dcase.module.impl.DCasePeerModule;
import edu.casetools.rcase.modelio.properties.IPropertyContent;
import edu.casetools.rcase.utils.PropertiesUtils;

public class STRPropertyPage implements IPropertyContent {

    private static final Logger LOGGER = Logger.getLogger(STRPropertyPage.class.getName());

    @Override
    public void changeProperty(ModelElement element, int row, String value) {
	try {
	    switch (row) {
	    case 2:
		PropertiesUtils.getInstance().findAndAddValue(DCaseModule.getInstance(), DCasePeerModule.MODULE_NAME,
			DCaseProperties.PROPERTY_STR_PLATFORM, value, element);
		break;	
	    default:
		break;
	    }
	} catch (AssertionFailedException e) {
	    LOGGER.log(Level.SEVERE, e.getMessage(), e);
	}

    }

    @Override
    public void update(ModelElement element, IModulePropertyTable table) {
	String property;

	// ID
	property = element.getTagValue(DCasePeerModule.MODULE_NAME, DCaseProperties.PROPERTY_STR_ID);
	table.addConsultProperty(I18nMessageService.getString("Ui.State.Property.TagId"), property);
	
	// TagPlatformValue
	updateSTRPlatform((ModelElement) element);
	property = element.getTagValue(DCasePeerModule.MODULE_NAME, DCaseProperties.PROPERTY_STR_PLATFORM);
	table.addConsultProperty(I18nMessageService.getString("Ui.Platform"), property);

    }
    
    public static void updateSTRPlatform(ModelElement element){
    	ContextStatePropertyPage.putTagValue(element, DCaseProperties.PROPERTY_STR_PLATFORM, getSTRPlatformType(element));
    }

	public static String getSTRPlatformType(ModelElement element) {
		ModelElement antecedentGroup = (ModelElement)((Dependency)element).getCompositionOwner();		
		if(antecedentGroup.isStereotyped(DCasePeerModule.MODULE_NAME, DCaseStereotypes.STEREOTYPE_ANTECEDENT_GROUP))
			return AntecedentGroupPropertyPage.getAntecedentGroupPlatformType(antecedentGroup);
		return I18nMessageService.getString("Ui.Platform.Unknown");
	}


}
