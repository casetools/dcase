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
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Modelio.  If not, see <http://www.gnu.org/licenses/>.
 * 
 */
package edu.casetools.dcase.modelio.diagrams.mrules.tools.elements;

import java.util.List;

import org.modelio.api.modelio.diagram.IDiagramGraphic;
import org.modelio.api.modelio.diagram.IDiagramNode;
import org.modelio.api.modelio.model.IModelingSession;
import org.modelio.metamodel.mmextensions.infrastructure.ExtensionNotFoundException;
import org.modelio.metamodel.uml.infrastructure.ModelElement;
import org.modelio.vcore.smkernel.mapi.MObject;

import edu.casetools.rcase.modelio.diagrams.ElementTool;
import edu.casetools.dcase.module.api.DCaseProperties;
import edu.casetools.dcase.module.api.DCaseStereotypes;
import edu.casetools.dcase.module.i18n.I18nMessageService;
import edu.casetools.dcase.module.impl.DCaseModule;
import edu.casetools.dcase.module.impl.DCasePeerModule;
import edu.casetools.rcase.utils.ElementUtils;

/**
 * The Class RequirementTool is the tool for creating a Requirement.
 */
public class AntecedentTool extends ElementTool {

    /*
     * (non-Javadoc)
     * 
     * @see edu.casesuite.modelio.diagrams.ElementTool# createOwnElement
     * (org.modelio.api.model.IModelingSession,
     * org.modelio.vcore.smkernel.mapi.MObject)
     */
    @Override
    public MObject createOwnElement(IModelingSession session, MObject element) {
	String name = I18nMessageService.getString("Names.Antecedent");

	MObject auxiliarElement = ElementUtils.getInstance().createClass(DCaseModule.getInstance(), adaptElement(element), session, name,
		DCaseStereotypes.STEREOTYPE_ANTECEDENT);
	return setDefaultValues(auxiliarElement);
    }

    private MObject setDefaultValues(MObject auxiliarElement) {
	try {
	    ((ModelElement) auxiliarElement).putTagValue(DCasePeerModule.MODULE_NAME,
		    DCaseProperties.PROPERTY_ANTECEDENT_STATE_NAME, I18nMessageService.getString("Ui.None"));
	    ((ModelElement) auxiliarElement).putTagValue(DCasePeerModule.MODULE_NAME,
		    DCaseProperties.PROPERTY_ANTECEDENT_STATE_VALUE,
		    I18nMessageService.getString("Ui.Antecedent.Property.TagStateValue.False"));
	} catch (ExtensionNotFoundException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}
	return auxiliarElement;
    }

    /*
     * (non-Javadoc)
     * 
     * @see edu.casesuite.modelio.diagrams.ElementTool# representAsImage
     * (java.util.List)
     */
    @Override
    protected List<IDiagramGraphic> representationConfigs(List<IDiagramGraphic> graph) {

	if ((null != graph) && (!graph.isEmpty()) && (graph.get(0) instanceof IDiagramNode)) {
	    IDiagramNode dnode = (IDiagramNode) graph.get(0);
	    dnode.setProperty("REPMODE", "SIMPLE");
	    dnode.setProperty("FILLCOLOR", "255,255,255");
	    dnode.setProperty("FILLMODE", "SOLID");
	    dnode.setProperty("LINECOLOR", "0,0,0");
	    dnode.setProperty("INTAUTOUNMASK", "TRUE");
	    dnode.setProperty("INNERUNMASKFILTER", "ALL");
	}

	return graph;
    }

}
