package edu.casetools.dcase.extensions.io.gen.mobile.areasoner.classes.contextmapper;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.lang.model.element.Modifier;

import org.metawidget.util.simple.StringUtils;
import org.modelio.vcore.smkernel.mapi.MObject;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.FieldSpec.Builder;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;

import edu.casetools.dcase.extensions.io.gen.ClassTemplate;

public class ContextMapperGenerator implements ClassTemplate{

	private List<MObject> situations;

	public ContextMapperGenerator(List<MObject>  situations){
		this.situations = situations;
	}
	
	@Override
	public JavaFile generate() {
		ClassName contextMapper = ClassName.get("edu.casetools.icase.custom", "CustomContextMapper");
		ClassName abstractContextMapper = ClassName.get("org.poseidon_project.context.reasoner", "AbstractContextMapper");
		ClassName contextReasonerCore = ClassName.get("org.poseidon_project.context", "ContextReasonerCore");
		ClassName reasonerManager = ClassName.get("org.poseidon_project.context.reasoner", "ReasonerManager");
		ClassName context = ClassName.get("android.content","Context");
		List<ClassName> situationsClasses = getClassesFromSOIs();
		
		//String projectName = ModelioUtils.getInstance().getProjectName(DCaseModule.getInstance()).replaceAll("\\s", "");
		
		DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		Date date = new Date();
		
		MethodSpec constructor = MethodSpec.constructorBuilder()
	        .addModifiers(Modifier.PUBLIC)
	        .addParameter(contextReasonerCore, "crc")
	        .addParameter(reasonerManager, "rm")
	        .addParameter(context, "con")
	        .addStatement("super($S,crc,rm,con)","CustomContextMapper")
	        .addStatement("initialiseSituationsOfInterest()","")
	        .build();
		
		MethodSpec.Builder method = MethodSpec.methodBuilder("initialiseSituationsOfInterest")
			.addModifiers(Modifier.PRIVATE)
			.returns(void.class);
		
		for(ClassName soiClass : situationsClasses){
			method.addStatement("situationsOfInterest.add(new $L())", soiClass);
		}

		TypeSpec contextClass = TypeSpec.classBuilder(contextMapper).addModifiers(Modifier.PUBLIC)
			.addJavadoc("import javax.swing.JFrame;\n")
			.addJavadoc("import javax.swing.JOptionPane;\n")
			.superclass(abstractContextMapper)
			.addMethod(constructor)
			.addMethod(method.build())
			.build();

		return JavaFile.builder("edu.casetools.icase.mreasoner.extensions.actuators", contextClass)
			.addFileComment("/* This code skeleton has been automatically generated \n * as part of the DCase Android Reasoner code generator \n * Date: $L, \n */",
				dateFormat.format(date))
			.build();

	}

	private List<ClassName> getClassesFromSOIs() {
		List<ClassName> situationsClasses = new ArrayList<>();

		for(MObject soi : situations){
			String name = StringUtils.uppercaseFirstLetter(StringUtils.camelCase(soi.getName(),' ')+"SOI");
			situationsClasses.add(ClassName.get("edu.casetools.icase.custom.situations",name));
		}
		return situationsClasses;
	}

}
