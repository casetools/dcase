package edu.casetools.dcase.extensions.io.gen.mobile.areasoner.classes.soi;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.lang.model.element.Modifier;

import org.metawidget.util.simple.StringUtils;
import org.modelio.metamodel.uml.infrastructure.Dependency;
import org.modelio.metamodel.uml.infrastructure.ModelElement;
import org.modelio.vcore.smkernel.mapi.MObject;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;
import com.squareup.javapoet.TypeSpec.Builder;

import edu.casetools.dcase.extensions.io.gen.ClassTemplate;
import edu.casetools.dcase.extensions.io.gen.mobile.areasoner.classes.prefs.PreferencesGenerator;
import edu.casetools.dcase.extensions.io.gen.mobile.areasoner.csparql.CSPARQLWriter;
import edu.casetools.dcase.extensions.io.gen.mobile.areasoner.reasoner.MobileReasoner;
import edu.casetools.dcase.extensions.io.gen.stationary.reasoner.MdData;
import edu.casetools.dcase.m2nusmv.data.MData;
import edu.casetools.dcase.m2nusmv.data.elements.Rule;
import edu.casetools.dcase.m2nusmv.data.elements.RuleElement;
import edu.casetools.dcase.module.api.DCaseStereotypes;
import edu.casetools.dcase.module.impl.DCaseModule;
import edu.casetools.dcase.module.impl.DCasePeerModule;
import edu.casetools.rcase.utils.tables.TableUtils;

public class SOIGenerator implements ClassTemplate{

	private MObject 		 soi;
	private List<MObject>    contextAttributeList;
	private List<FieldSpec>  modellingFields;
	private List<MethodSpec> modellingMethods;
	private List<FieldSpec>  reasoningFields;
	private List<MethodSpec> reasoningMethods;
	private List<Rule> 		 usedRules;
	private List<MObject>	 modellingRules;
	private List<String>	 reasoningRuleMethodNames;
	private MData 			 reasoningRuleData;
	private List<MObject>    preferences;
	
	public SOIGenerator(MObject soi, List<MObject> contextAttributeList){
		this.soi 				  		= soi;
		this.contextAttributeList 		= contextAttributeList;
		this.modellingFields      		= new ArrayList<FieldSpec>();
		this.modellingMethods     		= new ArrayList<MethodSpec>();	
		this.reasoningFields      		= new ArrayList<FieldSpec>();
		this.reasoningMethods     	  	= new ArrayList<MethodSpec>();	
		this.usedRules 			  	 	= new ArrayList<Rule>();
	    this.modellingRules 			= new ArrayList<MObject>();
	    this.reasoningRuleMethodNames 	= new ArrayList<String>();
	    this.preferences			 	= new ArrayList<MObject>();
	    MdData dData   			  	  	= new MdData();
	    dData.loadDiagramElements();
	    this.reasoningRuleData        	= dData.getMData();

	}
	
	@Override
	public JavaFile generate() {
		ClassName currentSOIClass = ClassName.get("edu.casetools.icase.custom.situations", StringUtils.uppercaseFirstLetter(StringUtils.camelCase(soi.getName(),' ')+"SOI"));
		ClassName SOIClass 		  = ClassName.get("org.poseidon_project.context.reasoner", "SituationOfInterest");
		Builder typeSpecBuilder   = TypeSpec.classBuilder(currentSOIClass).addModifiers(Modifier.PUBLIC).superclass(SOIClass); 
		
		DateFormat dateFormat 	  = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		Date date = new Date();
		
		this.modellingFields  	  = new ArrayList<FieldSpec>();
		this.modellingMethods 	  = new ArrayList<MethodSpec>();		

		typeSpecBuilder = typeSpecBuilder.addMethod(MethodSpec.constructorBuilder().addModifiers(Modifier.PUBLIC)
        .addStatement("super($S)",StringUtils.uppercaseFirstLetter(StringUtils.camelCase(soi.getName(),' ')+"SOI")).build());
        
		
		generateRules();
		
		
		typeSpecBuilder = generateMethodsAndFields(typeSpecBuilder);
		
		return JavaFile.builder("edu.casetools.icase.custom.situations", typeSpecBuilder.build())
				.addFileComment(
					"/* This code skeleton has been automatically generated \n * as part of the DCase Android Context Library code generator \n * Date: $L, \n */",
					dateFormat.format(date))
				.build();

	//	TypeSpec contextClass = TypeSpec.classBuilder("OntologyManager").addModifiers(Modifier.PUBLIC)
	//		.addJavadoc("import android.os.Environment;\n").superclass(ontologyManager).addField(baseOntology).addField(streamIRI).build();
	
	//		return JavaFile.builder("edu.casetools.icase.custom.situations", contextClass)
	//			.addFileComment(
	//				"/* This code skeleton has been automatically generated \n * as part of the DCase Android Context Library code generator \n * Date: $L, \n */",
	//				dateFormat.format(date))
	//			.build();
	}

