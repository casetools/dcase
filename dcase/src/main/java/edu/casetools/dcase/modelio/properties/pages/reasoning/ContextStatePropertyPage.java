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
import org.modelio.api.modelio.model.IModelingSession;
import org.modelio.api.modelio.model.ITransaction;
import org.modelio.api.module.propertiesPage.IModulePropertyTable;
import org.modelio.metamodel.mmextensions.infrastructure.ExtensionNotFoundException;
import org.modelio.metamodel.uml.infrastructure.Dependency;
import org.modelio.metamodel.uml.infrastructure.ModelElement;
import org.modelio.vcore.smkernel.mapi.MObject;

import edu.casetools.dcase.module.api.DCaseProperties;
import edu.casetools.dcase.module.api.DCaseStereotypes;
import edu.casetools.dcase.module.i18n.I18nMessageService;
import edu.casetools.dcase.module.impl.DCaseModule;
import edu.casetools.dcase.module.impl.DCasePeerModule;
import edu.casetools.rcase.modelio.properties.IPropertyContent;
import edu.casetools.rcase.utils.ModelioUtils;
import edu.casetools.rcase.utils.PropertiesUtils;

public class ContextStatePropertyPage implements IPropertyContent {

    private static final Logger LOGGER = Logger.getLogger(ContextStatePropertyPage.class.getName());

