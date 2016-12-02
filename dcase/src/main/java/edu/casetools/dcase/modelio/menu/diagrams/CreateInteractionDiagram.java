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
package edu.casetools.dcase.modelio.menu.diagrams;

import java.util.List;

import org.modelio.api.modelio.model.IModelingSession;
import org.modelio.api.module.IModule;
import org.modelio.metamodel.diagrams.SequenceDiagram;
import org.modelio.vcore.smkernel.mapi.MObject;

import edu.casetools.dcase.modelio.menu.CreateBehaviourDiagram;
import edu.casetools.dcase.module.api.DCaseResources;
import edu.casetools.dcase.module.api.DCaseStereotypes;
import edu.casetools.dcase.module.i18n.I18nMessageService;
import edu.casetools.dcase.utils.DiagramUtils;

/**
 * The Class CreateRequirementsDiagram creates a Requirements Diagram.
 */
public class CreateInteractionDiagram extends CreateBehaviourDiagram {

    /*
     * (non-Javadoc)
     * 
     * @see edu.casesuite.modelio.menu.CreateDiagram#createOwnDiagram
     * (java.util.List, org.modelio.api.model.IModelingSession)
     */
    @Override
    protected SequenceDiagram createOwnDiagram(List<MObject> selectedElements, IModelingSession session) {
	String name = I18nMessageService.getString("Ui.Command.CreateSequenceDiagram.Label");
	SequenceDiagram diagram = DiagramUtils.getInstance().createAndAddSequenceDiagram(selectedElements, session,
		name, DCaseStereotypes.DIAGRAM_INTERACTION);
	diagram = (SequenceDiagram) addStyle(diagram, DCaseResources.STYLE_INTERACTION_DIAGRAM);
	return diagram;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.modelio.api.module.commands.DefaultModuleCommandHandler#accept(java
     * .util.List, org.modelio.api.module.IModule)
     */
    @Override
    public boolean accept(List<MObject> selectedElements, IModule module) {
	return commonCheck(selectedElements, module);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.modelio.api.module.commands.DefaultModuleCommandHandler#isActiveFor
     * (java.util.List, org.modelio.api.module.IModule)
     */
    @Override
    public boolean isActiveFor(List<MObject> selectedElements, IModule module) {
	return commonCheck(selectedElements, module);
    }

}
