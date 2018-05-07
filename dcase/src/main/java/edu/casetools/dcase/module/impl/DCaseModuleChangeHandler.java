/*
 * Copyright 2015 The ContextModeller Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package edu.casetools.dcase.module.impl;

import java.util.Set;

import org.modelio.api.modelio.model.IModelingSession;
import org.modelio.api.modelio.model.event.IModelChangeEvent;
import org.modelio.api.modelio.model.event.IModelChangeHandler;
import org.modelio.metamodel.uml.infrastructure.Dependency;
import org.modelio.metamodel.uml.infrastructure.ModelElement;
import org.modelio.metamodel.uml.infrastructure.Note;
import org.modelio.metamodel.uml.infrastructure.TagParameter;
import org.modelio.metamodel.uml.infrastructure.TaggedValue;
import org.modelio.metamodel.uml.statik.Association;
import org.modelio.vcore.smkernel.mapi.MObject;

import edu.casetools.dcase.module.api.DCaseNotes;
import edu.casetools.dcase.module.api.DCaseProperties;
import edu.casetools.dcase.module.api.DCaseStereotypes;
import edu.casetools.rcase.utils.PropertiesUtils;


public class DCaseModuleChangeHandler implements IModelChangeHandler {

	@Override
	public void handleModelChange(IModelingSession session,
			IModelChangeEvent event) {

		Set<MObject> addedElements = event.getCreationEvents();

		for (MObject element : addedElements) {

			if (element instanceof TaggedValue) {
				updateStereotypeInformation((TaggedValue) element);
			}

		}

		Set<MObject> updatedElements = event.getUpdateEvents();

		for (MObject element : updatedElements) {

			if (element instanceof TagParameter) {
				updateStereotypeInformation(((TagParameter) element)
						.getAnnoted());
			}

			if (element instanceof Note) {

			}
		}

	}

	private static void updateStereotypeInformation(TaggedValue property) {

		ModelElement element = property.getAnnoted();

		if (element.isStereotyped(DCasePeerModule.MODULE_NAME, DCaseStereotypes.STEREOTYPE_FEEDS_IN_WINDOW)) {
			updateContextSourceRelationships(element);
		}

	}

	private static void updateContextSourceRelationships(ModelElement element) {
		String astream = PropertiesUtils.getInstance().getTaggedValue(DCaseProperties.PROPERTY_FEEDS_IN_WINDOW_STREAM, element);
		String aevery = PropertiesUtils.getInstance().getTaggedValue(DCaseProperties.PROPERTY_FEEDS_IN_WINDOW_EVERY, element);
		String afor =PropertiesUtils.getInstance().getTaggedValue(DCaseProperties.PROPERTY_FEEDS_IN_WINDOW_FOR, element);

		StringBuilder value = new StringBuilder();

		if (! astream.isEmpty()) {
			value.append("Stream: ");
			value.append(astream + "\n");
		}
		if (! aevery.isEmpty()) {
			value.append("Every: ");
			value.append(aevery + "\n");
		}
		if (! afor.isEmpty()) {
			value.append("For: ");
			value.append(afor);
		}

		Note note = element.getNote(DCasePeerModule.MODULE_NAME, DCaseNotes.NOTE_FEEDS_IN_WINDOW);
		note.setContent(value.toString());
		//element.setName(value.toString());

	}





}
