package edu.casetools.dcase.module.impl;

import org.modelio.api.module.IModuleAPIConfiguration;
import org.modelio.vbasic.version.Version;

import edu.casetools.dcase.module.api.IDCasePeerModule;

/**
 * Implementation of Module services <br>
 * When a module is built using the MDA Modeler tool, a public interface is
 * generated and accessible for the other module developments. <br>
 * The main class that allows developpers to get specific module services has to
 * implement the current interface. <br>
 * Each mda component brings a specific interface that inherit from this one and
 * gives all the desired module services.
 *
 */
public class DCasePeerModule implements IDCasePeerModule {
    public static final String MODULE_NAME = "DCase";

    private DCaseModule module;

    private IModuleAPIConfiguration peerConfiguration;

    public DCasePeerModule(DCaseModule statModuleModule, IModuleAPIConfiguration peerConfiguration) {
	super();
	this.module = statModuleModule;
	this.peerConfiguration = peerConfiguration;
    }

    /**
     * @see org.modelio.api.module.IPeerModule#getConfiguration()
     */
    @Override
    public IModuleAPIConfiguration getConfiguration() {
	return this.peerConfiguration;
    }

    /**
     * @see org.modelio.api.module.IPeerModule#getDescription()
     */
    @Override
    public String getDescription() {
	return this.module.getDescription();
    }

    /**
     * @see org.modelio.api.module.IPeerModule#getName()
     */
    @Override
    public String getName() {
	return this.module.getName();
    }

    /**
     * @see org.modelio.api.module.IPeerModule#getVersion()
     */
    @Override
    public Version getVersion() {
	return this.module.getVersion();
    }

}
