package edu.casetools.dcase.extensions.io.gen.mobile.areasoner;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.dialogs.MessageDialog;
import org.modelio.metamodel.uml.infrastructure.Dependency;
import org.modelio.metamodel.uml.infrastructure.ModelElement;
import org.modelio.vcore.smkernel.mapi.MObject;

import edu.casetools.dcase.extensions.io.gen.TemplateManager;
import edu.casetools.dcase.extensions.io.gen.mobile.areasoner.classes.contextmapper.ContextMapperGenerator;
import edu.casetools.dcase.extensions.io.gen.mobile.areasoner.classes.ontologies.OntologyManagerGenerator;
import edu.casetools.dcase.extensions.io.gen.mobile.areasoner.classes.prefs.PreferencesGenerator;
import edu.casetools.dcase.extensions.io.gen.mobile.areasoner.classes.soi.SOIGenerator;
import edu.casetools.dcase.module.api.DCaseProperties;
import edu.casetools.dcase.module.api.DCaseStereotypes;
import edu.casetools.dcase.module.impl.DCaseModule;
import edu.casetools.dcase.module.impl.DCasePeerModule;
import edu.casetools.rcase.module.api.RCaseProperties;
import edu.casetools.rcase.module.api.RCaseStereotypes;
import edu.casetools.rcase.module.impl.RCasePeerModule;
import edu.casetools.rcase.utils.tables.TableUtils;

public class AReasonerGenerator implements TemplateManager{

	
	 @Override
	 public void generateTemplates(String folder){
			getAndroidReasoner(folder);
			getSituationsOfInterest(folder); 
			getPreferences(folder);
	 }

		private void getPreferences(String folder) {
			List<MObject> preferences = TableUtils.getInstance().getAllElementsStereotypedAs(DCaseModule.getInstance(), 
				DCasePeerModule.MODULE_NAME, new ArrayList<>(), DCaseStereotypes.STEREOTYPE_PREFERENCE_SENSOR);
			List<MObject> filteredPreferences = new ArrayList<>();
			
			for(MObject preference: preferences){
				for(MObject dependency : preference.getCompositionChildren()){
					if( ((ModelElement)dependency).isStereotyped(DCasePeerModule.MODULE_NAME, DCaseStereotypes.STEREOTYPE_FEEDS)){
						MObject rule = ((Dependency) dependency).getDependsOn();
						if(((ModelElement) rule).isStereotyped(DCasePeerModule.MODULE_NAME, DCaseStereotypes.STEREOTYPE_RDF_MODELLING_RULE)){
							filteredPreferences.add(preference);
						}
					}
				}
			}
			generatePreferences(folder, filteredPreferences);
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
			    	generateOntologyManager(folder,baseOntologyString,streamIRIString);
			    }

			}
			return androidReasonerList;
		}
		
		private void getSituationsOfInterest(String folder) {
			List<MObject> usedSOIs = new ArrayList<>();
			List<MObject> detectionPlanList = TableUtils.getInstance().getAllElementsStereotypedAs(DCaseModule.getInstance(), 
				RCasePeerModule.MODULE_NAME, new ArrayList<>(), RCaseStereotypes.STEREOTYPE_SITUATION_DETECTION_PLAN);
			
			for (MObject detectionPlan : detectionPlanList) {
				MObject soi = null;
				List<MObject> contextAttributeList = new ArrayList<>();
				if(isToBeImplemented(detectionPlan)){
					for (MObject element : detectionPlan.getCompositionChildren()) {
					    if ((element instanceof ModelElement)){
					    	if(isDetects(element)) 
					    		if(isSOI(((Dependency) element).getDependsOn()))
					    			soi = ((Dependency) element).getDependsOn();
					    	if(isContextAttribute(element))
					    		contextAttributeList.add(element);
					    }
					}
				}
				if(soi==null)
					MessageDialog.openInformation(null, "Error", "No situation of interest associated to the '"+detectionPlan.getName()+"' detection plan.");
				else if(contextAttributeList.isEmpty())
					MessageDialog.openInformation(null, "Error", "No context attributes to define the '"+detectionPlan.getName()+"' detection plan.");
				else if(usedSOIs.contains(soi))
					MessageDialog.openInformation(null, "Warning", "The situation of interest '"+soi.getName()+"' is referenced by more than one detection plan. The detection plan '"+detectionPlan.getName()+"' will not be implemented.");
				else{
					generateSituationOfInterest(folder, soi, contextAttributeList);
					usedSOIs.add(soi);
				}
			}
			generateContextMapper(folder,usedSOIs);
		}
		


		private boolean isToBeImplemented(MObject detectionPlan) {
			if(detectionPlan instanceof ModelElement){
				String result = ((ModelElement) detectionPlan).getTagValue(RCasePeerModule.MODULE_NAME,
			    RCaseProperties.PROPERTY_SITUATION_DETECTION_PLAN_TOBEIMPLEMENTED);
			    if(result.equalsIgnoreCase("True"))
			    	return true;
			}
			return false;
		}

		private boolean isDetects(MObject element) {
	    	return ((ModelElement) element).isStereotyped(RCasePeerModule.MODULE_NAME,
	    		RCaseStereotypes.STEREOTYPE_CONTEXT_DETECTS);
	    }
		
		private boolean isSOI(MObject element) {
	    	return ((ModelElement) element).isStereotyped(RCasePeerModule.MODULE_NAME,
	    		RCaseStereotypes.STEREOTYPE_SITUATION_OF_INTEREST);
	    }
		
		private boolean isContextAttribute(MObject element) {
	    	return ((ModelElement) element).isStereotyped(RCasePeerModule.MODULE_NAME,
	    		RCaseStereotypes.STEREOTYPE_CONTEXT_ATTRIBUTE);
	    }

	    private void generateOntologyManager(String folder, String baseOntologyString, String streamIRIString) {
			try {
				new OntologyManagerGenerator(baseOntologyString, streamIRIString).generate().writeTo(new File(folder));
			} catch (IOException e) {
			    e.printStackTrace();
			}

	    }
	    
	    private void generateContextMapper(String folder, List<MObject> usedSOIs) {
			try {
				new ContextMapperGenerator(usedSOIs).generate().writeTo(new File(folder));
			} catch (IOException e) {
			    e.printStackTrace();
			}
		
		}
	    
	    private void generateSituationOfInterest(String folder, MObject soi, List<MObject> contextAttributeList) {
	    	try {
	    		new SOIGenerator(soi,contextAttributeList).generate().writeTo(new File(folder));
			} catch (IOException e) {
			    e.printStackTrace();
			}

	    }
	    
	    private void generatePreferences(String folder, List<MObject> preferences) {
	    	try {
	    		new PreferencesGenerator(preferences).generate().writeTo(new File(folder));
			} catch (IOException e) {
			    e.printStackTrace();
			}

	    }

}
