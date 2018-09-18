package edu.casetools.dcase.modelio.menu.io;

import java.io.IOException;
import java.util.List;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.FileDialog;
import org.modelio.api.module.IModule;
import org.modelio.api.module.command.DefaultModuleCommandHandler;
import org.modelio.metamodel.uml.infrastructure.ModelElement;
import org.modelio.vcore.smkernel.mapi.MObject;

import edu.casetools.dcase.extensions.io.stationary.m.MdData;
import edu.casetools.dcase.m2nusmv.M2NuSMV;
import edu.casetools.dcase.m2nusmv.data.MData;
import edu.casetools.dcase.module.api.DCaseProperties;
import edu.casetools.dcase.module.api.DCaseStereotypes;
import edu.casetools.dcase.module.i18n.I18nMessageService;
import edu.casetools.dcase.module.impl.DCasePeerModule;
import edu.casetools.dcase.utils.IOUtils;
import edu.casetools.rcase.utils.PropertiesUtils;

public class GenerateNuSMV extends DefaultModuleCommandHandler {

    M2NuSMV m2nusmv;

    /**
     * Constructor.
     */
    public GenerateNuSMV() {
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

	if (selectedElements.size() > 1) {
	    MessageDialog.openInformation(null, "Code generation cancelled",
		    "More than one elements were selected. To generate the NuSMV model right click on an M Rule Diagram and then click on "
			    + I18nMessageService.getString("Module.ModuleLabel") + " -> "
			    + I18nMessageService.getString("Module.Export") + " -> "
			    + I18nMessageService.getString("Module.NuSMV"));

	} else {
	    if (((ModelElement) selectedElements.get(0)).isStereotyped(DCasePeerModule.MODULE_NAME,
		    DCaseStereotypes.STEREOTYPE_DIAGRAM_REASONING))
		generateNuSMVModel(selectedElements.get(0));
	    else
		MessageDialog.openInformation(null, "Code generation cancelled",
			"The element selected is not an M Rule Diagram. To generate the NuSMV model right click on an M Rule Diagram and then click on "
				+ I18nMessageService.getString("Module.ModuleLabel") + " -> "
				+ I18nMessageService.getString("Module.Export") + " -> "
				+ I18nMessageService.getString("Module.NuSMV"));

	}

    }

    private void generateNuSMVModel(MObject element) {
	FileDialog dialog = IOUtils.getInstance().getFileDialog(((ModelElement) element).getName().replaceAll("\\s+","_") + "_NuSMV.smv",
		new String[] { "*.txt", "*.smv", "*.*" }, SWT.SAVE);
	String fileLocation = dialog.open();
	int maxExecutionTime = getMaxExecutionTime(element);
	if ((maxExecutionTime > 0) && (!IOUtils.getInstance().checkIfExists(fileLocation))) {
	    translate(fileLocation, maxExecutionTime);
	}
    }

    private int getMaxExecutionTime(MObject element) {
	String maxExecutionTime = "";

	if (((ModelElement) element).isStereotyped(DCasePeerModule.MODULE_NAME,
		DCaseStereotypes.STEREOTYPE_DIAGRAM_REASONING))
	    maxExecutionTime = PropertiesUtils.getInstance().getTaggedValue(DCaseProperties.PROPERTY_MAX_EXECUTION_TIME,
		    (ModelElement) element);

	return handleResult(maxExecutionTime);
    }

    private int handleResult(String maxExecutionTime) {
	// @todo Handle this with a proper exception mechanism
	int time = -1;
	if (maxExecutionTime == null) {
	    MessageDialog.openError(null, "Execution Time Exception",
		    "No execution time was provided. Make sure that the diagram is selected when clicking export. Right click on the Reasoning Diagram to be exported. The export of the model has been cancelled.\n");
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

    private void translate(String fileLocation, int maxExecutionTime) {
	try {
	    m2nusmv = new M2NuSMV();
	    m2nusmv.writeModel(getData(fileLocation, maxExecutionTime));
	    MessageDialog.openInformation(null, "Model Exported", "Model exported to NuSMV at:\n" + fileLocation);
	} catch (IOException e) {
	    MessageDialog.openInformation(null, "I/O Exception", "File:" + fileLocation + " \n" + e.getMessage());
	    e.printStackTrace();
	}
    }

    private MData getData(String fileLocation, int maxExecutionTime) {
	MdData mddata = new MdData();
	mddata.loadDiagramElements();
	mddata.loadSpecifications();
	MData mdata = mddata.getMData();
	mdata.setFilePath(fileLocation);
	mdata.setMaxIteration(maxExecutionTime);
	return mdata;
    }

}
