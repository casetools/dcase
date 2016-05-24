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

import org.modelio.api.module.IModule;
import org.modelio.api.module.propertiesPage.AbstractModulePropertyPage;
import org.modelio.api.module.propertiesPage.IModulePropertyTable;
import org.modelio.metamodel.uml.infrastructure.ModelElement;
import org.modelio.vcore.smkernel.mapi.MObject;

/**
 * The Class DCasePropertyPage is the own property page of the DCASE module.
 */
public class DCasePropertyPage extends AbstractModulePropertyPage {

    ModelElement element;
    PropertyManager dCasePage;

    /**
     * Instantiates a new dcase property page.
     *
     * @param module
     *            the module
     * @param name
     *            the name
     * @param label
     *            the label
     * @param bitmap
     *            the bitmap
     */
    public DCasePropertyPage(IModule module, String name, String label, String bitmap) {
	super(module, name, label, bitmap);
    }

    private void init(List<MObject> selectedElements) {
	element = (ModelElement) selectedElements.get(0);
	this.dCasePage = new PropertyManager();
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.modelio.api.module.propertiesPage.IModulePropertyPage#changeProperty
     * (java.util.List, int, java.lang.String)
     */
    @Override
    public void changeProperty(List<MObject> selectedElements, int row, String value) {
	if ((null != selectedElements) && (!selectedElements.isEmpty())
		&& (selectedElements.get(0) instanceof ModelElement)) {
	    init(selectedElements);
	    this.dCasePage.changeProperty(element, row, value);
	}
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.modelio.api.module.propertiesPage.IModulePropertyPage#update(java
     * .util.List, org.modelio.api.module.propertiesPage.IModulePropertyTable)
     */
    @Override
    public void update(List<MObject> selectedElements, IModulePropertyTable table) {
	if ((null != selectedElements) && (!selectedElements.isEmpty()) && (null != selectedElements.get(0))
		&& (selectedElements.get(0) instanceof ModelElement)) {
	    init(selectedElements);
	    this.dCasePage.update(element, table);
	}
    }
}