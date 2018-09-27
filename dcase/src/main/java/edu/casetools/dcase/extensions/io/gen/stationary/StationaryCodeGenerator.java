package edu.casetools.dcase.extensions.io.gen.stationary;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.dialogs.MessageDialog;
import org.modelio.vcore.smkernel.mapi.MObject;

import edu.casetools.dcase.extensions.io.gen.TemplateManager;
import edu.casetools.dcase.extensions.io.gen.stationary.classes.JavaActuatorGenerator;
import edu.casetools.dcase.extensions.io.gen.stationary.configs.ConfigurationGenerator;
import edu.casetools.dcase.extensions.io.gen.stationary.configs.SSHConfigurationGenerator;
import edu.casetools.dcase.module.api.DCaseStereotypes;
import edu.casetools.dcase.module.impl.DCaseModule;
import edu.casetools.dcase.module.impl.DCasePeerModule;
import edu.casetools.rcase.utils.tables.TableUtils;

public class StationaryCodeGenerator implements TemplateManager{

	private final String configsFolder = "\\configurations";
	private final String mReasoner = "\\mreasoner";
	private final String configsFileName = "\\session.txt";
	private final String sshConfigsFileName = "\\ssh_configs.txt";
	
	 @Override
	 public void generateTemplates(String folder){
			getConfigurations(folder+configsFolder);
			getSSHConfigurations(folder+configsFolder);
			getJavaActuator(folder+mReasoner);
	 }

	private void getJavaActuator(String folder) {
		List<MObject> javaActuators = TableUtils.getInstance().getAllElementsStereotypedAs(DCaseModule.getInstance(), 
				DCasePeerModule.MODULE_NAME, new ArrayList<>(), DCaseStereotypes.STEREOTYPE_JAVA_ACTUATOR);
		for(MObject javaActuator : javaActuators){
			generateJavaActuator(folder, javaActuator);
		}
	
	}

	private void getSSHConfigurations(String folder) {
			List<MObject> configurationElements = TableUtils.getInstance().getAllElementsStereotypedAs(DCaseModule.getInstance(), 
					DCasePeerModule.MODULE_NAME, new ArrayList<>(), DCaseStereotypes.STEREOTYPE_VERA_ROUTER);
				if(!checkAList(configurationElements,"Vera router")) return;
			new SSHConfigurationGenerator(configurationElements.get(0)).writeToFile(new File(folder+sshConfigsFileName));
	}

		private void getConfigurations(String folder) {
			List<MObject> configurationElements = TableUtils.getInstance().getAllElementsStereotypedAs(DCaseModule.getInstance(), 
				DCasePeerModule.MODULE_NAME, new ArrayList<>(), DCaseStereotypes.STEREOTYPE_M_REASONER);
			if(!checkAList(configurationElements,"M Reasoner")) return;
			MObject mReasoner = configurationElements.get(0);
			configurationElements = TableUtils.getInstance().getAllElementsStereotypedAs(DCaseModule.getInstance(), 
					DCasePeerModule.MODULE_NAME, new ArrayList<>(), DCaseStereotypes.STEREOTYPE_M_DATABASE);
			if(!checkAList(configurationElements,"M Database")) return;
			MObject mDatabase = configurationElements.get(0);
			new ConfigurationGenerator(mReasoner, mDatabase).writeToFile(new File(folder+configsFileName));
		}
		
		

	    private boolean checkAList(List<MObject> configurationElements, String element) {
			if(configurationElements.size()>1){
				String mReasonerNames = "";
				for(MObject mReasoner : configurationElements){
					mReasonerNames = mReasonerNames + mReasoner.getName()+" ";
				}
				MessageDialog.openInformation(null, "Error", "More than one "+element+" declared in the model : "+mReasonerNames+" ");
				return false;
			}
			return true;
		}
	    
	    private void generateJavaActuator(String folder, MObject javaActuator) {
	    	try {
	    		new JavaActuatorGenerator(javaActuator).generate().writeTo(new File(folder));
			} catch (IOException e) {
			    e.printStackTrace();
			}

	    }
	    
	    	    
//	    private void generateMySQLRules(String folder, MObject soi, List<MObject> contextAttributeList) {
//	    	try {
//	    		new MySQLRuleGenerator(soi,contextAttributeList).generate().writeTo(new File(folder));
//			} catch (IOException e) {
//			    e.printStackTrace();
//			}
//
//	    }

}
