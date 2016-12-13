package edu.casetools.dcase.modelio.menu.io;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
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
import edu.casetools.dcase.extensions.io.data.MdTestCase;
import edu.casetools.dcase.m2uppaal.M2Uppaal;
import edu.casetools.dcase.module.api.DCaseStereotypes;
import edu.casetools.dcase.module.impl.DCasePeerModule;
import edu.casetools.dcase.utils.ModelioUtils;

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

	createTestCases(modelelt);

    }

    private void createTestCases(ModelElement modelelt) {
	List<MObject> testCases = getSelectedTestCases(modelelt);

	for (MObject test : testCases) {
	    FileDialog dialog = showDialog(modelelt, test.getName());
	    String fileLocation = dialog.open();
	    checkIfExists(fileLocation);
	    translate(fileLocation, new MdTestCase(test));
	}
    }

    private List<MObject> getSelectedTestCases(ModelElement modelelt) {
	List<MObject> allElements = new ArrayList<>();
	List<MObject> testCases = new ArrayList<>();
	allElements = ModelioUtils.getInstance().getElementsFromMObject((ArrayList<MObject>) allElements,
		(MObject) modelelt);
	for (MObject element : allElements) {
	    if (element instanceof ModelElement) {
		if (((ModelElement) element).isStereotyped(DCasePeerModule.MODULE_NAME,
			DCaseStereotypes.TEST_CASE_INTERACTION)) {
		    testCases.add(element);
		}
	    }
	}
	return testCases;
    }

    private FileDialog showDialog(ModelElement modelelt, String name) {
	FileDialog dialog = new FileDialog(Display.getCurrent().getActiveShell(), SWT.SAVE);
	dialog.setFilterNames(new String[] { "Text Files", "All Files (*.*)" });
	dialog.setFilterExtensions(new String[] { "*.txt", "*.*" }); // Windows
	// wild
	// cards
	dialog.setFilterPath(System.getProperty("user.home") + "/Desktop"); // Windows
									    // path
	dialog.setFileName(modelelt.getName() + "_" + name + "_UPPAAL.xml");
	return dialog;
    }

    private void translate(String fileLocation, MdTestCase testCase) {
	try {
	    MdData data = new MdData(testCase);
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