	private Builder generateMethodsAndFields(Builder typeSpecBuilder) {
		
	
		for(FieldSpec field : modellingFields){
			typeSpecBuilder = typeSpecBuilder.addField(field);
		}
		
		for(MethodSpec method : modellingMethods){
			typeSpecBuilder = typeSpecBuilder.addMethod(method);
		}
		
		for(FieldSpec field : reasoningFields){
			typeSpecBuilder = typeSpecBuilder.addField(field);
		}
		
		for(MethodSpec method : reasoningMethods){
			typeSpecBuilder = typeSpecBuilder.addMethod(method);
		}
		
		typeSpecBuilder = typeSpecBuilder.addMethod(generateRegisterSOIMethod());
		typeSpecBuilder = typeSpecBuilder.addMethod(generateUnRegisterSOIMethod());
		return typeSpecBuilder;
	}
	

	private MethodSpec generateRegisterSOIMethod() {
		ClassName reasonerManager = ClassName.get("org.poseidon_project.context.reasoner", "ReasonerManager");
		ClassName abstractContextMapper = ClassName.get("org.poseidon_project.context.reasoner", "AbstractContextMapper");
		ClassName dataLogger = ClassName.get("org.poseidon_project.context.logging", "DataLogger");
		ClassName sharedPreferences = ClassName.get("android.content", "SharedPreferences");
		ClassName prefs = ClassName.get("edu.casetools.icase.custom.preferences", "Preferences");

		
		MethodSpec.Builder builder =   MethodSpec.methodBuilder("registerSituationOfInterest")
				 .addModifiers(Modifier.PUBLIC)
				 .returns(boolean.class)
				 .addParameter(reasonerManager, "mReasonerManager")
				 .addParameter(abstractContextMapper, "contextMapper")
				 .addParameter(sharedPreferences, "mRuleSettings")
				 .addParameter(dataLogger, "mLogger")
				 .addParameter(String.class, "logTag")
				 .addParameter(Map.class, "parameters");
		for(MObject preference : preferences){
		builder.addStatement("String $L    = String.valueOf(mRuleSettings.get$L($T.$L, $L))", 
				preference.getName().replaceAll("\\s+", "_"),
				PreferencesGenerator.getPreferenceDataType(preference),
				prefs,
				StringUtils.uncamelCase(preference.getName()).replaceAll("\\s+", "_").toUpperCase(), PreferencesGenerator.getPreferenceValue(preference));
		}
		
		 builder.addStatement("$L", "boolean okExit = true");
		 for(int i=0;i<modellingRules.size();i++){
			 String ruleName = StringUtils.uppercaseFirstLetter(StringUtils.camelCase(modellingRules.get(i).getName(),' '));
			 builder.addStatement("eu.larkc.csparql.core.engine.CsparqlQueryResultProxy c$L = mReasonerManager.registerCSPARQLQuery(get$L($L))", i,ruleName, getProperties(modellingRules.get(i))); //TODO ADD PREFERENCE VARIABLES
			 builder.addStatement("okExit = contextMapper.registerModellingRule(\"$L\", c$L, okExit)", ruleName,i);
		 }
		 for(int i=0;i<reasoningRuleMethodNames.size();i++){
			 builder.addStatement("mReasonerManager.registerReasoningRule(\"$L\", get$L())", reasoningRuleMethodNames.get(i),reasoningRuleMethodNames.get(i)); //TODO ADD PREFERENCE VARIABLES
		 }
		// TODO CREATE PREFERENCES METHODS
		 builder.addStatement("mLogger.logVerbose(DataLogger.REASONER, logTag, \"Registered $L Situation of Interest\")", StringUtils.uppercaseFirstLetter(StringUtils.camelCase(soi.getName(),' ')+"SOI"));
		 builder.addStatement("return contextMapper.addObserverRequirementWithParameters(\"engine\", \"$L\",okExit, parameters)", StringUtils.uppercaseFirstLetter(StringUtils.camelCase(soi.getName(),' ')+"SOI"));
		 return builder.build();
	}

