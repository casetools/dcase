package edu.casetools.dcase.module.impl;

import org.modelio.api.module.AbstractJavaModule;
import org.modelio.api.module.context.IModuleContext;
import org.modelio.api.module.lifecycle.IModuleLifeCycleHandler;
import org.modelio.api.module.parameter.IParameterEditionModel;

import edu.casetools.dcase.module.api.DCaseResources;

/**
 * Implementation of the IModule interface. <br>
 * All Modelio java modules should inherit from this class.
 * 
 */
public class DCaseModule extends AbstractJavaModule {

    private DCasePeerModule peerModule = null;

    private DCaseSession session = null;

    private static DCaseModule instance;

    /**
     * Builds a new module.
     * <p>
     * <p>
     * This constructor must not be called by the user. It is automatically
     * invoked by Modelio when the module is installed, selected or started.
     * 
     * @param moduleContext
     *            context of the module, needed to access Modelio features.
     */
    public DCaseModule(IModuleContext moduleContext) {
	super(moduleContext);
	this.session = new DCaseSession(this);
	this.peerModule = new DCasePeerModule(this, moduleContext.getPeerConfiguration());
	instance = this;
    }

    public static DCaseModule getInstance() {
	return instance;
    }

    @Override
    public DCasePeerModule getPeerModule() {
	return this.peerModule;
    }

    /**
     * Return the lifecycle handler attached to the current module.
     * <p>
     * <p>
     * This handler is used to manage the module lifecycle by declaring the
     * desired implementation on start, select... methods.
     */
    @Override
    public IModuleLifeCycleHandler getLifeCycleHandler() {
	return this.session;
    }

    /**
     * @see org.modelio.api.module.AbstractJavaModule#getParametersEditionModel()
     */
    @Override
    public IParameterEditionModel getParametersEditionModel() {
	if (this.parameterEditionModel == null) {
	    this.parameterEditionModel = super.getParametersEditionModel();
	}
	return this.parameterEditionModel;
    }

    @Override
    public String getModuleImagePath() {
	return DCaseResources.ICON_MODULE;
    }

}
