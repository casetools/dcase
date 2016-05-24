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
package edu.casetools.dcase.utils;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.modelio.api.model.IModelingSession;
import org.modelio.api.model.IUmlModel;
import org.modelio.api.modelio.Modelio;
import org.modelio.metamodel.diagrams.CommunicationDiagram;
import org.modelio.metamodel.diagrams.SequenceDiagram;
import org.modelio.metamodel.factory.ExtensionNotFoundException;
import org.modelio.metamodel.uml.behavior.communicationModel.CommunicationChannel;
import org.modelio.metamodel.uml.behavior.communicationModel.CommunicationInteraction;
import org.modelio.metamodel.uml.behavior.communicationModel.CommunicationMessage;
import org.modelio.metamodel.uml.behavior.interactionModel.Interaction;
import org.modelio.metamodel.uml.infrastructure.Dependency;
import org.modelio.metamodel.uml.infrastructure.ModelElement;
import org.modelio.metamodel.uml.infrastructure.Note;
import org.modelio.metamodel.uml.infrastructure.Stereotype;
import org.modelio.metamodel.uml.statik.Collaboration;
import org.modelio.metamodel.uml.statik.NameSpace;
import org.modelio.vcore.smkernel.mapi.MObject;

import edu.casetools.dcase.module.i18n.I18nMessageService;
import edu.casetools.dcase.module.impl.DCasePeerModule;

// TODO: Auto-generated Javadoc
/**
 * The Class DiagramUtils.
 */
public class DiagramUtils {
    private static final Logger logger = Logger.getLogger(DiagramUtils.class.getName());
    private static final String LOCALS = "Ui.Collaboration.Locals";
    private static DiagramUtils instance = null;

    /**
     * Gets the single instance of DiagramUtils.
     *
     * @return single instance of DiagramUtils
     */
    public static DiagramUtils getInstance() {
	if (instance == null) {
	    instance = new DiagramUtils();
	}
	return instance;
    }

    /**
     * Sets the free name.
     *
     * @param element
     *            the element
     * @param testedName
     *            the tested name
     */
    public void setFreeName(ModelElement element, String testedName) {
	List<MObject> nameList = ModelioUtils.getInstance().getAllElements();
	String extension = "";
	int i = 1;
	while (nameExists(nameList, testedName + extension)) {
	    extension = Integer.toString(i);
	    i++;
	}

	element.setName(testedName + extension);
    }

    /**
     * Name exists.
     *
     * @param nameList
     *            the name list
     * @param name
     *            the name
     * @return true, if successful
     */
    public boolean nameExists(List<MObject> nameList, String name) {
	for (MObject object : nameList) {
	    if (object.getName().equals(name))
		return true;
	}
	return false;
    }

    public SequenceDiagram createAndAddSequenceDiagram(List<MObject> selectedElements, IModelingSession session,
	    String diagramName, String stereotype) {
	Interaction interaction;
	for (MObject owner : selectedElements) {
	    if (owner instanceof Interaction)
		interaction = (Interaction) owner;
	    else
		interaction = createInteraction((ModelElement) owner, session);

	    if (!findOwnedCollaborations(interaction))
		createCollaboration(interaction, session);

	    SequenceDiagram diagram = createSequenceDiagram(diagramName, stereotype, interaction, session);

	    Modelio.getInstance().getEditionService().openEditor(diagram);

	    return diagram;
	}
	return null;
    }

    private static SequenceDiagram createSequenceDiagram(String diagramName, String stereotype, Interaction interaction,
	    IModelingSession session) {
	SequenceDiagram diagram = session.getModel().createSequenceDiagram();
	diagram.setOrigin(interaction);
	DiagramUtils.getInstance().setFreeName(diagram, diagramName);
	try {
	    Stereotype sysSeqSter = session.getMetamodelExtensions().getStereotype(DCasePeerModule.MODULE_NAME,
		    stereotype,
		    Modelio.getInstance().getMetamodelService().getMetamodel().getMClass(SequenceDiagram.class));
	    diagram.getExtension().add(sysSeqSter);
	} catch (Exception e) {
	    logger.log(Level.SEVERE, e.getMessage(), e);
	}
	return diagram;
    }

    private static void createCollaboration(Interaction interaction, IModelingSession session) {
	Collaboration locals = session.getModel().createCollaboration();
	locals.setName(I18nMessageService.getString(LOCALS));
	interaction.getOwnedCollaboration().add(locals);
    }

    private static void createCollaboration(CommunicationInteraction interaction, IModelingSession session) {
	Collaboration locals = session.getModel().createCollaboration();
	locals.setName(I18nMessageService.getString(LOCALS));
	interaction.getOwnedCollaboration().add(locals);
    }

