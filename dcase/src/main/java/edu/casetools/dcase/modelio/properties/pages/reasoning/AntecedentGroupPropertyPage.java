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

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.core.runtime.AssertionFailedException;
import org.modelio.api.module.propertiesPage.IModulePropertyTable;
import org.modelio.metamodel.uml.infrastructure.ModelElement;
import org.modelio.vcore.smkernel.mapi.MObject;

import edu.casetools.dcase.module.api.DCaseProperties;
import edu.casetools.dcase.module.api.DCaseStereotypes;
import edu.casetools.dcase.module.i18n.I18nMessageService;
import edu.casetools.dcase.module.impl.DCaseModule;
import edu.casetools.dcase.module.impl.DCasePeerModule;
import edu.casetools.rcase.modelio.properties.IPropertyContent;
import edu.casetools.rcase.utils.PropertiesUtils;

public class AntecedentGroupPropertyPage implements IPropertyContent {

    private static final Logger LOGGER = Logger.getLogger(AntecedentGroupPropertyPage.class.getName());

    @Override
    public void changeProperty(ModelElement element, int row, String value) {
	try {
	    switch (row) {
	    case 1:
	    	element.setName(value);
	    	break;
	    case 2:
		PropertiesUtils.getInstance().findAndAddValue(DCaseModule.getInstance(), DCasePeerModule.MODULE_NAME,
			DCaseProperties.PROPERTY_ANTECEDENT_GROUP_PLATFORM, value, element);
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

	// Name
	table.addProperty(DCaseProperties.PROPERTY_NAME, element.getName());
	
	// TagPlatformValue
	updateAntecedentGroupPlatform((ModelElement) element);
	property = element.getTagValue(DCasePeerModule.MODULE_NAME, DCaseProperties.PROPERTY_ANTECEDENT_GROUP_PLATFORM);
	table.addConsultProperty(I18nMessageService.getString("Ui.Platform"), property);

    }
    
    public static void updateAntecedentGroupPlatform(ModelElement element){
    	ContextStatePropertyPage.putTagValue(element, DCaseProperties.PROPERTY_ANTECEDENT_GROUP_PLATFORM, getAntecedentGroupPlatformType(element));
    }

	public static String getAntecedentGroupPlatformType(ModelElement element) {
		List<String> results = new ArrayList<String>();
		for(MObject child : element.getCompositionChildren()){
			if(((ModelElement)child).isStereotyped(DCasePeerModule.MODULE_NAME, DCaseStereotypes.STEREOTYPE_ANTECEDENT)||
					((ModelElement)child).isStereotyped(DCasePeerModule.MODULE_NAME, DCaseStereotypes.STEREOTYPE_PAST_OPERATOR)){
				results.add(AntecedentPropertyPage.getAntecedentPlatformType((ModelElement)child));
			}
		}
		
		return checkResults(results);
	}

	private static String checkResults(List<String> results) {
		String reference = "";
		boolean isConsistent = true;
		if(!results.isEmpty())
			reference = results.get(0);
		if(!reference.equals(I18nMessageService.getString("Ui.Platform.Unknown"))){
			for(String result : results){
				if(!result.equals(reference))
					isConsistent = false;
			}
		} 
		
		if(isConsistent) return reference;
		
		return I18nMessageService.getString("Ui.Platform.Unknown");
	}

}
