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
package edu.casetools.dcase.modelio.diagrams;

import java.util.List;

import org.modelio.api.modelio.Modelio;
import org.modelio.api.modelio.diagram.IDiagramGraphic;
import org.modelio.api.modelio.diagram.IDiagramHandle;
import org.modelio.api.modelio.diagram.IDiagramLink;
import org.modelio.api.modelio.diagram.ILinkPath;
import org.modelio.api.modelio.model.IModelingSession;
import org.modelio.api.modelio.model.ITransaction;
import org.modelio.metamodel.mmextensions.infrastructure.ExtensionNotFoundException;
import org.modelio.metamodel.uml.infrastructure.Dependency;
import org.modelio.metamodel.uml.infrastructure.ModelElement;
import org.modelio.metamodel.uml.infrastructure.Note;

import edu.casetools.dcase.module.impl.DCaseModule;
import edu.casetools.dcase.module.impl.DCasePeerModule;
import edu.casetools.rcase.modelio.diagrams.RelationTool;
import edu.casetools.rcase.utils.ElementUtils;


/**
 * The Class RelationTool has the common methods to create the tool of a
 * relation.
 */
@SuppressWarnings("deprecation")
public abstract class RelationNoteTool extends RelationTool {
    /*
     * (non-Javadoc)
     * 
     * @see
     * org.modelio.api.diagram.tools.DefaultLinkTool#actionPerformed(org.modelio
     * .api.diagram.IDiagramHandle, org.modelio.api.diagram.IDiagramGraphic,
     * org.modelio.api.diagram.IDiagramGraphic,
     * org.modelio.api.diagram.IDiagramLink.LinkRouterKind,
     * org.modelio.api.diagram.ILinkPath)
     */
    @Override
    public void actionPerformed(IDiagramHandle representation, IDiagramGraphic origin, IDiagramGraphic target,
	    IDiagramLink.LinkRouterKind kind, ILinkPath path) {
	IModelingSession session = Modelio.getInstance().getModelingSession(); 
	ITransaction transaction = session
		.createTransaction("Create");

	try {
	    ModelElement originElement = (ModelElement) origin.getElement();
	    ModelElement targetElement = (ModelElement) target.getElement();

	    Dependency dependency = createDependency(originElement, targetElement);

		Note note = ElementUtils.getInstance().createNote(DCaseModule.getInstance().getModuleContext().getModelingSession().getModel(), DCasePeerModule.MODULE_NAME, dependency, "");
		note.setContent("Stream: \nEvery: \nFor: ");
	    
	    List<IDiagramGraphic> graphics = representation.unmask(note, 0, 0);
	    graphics = representation.unmask(dependency, 0, 0);
	    
	    for (IDiagramGraphic graphic : graphics) {
		createLink(kind, path, graphic);
	    }

	    representation.save();
	    representation.close();
	    transaction.commit();
	    transaction.close();
	} catch (ExtensionNotFoundException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	} finally {
	    transaction.close();
	}
    }

 

}