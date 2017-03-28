package edu.casetools.dcase.modelio.menu.io;

import java.util.List;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.Display;
import org.modelio.api.module.IModule;
import org.modelio.api.module.command.DefaultModuleCommandHandler;
import org.modelio.vcore.smkernel.mapi.MObject;

import edu.casetools.dcase.extensions.io.acl.ACLGenerator;

public class GenerateACLCode extends DefaultModuleCommandHandler {

    @Override
    public void actionPerformed(List<MObject> arg0, IModule arg1) {
	DirectoryDialog dialog = new DirectoryDialog(Display.getCurrent().getActiveShell());
	String path = dialog.open();
	ACLGenerator generator = new ACLGenerator();
	generator.generateACLTemplates(path);
	MessageDialog.openInformation(null, "Model Exported", "Android code skeleton generated at:\n" + path);
    }

}
