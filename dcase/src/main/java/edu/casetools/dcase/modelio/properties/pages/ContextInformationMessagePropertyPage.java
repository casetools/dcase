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

import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.core.runtime.AssertionFailedException;
import org.modelio.api.modelio.Modelio;
import org.modelio.api.modelio.model.IModelingSession;
import org.modelio.api.modelio.model.ITransaction;
import org.modelio.api.module.propertiesPage.IModulePropertyTable;
import org.modelio.metamodel.factory.ExtensionNotFoundException;
import org.modelio.metamodel.uml.infrastructure.Dependency;
import org.modelio.metamodel.uml.infrastructure.ModelElement;
import org.modelio.vcore.smkernel.mapi.MObject;

import edu.casetools.dcase.modelio.properties.IPropertyContent;
import edu.casetools.dcase.module.api.DCaseProperties;
import edu.casetools.dcase.module.i18n.I18nMessageService;
import edu.casetools.dcase.module.impl.DCasePeerModule;
import edu.casetools.dcase.utils.ModelioUtils;
import edu.casetools.dcase.utils.PropertiesUtils;
import edu.casetools.dcase.utils.tables.TableUtils;

public class ContextInformationMessagePropertyPage implements IPropertyContent {

    private static final Logger logger = Logger.getLogger(ContextInformationMessagePropertyPage.class.getName());

    // TODO Reduce the complexity of the switch case
    @Override
    public void changeProperty(ModelElement element, int row, String value) {
	try {
	    switch (row) {
	    case 1:
		element.setName(value);
		break;
	    case 2:
		PropertiesUtils.getInstance().findAndAddValue(DCasePeerModule.MODULE_NAME,
			DCaseProperties.PROPERTY_MESSAGE_ID, value, element);
		break;
	    case 3:
		element.putTagValue(DCasePeerModule.MODULE_NAME, DCaseProperties.PROPERTY_MESSAGE_RESPONSIBILITY,
			value);
		break;
	    case 4:
		element.putTagValue(DCasePeerModule.MODULE_NAME, DCaseProperties.PROPERTY_MESSAGE_REGULARITY, value);
		break;
	    case 5:
		element.putTagValue(DCasePeerModule.MODULE_NAME, DCaseProperties.PROPERTY_MESSAGE_FREQUENCY, value);
		break;
	    case 6:
		element.putTagValue(DCasePeerModule.MODULE_NAME, DCaseProperties.PROPERTY_MESSAGE_SYNCHRONICITY, value);
		break;
	    case 7:
		element.putTagValue(DCasePeerModule.MODULE_NAME, DCaseProperties.PROPERTY_MESSAGE_SITUATIONAL_PARAMETER,
			value);
		refreshLinks(element, value);
		break;
	    default:
		break;
	    }
	} catch (ExtensionNotFoundException | AssertionFailedException e) {
	    logger.log(Level.SEVERE, e.getMessage(), e);
	}

    }

    private void refreshLinks(ModelElement element, String value) {
	removeOldTracedContextAttributes(element);
	if (!value.equals(
		I18nMessageService.getString("Ui.ContextInformationMessage.Property.TagContextAttribute.None")))
	    traceElementToContextAttribute(element, value);
    }