    private static boolean findOwnedCollaborations(Interaction interaction) {
	boolean notFound = false;
	for (Collaboration colla : interaction.getOwnedCollaboration()) {
	    if (colla.getName().equals(I18nMessageService.getString(LOCALS))) {
		notFound = true;
		break;
	    }

	}
	return notFound;

    }

    private static boolean findOwnedCollaborations(CommunicationInteraction interaction) {
	boolean notFound = false;
	for (Collaboration colla : interaction.getOwnedCollaboration()) {
	    if (colla.getName().equals(I18nMessageService.getString(LOCALS))) {
		notFound = true;
		break;
	    }

	}
	return notFound;
    }

    private static Interaction createInteraction(ModelElement owner, IModelingSession session) {
	Interaction interaction;
	interaction = session.getModel().createInteraction();
	interaction.setOwner((NameSpace) owner);
	DiagramUtils.getInstance().setFreeName(interaction,
		I18nMessageService.getString("Ui.Create.Sequence.Interaction.Name"));
	return interaction;
    }

    /**
     * Creates the note.
     *
     * @param model
     *            the model
     * @param owner
     *            the owner
     * @param stereotypeName
     *            the stereotype name
     * @return the note
     * @throws ExtensionNotFoundException
     *             the extension not found exception
     */
    public Note createNote(IUmlModel model, ModelElement owner, String stereotypeName)
	    throws ExtensionNotFoundException {
	return model.createNote(DCasePeerModule.MODULE_NAME, stereotypeName, owner, "");
    }

    /**
     * Creates the dependency.
     *
     * @param origin
     *            the origin
     * @param target
     *            the target
     * @param stereotype
     *            the stereotype
     * @return the dependency
     */
    public Dependency createDependency(ModelElement origin, ModelElement target, String stereotype) {
	try {
	    Dependency dependency = Modelio.getInstance().getModelingSession().getModel().createDependency(origin,
		    target, DCasePeerModule.MODULE_NAME, stereotype);
	    dependency.setName("");
	    return dependency;
	} catch (ExtensionNotFoundException e) {
	    logger.log(Level.SEVERE, e.getMessage(), e);
	}
	return null;
    }

    public CommunicationDiagram createAndAddCommunicationDiagram(List<MObject> selectedElements,
	    IModelingSession session, String name, String stereotype) {
	CommunicationInteraction interaction;
	for (MObject owner : selectedElements) {
	    if (owner instanceof CommunicationInteraction)
		interaction = (CommunicationInteraction) owner;
	    else
		interaction = createCommunicationInteraction((ModelElement) owner, session);

	    if (!findOwnedCollaborations(interaction))
		createCollaboration(interaction, session);

	    CommunicationDiagram diagram = createCommunicationDiagram(name, stereotype, interaction, session);

	    Modelio.getInstance().getEditionService().openEditor(diagram);

	    return diagram;
	}
	return null;
    }

    private CommunicationInteraction createCommunicationInteraction(ModelElement owner, IModelingSession session) {
	CommunicationInteraction interaction;
	interaction = (CommunicationInteraction) session.getModel().createElement(Modelio.getInstance()
		.getMetamodelService().getMetamodel().getMClass(CommunicationInteraction.class).getName());
	interaction.setOwner((NameSpace) owner);
	DiagramUtils.getInstance().setFreeName(interaction,
		I18nMessageService.getString("Ui.Create.Communication.Interaction.Name"));
	return interaction;
    }

    private CommunicationDiagram createCommunicationDiagram(String diagramName, String stereotype,
	    CommunicationInteraction interaction, IModelingSession session) {
	CommunicationDiagram diagram = session.getModel().createCommunicationDiagram(
		I18nMessageService.getString("Ui.Command.CreateCommunicationDiagram.Label"), interaction, null);
	diagram.setOrigin(interaction);
	DiagramUtils.getInstance().setFreeName(diagram, diagramName);
	try {
	    Stereotype sysSeqSter = session.getMetamodelExtensions().getStereotype(DCasePeerModule.MODULE_NAME,
		    stereotype,
		    Modelio.getInstance().getMetamodelService().getMetamodel().getMClass(CommunicationDiagram.class));
	    diagram.getExtension().add(sysSeqSter);
	} catch (Exception e) {
	    logger.log(Level.SEVERE, e.getMessage(), e);
	}
	return diagram;
    }

    public CommunicationMessage createCommunicationMessage(IUmlModel model, ModelElement owner, String stereotypeName) {
	CommunicationMessage createdElement = model.createCommunicationMessage();
	createdElement.setChannel((CommunicationChannel) owner);
	createdElement.setOwnerTemplateParameter(owner.getOwnerTemplateParameter());
	try {
	    createdElement.addStereotype(DCasePeerModule.MODULE_NAME, stereotypeName);
	} catch (ExtensionNotFoundException e) {
	    logger.log(Level.SEVERE, e.getMessage(), e);
	}
	DiagramUtils.getInstance().setFreeName(createdElement, "message");
	return createdElement;
    }

}
