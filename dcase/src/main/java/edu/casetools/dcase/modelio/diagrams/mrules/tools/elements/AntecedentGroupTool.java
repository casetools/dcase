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
import org.modelio.vcore.smkernel.mapi.MObject;

import edu.casetools.dcase.modelio.diagrams.ElementTool;
import edu.casetools.dcase.module.api.DCaseStereotypes;
import edu.casetools.dcase.module.i18n.I18nMessageService;
import edu.casetools.dcase.utils.DiagramUtils;

/**
 * The Class RequirementTool is the tool for creating a Requirement.
 */
public class AntecedentGroupTool extends ElementTool {

    /*
     * (non-Javadoc)
     * 
     * @see edu.casesuite.modelio.diagrams.ElementTool# createOwnElement
     * (org.modelio.api.model.IModelingSession,
     * org.modelio.vcore.smkernel.mapi.MObject)
     */
    @Override
    public MObject createOwnElement(IModelingSession session, MObject element) {
	String name = I18nMessageService.getString("Names.AntecedentGroup");

	return DiagramUtils.getInstance().createPackage(adaptElement(element), session, name,
		DCaseStereotypes.STEREOTYPE_ANTECEDENT_GROUP);
    }

    /*
     * (non-Javadoc)
     * 
     * @see edu.casesuite.modelio.diagrams.ElementTool# representAsImage
     * (java.util.List)
     */
    @Override
    protected List<IDiagramGraphic> representAsImage(List<IDiagramGraphic> graph) {

	if ((null != graph) && (!graph.isEmpty()) && (graph.get(0) instanceof IDiagramNode)) {
	    IDiagramNode dnode = (IDiagramNode) graph.get(0);
	    dnode.setProperty("FILLCOLOR", "217,236,255");
	    dnode.setProperty("FILLMODE", "SOLID");
	    dnode.setProperty("LINECOLOR", "0,128,255");
	    dnode.setProperty("INTAUTOUNMASK", "TRUE");
	    dnode.setProperty("INNERUNMASKFILTER", "ALL");
	}

	return graph;
    }

}
