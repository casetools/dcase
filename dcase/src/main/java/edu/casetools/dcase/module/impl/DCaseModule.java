package edu.casetools.dcase.module.impl;

import org.modelio.api.model.IModelingSession;
import org.modelio.api.module.AbstractJavaModule;
import org.modelio.api.module.IModuleAPIConfiguration;
import org.modelio.api.module.IModuleSession;
import org.modelio.api.module.IModuleUserConfiguration;
import org.modelio.api.module.IParameterEditionModel;
import org.modelio.metamodel.mda.ModuleComponent;

import edu.casetools.dcase.module.api.DCaseResources;

/**
 * Implementation of the IModule interface. <br>
 * All Modelio java modules should inherit from this class.
 * 
 */
public class DCaseModule extends AbstractJavaModule {

    private DCasePeerModule peerModule = null;

    private DCaseSession session = null;

    /**
     * Builds a new module.
     * <p>
     * <p>
     * This constructor must not be called by the user. It is automatically
     * invoked by Modelio when the module is installed, selected or started.
     * 
     * @param modelingSession
     *            the modeling session this module is deployed into.
     * @param model
     *            the model part of this module.
     * @param moduleConfiguration
     *            the module configuration, to get and set parameter values from
     *            the module itself.
     * @param peerConfiguration
     *            the peer module configuration, to get and set parameter values
     *            from another module.
     */
    public DCaseModule(IModelingSession modelingSession, ModuleComponent moduleComponent,
	    IModuleUserConfiguration moduleConfiguration, IModuleAPIConfiguration peerConfiguration) {
	super(modelingSession, moduleComponent, moduleConfiguration);
	this.session = new DCaseSession(this);
	this.peerModule = new DCasePeerModule(this, peerConfiguration);
    }

    @Override
    public DCasePeerModule getPeerModule() {
	return this.peerModule;
    }

    /**
     * Return the session attached to the current module.
     * <p>
     * <p>
     * This session is used to manage the module lifecycle by declaring the
     * desired implementation on start, select... methods.
     */
    @Override
    public IModuleSession getSession() {
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

    @Override
    public int hashCode() {
	final int prime = 31;
	int result = super.hashCode();
	result = prime * result + ((peerModule == null) ? 0 : peerModule.hashCode());
	result = prime * result + ((session == null) ? 0 : session.hashCode());
	return result;
    }

    @Override
    public boolean equals(Object obj) {
	if (this == obj)
	    return true;
	if (!super.equals(obj))
	    return false;
	if (getClass() != obj.getClass())
	    return false;
	DCaseModule other = (DCaseModule) obj;
	if (peerModule == null) {
	    if (other.peerModule != null)
		return false;
	} else if (!peerModule.equals(other.peerModule))
	    return false;
	if (session == null) {
	    if (other.session != null)
		return false;
	} else if (!session.equals(other.session))
	    return false;
	return true;
    }

}