    @Override
    public void update(ModelElement element, IModulePropertyTable table) {
	String property;

	// Name
	table.addProperty(DCaseProperties.PROPERTY_NAME, element.getName());

	// TagId
	String string = PropertiesUtils.getInstance().getTaggedValue(DCaseProperties.PROPERTY_MESSAGE_ID, element);
	table.addProperty(I18nMessageService.getString("Ui.ContextInformationMessage.Property.TagId"), string);

	// TagResponsibility
	property = element.getTagValue(DCasePeerModule.MODULE_NAME, DCaseProperties.PROPERTY_MESSAGE_RESPONSIBILITY);
	table.addProperty(I18nMessageService.getString("Ui.ContextInformationMessage.Property.TagResponsibility"),
		property,
		new String[] {
			I18nMessageService.getString("Ui.ContextInformationMessage.Property.TagResponsibility.Pull"),
			I18nMessageService.getString("Ui.ContextInformationMessage.Property.TagResponsibility.Push") });

	// TagRegularity
	property = element.getTagValue(DCasePeerModule.MODULE_NAME, DCaseProperties.PROPERTY_MESSAGE_REGULARITY);
	table.addProperty(I18nMessageService.getString("Ui.ContextInformationMessage.Property.TagRegularity"), property,
		new String[] {
			I18nMessageService.getString("Ui.ContextInformationMessage.Property.TagRegularity.Regular"),
			I18nMessageService
				.getString("Ui.ContextInformationMessage.Property.TagRegularity.Irregular") });

	// TagFrequency
	string = PropertiesUtils.getInstance().getTaggedValue(DCaseProperties.PROPERTY_MESSAGE_FREQUENCY, element);
	table.addProperty(I18nMessageService.getString("Ui.ContextInformationMessage.Property.TagFrequency"), string);

	// TagSynchronicity
	property = element.getTagValue(DCasePeerModule.MODULE_NAME, DCaseProperties.PROPERTY_MESSAGE_SYNCHRONICITY);
	table.addProperty(I18nMessageService.getString("Ui.ContextInformationMessage.Property.TagSynchronicity"),
		property,
		new String[] {
			I18nMessageService
				.getString("Ui.ContextInformationMessage.Property.TagSynchronicity.Synchronous"),
			I18nMessageService
				.getString("Ui.ContextInformationMessage.Property.TagSynchronicity.Asynchronous") });

	// TagContextAttribute
	property = element.getTagValue(DCasePeerModule.MODULE_NAME,
		DCaseProperties.PROPERTY_MESSAGE_SITUATIONAL_PARAMETER);
	table.addProperty(I18nMessageService.getString("Ui.ContextInformationMessage.Property.TagContextAttribute"),
		property, getAllContextAttributes());

    }

    private String[] getAllContextAttributes() {

	MObject ContextAttribute;
	ArrayList<MObject> ContextAttributes = new ArrayList<>();

	ContextAttributes = (ArrayList<MObject>) TableUtils.getInstance()
		.getAllElementsStereotypedAs(ContextAttributes, "RCase", "ContextAttributeStereotype");
	String[] ContextAttributeNames = new String[ContextAttributes.size() + 1];
	ContextAttributeNames[0] = new String(
		I18nMessageService.getString("Ui.ContextInformationMessage.Property.TagContextAttribute.None"));
	for (int i = 0; i < ContextAttributes.size(); i++) {
	    ContextAttribute = ContextAttributes.get(i);
	    ContextAttributeNames[i + 1] = ContextAttribute.getName();
	}
	return ContextAttributeNames;
    }

    private void traceElementToContextAttribute(ModelElement element, String value) {
	IModelingSession session = Modelio.getInstance().getModelingSession();
	ITransaction transaction = session
		.createTransaction(I18nMessageService.getString("Info.Session.Create", new String[] { "" }));
	ModelElement ContextAttribute = (ModelElement) ModelioUtils.getInstance().getElementByName(value);

	try {
	    session.getModel().createDependency(element, ContextAttribute, "ModelerModule", "trace");
	    transaction.commit();
	} catch (ExtensionNotFoundException e) {
	    logger.log(Level.SEVERE, e.getMessage(), e);
	} finally {
	    transaction.close();
	}

    }

    private void removeOldTracedContextAttributes(ModelElement element) {

	for (MObject child : element.getCompositionChildren()) {
	    if (child instanceof ModelElement) {
		ModelElement auxiliarChild = (ModelElement) child;
		if (auxiliarChild.isStereotyped("ModelerModule", "trace")) {
		    deleteContextAttribute(auxiliarChild);
		}
	    }

	}

    }

    private void deleteContextAttribute(ModelElement auxiliarChild) {
	if (auxiliarChild instanceof Dependency) {
	    Dependency dependency = (Dependency) auxiliarChild;
	    ModelElement target = dependency.getDependsOn();
	    if (target.isStereotyped("RCase", "ContextAttributeStereotype"))
		auxiliarChild.delete();

	}
    }

}
