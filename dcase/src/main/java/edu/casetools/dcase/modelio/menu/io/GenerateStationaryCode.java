package edu.casetools.dcase.modelio.menu.io;

import java.util.List;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.Display;
import org.modelio.api.module.IModule;
import org.modelio.api.module.command.DefaultModuleCommandHandler;
import org.modelio.vcore.smkernel.mapi.MObject;

import edu.casetools.dcase.extensions.io.gen.stationary.StationaryCodeGenerator;
import edu.casetools.dcase.module.impl.DCaseModule;
import edu.casetools.rcase.utils.ModelioUtils;

public class GenerateStationaryCode extends DefaultModuleCommandHandler {

	private static final String stationaryFolder = "\\stationary";
	
    @Override
    public void actionPerformed(List<MObject> arg0, IModule arg1) {
    String projectName = ModelioUtils.getInstance().getProjectName(DCaseModule.getInstance()).replaceAll("\\s", "");
	DirectoryDialog dialog = new DirectoryDialog(Display.getCurrent().getActiveShell());
	String path = dialog.open();
	path = path.concat("\\").concat(projectName).concat(stationaryFolder);
	StationaryCodeGenerator generator = new StationaryCodeGenerator();
	generator.generateTemplates(path);
	MessageDialog.openInformation(null, "Model Exported", "Android code skeleton generated at:\n" + path);
    }

}
