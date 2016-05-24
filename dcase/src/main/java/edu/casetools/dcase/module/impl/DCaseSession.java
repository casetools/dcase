package edu.casetools.dcase.module.impl;

import org.modelio.api.log.ILogService;
import org.modelio.api.modelio.Modelio;
import org.modelio.api.module.DefaultModuleSession;
import org.modelio.api.module.ModuleException;
import org.modelio.vbasic.version.Version;

/**
 * Implementation of the IModuleSession interface. <br>
 * This default implementation may be inherited by the module developers in
 * order to simplify the code writing of the module session.
 */
public class DCaseSession extends DefaultModuleSession {

    /**
     * Constructor.
     * 
     * @param module
     *            the Module this session is instanciated for.
     */
    public DCaseSession(DCaseModule module) {
	super(module);
    }

    /**
     * @see org.modelio.api.module.DefaultModuleSession#start()
     */
    @Override
    public boolean start() throws ModuleException {
	// get the version of the module
	Version moduleVersion = this.module.getVersion();

	// get the Modelio log service
	ILogService logService = Modelio.getInstance().getLogService();

	String message = "Start of " + this.module.getName() + " " + moduleVersion;
	logService.info(this.module, message);
	return super.start();
    }

    public static boolean install(String modelioPath, String mdaPath) throws ModuleException {
	return DefaultModuleSession.install(modelioPath, mdaPath);
    }

}
