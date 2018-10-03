package edu.casetools.dcase.modelio.menu.io;

import java.util.List;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.Display;
import org.modelio.api.module.IModule;
import org.modelio.api.module.command.DefaultModuleCommandHandler;
import org.modelio.vcore.smkernel.mapi.MObject;

import edu.casetools.dcase.extensions.io.gen.mobile.MobileCodeGenerator;
import edu.casetools.dcase.extensions.io.gen.stationary.StationaryCodeGenerator;
import edu.casetools.dcase.module.impl.DCaseModule;
import edu.casetools.rcase.utils.ModelioUtils;

public class GenerateMobileCode extends DefaultModuleCommandHandler {

	private static final String mobileFolder = "\\mobile";
	private static final String stationaryFolder = "\\stationary";
	
    @Override
    public void actionPerformed(List<MObject> arg0, IModule arg1) {
    String projectName = ModelioUtils.getInstance().getProjectName(DCaseModule.getInstance()).replaceAll("\\s", "");
	DirectoryDialog dialog = new DirectoryDialog(Display.getCurrent().getActiveShell());
	String selectedPath = dialog.open();
	
	String path = selectedPath.concat("\\").concat(projectName).concat(mobileFolder);
	MobileCodeGenerator mobileGenerator = new MobileCodeGenerator();
	mobileGenerator.generateTemplates(path);
	path = selectedPath.concat("\\").concat(projectName).concat(stationaryFolder);
	StationaryCodeGenerator stationaryGenerator = new StationaryCodeGenerator();
	stationaryGenerator.generateTemplates(path);
	MessageDialog.openInformation(null, "Model Exported", "All code skeletons have been generated at:\n" + selectedPath);
    }

}