	private String getProperties(MObject rule) {
		String result = "";
		for(Dependency relation : ((ModelElement)rule).getImpactedDependency()){
			if(relation.isStereotyped(DCasePeerModule.MODULE_NAME, DCaseStereotypes.STEREOTYPE_FEEDS)){
				MObject element = ((Dependency)relation).getImpacted();
				if(((ModelElement)element).isStereotyped(DCasePeerModule.MODULE_NAME, DCaseStereotypes.STEREOTYPE_PREFERENCE_SENSOR)){
					if(result.equals("")){
						result = element.getName();
					} else {
						result = result + ", "+ element.getName();
					}
				}
			}
		}
		
		
		return result;
	}

	private MethodSpec generateUnRegisterSOIMethod() {
		// TODO CREATE PREFERENCES VARIABLES
		ClassName contextReasonerCore = ClassName.get("org.poseidon_project.context", "ContextReasonerCore");
		ClassName reasonerManager = ClassName.get("org.poseidon_project.context.reasoner", "ReasonerManager");
		ClassName abstractContextMapper = ClassName.get("org.poseidon_project.context.reasoner", "AbstractContextMapper");
		ClassName dataLogger = ClassName.get("org.poseidon_project.context.logging", "DataLogger");
		MethodSpec.Builder builder =   MethodSpec.methodBuilder("unRegisterSituationOfInterest")
				 .addModifiers(Modifier.PUBLIC)
				 .returns(boolean.class)
				 .addParameter(contextReasonerCore, "mReasonerCore")
				 .addParameter(reasonerManager, "mReasonerManager")
				 .addParameter(abstractContextMapper, "contextMapper")
				 .addParameter(dataLogger, "mLogger")
				 .addParameter(String.class, "logTag");
		 builder.addStatement("boolean okExit = contextMapper.removeObserverRequirement(\"engine\", \"$L\")", StringUtils.uppercaseFirstLetter(StringUtils.camelCase(soi.getName(),' ')+"SOI"));
		 for(int i=0;i<modellingRules.size();i++){
			 builder.addStatement("okExit = contextMapper.unregisterModellingRule(\"$L\", okExit)", StringUtils.uppercaseFirstLetter(StringUtils.camelCase(modellingRules.get(i).getName(),' ')));
		 }
		 for(int i=0;i<reasoningRuleMethodNames.size();i++){
			 builder.addStatement("mReasonerManager.unregisterReasoningRule(\"$L\")", reasoningRuleMethodNames.get(i)); //TODO ADD PREFERENCE VARIABLES
		 }
		// TODO UNREGISTER PREFERENCES METHODS
		 builder.addStatement("mLogger.logVerbose(DataLogger.REASONER, logTag, \"Unregistered $L Situation of Interest\")", StringUtils.uppercaseFirstLetter(StringUtils.camelCase(soi.getName(),' ')+"SOI"));
		 builder.addStatement("return okExit", StringUtils.uppercaseFirstLetter(StringUtils.camelCase(soi.getName(),' ')+"SOI"));
		 return builder.build();
	}
	
	private void generateRules() {
	
		for(MObject contextAttribute:contextAttributeList){
			List<MObject> sensors = getContextAttributeSensors(contextAttribute);
			for(MObject sensor : sensors){

				for(MObject rule : getSensorRules(sensor,DCaseStereotypes.STEREOTYPE_FEEDS_IN_WINDOW)){
					generateModellingRule(rule);
					handleReasoningRuleFromModellingRule(rule);
				}				
			}
		}
	}

