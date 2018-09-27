package edu.casetools.dcase.extensions.io.gen.stationary.classes;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.lang.model.element.Modifier;

import org.metawidget.util.simple.StringUtils;
import org.modelio.vcore.smkernel.mapi.MObject;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;

import edu.casetools.dcase.extensions.io.gen.ClassTemplate;

public class JavaActuatorGenerator implements ClassTemplate{

	private MObject javaActuator;

	public JavaActuatorGenerator(MObject javaActuator){
		this.javaActuator = javaActuator;
	}
	
	@Override
	public JavaFile generate() {
		String mainClassName = StringUtils.uppercaseFirstLetter(StringUtils.camelCase(javaActuator.getName(),' ')+"Actuator");
		ClassName newJavaActuator = ClassName.get("edu.casetools.icase.mreasoner.extensions.actuators", mainClassName);
		ClassName superJavaActuator = ClassName.get("edu.casetools.icase.mreasoner.extensions.actuators", "JavaActuator");
		ClassName action = ClassName.get("edu.casetools.icase.mreasoner.vera.actuators.data","Action");
		
		DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		Date date = new Date();

		MethodSpec checkContext = MethodSpec.methodBuilder("performAction").addModifiers(Modifier.PUBLIC).addParameter(action,"action")
			.addAnnotation(Override.class).returns(void.class).addComment("Add your own custom code")
			.addComment("JFrame messageFrame = new JFrame();\n JOptionPane.showMessageDialog(messageFrame,\"Put here your custom message.\");\n")
			.build();

		TypeSpec contextClass = TypeSpec.classBuilder(newJavaActuator).addModifiers(Modifier.PUBLIC)
			.addJavadoc("import javax.swing.JFrame;\n")
			.addJavadoc("import javax.swing.JOptionPane;\n")
			.superclass(superJavaActuator)
			.addMethod(checkContext).build();

		return JavaFile.builder("edu.casetools.icase.mreasoner.extensions.actuators", contextClass)
			.addFileComment("/* This code skeleton has been automatically generated \n * as part of the DCase M Reasoner code generator \n * Date: $L, \n */",
				dateFormat.format(date))
			.build();

	}

}
