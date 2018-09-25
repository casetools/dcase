package edu.casetools.dcase.extensions.io.gen.mobile.acl.classes;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import javax.lang.model.element.Modifier;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;

import edu.casetools.dcase.extensions.io.gen.ClassTemplate;
import edu.casetools.dcase.module.impl.DCaseModule;
import edu.casetools.rcase.utils.ModelioUtils;

public class ReceiverGenerator implements ClassTemplate {

	@Override
	public JavaFile generate() {
		ClassName contextReceiver = ClassName.get("uk.ac.mdx.cs.ie.acontextlib", "ContextReceiver");

		String projectName = ModelioUtils.getInstance().getProjectName(DCaseModule.getInstance()).replaceAll("\\s", "");

		DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		Date date = new Date();


		MethodSpec newContextValue0 = MethodSpec.methodBuilder("newContextValue").addModifiers(Modifier.PUBLIC)
			.addParameter(String.class, "name").addParameter(Long.class, "value").addAnnotation(Override.class)
			.returns(void.class)
			.addComment(
				"\n /* \nString strValue = String.valueOf(value) + \"^^http://www.w3.org/2001/XMLSchema#integer\";\n\n   if(name.equals(\"sensor.battery_level\")) {\n\t   getReasonerManager().updateValues(\"system#device"
					+ "batteryRemaining\", strValue);\n   } else if (name.equals(\"sensor.light_lumens\")) {\n\t   getReasonerManager().updateValues(\"system#device\", \"hasLightLevel\", strValue);\n   } // else if (<YOUR SENSOR NAME>) {\n\t  // Put your code here\n\t  // Put the incoming values in the reasoner\n\t  // getReasonerManager().updateValues(\"user#pu\" + mCounter, \"user#hasNavigationStatus\", strValue);\n   } \n*/")
			.build();

		MethodSpec newContextValue1 = MethodSpec.methodBuilder("newContextValue").addModifiers(Modifier.PUBLIC)
			.addParameter(String.class, "name").addParameter(Double.class, "value").addAnnotation(Override.class)
			.returns(void.class).addComment("Repeat the example code for your specific needed values").build();

		MethodSpec newContextValue2 = MethodSpec.methodBuilder("newContextValue").addModifiers(Modifier.PUBLIC)
			.addParameter(String.class, "name").addParameter(Boolean.class, "value").addAnnotation(Override.class)
			.returns(void.class).addComment("Repeat the example code for your specific needed values").build();

		MethodSpec newContextValue3 = MethodSpec.methodBuilder("newContextValue").addModifiers(Modifier.PUBLIC)
			.addParameter(String.class, "name").addParameter(String.class, "value").addAnnotation(Override.class)
			.returns(void.class).addComment("Repeat the example code for your specific needed values").build();

		MethodSpec newContextValue4 = MethodSpec.methodBuilder("newContextValue").addModifiers(Modifier.PUBLIC)
			.addParameter(String.class, "name").addParameter(Object.class, "value").addAnnotation(Override.class)
			.returns(void.class).addComment("Repeat the example code for your specific needed values").build();

		TypeName mapName = ParameterizedTypeName.get(Map.class, String.class, String.class);

		ParameterSpec mapSpec = ParameterSpec.builder(mapName, "value", Modifier.FINAL).build();

		MethodSpec newContextValue5 = MethodSpec.methodBuilder("newContextValue").addModifiers(Modifier.PUBLIC)
			.addParameter(String.class, "name").addParameter(mapSpec).addAnnotation(Override.class)
			.returns(void.class).addComment("Repeat the example code for your specific needed values").build();

		TypeSpec contextClass = TypeSpec.classBuilder(projectName + "Receiver").addModifiers(Modifier.PUBLIC)
			.addJavadoc(
				"import uk.ac.mdx.cs.ie.acontextlib.IContextManager;\nimport uk.ac.mdx.cs.ie.acontextlib.IReasonerManager;\n")
			.superclass(contextReceiver).addMethod(newContextValue0).addMethod(newContextValue1)
			.addMethod(newContextValue2).addMethod(newContextValue3).addMethod(newContextValue4)
			.addMethod(newContextValue5).build();

		return JavaFile.builder("uk.ac.mdx.cs.ie.acontextlib",contextClass)
			.addFileComment(
				"/* This code skeleton has been automatically generated \n * as part of the DCase Android Context Library code generator \n * Date: $L, \n */",
				dateFormat.format(date))
			.build();
	}

}
