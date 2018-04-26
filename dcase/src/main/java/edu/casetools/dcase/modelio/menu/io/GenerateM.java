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

import edu.casetools.dcase.extensions.io.m.MGenerator;
import edu.casetools.dcase.module.api.DCaseStereotypes;
import edu.casetools.dcase.module.i18n.I18nMessageService;
import edu.casetools.dcase.module.impl.DCaseModule;
import edu.casetools.dcase.module.impl.DCasePeerModule;
import edu.casetools.dcase.utils.IOUtils;
import edu.casetools.rcase.utils.ModelioUtils;

public class GenerateM extends DefaultModuleCommandHandler {

    @Override
    public void actionPerformed(List<MObject> arg0, IModule arg1) {
	if (arg0.size() > 1) {
	    MessageDialog.openInformation(null, "Code generation cancelled",
		    "More than one elements were selected. To generate the M code right click on an M Rule Diagram and then click on "
			    + I18nMessageService.getString("Module.ModuleLabel") + " -> "
			    + I18nMessageService.getString("Module.Export") + " -> "
			    + I18nMessageService.getString("Module.M"));

	} else {
	    if (((ModelElement) arg0.get(0)).isStereotyped(DCasePeerModule.MODULE_NAME,
		    DCaseStereotypes.STEREOTYPE_DIAGRAM_REASONING))
		generateMCode();
	    else
		MessageDialog.openInformation(null, "Code generation cancelled",
			"The element selected is not an M Rule Diagram. To generate the M model right click on an M Rule Diagram and then click on "
				+ I18nMessageService.getString("Module.ModuleLabel") + " -> "
				+ I18nMessageService.getString("Module.Export") + " -> "
				+ I18nMessageService.getString("Module.M"));

	}
    }

    private void generateMCode() {
	try {
	    String filename = ModelioUtils.getInstance().getProjectName(DCaseModule.getInstance()).replaceAll("\\s+","_") + ".mtpl";
	    FileDialog dialog = IOUtils.getInstance().getFileDialog(filename, new String[] { "*.mtpl", "*.txt" },
		    SWT.SAVE);
	    String file = dialog.open();
	    if (!IOUtils.getInstance().checkIfExists(dialog.getFileName())) {
		MGenerator generator = new MGenerator();
		generator.generate(file);
		MessageDialog.openInformation(null, "Model Exported",
			"Model exported to M at:\n" + dialog.getFileName());
	    }
	} catch (IOException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}
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

}
