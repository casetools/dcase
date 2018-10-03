package edu.casetools.dcase.extensions.io.gen.mobile;

import edu.casetools.dcase.extensions.io.gen.TemplateManager;
import edu.casetools.dcase.extensions.io.gen.mobile.acontextlibrary.ACLGenerator;
import edu.casetools.dcase.extensions.io.gen.mobile.areasoner.AReasonerGenerator;

public class MobileCodeGenerator implements TemplateManager {

	private final String contextLibraryFolder = "\\aContextLibrary";
	private final String contextReasonerFolder = "\\aContextReasoner";
	
	@Override
	public void generateTemplates(String folder) {
		new ACLGenerator().generateTemplates(folder+contextLibraryFolder);
		new AReasonerGenerator().generateTemplates(folder+contextReasonerFolder);
	}

}
