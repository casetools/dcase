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

public class DeploymentModuleGenerator implements ClassTemplate{

	private MObject contextAttribute;
	private String  frequency;
	
	public DeploymentModuleGenerator(MObject contextAttribute, String frequency){
		this.contextAttribute = contextAttribute;
		this.frequency = frequency;
	}
	
	@Override
	public JavaFile generate() {
		ClassName pullObserver = ClassName.get("uk.ac.mdx.cs.ie.acontextlib", "PullObserver");
		ClassName androidContext = ClassName.get("android.content", "Context");
		String mainClassName = StringUtils.camelCase(contextAttribute.getName(),' ');

		DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		Date date = new Date();

		MethodSpec constructor = MethodSpec.constructorBuilder().addModifiers(Modifier.PUBLIC)
			.addParameter(androidContext, "c").addStatement("super($N,$N,\"$N\")", "c", frequency, mainClassName)
			.addJavadoc(
				"\t//Include your variables here\n\t//This is an example for checking the external storage\n\n    private final static long SIZE_KB = 1024L;\n    private final static long SIZE_MB = SIZE_KB * SIZE_KB;\n    private long mCurrentSpace;\n\n")
			.addComment("mCurrentSpace = 0;").build();

		MethodSpec checkContext = MethodSpec.methodBuilder("checkContext").addModifiers(Modifier.PUBLIC)
			.addAnnotation(Override.class).returns(void.class).addComment("Object object) {")
			.addComment(
				"\n/*\t// Include your main code to observe context here\n\t// This is an example of checking the external storage of the phone\n\tlong v = Environment.getExternalStorageDirectory().getUsableSpace();\n\tif (mCurrentSpace != v) {\n\t\tsendToContextReceivers(\"sensor.external_storage_remaining\", v / SIZE_MB);\n\t\tmCurrentSpace = v;\n\t}\n*/")
			.build();

		TypeSpec contextClass = TypeSpec.classBuilder(mainClassName + "Context").addModifiers(Modifier.PUBLIC)
			.addJavadoc("import android.os.Environment;\n").superclass(pullObserver).addMethod(constructor)
			.addMethod(checkContext).build();

		return JavaFile.builder("uk.ac.mdx.cs.ie.acontextlib", contextClass)
			.addFileComment(
				"/* This code skeleton has been automatically generated \n * as part of the DCase Android Context Library code generator \n * Date: $L, \n */",
				dateFormat.format(date))
			.build();

	}

}
