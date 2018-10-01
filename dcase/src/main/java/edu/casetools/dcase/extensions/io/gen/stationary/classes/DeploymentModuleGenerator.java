package edu.casetools.dcase.extensions.io.gen.stationary.classes;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Vector;

import javax.lang.model.element.Modifier;

import org.metawidget.util.simple.StringUtils;
import org.modelio.metamodel.uml.infrastructure.Dependency;
import org.modelio.metamodel.uml.infrastructure.ModelElement;
import org.modelio.vcore.smkernel.mapi.MObject;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeSpec;
import com.squareup.javapoet.TypeSpec.Builder;

import edu.casetools.dcase.extensions.io.gen.ClassTemplate;
import edu.casetools.dcase.module.api.DCaseProperties;
import edu.casetools.dcase.module.api.DCaseStereotypes;
import edu.casetools.dcase.module.impl.DCaseModule;
import edu.casetools.dcase.module.impl.DCasePeerModule;
import edu.casetools.rcase.utils.ModelioUtils;
import edu.casetools.rcase.utils.tables.TableUtils;

public class DeploymentModuleGenerator implements ClassTemplate{

	private String projectName;
	private List<MObject>    sensorList;
	
	public DeploymentModuleGenerator(List<MObject> sensorList){
		this.projectName =  StringUtils.uppercaseFirstLetter(StringUtils.camelCase(ModelioUtils.getInstance().getProjectName(DCaseModule.getInstance())));
		this.sensorList 				= sensorList;

	}
	
