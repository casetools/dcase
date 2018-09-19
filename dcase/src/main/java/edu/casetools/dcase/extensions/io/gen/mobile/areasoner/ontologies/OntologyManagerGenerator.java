package edu.casetools.dcase.extensions.io.gen.mobile.areasoner.ontologies;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.lang.model.element.Modifier;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.TypeSpec;

import edu.casetools.dcase.extensions.io.gen.ClassTemplate;
import edu.casetools.dcase.module.impl.DCaseModule;
import edu.casetools.rcase.utils.ModelioUtils;

public class OntologyManagerGenerator implements ClassTemplate{

	private String baseOntologyString;
	private String streamIRIString;
	
	public OntologyManagerGenerator(String baseOntologyString, String streamIRIString){
		this.baseOntologyString = baseOntologyString;
		this.streamIRIString = streamIRIString;
	}
	
	@Override
	public JavaFile generate() {
		ClassName ontologyManager = ClassName.get("edu.casetools.icase.custom", "OntologyManager");
		String projectName = ModelioUtils.getInstance().getProjectName(DCaseModule.getInstance()).replaceAll("\\s", "");

		DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		Date date = new Date();

		FieldSpec baseOntology = FieldSpec.builder(String.class, "BASE_ONTOLOGY")
			    .addModifiers(Modifier.PUBLIC)
			    .addModifiers(Modifier.STATIC)
			    .addModifiers(Modifier.FINAL)
			    .initializer("$S", baseOntologyString)
			    .build();
		
		FieldSpec streamIRI = FieldSpec.builder(String.class, "STREAM_IRI")
			    .addModifiers(Modifier.PUBLIC)
			    .addModifiers(Modifier.STATIC)
			    .addModifiers(Modifier.FINAL)
			    .initializer("$S", streamIRIString)
			    .build();
		TypeSpec contextClass = TypeSpec.classBuilder("OntologyManager").addModifiers(Modifier.PUBLIC)
			.addJavadoc("import android.os.Environment;\n").superclass(ontologyManager).addField(baseOntology).addField(streamIRI).build();

		return JavaFile.builder(projectName, contextClass)
			.addFileComment(
				"/* This code skeleton has been automatically generated \n * as part of the DCase Android Context Library code generator \n * Date: $L, \n */",
				dateFormat.format(date))
			.build();

	}

}
