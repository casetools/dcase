package edu.casetools.dcase.extensions.io.gen.stationary.classes;

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

public class DeploymentModuleGenerator implements ClassTemplate{

	private MObject 		 soi;
	private List<MObject>    contextAttributeList;
	private List<FieldSpec>  modellingFields;
	private List<MethodSpec> modellingMethods;
	private List<FieldSpec>  reasoningFields;
	private List<MethodSpec> reasoningMethods;
	private List<Rule> 		 usedRules;
	private List<String>	 modellingRuleMethodNames;
	private List<String>	 reasoningRuleMethodNames;
	private MData reasoningRuleData;
	
	public DeploymentModuleGenerator(MObject soi, List<MObject> contextAttributeList){
		this.soi 				  		= soi;
		this.contextAttributeList 		= contextAttributeList;
		this.modellingFields      		= new ArrayList<FieldSpec>();
		this.modellingMethods     		= new ArrayList<MethodSpec>();	
		this.reasoningFields      		= new ArrayList<FieldSpec>();
		this.reasoningMethods     	  	= new ArrayList<MethodSpec>();	
		this.usedRules 			  	 	= new ArrayList<Rule>();
	    this.modellingRuleMethodNames 	= new ArrayList<String>();
	    this.reasoningRuleMethodNames 	= new ArrayList<String>();
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
		// TODO CREATE PREFERENCES VARIABLES
		ClassName reasonerManager = ClassName.get("org.poseidon_project.context.reasoner", "ReasonerManager");
		ClassName abstractContextMapper = ClassName.get("org.poseidon_project.context.reasoner", "AbstractContextMapper");
		ClassName dataLogger = ClassName.get("org.poseidon_project.context.logging", "DataLogger");
		ClassName sharedPreferences = ClassName.get("android.content", "SharedPreferences");

		MethodSpec.Builder builder =   MethodSpec.methodBuilder("registerSituationOfInterest")
				 .addModifiers(Modifier.PUBLIC)
				 .returns(boolean.class)
				 .addParameter(reasonerManager, "mReasonerManager")
				 .addParameter(abstractContextMapper, "contextMapper")
				 .addParameter(sharedPreferences, "mRuleSettings")
				 .addParameter(dataLogger, "mLogger")
				 .addParameter(String.class, "logTag")
				 .addParameter(Map.class, "parameters");
		 builder.addStatement("$L", "boolean okExit = true");
		 for(int i=0;i<modellingRuleMethodNames.size();i++){
			 builder.addStatement("eu.larkc.csparql.core.engine.CsparqlQueryResultProxy c$L = mReasonerManager.registerCSPARQLQuery(get$L())", i,modellingRuleMethodNames.get(i)); //TODO ADD PREFERENCE VARIABLES
			 builder.addStatement("okExit = contextMapper.registerModellingRule(\"$L\", c$L, okExit)", modellingRuleMethodNames.get(i),i);
		 }
		 for(int i=0;i<reasoningRuleMethodNames.size();i++){
			 builder.addStatement("mReasonerManager.registerReasoningRule(\"$L\", get$L())", reasoningRuleMethodNames.get(i),reasoningRuleMethodNames.get(i)); //TODO ADD PREFERENCE VARIABLES
		 }
		// TODO CREATE PREFERENCES METHODS
		 builder.addStatement("mLogger.logVerbose(DataLogger.REASONER, logTag, \"Registered $L Situation of Interest\")", StringUtils.uppercaseFirstLetter(StringUtils.camelCase(soi.getName(),' ')+"SOI"));
		 builder.addStatement("return contextMapper.addObserverRequirementWithParameters(\"engine\", \"$L\",okExit, parameters)", StringUtils.uppercaseFirstLetter(StringUtils.camelCase(soi.getName(),' ')+"SOI"));
		 return builder.build();
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
		 for(int i=0;i<modellingRuleMethodNames.size();i++){
			 builder.addStatement("okExit = contextMapper.unregisterModellingRule(\"$L\", okExit)", modellingRuleMethodNames.get(i));
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
			List<MObject> sensors = getContextPrefAttributeSensors(contextAttribute);
			for(MObject sensor : sensors){
				
				for(MObject rule : getSensorRules(sensor,DCaseStereotypes.STEREOTYPE_FEEDS_IN_WINDOW)){
					generateModellingRule(rule);
					handleReasoningRuleFromModellingRule(rule);
				}
				for(MObject rule : getSensorRules(sensor,DCaseStereotypes.STEREOTYPE_FEEDS)){
					// TO BE IMPLEMENTED: FEATURE FOR GENERATING PREFS
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
		}
		for(Rule reasoningRule : reasoningRules){
			if(!checkIsRepeated(reasoningRule)){
				generateReasoningRule(reasoningRule);
				this.usedRules.add(reasoningRule);
			}
		}
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
		this.modellingFields.add(generateField(rule.getName(), CSPARQLWriter.generateCSPARQLQuery(result, (ModelElement)rule).toString()));
		this.modellingMethods.add(generateMethod(rule.getName()));
	    this.modellingRuleMethodNames.add(StringUtils.uppercaseFirstLetter(StringUtils.camelCase(rule.getName(),' ')));
	}
	
	private void generateReasoningRule(Rule rule) {
		String name = StringUtils.camelCase(soi.getName(),' ')+StringUtils.camelCase(rule.getConsequent().getName(),' ');
		this.reasoningFields.add(generateField(name, MobileReasoner.generateReasoningRuleQuery(rule).toString()));
		this.reasoningMethods.add(generateMethod(name));
		this.reasoningRuleMethodNames.add(StringUtils.uppercaseFirstLetter(StringUtils.camelCase(name,' ')));
	}
	
	private MethodSpec generateMethod(String name) {
		return MethodSpec.methodBuilder("get"+StringUtils.uppercaseFirstLetter(StringUtils.camelCase(name,' '))).addModifiers(Modifier.PUBLIC)
				.returns(String.class).addStatement("$L", "return "+name)
				.build();
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

	private List<MObject> getContextPrefAttributeSensors(MObject contextAttribute) {
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