	@Override
	public JavaFile generate() {
		ClassName deploymentModule 		   		 = ClassName.get("edu.casetools.icase.mreasoner.extensions.modules", projectName+"DeploymentModule");
		ClassName abstractDeploymentModule 		 = ClassName.get("edu.casetools.icase.mreasoner.deployment.realenvironment", "AbstractDeploymentModule");
		ClassName sensorClass 				   	 = ClassName.get("edu.casetools.icase.mreasoner.deployment.sensors","Sensor");
		ClassName veraActuator					 = ClassName.get("edu.casetools.icase.mreasoner.vera.actuators.device","VeraActuator");
		Builder typeSpecBuilder   				 = TypeSpec.classBuilder(deploymentModule).addModifiers(Modifier.PUBLIC).superclass(abstractDeploymentModule); 
		List<MethodSpec.Builder> observerMethods = new ArrayList<>();
		List<MethodSpec.Builder> stateMethods    = new ArrayList<>();
		
		DateFormat dateFormat 	  = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		Date date = new Date();		
		
		MethodSpec constructor = MethodSpec.constructorBuilder()
		        .addModifiers(Modifier.PUBLIC)
		        .addStatement("super()")
		        .build();
		
		
		MethodSpec.Builder initialiseSensorObserversBuilder = MethodSpec.methodBuilder("initialiseSensorObservers")
				.addModifiers(Modifier.PROTECTED)
				.addAnnotation(Override.class)
				.returns(void.class);
		
		for(MObject sensor : sensorList){

			ParameterizedTypeName stringVector = ParameterizedTypeName.get(Vector.class, String.class);
			String sensorName = StringUtils.uppercaseFirstLetter(StringUtils.camelCase(sensor.getName()));
			ClassName observer = ClassName.get("edu.casetools.icase.mreasoner.extensions.sensors", sensorName+"Observer");
			
			
			initialiseSensorObserversBuilder.addStatement("initialise$LObserver()", sensorName);	
			observerMethods.add(generateObserverMethod(sensorClass, sensor, stringVector, sensorName, observer));
			stateMethods.add(generateStateMethod(stringVector, sensorName, sensor));
			
		}
		
		MethodSpec.Builder initialiseActuatorsBuilder = MethodSpec.methodBuilder("initialiseActuators")
				.addModifiers(Modifier.PROTECTED)
				.addAnnotation(Override.class)
				.returns(void.class);
		
		initialiseActuatorsBuilder = addVeraActuators(initialiseActuatorsBuilder, veraActuator);
		initialiseActuatorsBuilder = addJavaActuators(initialiseActuatorsBuilder);
		
		typeSpecBuilder = typeSpecBuilder.addMethod(constructor)
										 .addMethod(initialiseSensorObserversBuilder.build());
		typeSpecBuilder = addSensorMethods(typeSpecBuilder, observerMethods, stateMethods);
		typeSpecBuilder = typeSpecBuilder.addMethod(initialiseActuatorsBuilder.build());
		
		return JavaFile.builder("edu.casetools.icase.mreasoner.extensions.modules", typeSpecBuilder.build())
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

	private MethodSpec.Builder addVeraActuators(MethodSpec.Builder initialiseActuatorsBuilder, ClassName veraActuatorClass) {
		List<MObject> veraActuators = TableUtils.getInstance().getAllElementsStereotypedAs(DCaseModule.getInstance(), 
				DCasePeerModule.MODULE_NAME, new ArrayList<>(), DCaseStereotypes.STEREOTYPE_VERA_ACTUATOR);
		for(MObject veraActuator : veraActuators){
			String veraActuatorName = StringUtils.lowercaseFirstLetter(StringUtils.camelCase(veraActuator.getName()));
			initialiseActuatorsBuilder.addStatement("$T $LActuator = new $T($S,$S)", 
					veraActuatorClass, veraActuatorName, veraActuatorClass,
					getProperty(veraActuator,DCaseProperties.PROPERTY_VERA_ACTUATOR_SERVICE_ID),
					getProperty(veraActuator,DCaseProperties.PROPERTY_VERA_ACTUATOR_ACTION_COMMAND)
					);
			initialiseActuatorsBuilder.addStatement("this.actuators.add($LActuator)",veraActuatorName);
		}
		return initialiseActuatorsBuilder;
	}
	
	private MethodSpec.Builder addJavaActuators(MethodSpec.Builder initialiseActuatorsBuilder) {
		List<MObject> javaActuators = TableUtils.getInstance().getAllElementsStereotypedAs(DCaseModule.getInstance(), 
				DCasePeerModule.MODULE_NAME, new ArrayList<>(), DCaseStereotypes.STEREOTYPE_JAVA_ACTUATOR);
		for(MObject javaActuator : javaActuators){
			String javaActuatorName = StringUtils.uppercaseFirstLetter(StringUtils.camelCase(javaActuator.getName()));
			ClassName javaActuatorClass = ClassName.get("edu.casetools.icase.mreasoner.extensions.actuators",javaActuatorName+"Actuator");
			initialiseActuatorsBuilder.addStatement("$T $L = new $T()", javaActuatorClass, StringUtils.lowercaseFirstLetter(javaActuatorName), javaActuatorClass);
			initialiseActuatorsBuilder.addStatement("this.actuators.add($L)",StringUtils.lowercaseFirstLetter(javaActuatorName));
		}
		return initialiseActuatorsBuilder;
	}

	private MethodSpec.Builder generateStateMethod(ParameterizedTypeName stringVector,
			String sensorName, MObject sensor) {
		MethodSpec.Builder builder =  MethodSpec.methodBuilder("initialise"+sensorName+"States")
		.addModifiers(Modifier.PRIVATE)
		.returns(stringVector)
		.addStatement("$T states = new $T()", stringVector, stringVector);
		
		for(MObject contextState : getContextStates(sensor)){
			builder = builder.addStatement("states.add($S)", StringUtils.lowercaseFirstLetter(StringUtils.camelCase(contextState.getName())));
		}
		builder = builder.addStatement("return states");
		return builder;
	}

	private com.squareup.javapoet.MethodSpec.Builder generateObserverMethod(ClassName sensorClass, MObject sensor,
			ParameterizedTypeName stringVector, String sensorName, ClassName observer) {
		return MethodSpec.methodBuilder("initialise"+sensorName+"Observer")
		.addModifiers(Modifier.PRIVATE)
		.returns(void.class)
		.addStatement("$T states = initialise$LStates()", stringVector, sensorName)
		.addStatement("$T $LObserver =  new $T()", observer, StringUtils.lowercaseFirstLetter(sensorName),observer)	
		.addStatement("$T $LSensor =  new $T($S,$S,$S,$S,$S,$S,$S,$L,states)", 
				sensorClass, StringUtils.lowercaseFirstLetter(sensorName), sensorClass,
				getProperty(sensor,DCaseProperties.PROPERTY_STATIONARY_SENSOR_VERA_ID),
				sensorName,
				getProperty(sensor,DCaseProperties.PROPERTY_STATIONARY_SENSOR_MODEL_NAME),
				getProperty(sensor,DCaseProperties.PROPERTY_STATIONARY_SENSOR_PHYSICAL_LOCATION),//LOCATION
				getProperty(sensor,DCaseProperties.PROPERTY_SENSOR_VALUE_TYPE),
				getProperty(sensor,DCaseProperties.PROPERTY_STATIONARY_SENSOR_MIN_VALUE),
				getProperty(sensor,DCaseProperties.PROPERTY_STATIONARY_SENSOR_MAX_VALUE),
				getProperty(sensor,DCaseProperties.PROPERTY_STATIONARY_SENSOR_IS_BOOLEAN).toLowerCase()
				)	
		.addStatement("$LObserver.setSensor($LSensor)", StringUtils.lowercaseFirstLetter(sensorName), StringUtils.lowercaseFirstLetter(sensorName))
		.addStatement("this.sensorObservers.add($LObserver)", StringUtils.lowercaseFirstLetter(sensorName));
	}

	private String getProperty(MObject element, String property){
		return ((ModelElement) element).getTagValue(DCasePeerModule.MODULE_NAME,
			    property);
	}
	

	private Builder addSensorMethods(Builder typeSpecBuilder, List<MethodSpec.Builder> observerMethods, List<MethodSpec.Builder> stateMethods) {
		
	
		for(MethodSpec.Builder method : observerMethods){
			typeSpecBuilder = typeSpecBuilder.addMethod(method.build());
		}
		
		for(MethodSpec.Builder method : stateMethods){
			typeSpecBuilder = typeSpecBuilder.addMethod(method.build());
		}
		return typeSpecBuilder;
	}

	

	


	private List<MObject> getContextStates(MObject sensor) {
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

}
