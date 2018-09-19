package edu.casetools.dcase.extensions.io.gen.mobile.areasoner;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.modelio.metamodel.uml.infrastructure.ModelElement;
import org.modelio.vcore.smkernel.mapi.MObject;

import edu.casetools.dcase.extensions.io.gen.TemplateManager;
import edu.casetools.dcase.extensions.io.gen.mobile.acl.classes.ReceiverGenerator;
import edu.casetools.dcase.module.api.DCaseProperties;
import edu.casetools.dcase.module.api.DCaseStereotypes;
import edu.casetools.dcase.module.impl.DCaseModule;
import edu.casetools.dcase.module.impl.DCasePeerModule;
import edu.casetools.rcase.utils.tables.TableUtils;

public class AReasonerGenerator implements TemplateManager{

	 @Override
	 public void generateTemplates(String folder){
			List<MObject> contextAttributeList = getAndroidReasoner(folder);
			
			if (!contextAttributeList.isEmpty()) {
			    generateOntologyManager(folder);
			}

	 }

		private List<MObject> getAndroidReasoner(String folder) {
			List<MObject> androidReasonerList = new ArrayList<>();
			androidReasonerList = TableUtils.getInstance().getAllElementsStereotypedAs(DCaseModule.getInstance(), 
				DCasePeerModule.MODULE_NAME, androidReasonerList, DCaseStereotypes.STEREOTYPE_ANDROID_REASONER);
			
			for (MObject androidReasoner : androidReasonerList) {
			    String baseOntologyString = ((ModelElement) androidReasoner).getTagValue(DCasePeerModule.MODULE_NAME,
				    DCaseProperties.PROPERTY_ANDROID_REASONER_ONTOLOGY);
			    String streamIRIString = ((ModelElement) androidReasoner).getTagValue(DCasePeerModule.MODULE_NAME,
					    DCaseProperties.PROPERTY_ANDROID_REASONER_STREAM_IRI);
			    if(baseOntologyString != null && streamIRIString != null){
			    	generateOntologyManager(folder);
			    }

			}
			return androidReasonerList;
		}

	    private void generateOntologyManager(String folder) {
			try {
				new ReceiverGenerator().generate().writeTo(new File(folder));
			} catch (IOException e) {
			    e.printStackTrace();
			}

	    }

}
