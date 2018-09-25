package edu.casetools.dcase.extensions.io.gen.mobile.areasoner.soi;

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
import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;
import com.squareup.javapoet.TypeSpec.Builder;

import edu.casetools.dcase.extensions.io.gen.ClassTemplate;
import edu.casetools.dcase.extensions.io.gen.mobile.areasoner.csparql.CSPARQLWriter;
import edu.casetools.dcase.module.api.DCaseStereotypes;
import edu.casetools.dcase.module.impl.DCaseModule;
import edu.casetools.dcase.module.impl.DCasePeerModule;
import edu.casetools.rcase.utils.tables.TableUtils;

public class SOIGenerator implements ClassTemplate{

	MObject 		 soi;
	List<MObject>    contextAttributeList;
	List<FieldSpec>  fields;
	List<MethodSpec> methods;
	
	public SOIGenerator(MObject soi, List<MObject> contextAttributeList){
		this.soi = soi;
		this.contextAttributeList = contextAttributeList;
		this.fields = new ArrayList<FieldSpec>();
		this.methods = new ArrayList<MethodSpec>();		
	}
	
	@Override
	public JavaFile generate() {
		ClassName currentSOIClass = ClassName.get("edu.casetools.icase.custom.situations", StringUtils.uppercaseFirstLetter(StringUtils.camelCase(soi.getName(),' ')+"SOI"));
		ClassName SOIClass 		  = ClassName.get("org.poseidon_project.context.reasoner", "SituationOfInterest");
		Builder typeSpecBuilder   = TypeSpec.classBuilder(currentSOIClass).addModifiers(Modifier.PUBLIC).superclass(SOIClass); 
		
		DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		Date date = new Date();
		
		this.fields = new ArrayList<FieldSpec>();
		this.methods = new ArrayList<MethodSpec>();		

		generateRules();
		
		
		for(FieldSpec field : fields){
			typeSpecBuilder = typeSpecBuilder.addField(field);
		}
		
		for(MethodSpec method : methods){
			typeSpecBuilder = typeSpecBuilder.addMethod(method);
		}
		

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


	private void generateRules() {
	
		for(MObject contextAttribute:contextAttributeList){
			List<MObject> sensors = getContextPrefAttributeSensors(contextAttribute);
			for(MObject sensor : sensors){
				
				for(MObject rule : getSensorRules(sensor,DCaseStereotypes.STEREOTYPE_FEEDS_IN_WINDOW)){
					generateModellingRule(rule);
					generateReasoningRule(rule);
				}
				for(MObject rule : getSensorRules(sensor,DCaseStereotypes.STEREOTYPE_FEEDS)){
					// TO BE IMPLEMENTED: FEATURE FOR GENERATING PREFS
				}
				
			}
		}
	}

	private void generateReasoningRule(MObject rule) {
		
		
	}

	private void generateModellingRule(MObject rule) {
		StringBuilder result = new StringBuilder(150);
		generateField(rule.getName(), CSPARQLWriter.generateCSPARQLQuery(result, (ModelElement)rule).toString());
		generateMethod(rule.getName());
	}
	
	private void generateMethod(String name) {
		this.methods.add(MethodSpec.methodBuilder("get"+StringUtils.camelCase(name,' ')).addModifiers(Modifier.PUBLIC)
				.returns(String.class).addStatement("$L", "return "+name)
				.build());
		
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
					if(sensor.getUuid().equals(sensorChild.getCompositionOwner().getUuid()))
						contextAttributeSensorList.add(sensorChild.getCompositionOwner());	
				}
			}
		}
		
		
		return contextAttributeSensorList;
	}
	
	private boolean isObserve(MObject element){
		return ((ModelElement) element).isStereotyped(DCasePeerModule.MODULE_NAME,
	    		DCaseStereotypes.STEREOTYPE_OBSERVE);
	}

	private void generateField(String name, String query){
		query = query.replaceAll("\"", "\\\\\"");
		query = query.replaceAll(System.lineSeparator(), "\" +"+System.lineSeparator()+"\"");
		this.fields.add(FieldSpec.builder(String.class, name)
			    .addModifiers(Modifier.PRIVATE)
			    .addModifiers(Modifier.FINAL)
			    .initializer("\"$L\"",query)
			    .build());
	}

}
