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
package edu.casetools.dcase.modelio.properties.pages.info;

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

public class InfoPropertyPage implements IPropertyContent {

    private static final Logger LOGGER = Logger.getLogger(InfoPropertyPage.class.getName());

    // TODO Reduce the complexity of the switch case
    @Override
    public void changeProperty(ModelElement element, int row, String value) {
	try {
	    switch (row) {
	    case 1:
		PropertiesUtils.getInstance().findAndAddValue(DCaseModule.getInstance(), DCasePeerModule.MODULE_NAME,
			DCaseProperties.PROPERTY_INFO_ID, value, element);
		break;
	    case 2:
		element.setName(value);
		break;
	    case 3:
		element.putTagValue(DCasePeerModule.MODULE_NAME, DCaseProperties.PROPERTY_INFO_DESCRIPTION,
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

		// TagId
		String string = PropertiesUtils.getInstance().getTaggedValue(DCaseProperties.PROPERTY_INFO_ID, element);
		table.addProperty(I18nMessageService.getString("Ui.ACLContext.Property.TagId"), string);
	
		// Name
		table.addProperty(DCaseProperties.PROPERTY_NAME, element.getName());
	
//		// TagContent
//		property = element.getTagValue(DCasePeerModule.MODULE_NAME, DCaseProperties.PROPERTY_INFO_CONTEXT_ATTRIBUTE);
//
//		table.addProperty(I18nMessageService.getString("Ui.Info.ContextAttribute"), property, getAllContextAttributes());
		
		string = PropertiesUtils.getInstance().getTaggedValue(DCaseProperties.PROPERTY_INFO_DESCRIPTION, element);
		table.addProperty(I18nMessageService.getString("Ui.ACLContext.Property.TagDescription"), string);
		
    }
    
//    private String[] getAllContextAttributes(){
//		List<MObject> contextAttributes = new ArrayList<>();
//		List<String>  contextAttributeNames = new ArrayList<>();
//		contextAttributes = TableUtils.getInstance().getAllElementsStereotypedAs(DCaseModule.getInstance(), RCasePeerModule.MODULE_NAME, contextAttributes, RCaseStereotypes.STEREOTYPE_CONTEXT_ATTRIBUTE);
//		for(MObject contextAttribute: contextAttributes){
//			contextAttributeNames.add(contextAttribute.getName());
//		}
//		return contextAttributeNames.toArray(new String[0]);
//    
//    }

}
