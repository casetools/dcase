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
package edu.casetools.dcase.modelio.diagrams.testcase.tools.messages;

import org.modelio.api.modelio.diagram.IDiagramGraphic;
import org.modelio.api.modelio.diagram.IDiagramHandle;
import org.modelio.api.modelio.model.IModelingSession;
import org.modelio.metamodel.factory.ExtensionNotFoundException;
import org.modelio.metamodel.uml.behavior.interactionModel.Lifeline;
import org.modelio.metamodel.uml.behavior.interactionModel.Message;
import org.modelio.metamodel.uml.infrastructure.ModelElement;

import edu.casetools.dcase.modelio.diagrams.MessageTool;
import edu.casetools.dcase.module.api.DCaseStereotypes;
import edu.casetools.dcase.module.impl.DCasePeerModule;
import edu.casetools.dcase.utils.DiagramUtils;

/**
 * The Class ContextNoteTool is the tool for creating a Context Note.
 */
public class EventMessageTool extends MessageTool {

    @Override
    protected Message createOwnCommunicationMessage(IModelingSession session, ModelElement model, ModelElement owner)
	    throws ExtensionNotFoundException {
	return DiagramUtils.getInstance().createMessage(session, (Lifeline) model, (Lifeline) owner,
		DCaseStereotypes.EVENT);
    }

    @Override
    public boolean acceptFirstElement(IDiagramHandle representation, IDiagramGraphic target) {
	if (target.getElement() instanceof ModelElement) {
	    return ((ModelElement) target.getElement()).isStereotyped(DCasePeerModule.MODULE_NAME,
		    DCaseStereotypes.EVENTS_SIMULATOR);
	} else
	    return false;
    }

    @Override
    public boolean acceptSecondElement(IDiagramHandle representation, IDiagramGraphic source, IDiagramGraphic target) {
	if (target.getElement() instanceof ModelElement) {
	    return ((ModelElement) target.getElement()).isStereotyped(DCasePeerModule.MODULE_NAME,
		    DCaseStereotypes.M_SYSTEM);
	} else
	    return false;
    }

}
