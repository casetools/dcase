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
package edu.casetools.dcase.modelio.properties.pages.deployment;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.core.runtime.AssertionFailedException;
import org.modelio.api.module.propertiesPage.IModulePropertyTable;
import org.modelio.metamodel.mmextensions.infrastructure.ExtensionNotFoundException;
import org.modelio.metamodel.uml.infrastructure.ModelElement;

import edu.casetools.dcase.module.api.DCaseProperties;
import edu.casetools.dcase.module.i18n.I18nMessageService;
import edu.casetools.dcase.module.impl.DCasePeerModule;
import edu.casetools.rcase.modelio.properties.IPropertyContent;
import edu.casetools.rcase.utils.PropertiesUtils;

public class MReasonerPropertyPage implements IPropertyContent {

    private static final Logger LOGGER = Logger.getLogger(MReasonerPropertyPage.class.getName());

    // TODO Reduce the complexity of the switch case
    @Override
    public void changeProperty(ModelElement element, int row, String value) {
	try {
	    switch (row) {
		    case 1:
			element.putTagValue(DCasePeerModule.MODULE_NAME, DCaseProperties.PROPERTY_M_REASONER_FIXED_ITERATION_TIME, value);
			break;
		    case 2:
			element.putTagValue(DCasePeerModule.MODULE_NAME, DCaseProperties.PROPERTY_M_REASONER_ITERATION_TIME, value);
			break;
		    case 3:
			element.putTagValue(DCasePeerModule.MODULE_NAME, DCaseProperties.PROPERTY_M_REASONER_HAS_MAX_EXECUTION_TIME, value);
			break;
		    case 4:
			element.putTagValue(DCasePeerModule.MODULE_NAME, DCaseProperties.PROPERTY_M_REASONER_MAX_EXECUTION_TIME, value);
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


	// TagSpecification
	property = PropertiesUtils.getInstance().getTaggedValue(DCaseProperties.PROPERTY_M_REASONER_FIXED_ITERATION_TIME, element);
	table.addProperty(I18nMessageService.getString("Ui.MReasoner.Property.HasFixedIterationTime"),
			property,
			new String[] {
				I18nMessageService.getString("Ui.State.Property.TagIndependent.True"),
				I18nMessageService.getString("Ui.State.Property.TagIndependent.False")});

	property = PropertiesUtils.getInstance().getTaggedValue(DCaseProperties.PROPERTY_M_REASONER_ITERATION_TIME, element);
	table.addProperty(I18nMessageService.getString("Ui.MReasoner.Property.IterationTime"), property);
	
	property = PropertiesUtils.getInstance().getTaggedValue(DCaseProperties.PROPERTY_M_REASONER_HAS_MAX_EXECUTION_TIME, element);
	table.addProperty(I18nMessageService.getString("Ui.MReasoner.Property.HasMaxExecTime"),
			property,
			new String[] {
				I18nMessageService.getString("Ui.State.Property.TagIndependent.True"),
				I18nMessageService.getString("Ui.State.Property.TagIndependent.False")});
	
	property = PropertiesUtils.getInstance().getTaggedValue(DCaseProperties.PROPERTY_M_REASONER_MAX_EXECUTION_TIME, element);
	table.addProperty(I18nMessageService.getString("Ui.MReasoner.Property.MaxExecTime"), property);
	

    }
    

}
