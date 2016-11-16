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
import org.modelio.metamodel.uml.infrastructure.ModelElement;

import edu.casetools.dcase.module.api.DCaseProperties;
import edu.casetools.dcase.module.i18n.I18nMessageService;
import edu.casetools.dcase.module.impl.DCasePeerModule;
import edu.casetools.dcase.utils.PropertiesUtils;

public class AbsolutePastOperatorPropertyPage extends PastOperatorPropertyPage {

    private static final Logger logger = Logger.getLogger(AbsolutePastOperatorPropertyPage.class.getName());

    public AbsolutePastOperatorPropertyPage() {
	super(DCaseProperties.PROPERTY_PAST_OPERATOR_ABSOLUTE_STATE_NAME,
		DCaseProperties.PROPERTY_PAST_OPERATOR_ABSOLUTE_STATE_VALUE);
    }

    @Override
    public void changeProperty(ModelElement element, int row, String value) {
	super.changeProperty(element, row, value);
	try {
	    switch (row) {
	    case 4:
		PropertiesUtils.getInstance().findAndAddValue(DCasePeerModule.MODULE_NAME,
			DCaseProperties.PROPERTY_PAST_OPERATOR_LOWBOUND, value, element);
		break;
	    case 5:
		PropertiesUtils.getInstance().findAndAddValue(DCasePeerModule.MODULE_NAME,
			DCaseProperties.PROPERTY_PAST_OPERATOR_UPPBOUND, value, element);
		break;
	    default:
		break;
	    }
	} catch (AssertionFailedException e) {
	    logger.log(Level.SEVERE, e.getMessage(), e);
	}

    }

    @Override
    public void update(ModelElement element, IModulePropertyTable table) {
	super.update(element, table);

	String string = PropertiesUtils.getInstance().getTaggedValue(DCaseProperties.PROPERTY_PAST_OPERATOR_LOWBOUND,
		element);
	table.addProperty(I18nMessageService.getString("Ui.AbsolutePastOperator.Property.TagLowBound"), string);

	string = PropertiesUtils.getInstance().getTaggedValue(DCaseProperties.PROPERTY_PAST_OPERATOR_UPPBOUND, element);
	table.addProperty(I18nMessageService.getString("Ui.AbsolutePastOperator.Property.TagUppBound"), string);

    }

}
