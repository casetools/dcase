/*
 * Copyright 2015 @author Unai Alegre 
 * 
 * This file is part of RCASE (Requirements for Context-Aware Systems Engineering), a module 
 * of Modelio that aids the requirements elicitation stage of a Context-Aware System (C-AS). 
 * 
 * RCASE is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * RCASE is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with RCASE.  If not, see <http://www.gnu.org/licenses/>.
 * 
 */
package edu.casetools.dcase.modelio.menu.tables;

import java.util.List;

import org.modelio.api.module.IModule;
import org.modelio.api.module.command.DefaultModuleCommandHandler;
import org.modelio.vcore.smkernel.mapi.MObject;

import edu.casetools.dcase.extensions.tables.contextmodel.ContextModelTable;

/**
 * The Class OpenMessagesTable opens the Context Information Messages Table from
 * the contextual menu.
 */
public class OpenMessagesTable extends DefaultModuleCommandHandler {

    private ContextModelTable tableDialog;

    /**
     * Instantiates a new open dependency table.
     */
    public OpenMessagesTable() {
	super();
	this.tableDialog = null;
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
	return true;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.modelio.api.module.commands.DefaultModuleCommandHandler#
     * actionPerformed (java.util.List, org.modelio.api.module.IModule)
     */
    @Override
    public void actionPerformed(List<MObject> selectedElements, IModule module) {
	if (this.tableDialog != null)
	    this.tableDialog.setVisible(true);
	else
	    this.tableDialog = new ContextModelTable();

	this.tableDialog.refresh();
	this.tableDialog.toFront();
    }

}
