package edu.casetools.dcase.extensions.io.gen.stationary.classes;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.lang.model.element.Modifier;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;

import edu.casetools.dcase.extensions.io.gen.ClassTemplate;

public class MainGenerator implements ClassTemplate{

	private String projectName;

	public MainGenerator(String projectName){
		this.projectName = projectName;
	}
	
	@Override
	public JavaFile generate() {
		ClassName main = ClassName.get("edu.casetools.icase.mreasoner.gui", "Main");
		ClassName model = ClassName.get("edu.casetools.icase.mreasoner.gui.model", "Model");
		ClassName view = ClassName.get("edu.casetools.icase.mreasoner.gui.view","View");
		ClassName controller = ClassName.get("edu.casetools.icase.mreasoner.gui.controller","Controller");	
		
		DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		Date date = new Date();

		MethodSpec mainMethod = MethodSpec.methodBuilder("main")
			    .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
			    .returns(void.class)
			    .addParameter(String[].class, "args")
			    .addStatement("$T controller = new $T(new $T(),new $T(new $LDeploymentModule()))", controller, controller, view, model,projectName)
			    .addStatement("controller.start()")
			    .build();

		TypeSpec contextClass = TypeSpec.classBuilder(main)
			.addMethod(mainMethod).build();

		return JavaFile.builder("edu.casetools.icase.mreasoner.extensions.actuators", contextClass)
			.addFileComment("/* This code skeleton has been automatically generated \n * as part of the DCase M Reasoner code generator \n * Date: $L, \n */",
				dateFormat.format(date))
			.build();

	}

}
