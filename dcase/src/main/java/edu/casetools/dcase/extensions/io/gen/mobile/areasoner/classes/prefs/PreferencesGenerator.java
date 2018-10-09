package edu.casetools.dcase.extensions.io.gen.mobile.areasoner.classes.prefs;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.lang.model.element.Modifier;

import org.metawidget.util.simple.StringUtils;
import org.modelio.metamodel.uml.infrastructure.ModelElement;
import org.modelio.vcore.smkernel.mapi.MObject;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;
import com.squareup.javapoet.TypeSpec.Builder;

import edu.casetools.dcase.extensions.io.gen.ClassTemplate;
import edu.casetools.dcase.module.api.DCaseProperties;
import edu.casetools.dcase.module.i18n.I18nMessageService;
import edu.casetools.rcase.utils.PropertiesUtils;

public class PreferencesGenerator implements ClassTemplate{

	private List<MObject> preferences;

	public PreferencesGenerator(List<MObject>  preferences){
		this.preferences = preferences;
	}
	
	@Override
	public JavaFile generate() {
		ClassName prefsClass = ClassName.get("edu.casetools.icase.custom.preferences", "Preferences");
		ClassName contextClass = ClassName.get("android.content","Context");
		ClassName sharedPreferences = ClassName.get("android.content","SharedPreferences");
		//String projectName = ModelioUtils.getInstance().getProjectName(DCaseModule.getInstance()).replaceAll("\\s", "");
		
		DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		Date date = new Date();
		
		
		MethodSpec.Builder method = MethodSpec.methodBuilder("setupFirstTime")
			.addModifiers(Modifier.PUBLIC)
			.addModifiers(Modifier.STATIC)
			.addParameter(contextClass, "context")
			.addStatement("$T reasoner = context.getSharedPreferences(REASONER_PREFS, 0)", sharedPreferences)
			.addStatement("boolean setup = reasoner.getBoolean(REASONER_SETUPDONE, false)", sharedPreferences)
			.beginControlFlow("if (! setup)")
			.addStatement("$T.Editor editor = reasoner.edit()", sharedPreferences)
			.addStatement("editor.putBoolean(REASONER_SETUPDONE, true)")
			.addStatement("editor.putBoolean(REASONER_LEARNING, true)")
			.addStatement("editor.commit()")
			.addStatement("$T rules = context.getSharedPreferences(RULE_PREFS, 0)", sharedPreferences)
			.addStatement("editor = rules.edit()")
			.returns(void.class);
		
		method = getMethodPreferences(method);
		
		method.addStatement("editor.commit()")
		.endControlFlow();
		
		TypeSpec.Builder prefsClassBuilder = TypeSpec.classBuilder(prefsClass).addModifiers(Modifier.PUBLIC);
		prefsClassBuilder = addReasonerPreferences(prefsClassBuilder);
		prefsClassBuilder = addContextPreferences(prefsClassBuilder);
		prefsClassBuilder.addMethod(method.build());
		
		return JavaFile.builder("edu.casetools.icase.custom.preferences", prefsClassBuilder.build())
			.addFileComment("/* This code skeleton has been automatically generated \n * as part of the DCase Android Reasoner code generator \n * Date: $L, \n */",
				dateFormat.format(date))
			.build();

	}

	
	private MethodSpec.Builder getMethodPreferences(MethodSpec.Builder method) {
		for(MObject preference : preferences){
			method.addStatement("editor.put$L($L, $L)", getPreferenceDataType(preference), 
					StringUtils.uncamelCase(preference.getName()).replaceAll("\\s+", "_").toUpperCase(), getPreferenceValue(preference));
		}
		return method;
	}

	public static String getPreferenceValue(MObject preference) {
		String valueType = PropertiesUtils.getInstance().getTaggedValue(DCaseProperties.PROPERTY_SENSOR_VALUE_TYPE,
				(ModelElement)preference);
		String defaultValue = PropertiesUtils.getInstance().getTaggedValue(DCaseProperties.PROPERTY_PREFERENCE_SENSOR_DEFAULT_VALUE,
				(ModelElement)preference);
		if(valueType.equals(I18nMessageService.getString("Ui.SensorPropertyPage.Property.TagValueType.String"))){
			return "\""+defaultValue+"\"";
		} else if(valueType.equals(I18nMessageService.getString("Ui.SensorPropertyPage.Property.TagValueType.Boolean"))){
			return defaultValue.toLowerCase();
		} else {
			return defaultValue;
		}
	}

	public static  String getPreferenceDataType(MObject preference) {
		String valueType = PropertiesUtils.getInstance().getTaggedValue(DCaseProperties.PROPERTY_SENSOR_VALUE_TYPE,
				(ModelElement)preference);
		
		if(valueType.equals(I18nMessageService.getString("Ui.SensorPropertyPage.Property.TagValueType.Boolean"))){
			return "Boolean";
		} else if(valueType.equals(I18nMessageService.getString("Ui.SensorPropertyPage.Property.TagValueType.Integer"))){
			return "Int";
		} else if(valueType.equals(I18nMessageService.getString("Ui.SensorPropertyPage.Property.TagValueType.Float"))){
			return "Float";
		} else if(valueType.equals(I18nMessageService.getString("Ui.SensorPropertyPage.Property.TagValueType.Long"))){
			return "Long";
		} else {
			return "String";
		} 

	}

	private Builder addContextPreferences(Builder prefsClassBuilder) {
		for(MObject preference : preferences){
			prefsClassBuilder.addField(generateField(
					StringUtils.uncamelCase(preference.getName()).replaceAll("\\s+", "_").toUpperCase(),
					preference.getName().replaceAll("\\s+", "_")));
		}
		return prefsClassBuilder;
	}

	private TypeSpec.Builder addReasonerPreferences(TypeSpec.Builder prefsClassBuilder) {
		prefsClassBuilder.addField(generateField("REASONER_PREFS","ContextPrefs"))
				.addField(generateField("RULE_PREFS","RulePrefs"))
				.addField(generateField("REASONER_LASTBACKUP","logLastBackup"))
				.addField(generateField("REASONER_USERID","userId"))
				.addField(generateField("REASONER_USERVERSION","userIdVersion"))
				.addField(generateField("REASONER_DEVICEID","deviceId"))
				.addField(generateField("REASONER_BACKUPHOUR","logBackupHour"))
				.addField(generateField("REASONER_BACKUPMIN","logBackupMin"))
				.addField(generateField("RULE_PREF_LASTUPATE","prefLastUpdate"))
				.addField(generateField("REASONER_SETUPDONE","reasoner_setup"))
				.addField(generateField("REASONER_LEARNING","reasoner_learning"));
		return prefsClassBuilder;
	}

	private FieldSpec generateField(String fieldName, String fieldValue){
		return FieldSpec.builder(String.class, fieldName)
			    .addModifiers(Modifier.PUBLIC)
			    .addModifiers(Modifier.STATIC)
			    .addModifiers(Modifier.FINAL)
			    .initializer("$S",fieldValue)
			    .build();
	}
	

}