	private void handleReasoningRuleFromModellingRule(MObject modellingRule) {
		List<MObject> contextStates = getContextStates(modellingRule);
		List<Rule> reasoningRules = new ArrayList<>();
		 //FIND CORRESPONDING RULES
				//FIND STR
		for(MObject contextState : contextStates){
			reasoningRules = getReasoningRulesFromContextState(contextState,reasoningRules);
			reasoningRules.addAll(getDependentContextStatesRules(contextState, reasoningRules));
		}
		for(Rule reasoningRule : reasoningRules){
			if(!checkIsRepeated(reasoningRule)){
				generateReasoningRule(reasoningRule);
				this.usedRules.add(reasoningRule);
			}
		}
	}
	
	private List<Rule> getDependentContextStatesRules(MObject contextState, List<Rule> reasoningRules) {
		List<Rule> dependentContextStatesRules = new ArrayList<Rule>();
		
		for(Rule str : reasoningRuleData.getStrs()){
			for(RuleElement antecedent : str.getAntecedents()){
				if(antecedent.getName().equals(contextState.getName())){
					if(!reasoningRules.contains(str) && !usedRules.contains(str)){
						dependentContextStatesRules.add(str);
						usedRules.add(str);
					}
				}
			}
		} 
		
		for(Rule ntr : reasoningRuleData.getNtrs()){
			for(RuleElement antecedent : ntr.getAntecedents()){
				if(antecedent.getName().equals(contextState.getName())){
					if(!reasoningRules.contains(ntr) && !usedRules.contains(ntr)){
						dependentContextStatesRules.add(ntr);
						usedRules.add(ntr);
					}
				}
			}
		} 
		
		return dependentContextStatesRules;
	}

	private boolean checkIsRepeated(Rule reasoningRule) {
		for(Rule storedRule : this.usedRules){
			if(storedRule.equals(reasoningRule)){
				return true;
			}
		}
		return false;
	}

	private List<Rule> getReasoningRulesFromContextState(MObject contextState, List<Rule> reasoningRules){
		for(Rule str : reasoningRuleData.getStrs()){
			for(RuleElement antecedent : str.getAntecedents()){
				if(antecedent.getName().equals(contextState.getName())){
					reasoningRules.add(str);
				}
			}
		}
		for(Rule ntr : reasoningRuleData.getNtrs()){
			for(RuleElement antecedent : ntr.getAntecedents()){
				if(antecedent.getName().equals(contextState.getName())){
					reasoningRules.add(ntr);
				}
			}
		}
		return reasoningRules;
	}

	private List<MObject> getContextStates(MObject rule) {
		List<MObject> contextStates = new ArrayList<>();
		for(MObject element : rule.getCompositionChildren()){
			if(isStereotyped(element, DCaseStereotypes.STEREOTYPE_PRODUCE)){
				contextStates.add(((Dependency)element).getDependsOn());
			}
		}
		return contextStates;
	}

	private void generateModellingRule(MObject rule) {
		StringBuilder result = new StringBuilder(150);
		String name = StringUtils.lowercaseFirstLetter(StringUtils.camelCase(rule.getName(),' '));
		this.modellingFields.add(generateField(name, CSPARQLWriter.generateCSPARQLQuery(result, (ModelElement)rule).toString()));
		MethodSpec.Builder methodBuilder = generateMethod(name);
		for(MObject preference : getRulePreferences(rule)){
			String preferenceName = StringUtils.lowercaseFirstLetter(StringUtils.camelCase(preference.getName(),' ')+"Value");
			methodBuilder.addParameter(String.class, preferenceName);
			methodBuilder.addStatement("query = query.replace($S, String.valueOf($L))", "$$"+ StringUtils.uppercaseFirstLetter(StringUtils.camelCase(preference.getName())), preferenceName);
		}
		methodBuilder.addStatement("$L", "return query");
		this.modellingMethods.add(methodBuilder.build());
	    this.modellingRules.add(rule);
	    getPreferences(rule);
	}

