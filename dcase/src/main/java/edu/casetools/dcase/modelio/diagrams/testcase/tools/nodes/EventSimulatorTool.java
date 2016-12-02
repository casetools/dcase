/*
 * Copyright 2015 @author Unai Alegre 
 * 
 * This file is part of DCASE (Design for Context-Aware Systems Engineering), a module 
 * of Modelio that aids the requirements elicitation stage of a Context-Aware System (C-AS). 
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
package edu.casetools.dcase.modelio.diagrams.testcase.tools.nodes;

import java.util.List;

import org.modelio.api.modelio.diagram.IDiagramGraphic;
import org.modelio.api.modelio.diagram.IDiagramHandle;
import org.modelio.api.modelio.diagram.IDiagramNode;
import org.modelio.api.modelio.model.IModelingSession;
import org.modelio.metamodel.factory.ExtensionNotFoundException;
import org.modelio.metamodel.uml.behavior.communicationModel.CommunicationChannel;
import org.modelio.metamodel.uml.behavior.communicationModel.CommunicationMessage;
import org.modelio.metamodel.uml.behavior.communicationModel.CommunicationNode;
import org.modelio.metamodel.uml.behavior.interactionModel.Lifeline;
import org.modelio.metamodel.uml.infrastructure.ModelElement;
import org.modelio.vcore.smkernel.mapi.MObject;

import edu.casetools.dcase.modelio.diagrams.LifelineTool;
import edu.casetools.dcase.module.api.DCaseStereotypes;
import edu.casetools.dcase.module.i18n.I18nMessageService;
import edu.casetools.dcase.utils.DiagramUtils;

/**
 * The Class ContextNoteTool is the tool for creating a Context Note.
 */
public class EventSimulatorTool extends LifelineTool {

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.modelio.api.diagram.tools.DefaultAttachedBoxTool#acceptElement(org
     * .modelio.api.diagram.IDiagramHandle,
     * org.modelio.api.diagram.IDiagramGraphic)
     */
    @Override
    public boolean acceptElement(IDiagramHandle representation, IDiagramGraphic targetNode) {
	return true;
    }

    private void createContextMessageDependencies(ModelElement owner, CommunicationMessage message) {
	if (owner instanceof CommunicationChannel) {
	    CommunicationChannel channel = (CommunicationChannel) owner;
	    CommunicationNode origin = channel.getStart();
	    DiagramUtils.getInstance().createDependency(origin, message,
		    DCaseStereotypes.DEPENDENCY_PRODUCE);
	    CommunicationNode target = channel.getEnd();
	    DiagramUtils.getInstance().createDependency(target, message,
		    DCaseStereotypes.DEPENDENCY_CONSUME);
	}
    }

    @Override
    protected List<IDiagramGraphic> representAsImage(List<IDiagramGraphic> graph) {
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

    @Override
    protected Lifeline createOwnLifeline(IModelingSession session, MObject element) {

	Lifeline lifeline = null;
	try {
	    lifeline = DiagramUtils.getInstance().createLifeline(element, session,
		    I18nMessageService.getString("Names.EventsSimulator"), DCaseStereotypes.EVENTS_SIMULATOR);
	} catch (ExtensionNotFoundException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}
	return lifeline;
    }

}
