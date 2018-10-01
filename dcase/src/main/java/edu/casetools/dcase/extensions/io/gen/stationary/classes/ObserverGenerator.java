package edu.casetools.dcase.extensions.io.gen.stationary.classes;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.lang.model.element.Modifier;

import org.metawidget.util.simple.StringUtils;
import org.modelio.metamodel.uml.infrastructure.Dependency;
import org.modelio.metamodel.uml.infrastructure.ModelElement;
import org.modelio.vcore.smkernel.mapi.MObject;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.MethodSpec.Builder;
import com.squareup.javapoet.TypeSpec;

import edu.casetools.dcase.extensions.io.gen.ClassTemplate;
import edu.casetools.dcase.module.api.DCaseProperties;
import edu.casetools.dcase.module.api.DCaseStereotypes;
import edu.casetools.dcase.module.impl.DCasePeerModule;

public class ObserverGenerator implements ClassTemplate{

	MObject sensor;
	
	public ObserverGenerator(MObject sensor){
		this.sensor = sensor;
	}
	
	@Override
	public JavaFile generate() {
		String observerName = StringUtils.uppercaseFirstLetter(StringUtils.camelCase(sensor.getName(),' ')+"Observer");
		ClassName observer = ClassName.get("edu.casetools.icase.mreasoner.extensions.sensors", observerName);
		ClassName sensorObserver = ClassName.get("edu.casetools.icase.mreasoner.deployment.sensors", "SensorObserver");
	
		DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		Date date = new Date();

		MethodSpec.Builder methodBuilder = MethodSpec.methodBuilder("applyCustomModellingRules")
			.addModifiers(Modifier.PUBLIC)
			.addParameter(String.class,"stateName")
			.addParameter(String.class,"iteration")
			.addParameter(String.class,"value")
			.addStatement("$L sensorValue = $L.valueOf(value)", getTypeProperty(sensor, DCaseProperties.PROPERTY_STATIONARY_SENSOR_IS_BOOLEAN),getTypeProperty(sensor, DCaseProperties.PROPERTY_STATIONARY_SENSOR_IS_BOOLEAN)) //Get value from value type
			.addStatement("boolean result = false")
			.addAnnotation(Override.class)
			.returns(boolean.class);
		
		methodBuilder = addContextStates(methodBuilder); 

		methodBuilder = methodBuilder.addStatement("return result");
		
		TypeSpec contextClass = TypeSpec.classBuilder(observer)
			.addModifiers(Modifier.PUBLIC)
			.superclass(sensorObserver)
			.addMethod(methodBuilder.build()).build();

		return JavaFile.builder("edu.casetools.icase.mreasoner.extensions.sensors", contextClass)
			.addFileComment("/* This code skeleton has been automatically generated \n * as part of the DCase M Reasoner code generator \n * Date: $L, \n */",
				dateFormat.format(date))
			.build();
	}
	
	private Builder addContextStates(Builder methodBuilder) {
		List<MObject> sensorRules = getSensorRules(sensor);
		for(MObject sensorRule : sensorRules){
			String contextStateName = StringUtils.lowercaseFirstLetter(StringUtils.camelCase(sensorRule.getName()));
			methodBuilder.beginControlFlow("if(stateName.equals($S))", contextStateName)
			.addStatement("result = $L",getProperty(sensorRule, DCaseProperties.PROPERTY_DB_MODELLING_RULE_RULE))
			.endControlFlow()
			;
		}
		return methodBuilder;
	}

	private List<MObject> getSensorRules(MObject sensor) {
		List<MObject> contextStates = new ArrayList<>();
		for(MObject element : sensor.getCompositionChildren()){
			if(isStereotyped(element, DCaseStereotypes.STEREOTYPE_FEEDS)){
				MObject modellingRule = ((Dependency)element).getDependsOn();
				if(isStereotyped(modellingRule, DCaseStereotypes.STEREOTYPE_DB_MODELLING_RULE)){
					for(MObject modellingRuleChild : modellingRule.getCompositionChildren()){
						if(isStereotyped(modellingRuleChild, DCaseStereotypes.STEREOTYPE_PRODUCE)){
							MObject contextState = ((Dependency)modellingRuleChild).getDependsOn();
							if(isStereotyped(contextState, DCaseStereotypes.STEREOTYPE_CONTEXT_STATE))
									contextStates.add(((Dependency)element).getDependsOn());	
						}
					}

				}
			}
		}
		return contextStates;
	}
	
	

	private boolean isStereotyped(MObject element, String stereotype) {
		return ((ModelElement) element).isStereotyped(DCasePeerModule.MODULE_NAME,
	    		stereotype);
	}

	private String getProperty(MObject element, String property){		
		return ((ModelElement) element).getTagValue(DCasePeerModule.MODULE_NAME,
			    property);
	}
	
	private String getTypeProperty(MObject element, String property){
		
		String result = ((ModelElement) element).getTagValue(DCasePeerModule.MODULE_NAME,
			    property);
		
		if(result.equalsIgnoreCase("true")){
			return "Boolean";
		} else return "String";
	}

}
