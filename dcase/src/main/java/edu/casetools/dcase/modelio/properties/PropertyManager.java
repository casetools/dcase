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
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with DCASE.  If not, see <http://www.gnu.org/licenses/>.
 * 
 */
package edu.casetools.dcase.modelio.properties;

import java.util.List;

import org.modelio.api.modelio.model.IMetamodelExtensions;
import org.modelio.api.module.propertiesPage.IModulePropertyTable;
import org.modelio.metamodel.uml.behavior.communicationModel.CommunicationMessage;
import org.modelio.metamodel.uml.behavior.interactionModel.Interaction;
import org.modelio.metamodel.uml.behavior.interactionModel.Message;
import org.modelio.metamodel.uml.infrastructure.ModelElement;
import org.modelio.metamodel.uml.infrastructure.Stereotype;
import org.modelio.metamodel.uml.statik.Class;

import edu.casetools.dcase.modelio.properties.pages.AbsolutePastOperatorPropertyPage;
import edu.casetools.dcase.modelio.properties.pages.AntecedentPropertyPage;
import edu.casetools.dcase.modelio.properties.pages.ContextInformationMessagePropertyPage;
import edu.casetools.dcase.modelio.properties.pages.ImmediatePastOperatorPropertyPage;
import edu.casetools.dcase.modelio.properties.pages.StatePropertyPage;
import edu.casetools.dcase.modelio.properties.pages.TestCaseMessagePropertyPage;
import edu.casetools.dcase.modelio.properties.pages.TestCasePropertyPage;
import edu.casetools.dcase.module.api.DCaseProperties;
import edu.casetools.dcase.module.api.DCaseStereotypes;
import edu.casetools.dcase.module.impl.DCaseModule;
import edu.casetools.dcase.module.impl.DCasePeerModule;
import edu.casetools.dcase.utils.PropertiesUtils;

/**
 * The Class PropertyManager manages all the property pages.
 */
public class PropertyManager {
    private IPropertyContent propertyPage;
    private IMetamodelExtensions extensions;
    private List<Stereotype> sterList;

    /**
     * Change property.
     *
     * @param element
     *            the element
     * @param row
     *            the row
     * @param value
     *            the value
     * @return the int
     */
    public int changeProperty(ModelElement element, int row, String value) {
	init(element);

	int currentRow = row;
	for (Stereotype ster : sterList) {

	    getPropertyPages(extensions, ster);

	    if (null != this.propertyPage) {
		this.propertyPage.changeProperty(element, currentRow, value);
		currentRow -= ster.getDefinedTagType().size();
		this.propertyPage = null;
	    }
	}

	return currentRow;
    }

    /**
     * Update.
     *
     * @param element
     *            the element
     * @param table
     *            the table
     */
    public void update(ModelElement element, IModulePropertyTable table) {
	init(element);

	for (Stereotype ster : sterList) {

	    getPropertyPages(extensions, ster);
	    updatePropertyPage(element, table);

	}

	this.propertyPage = new CommonPropertyPage();
	this.propertyPage.update(element, table);
    }

    private void init(ModelElement element) {
	this.propertyPage = null;
	extensions = DCaseModule.getInstance().getModuleContext().getModelingSession().getMetamodelExtensions();
	sterList = PropertiesUtils.getInstance().computePropertyList(element);
    }

    private void updatePropertyPage(ModelElement element, IModulePropertyTable table) {
	if (null != this.propertyPage) {
	    this.propertyPage.update(element, table);
	    this.propertyPage = null;
	}
    }

    private void getPropertyPages(IMetamodelExtensions extensions, Stereotype ster) {

	if (ster.equals(
		extensions.getStereotype(DCasePeerModule.MODULE_NAME, DCaseStereotypes.CONTEXT_INFORMATION_MESSAGE,
			DCaseModule.getInstance().getModuleContext().getModelioServices().getMetamodelService()
				.getMetamodel().getMClass(CommunicationMessage.class)))) {
	    this.propertyPage = new ContextInformationMessagePropertyPage();
	}

	if (ster.equals(extensions.getStereotype(DCasePeerModule.MODULE_NAME, DCaseStereotypes.STATE,
		DCaseModule.getInstance().getModuleContext().getModelioServices().getMetamodelService().getMetamodel()
			.getMClass(Class.class)))) {
	    this.propertyPage = new StatePropertyPage();
	}

	if (ster.equals(extensions.getStereotype(DCasePeerModule.MODULE_NAME, DCaseStereotypes.ANTECEDENT,
		DCaseModule.getInstance().getModuleContext().getModelioServices().getMetamodelService().getMetamodel()
			.getMClass(Class.class)))) {
	    this.propertyPage = new AntecedentPropertyPage(DCaseProperties.ANTECEDENT_STATE_NAME,
		    DCaseProperties.ANTECEDENT_STATE_VALUE);
	}

	if (ster.equals(extensions.getStereotype(DCasePeerModule.MODULE_NAME, DCaseStereotypes.CONSEQUENT,
		DCaseModule.getInstance().getModuleContext().getModelioServices().getMetamodelService().getMetamodel()
			.getMClass(Class.class)))) {
	    this.propertyPage = new AntecedentPropertyPage(DCaseProperties.CONSEQUENT_STATE_NAME,
		    DCaseProperties.CONSEQUENT_STATE_VALUE);
	}

	if (ster.equals(extensions.getStereotype(DCasePeerModule.MODULE_NAME, DCaseStereotypes.IMMEDIATE_PAST_OPERATOR,
		DCaseModule.getInstance().getModuleContext().getModelioServices().getMetamodelService().getMetamodel()
			.getMClass(Class.class)))) {
	    this.propertyPage = new ImmediatePastOperatorPropertyPage();
	}

	if (ster.equals(extensions.getStereotype(DCasePeerModule.MODULE_NAME, DCaseStereotypes.ABSOLUTE_PAST_OPERATOR,
		DCaseModule.getInstance().getModuleContext().getModelioServices().getMetamodelService().getMetamodel()
			.getMClass(Class.class)))) {
	    this.propertyPage = new AbsolutePastOperatorPropertyPage();
	}

	if (ster.equals(extensions.getStereotype(DCasePeerModule.MODULE_NAME, DCaseStereotypes.EVENT,
		DCaseModule.getInstance().getModuleContext().getModelioServices().getMetamodelService().getMetamodel()
			.getMClass(Message.class)))) {
	    this.propertyPage = new TestCaseMessagePropertyPage();
	}

	if (ster.equals(extensions.getStereotype(DCasePeerModule.MODULE_NAME, DCaseStereotypes.TEST_CASE_INTERACTION,
		DCaseModule.getInstance().getModuleContext().getModelioServices().getMetamodelService().getMetamodel()
			.getMClass(Interaction.class)))) {
	    this.propertyPage = new TestCasePropertyPage();
	}

    }

}
