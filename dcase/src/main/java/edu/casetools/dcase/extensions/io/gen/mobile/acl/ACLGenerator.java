package edu.casetools.dcase.extensions.io.gen.mobile.acl;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.modelio.metamodel.uml.infrastructure.ModelElement;
import org.modelio.vcore.smkernel.mapi.MObject;

import edu.casetools.dcase.extensions.io.gen.mobile.acl.classes.PullObserverGenerator;
import edu.casetools.dcase.extensions.io.gen.mobile.acl.classes.PushObserverGenerator;
import edu.casetools.dcase.extensions.io.gen.mobile.acl.classes.ReceiverGenerator;
import edu.casetools.dcase.module.api.DCaseProperties;
import edu.casetools.dcase.module.api.DCaseStereotypes;
import edu.casetools.dcase.module.i18n.I18nMessageService;
import edu.casetools.dcase.module.impl.DCaseModule;
import edu.casetools.dcase.module.impl.DCasePeerModule;
import edu.casetools.rcase.utils.tables.TableUtils;

public class ACLGenerator {
   
    public void generateTemplates(String folder) {
		List<MObject> contextAttributeList = generateContextAttributes(folder);
		
		if (!contextAttributeList.isEmpty()) {
		    generateReceiver(folder);
		}

    }

	private List<MObject> generateContextAttributes(String folder) {
		List<MObject> contextAttributeList = new ArrayList<>();
		contextAttributeList = TableUtils.getInstance().getAllElementsStereotypedAs(DCaseModule.getInstance(), 
			DCasePeerModule.MODULE_NAME, contextAttributeList, DCaseStereotypes.STEREOTYPE_MOBILE_SENSOR);
		
		for (MObject contextAttribute : contextAttributeList) {
		    String type = ((ModelElement) contextAttribute).getTagValue(DCasePeerModule.MODULE_NAME,
			    DCaseProperties.PROPERTY_SENSOR_RESPONSIBILITY);
		    if (type.equals(I18nMessageService.getString("Ui.ACLContext.Property.TagResponsibility.Pull"))) {
				String frequency = ((ModelElement) contextAttribute).getTagValue(DCasePeerModule.MODULE_NAME,
					DCaseProperties.PROPERTY_MOBILE_SENSOR_FREQUENCY);
				generatePullObserver(contextAttribute, folder, frequency);
		    } else if (type.equals(I18nMessageService.getString("Ui.ACLContext.Property.TagResponsibility.Push"))) {
		    	generatePushObserver(contextAttribute, folder);
		    }

		}
		return contextAttributeList;
	}

    private void generateReceiver(String folder) {
		try {
			new ReceiverGenerator().generate().writeTo(new File(folder));
		} catch (IOException e) {
		    e.printStackTrace();
		}

    }

    private void generatePullObserver(MObject contextAttribute, String folder, String frequency) {
		try {
			new PullObserverGenerator(contextAttribute,frequency).generate().writeTo(new File(folder));
	
		} catch (IOException e) {
		    e.printStackTrace();
		}
    }

    private void generatePushObserver(MObject contextAttribute, String folder) {
		try {
		    new PushObserverGenerator(contextAttribute).generate().writeTo(new File(folder));
	
		} catch (IOException e) {
		    e.printStackTrace();
		}	
    }



}
