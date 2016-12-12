package edu.casetools.dcase.modelio.menu.io;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.FileDialog;
import org.modelio.api.module.IModule;
import org.modelio.api.module.command.DefaultModuleCommandHandler;
import org.modelio.metamodel.uml.infrastructure.ModelElement;
import org.modelio.vcore.smkernel.mapi.MObject;

import edu.casetools.dcase.extensions.io.data.MdData;
import edu.casetools.dcase.m2uppaal.M2Uppaal;

public class ExportMDiagrams2UPPAAL extends DefaultModuleCommandHandler {

    /**
     * Constructor.
     */
    public ExportMDiagrams2UPPAAL() {
	super();
    }

    /**
     * @see org.modelio.api.module.commands.DefaultModuleContextualCommand#accept(java.util.List,
     *      org.modelio.api.module.IModule)
     */
    @Override
    public boolean accept(List<MObject> selectedElements, IModule module) {
	// Check that there is only one selected element
	return selectedElements.size() == 1;
    }

    /**
     * @see org.modelio.api.module.commands.DefaultModuleContextualCommand#actionPerformed(java.util.List,
     *      org.modelio.api.module.IModule)
     */

    @Override
    public void actionPerformed(List<MObject> selectedElements, IModule module) {

	ModelElement modelelt = (ModelElement) selectedElements.get(0);

	FileDialog dialog = showDialog(modelelt);
	String fileLocation = dialog.open();
	// if (!modelelt.isStereotyped(Utils.CONTEXT_MODELLER,
	// Utils.CONTEXT_MODEL)) {
	// MessageDialog.openError(null, "Error", "Element is not a Context
	// Model");
	// return;
	// }
	checkIfExists(fileLocation);
	translate(fileLocation);

    }

    private FileDialog showDialog(ModelElement modelelt) {
	FileDialog dialog = new FileDialog(Display.getCurrent().getActiveShell(), SWT.SAVE);
	dialog.setFilterNames(new String[] { "Text Files", "All Files (*.*)" });
	dialog.setFilterExtensions(new String[] { "*.txt", "*.*" }); // Windows
	// wild
	// cards
	dialog.setFilterPath(System.getProperty("user.home") + "/Desktop"); // Windows
									    // path
	dialog.setFileName(modelelt.getName() + "_UPPAAL.xml");
	return dialog;
    }

    private void translate(String fileLocation) {
	try {
	    MdData data = new MdData();
	    new M2Uppaal().translate(fileLocation, data.getMData());
	    MessageDialog.openInformation(null, "Model Exported", "Model exported to C-SPARQL at:\n" + fileLocation);
	} catch (IOException e) {
	    MessageDialog.openInformation(null, "I/O Exception", "File:" + fileLocation + " \n" + e.getMessage());
	    e.printStackTrace();
	}
    }

    private void checkIfExists(String fileLocation) {
	File newFile = new File(fileLocation);

	if (newFile.exists()) {
	    if (!MessageDialog.openConfirm(null, "Confirm Export",
		    "File already exists.\nDo you want to replace it?\n")) {
		return;
	    }
	}
    }

}
