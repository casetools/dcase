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
import org.eclipse.jface.dialogs.MessageDialog;
import org.modelio.api.module.propertiesPage.IModulePropertyTable;
import org.modelio.metamodel.factory.ExtensionNotFoundException;
import org.modelio.metamodel.uml.infrastructure.ModelElement;

import edu.casetools.dcase.modelio.properties.IPropertyContent;
import edu.casetools.dcase.module.api.DCaseProperties;
import edu.casetools.dcase.module.api.DCaseStereotypes;
import edu.casetools.dcase.module.i18n.I18nMessageService;
import edu.casetools.dcase.module.impl.DCasePeerModule;
import edu.casetools.dcase.utils.PropertiesUtils;

public class RuleDiagramPropertyPage implements IPropertyContent {

    private static final Logger logger = Logger.getLogger(RuleDiagramPropertyPage.class.getName());

    @Override
    public void changeProperty(ModelElement element, int row, String value) {
	try {
	    switch (row) {
	    case 1:
		changeExecutionTimeProperty(element, value);
		break;
	    case 2:
		element.putTagValue(DCasePeerModule.MODULE_NAME, DCaseProperties.PROPERTY_SPECIFICATION_DIAGRAM, value);
		break;
	    default:
		break;
	    }
	} catch (ExtensionNotFoundException | AssertionFailedException e) {
	    logger.log(Level.SEVERE, e.getMessage(), e);
	}

    }

    private void changeExecutionTimeProperty(ModelElement element, String value) throws ExtensionNotFoundException {
	int executionTime = 0;
	try {
	    executionTime = Integer.parseInt(value);
	    if (executionTime <= 0)
		throw new NumberFormatException();
	    element.putTagValue(DCasePeerModule.MODULE_NAME, DCaseProperties.PROPERTY_MAX_EXECUTION_TIME, value);
	} catch (NumberFormatException e) {
	    MessageDialog.openInformation(null, "Number format exception",
		    "The execution time provided does not match a positive Integer format. Make sure the M Rule Diagram execution time is set to a positive integer number\n\n"
			    + e.getMessage());
	}
    }

    @Override
    public void update(ModelElement element, IModulePropertyTable table) {
	String string = PropertiesUtils.getInstance().getTaggedValue(DCaseProperties.PROPERTY_MAX_EXECUTION_TIME,
		element);
	table.addProperty(I18nMessageService.getString("Ui.RuleDiagram.Property.ExecutionTime"), string);

	string = PropertiesUtils.getInstance().getTaggedValue(DCaseProperties.PROPERTY_SPECIFICATION_DIAGRAM, element);
	table.addProperty(I18nMessageService.getString("Ui.RuleDiagram.Property.SpecificationDiagram"), string,
		PropertiesUtils.getInstance().getAllElements(DCasePeerModule.MODULE_NAME,
			DCaseStereotypes.STEREOTYPE_DIAGRAM_SPECIFICATION, "Ui.None"));

    }

}
