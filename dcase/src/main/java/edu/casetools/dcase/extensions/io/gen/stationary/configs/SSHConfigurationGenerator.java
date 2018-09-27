package edu.casetools.dcase.extensions.io.gen.stationary.configs;

import org.modelio.metamodel.uml.infrastructure.ModelElement;
import org.modelio.metamodel.uml.statik.Package;
import org.modelio.vcore.smkernel.mapi.MObject;

import edu.casetools.dcase.module.api.DCaseProperties;
import edu.casetools.dcase.module.impl.DCasePeerModule;
import edu.casetools.dcase.extensions.io.AbstractModelWriter;

public class SSHConfigurationGenerator extends AbstractModelWriter {

	private MObject veraRouter;
	private StringBuilder result;
	private enum SSH_FIELDS {HOSTNAME,PORT,USERNAME,PASSWORD};
	
	public SSHConfigurationGenerator(MObject veraRouter){
		this.veraRouter = veraRouter;
		this.result = new StringBuilder();
	}
	

	@Override
	public void write(Package model) {
		
	}

	@Override
	public String writeToString() {
		result = new StringBuilder();
		veraRouterConfigs();
		return result.toString();
	}

	private void append(String string) {
		result.append(string);
		separator();
	}


	private void veraRouterConfigs() {
		veraRouterConfig(true,SSH_FIELDS.HOSTNAME);
		appendModelioValue(veraRouter,DCaseProperties.PROPERTY_VERA_ROUTER_HOSTNAME);
		veraRouterConfig(false,SSH_FIELDS.HOSTNAME);
		
		veraRouterConfig(true,SSH_FIELDS.PORT);
		appendModelioValue(veraRouter,DCaseProperties.PROPERTY_VERA_ROUTER_PORT);
		veraRouterConfig(false,SSH_FIELDS.PORT);

		veraRouterConfig(true,SSH_FIELDS.USERNAME);
		appendModelioValue(veraRouter,DCaseProperties.PROPERTY_VERA_ROUTER_USERNAME);
		veraRouterConfig(false,SSH_FIELDS.USERNAME);
		
		veraRouterConfig(true,SSH_FIELDS.PASSWORD);
		appendModelioValue(veraRouter,DCaseProperties.PROPERTY_VERA_ROUTER_PASSWORD);		
		veraRouterConfig(false,SSH_FIELDS.PASSWORD);
	}
	
	private void veraRouterConfig(boolean open, SSH_FIELDS field) {
		if(open) open();
		else openBar();
		switch(field){
			case HOSTNAME:
				result.append("HOSTNAME");
				break;
			case PORT:
				result.append("PORT");
				break;
			case USERNAME:
				result.append("USERNAME");
				break;
			case PASSWORD:
				result.append("PASSWORD");
				break;
		}
		close();
		separator();
	}

	private void separator() {
		result.append(System.lineSeparator());
	}

	private void appendModelioValue(MObject element, String property) {
		append(((ModelElement) element).getTagValue(DCasePeerModule.MODULE_NAME,
					    property));
	}
		
	private void open(){
		result.append("<");
	}
	
	private void close(){
		result.append(">");
	}
	
	private void openBar(){
		result.append("</");
	}
	
}