	private void getPreferences(MObject rule) {
		for(Dependency relation : ((ModelElement)rule).getImpactedDependency()){
			if(relation.isStereotyped(DCasePeerModule.MODULE_NAME, DCaseStereotypes.STEREOTYPE_FEEDS)){
				ModelElement element = (ModelElement) relation.getImpacted();
				if(element.isStereotyped(DCasePeerModule.MODULE_NAME, DCaseStereotypes.STEREOTYPE_PREFERENCE_SENSOR)){
					preferences.add(element);
				}
			}
		}
		
	}

	private List<MObject> getRulePreferences(MObject rule) {
		List<MObject> preferences = new ArrayList<>();
		for(Dependency relation : ((ModelElement)rule).getImpactedDependency()){
			if(relation.isStereotyped(DCasePeerModule.MODULE_NAME, DCaseStereotypes.STEREOTYPE_FEEDS)){
				ModelElement sensor = (ModelElement) relation.getImpacted();
				if(sensor.isStereotyped(DCasePeerModule.MODULE_NAME, DCaseStereotypes.STEREOTYPE_PREFERENCE_SENSOR)){
					preferences.add(sensor);
				}
			}
		}
		return preferences;
	}

	private void generateReasoningRule(Rule rule) {
		String name = StringUtils.camelCase(soi.getName(),' ')+StringUtils.camelCase(rule.getConsequent().getName(),' ');
		this.reasoningFields.add(generateField(name, MobileReasoner.generateReasoningRuleQuery(rule).toString()));
		this.reasoningMethods.add(generateMethod(name).addStatement("return query").build());
		this.reasoningRuleMethodNames.add(StringUtils.uppercaseFirstLetter(StringUtils.camelCase(name,' ')));
	}
	
	private MethodSpec.Builder generateMethod(String name) {
		return MethodSpec.methodBuilder("get"+StringUtils.uppercaseFirstLetter(StringUtils.camelCase(name,' ')))
				.addModifiers(Modifier.PUBLIC)
				.addStatement("String query = new String($L)",name)
				.returns(String.class);
	}
	
	private FieldSpec generateField(String name, String query){
		query = query.replaceAll("\"", "\\\\\"");
		query = query.replaceAll(System.lineSeparator(), "\" +"+System.lineSeparator()+"\"");
		return FieldSpec.builder(String.class, name)
			    .addModifiers(Modifier.PRIVATE)
			    .addModifiers(Modifier.FINAL)
			    .initializer("\"$L\"",query)
			    .build();
	}

	private List<MObject> getSensorRules(MObject sensor, String stereotype) {
		List<MObject> sensorRules = new ArrayList<>();
		for(MObject child:sensor.getCompositionChildren()){
			if(isStereotyped(child,stereotype)){
				sensorRules.add(((Dependency)child).getDependsOn());
			}
		}
		return sensorRules;
	}

	private boolean isStereotyped(MObject element, String stereotype) {
		return ((ModelElement) element).isStereotyped(DCasePeerModule.MODULE_NAME,
	    		stereotype);
	}

	private List<MObject> getContextAttributeSensors(MObject contextAttribute) {
		List<MObject> contextAttributeSensorList = new ArrayList<MObject>();
		List<MObject> sensorList = TableUtils.getInstance().getAllElementsStereotypedAs(DCaseModule.getInstance(), 
				DCasePeerModule.MODULE_NAME, new ArrayList<MObject>(), DCaseStereotypes.STEREOTYPE_MOBILE_SENSOR);
		
		for(MObject sensor : sensorList){
			for(MObject sensorChild : sensor.getCompositionChildren()){
				if(isObserve(sensorChild)){
					if(contextAttribute.getUuid().equals(((Dependency)sensorChild).getDependsOn().getUuid()))
						contextAttributeSensorList.add(sensor);	
				}
			}
		}
		
		
		return contextAttributeSensorList;
	}
	
	private boolean isObserve(MObject element){
		return ((ModelElement) element).isStereotyped(DCasePeerModule.MODULE_NAME,
	    		DCaseStereotypes.STEREOTYPE_OBSERVE);
	}

}
