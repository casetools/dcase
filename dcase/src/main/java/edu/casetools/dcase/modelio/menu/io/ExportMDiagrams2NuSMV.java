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

import edu.casetools.dcase.extensions.io.m.MdData;
import edu.casetools.dcase.m2nusmv.M2NuSMV;
import edu.casetools.dcase.m2nusmv.data.MData;
import edu.casetools.dcase.module.api.DCaseProperties;
import edu.casetools.dcase.module.api.DCaseStereotypes;
import edu.casetools.dcase.module.impl.DCasePeerModule;
import edu.casetools.dcase.utils.PropertiesUtils;

public class ExportMDiagrams2NuSMV extends DefaultModuleCommandHandler {

    M2NuSMV m2nusmv;

    /**
     * Constructor.
     */
    public ExportMDiagrams2NuSMV() {
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
	int maxExecutionTime = getMaxExecutionTime(selectedElements);
	if (maxExecutionTime > 0) {
	    checkIfExists(fileLocation);
	    translate(fileLocation, maxExecutionTime);
	}

    }

    private int getMaxExecutionTime(List<MObject> selectedElements) {
	String maxExecutionTime = "";
	for (MObject element : selectedElements) {
	    if (((ModelElement) element).isStereotyped(DCasePeerModule.MODULE_NAME,
		    DCaseStereotypes.STEREOTYPE_DIAGRAM_M_RULES))
		maxExecutionTime = PropertiesUtils.getInstance()
			.getTaggedValue(DCaseProperties.PROPERTY_MAX_EXECUTION_TIME, (ModelElement) element);
	}

	return handleResult(maxExecutionTime);
    }

    private int handleResult(String maxExecutionTime) {
	// @todo Handle this with a proper exception mechanism
	int time = -1;
	if (maxExecutionTime == null) {
	    MessageDialog.openError(null, "Execution Time Exception",
		    "No execution time was provided. Make sure that the diagram is selected when clicking export. Right click on the M Rules Diagram to be exported. The export of the model has been cancelled.\n");
	} else if (maxExecutionTime.equals("")) {
	    MessageDialog.openError(null, "Execution Time Exception",
		    "No execution time was provided. Make sure that there is a value on the selected M Rule Diagram. The export of the model has been cancelled.\n");
	} else {
	    time = Integer.parseInt(maxExecutionTime); // The value of the
						       // execution time cannot
						       // be
						       // other than an integer
						       // positive number, as
						       // these
						       // exceptions are
						       // controlled
						       // in the properties
						       // page.
						       // So, it will never
						       // reach
						       // this code.
	}
	return time;
    }

    private FileDialog showDialog(ModelElement modelelt) {
	FileDialog dialog = new FileDialog(Display.getCurrent().getActiveShell(), SWT.SAVE);
	dialog.setFilterNames(new String[] { "Text Files", "All Files (*.*)" });
	dialog.setFilterExtensions(new String[] { "*.txt", "*.smv", "*.*" }); // Windows
	// wild
	// cards
	dialog.setFilterPath(System.getProperty("user.home") + "/Desktop"); // Windows
									    // path
	dialog.setFileName(modelelt.getName() + "_NuSMV.smv");
	return dialog;
    }

    private void translate(String fileLocation, int maxExecutionTime) {
	try {
	    m2nusmv = new M2NuSMV();
	    m2nusmv.writeModel(getData(fileLocation, maxExecutionTime));
	    MessageDialog.openInformation(null, "Model Exported", "Model exported to C-SPARQL at:\n" + fileLocation);
	} catch (IOException e) {
	    MessageDialog.openInformation(null, "I/O Exception", "File:" + fileLocation + " \n" + e.getMessage());
	    e.printStackTrace();
	}
    }

    private MData getData(String fileLocation, int maxExecutionTime) {
	MdData mddata = new MdData();
	mddata.loadDiagramElements();
	MData mdata = mddata.getMData();
	mdata.setFilePath(fileLocation);
	mdata.setMaxIteration(maxExecutionTime);
	return mdata;
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
