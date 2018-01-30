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
package edu.casetools.dcase.modelio.properties.pages;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.core.runtime.AssertionFailedException;
import org.modelio.api.module.propertiesPage.IModulePropertyTable;
import org.modelio.metamodel.mmextensions.infrastructure.ExtensionNotFoundException;
import org.modelio.metamodel.uml.infrastructure.ModelElement;

import edu.casetools.dcase.modelio.properties.IPropertyContent;
import edu.casetools.dcase.module.api.DCaseProperties;
import edu.casetools.dcase.module.api.DCaseStereotypes;
import edu.casetools.dcase.module.i18n.I18nMessageService;
import edu.casetools.dcase.module.impl.DCasePeerModule;
import edu.casetools.dcase.utils.PropertiesUtils;

public class GenericContextPropertyPage implements IPropertyContent {

    private static final Logger LOGGER = Logger.getLogger(GenericContextPropertyPage.class.getName());

    // TODO Reduce the complexity of the switch case
    @Override
    public void changeProperty(ModelElement element, int row, String value) {
	try {
	    switch (row) {
	    case 1:
		PropertiesUtils.getInstance().findAndAddValue(DCasePeerModule.MODULE_NAME,
			DCaseProperties.PROPERTY_GENERIC_CONTEXT_TYPE, value, element);
		break;
	    default:
		break;
	    }
	} catch (AssertionFailedException e) {
	    LOGGER.log(Level.SEVERE, e.getMessage(), e);
	}

	updateStereotypes(element, value);
    }

    private void updateStereotypes(ModelElement element, String value) {
	if (value.equals(I18nMessageService.getString("Ui.GenericContext.Property.ContextType.ACLContext"))
		&& (!element.isStereotyped(DCasePeerModule.MODULE_NAME, DCaseStereotypes.STEREOTYPE_ACL_CONTEXT))) {
	    addStereotype(element, DCaseStereotypes.STEREOTYPE_ACL_CONTEXT);
	    element.removeStereotypes(DCasePeerModule.MODULE_NAME, DCaseStereotypes.STEREOTYPE_STATE);
	    // element.removeStereotypes(DCasePeerModule.MODULE_NAME,
	    // DCaseStereotypes.<YOUR CUSTOM STEREOTYPE NAME>);
	} else if ((value.equals(I18nMessageService.getString("Ui.GenericContext.Property.ContextType.MStateContext")))
		&& (!element.isStereotyped(DCasePeerModule.MODULE_NAME, DCaseStereotypes.STEREOTYPE_STATE))) {
	    addStereotype(element, DCaseStereotypes.STEREOTYPE_STATE);
	    element.removeStereotypes(DCasePeerModule.MODULE_NAME, DCaseStereotypes.STEREOTYPE_ACL_CONTEXT);
	    // element.removeStereotypes(DCasePeerModule.MODULE_NAME,
	    // DCaseStereotypes.<YOUR CUSTOM STEREOTYPE NAME>);
	} /*
	   * else if ((value.equals(I18nMessageService.getString(
	   * "Ui.GenericContext.Property.<YOUR CUSTOM STEREOTYPE NAME>"))) &&
	   * (!element.isStereotyped(DCasePeerModule.MODULE_NAME,
	   * DCaseStereotypes.<YOUR CUSTOM STEREOTYPE NAME>))) {
	   * addStereotype(element, DCaseStereotypes.<YOUR CUSTOM STEREOTYPE
	   * NAME>); element.removeStereotypes(DCasePeerModule.MODULE_NAME,
	   * DCaseStereotypes.STEREOTYPE_ACL_CONTEXT);
	   * element.removeStereotypes(DCasePeerModule.MODULE_NAME,
	   * DCaseStereotypes.STEREOTYPE_STATE); }
	   */
    }

    private void addStereotype(ModelElement element, String stereotype) {
	try {
	    element.addStereotype(DCasePeerModule.MODULE_NAME, stereotype);
	} catch (ExtensionNotFoundException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}
    }

    @Override
    public void update(ModelElement element, IModulePropertyTable table) {
	String property;

	// TagContextType
	property = element.getTagValue(DCasePeerModule.MODULE_NAME, DCaseProperties.PROPERTY_GENERIC_CONTEXT_TYPE);
	table.addProperty(I18nMessageService.getString("Ui.GenericContext.Property.ContextType"), property,
		new String[] { I18nMessageService.getString("Ui.GenericContext.Property.ContextType.ACLContext"),
			I18nMessageService.getString("Ui.GenericContext.Property.ContextType.MStateContext") });

    }

}