    @Override
    public void changeProperty(ModelElement element, int row, String value) {
	try {
	    switch (row) {
	    case 1:
		PropertiesUtils.getInstance().findAndAddValue(DCaseModule.getInstance(), DCasePeerModule.MODULE_NAME,
			DCaseProperties.PROPERTY_CONTEXT_STATE_ID, value, element);
		break;	    
	    case 2:
	    	element.setName(value);
		break;
	    case 3:
		PropertiesUtils.getInstance().findAndAddValue(DCaseModule.getInstance(), DCasePeerModule.MODULE_NAME,
			DCaseProperties.PROPERTY_CONTEXT_STATE_DESCRIPTION, value, element);
		break;
	    case 4:
		PropertiesUtils.getInstance().findAndAddValue(DCaseModule.getInstance(), DCasePeerModule.MODULE_NAME,
			DCaseProperties.PROPERTY_STATE_INDEPENDENT, value, element);
		break;
	    case 5:
		PropertiesUtils.getInstance().findAndAddValue(DCaseModule.getInstance(), DCasePeerModule.MODULE_NAME,
			DCaseProperties.PROPERTY_STATE_INITIAL_VALUE, value, element);
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

	// TagId
	String string = PropertiesUtils.getInstance().getTaggedValue(DCaseProperties.PROPERTY_CONTEXT_STATE_ID,
		element);
	table.addProperty(I18nMessageService.getString("Ui.PastOperator.Property.TagId"), string);

	// Name
	table.addProperty(DCaseProperties.PROPERTY_NAME, element.getName());
	
	// TagStateName
	property = element.getTagValue(DCasePeerModule.MODULE_NAME, DCaseProperties.PROPERTY_CONTEXT_STATE_DESCRIPTION);
	table.addProperty(I18nMessageService.getString("Ui.ContextState.Property.Description"), property);

	// TagStateValue
	property = element.getTagValue(DCasePeerModule.MODULE_NAME, DCaseProperties.PROPERTY_STATE_INDEPENDENT);
	table.addProperty(I18nMessageService.getString("Ui.ContextState.Property.IsIndependent"), property,
		new String[] { I18nMessageService.getString("Ui.PastOperator.Property.TagStateValue.True"),
			I18nMessageService.getString("Ui.PastOperator.Property.TagStateValue.False") });

	// TagStateValue
	property = element.getTagValue(DCasePeerModule.MODULE_NAME, DCaseProperties.PROPERTY_STATE_INITIAL_VALUE);
	table.addProperty(I18nMessageService.getString("Ui.ContextState.Property.InitialValue"), property,
			new String[] { I18nMessageService.getString("Ui.PastOperator.Property.TagStateValue.True"),
					I18nMessageService.getString("Ui.PastOperator.Property.TagStateValue.False") });
	
	// TagPlatformValue
	updateContextStatePlatform((ModelElement) element);
	property = element.getTagValue(DCasePeerModule.MODULE_NAME, DCaseProperties.PROPERTY_CONTEXT_STATE_PLATFORM);
	table.addConsultProperty(I18nMessageService.getString("Ui.Platform"), property);

    }
    
    public static void updateContextStatePlatform(ModelElement element){
    	putTagValue(element, DCaseProperties.PROPERTY_CONTEXT_STATE_PLATFORM, getContextStatePlatformType(element));
    }
    
    
    public static void putTagValue(ModelElement element, String property, String value){
    	IModelingSession session = DCaseModule.getInstance().getModuleContext().getModelingSession();
    	ITransaction transaction = session
    		.createTransaction(I18nMessageService.getString("Info.Session.Create", new String[] { "" }));
    	try {
    		element.putTagValue(DCasePeerModule.MODULE_NAME, property, value);
    	    transaction.commit();
    	} catch (ExtensionNotFoundException e) {
    	    e.printStackTrace();
    	} finally {
    	    transaction.close();
    	}
    }
    

	public static String getContextStatePlatformType(ModelElement element) {
		for(Dependency dependency : element.getImpactedDependency()){
			if(dependency.isStereotyped(DCasePeerModule.MODULE_NAME, DCaseStereotypes.STEREOTYPE_DEPENDENCY_PRODUCE)){
				MObject parent = dependency.getImpacted();
				if(((ModelElement) parent).isStereotyped(DCasePeerModule.MODULE_NAME, DCaseStereotypes.STEREOTYPE_RDF_MODELLING_RULE))
					return I18nMessageService.getString("Ui.Platform.Mobile");
				else if (((ModelElement) parent).isStereotyped(DCasePeerModule.MODULE_NAME, DCaseStereotypes.STEREOTYPE_DB_MODELLING_RULE))
					return I18nMessageService.getString("Ui.Platform.Stationary");
				else if (((ModelElement) parent).isStereotyped(DCasePeerModule.MODULE_NAME, DCaseStereotypes.STEREOTYPE_ANTECEDENT_GROUP)){
					for(MObject antecedent : parent.getCompositionChildren()){
						String stateName = getStateName(antecedent);
						if(stateName != null) {
							MObject contextState = ModelioUtils.getInstance().getElementByName(DCaseModule.getInstance(), stateName);
							if(contextState != null && checkProduceRelationship((ModelElement)contextState)) 
								return getContextStatePlatformType((ModelElement)contextState);
						}
					}
				}
			}
		}
		return I18nMessageService.getString("Ui.Platform.Unknown");
	}

	public static String getStateName(MObject antecedent) {
		if(((ModelElement)antecedent).isStereotyped(DCasePeerModule.MODULE_NAME, DCaseStereotypes.STEREOTYPE_ANTECEDENT)){
			return ((ModelElement) antecedent).getTagValue(DCasePeerModule.MODULE_NAME, DCaseProperties.PROPERTY_ANTECEDENT_STATE_NAME);
		} else if(((ModelElement)antecedent).isStereotyped(DCasePeerModule.MODULE_NAME, DCaseStereotypes.STEREOTYPE_PAST_OPERATOR)){
			return ((ModelElement) antecedent).getTagValue(DCasePeerModule.MODULE_NAME, DCaseProperties.PROPERTY_PAST_OPERATOR_STATE_NAME);
		} else if(((ModelElement)antecedent).isStereotyped(DCasePeerModule.MODULE_NAME, DCaseStereotypes.STEREOTYPE_CONSEQUENT)){
			return ((ModelElement) antecedent).getTagValue(DCasePeerModule.MODULE_NAME, DCaseProperties.PROPERTY_CONSEQUENT_STATE_NAME);
		}
		
		return null;
	}

	public static boolean checkProduceRelationship(ModelElement element) {
		for(Dependency dependency : element.getImpactedDependency()){
			
			if(dependency.isStereotyped(DCasePeerModule.MODULE_NAME, DCaseStereotypes.STEREOTYPE_DEPENDENCY_PRODUCE)){
				return true;
			}
		}
		return false;
	}

}
