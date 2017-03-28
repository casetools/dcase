package edu.casetools.dcase.module.impl;

import org.modelio.api.module.context.log.ILogService;
import org.modelio.api.module.lifecycle.DefaultModuleLifeCycleHandler;
import org.modelio.api.module.lifecycle.ModuleException;
import org.modelio.vbasic.version.Version;

/**
 * Implementation of the IModuleLifeCycleHandler interface. <br>
 * This default implementation may be inherited by the module developers in
 * order to simplify the code writing of the module session.
 */
public class DCaseSession extends DefaultModuleLifeCycleHandler {

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
     * @see org.modelio.api.module.DefaultModuleLifeCycleHandler#start()
     */
    @Override
    public boolean start() throws ModuleException {
	// get the version of the module
	Version moduleVersion = this.module.getVersion();

	// get the Modelio log service
	ILogService logService = this.module.getModuleContext().getLogService();

	String message = "Start of " + this.module.getName() + " " + moduleVersion;
	logService.info(message);
	return super.start();
    }

    public static boolean install(String modelioPath, String mdaPath) throws ModuleException {
	return DefaultModuleLifeCycleHandler.install(modelioPath, mdaPath);
    }

}
