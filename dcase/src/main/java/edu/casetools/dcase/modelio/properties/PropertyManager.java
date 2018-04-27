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
import org.modelio.metamodel.diagrams.StaticDiagram;
import org.modelio.metamodel.uml.infrastructure.ModelElement;
import org.modelio.metamodel.uml.infrastructure.Stereotype;
import org.modelio.metamodel.uml.statik.Class;

import edu.casetools.dcase.modelio.properties.pages.RuleDiagramPropertyPage;
import edu.casetools.dcase.modelio.properties.pages.acqmod.DBModellingRulePropertyPage;
import edu.casetools.dcase.modelio.properties.pages.acqmod.FeedsInWindowPropertyPage;
import edu.casetools.dcase.modelio.properties.pages.acqmod.MobileSensorPropertyPage;
import edu.casetools.dcase.modelio.properties.pages.acqmod.ModellingRulePropertyPage;
import edu.casetools.dcase.modelio.properties.pages.acqmod.PreferenceSensorPropertyPage;
import edu.casetools.dcase.modelio.properties.pages.acqmod.RDFModellingRulePropertyPage;
import edu.casetools.dcase.modelio.properties.pages.acqmod.SensorPropertyPage;
import edu.casetools.dcase.modelio.properties.pages.acqmod.StationarySensorPropertyPage;
import edu.casetools.dcase.modelio.properties.pages.deployment.ActuatorPropertyPage;
import edu.casetools.dcase.modelio.properties.pages.deployment.MDatabasePropertyPage;
import edu.casetools.dcase.modelio.properties.pages.deployment.MReasonerPropertyPage;
import edu.casetools.dcase.modelio.properties.pages.deployment.VeraActuatorPropertyPage;
import edu.casetools.dcase.modelio.properties.pages.info.InfoPropertyPage;
import edu.casetools.dcase.modelio.properties.pages.info.MessagePropertyPage;
import edu.casetools.dcase.modelio.properties.pages.reasoning.AbsolutePastOperatorPropertyPage;
import edu.casetools.dcase.modelio.properties.pages.reasoning.AntecedentPropertyPage;
import edu.casetools.dcase.modelio.properties.pages.reasoning.ImmediatePastOperatorPropertyPage;
import edu.casetools.dcase.modelio.properties.pages.reasoning.SpecificationPropertyPage;
import edu.casetools.dcase.module.api.DCaseProperties;
import edu.casetools.dcase.module.api.DCaseStereotypes;
import edu.casetools.dcase.module.impl.DCaseModule;
import edu.casetools.dcase.module.impl.DCasePeerModule;
import edu.casetools.rcase.modelio.properties.CommonPropertyPage;
import edu.casetools.rcase.modelio.properties.IPropertyContent;
import edu.casetools.rcase.utils.PropertiesUtils;

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
	sterList = PropertiesUtils.getInstance().computePropertyList(DCasePeerModule.MODULE_NAME, element);
    }


    private void updatePropertyPage(ModelElement element, IModulePropertyTable table) {
	if (null != this.propertyPage) {
	    this.propertyPage.update(element, table);
	    this.propertyPage = null;
	}
    }

    private void getPropertyPages(IMetamodelExtensions extensions, Stereotype ster) {

	if (ster.equals(extensions.getStereotype(DCasePeerModule.MODULE_NAME, DCaseStereotypes.STEREOTYPE_ANTECEDENT,
		DCaseModule.getInstance().getModuleContext().getModelioServices().getMetamodelService().getMetamodel()
			.getMClass(Class.class)))) {
	    this.propertyPage = new AntecedentPropertyPage(DCaseProperties.PROPERTY_ANTECEDENT_STATE_NAME,
		    DCaseProperties.PROPERTY_ANTECEDENT_STATE_VALUE);
	}

	if (ster.equals(extensions.getStereotype(DCasePeerModule.MODULE_NAME,
		DCaseStereotypes.STEREOTYPE_DIAGRAM_REASONING, DCaseModule.getInstance().getModuleContext()
			.getModelioServices().getMetamodelService().getMetamodel().getMClass(StaticDiagram.class)))) {
	    this.propertyPage = new RuleDiagramPropertyPage();
	}

	if (ster.equals(extensions.getStereotype(DCasePeerModule.MODULE_NAME, DCaseStereotypes.STEREOTYPE_CONSEQUENT,
		DCaseModule.getInstance().getModuleContext().getModelioServices().getMetamodelService().getMetamodel()
			.getMClass(Class.class)))) {
	    this.propertyPage = new AntecedentPropertyPage(DCaseProperties.PROPERTY_CONSEQUENT_STATE_NAME,
		    DCaseProperties.PROPERTY_CONSEQUENT_STATE_VALUE);
	}

	if (ster.equals(extensions.getStereotype(DCasePeerModule.MODULE_NAME,
		DCaseStereotypes.STEREOTYPE_IMMEDIATE_PAST_OPERATOR, DCaseModule.getInstance().getModuleContext()
			.getModelioServices().getMetamodelService().getMetamodel().getMClass(Class.class)))) {
	    this.propertyPage = new ImmediatePastOperatorPropertyPage();
	}

	if (ster.equals(extensions.getStereotype(DCasePeerModule.MODULE_NAME,
		DCaseStereotypes.STEREOTYPE_ABSOLUTE_PAST_OPERATOR, DCaseModule.getInstance().getModuleContext()
			.getModelioServices().getMetamodelService().getMetamodel().getMClass(Class.class)))) {
	    this.propertyPage = new AbsolutePastOperatorPropertyPage();
	}

	if (ster.equals(extensions.getStereotype(DCasePeerModule.MODULE_NAME, DCaseStereotypes.STEREOTYPE_SPECIFICATION,
		DCaseModule.getInstance().getModuleContext().getModelioServices().getMetamodelService().getMetamodel()
			.getMClass(Class.class)))) {
	    this.propertyPage = new SpecificationPropertyPage();
	}
	
	if (ster.equals(extensions.getStereotype(DCasePeerModule.MODULE_NAME, DCaseStereotypes.STEREOTYPE_MESSAGE,
			DCaseModule.getInstance().getModuleContext().getModelioServices().getMetamodelService().getMetamodel()
				.getMClass(Class.class)))) {
		    this.propertyPage = new MessagePropertyPage();
		}

	if (ster.equals(extensions.getStereotype(DCasePeerModule.MODULE_NAME, DCaseStereotypes.STEREOTYPE_INFO,
			DCaseModule.getInstance().getModuleContext().getModelioServices().getMetamodelService().getMetamodel()
				.getMClass(Class.class)))) {
		    this.propertyPage = new InfoPropertyPage();
		}	

	if (ster.equals(extensions.getStereotype(DCasePeerModule.MODULE_NAME, DCaseStereotypes.STEREOTYPE_SENSOR,
			DCaseModule.getInstance().getModuleContext().getModelioServices().getMetamodelService().getMetamodel()
				.getMClass(Class.class)))) {
		    this.propertyPage = new SensorPropertyPage();
		}	
	
	if (ster.equals(extensions.getStereotype(DCasePeerModule.MODULE_NAME, DCaseStereotypes.STEREOTYPE_PREFERENCE_SENSOR,
			DCaseModule.getInstance().getModuleContext().getModelioServices().getMetamodelService().getMetamodel()
				.getMClass(Class.class)))) {
		    this.propertyPage = new PreferenceSensorPropertyPage();
		}		
	if (ster.equals(extensions.getStereotype(DCasePeerModule.MODULE_NAME, DCaseStereotypes.STEREOTYPE_STATIONARY_SENSOR,
			DCaseModule.getInstance().getModuleContext().getModelioServices().getMetamodelService().getMetamodel()
				.getMClass(Class.class)))) {
		    this.propertyPage = new StationarySensorPropertyPage();
		}	
	if (ster.equals(extensions.getStereotype(DCasePeerModule.MODULE_NAME, DCaseStereotypes.STEREOTYPE_MOBILE_SENSOR,
			DCaseModule.getInstance().getModuleContext().getModelioServices().getMetamodelService().getMetamodel()
				.getMClass(Class.class)))) {
		    this.propertyPage = new MobileSensorPropertyPage();
		}	
	if (ster.equals(extensions.getStereotype(DCasePeerModule.MODULE_NAME, DCaseStereotypes.STEREOTYPE_MODELLING_RULE,
			DCaseModule.getInstance().getModuleContext().getModelioServices().getMetamodelService().getMetamodel()
				.getMClass(Class.class)))) {
		    this.propertyPage = new ModellingRulePropertyPage();
		}		
	if (ster.equals(extensions.getStereotype(DCasePeerModule.MODULE_NAME, DCaseStereotypes.STEREOTYPE_DB_MODELLING_RULE,
			DCaseModule.getInstance().getModuleContext().getModelioServices().getMetamodelService().getMetamodel()
				.getMClass(Class.class)))) {
		    this.propertyPage = new DBModellingRulePropertyPage();
		}			
	if (ster.equals(extensions.getStereotype(DCasePeerModule.MODULE_NAME, DCaseStereotypes.STEREOTYPE_RDF_MODELLING_RULE,
			DCaseModule.getInstance().getModuleContext().getModelioServices().getMetamodelService().getMetamodel()
				.getMClass(Class.class)))) {
		    this.propertyPage = new RDFModellingRulePropertyPage();
		}		
	if (ster.equals(extensions.getStereotype(DCasePeerModule.MODULE_NAME, DCaseStereotypes.STEREOTYPE_FEEDS_IN_WINDOW,
			DCaseModule.getInstance().getModuleContext().getModelioServices().getMetamodelService().getMetamodel()
				.getMClass(Class.class)))) {
		    this.propertyPage = new FeedsInWindowPropertyPage();
		}	
	if (ster.equals(extensions.getStereotype(DCasePeerModule.MODULE_NAME, DCaseStereotypes.STEREOTYPE_ACTUATOR,
			DCaseModule.getInstance().getModuleContext().getModelioServices().getMetamodelService().getMetamodel()
				.getMClass(Class.class)))) {
		    this.propertyPage = new ActuatorPropertyPage();
		}		
	if (ster.equals(extensions.getStereotype(DCasePeerModule.MODULE_NAME, DCaseStereotypes.STEREOTYPE_VERA_ACTUATOR,
			DCaseModule.getInstance().getModuleContext().getModelioServices().getMetamodelService().getMetamodel()
				.getMClass(Class.class)))) {
		    this.propertyPage = new VeraActuatorPropertyPage();
		}		
	if (ster.equals(extensions.getStereotype(DCasePeerModule.MODULE_NAME, DCaseStereotypes.STEREOTYPE_M_DATABASE,
			DCaseModule.getInstance().getModuleContext().getModelioServices().getMetamodelService().getMetamodel()
				.getMClass(Class.class)))) {
		    this.propertyPage = new MDatabasePropertyPage();
		}		
	if (ster.equals(extensions.getStereotype(DCasePeerModule.MODULE_NAME, DCaseStereotypes.STEREOTYPE_M_REASONER,
			DCaseModule.getInstance().getModuleContext().getModelioServices().getMetamodelService().getMetamodel()
				.getMClass(Class.class)))) {
		    this.propertyPage = new MReasonerPropertyPage();
		}			
    }

}
