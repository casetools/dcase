package edu.casetools.dcase.extensions.io.acl;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.lang.model.element.Modifier;

import org.modelio.metamodel.uml.infrastructure.ModelElement;
import org.modelio.vcore.smkernel.mapi.MObject;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;

import edu.casetools.dcase.module.api.DCaseProperties;
import edu.casetools.dcase.module.api.DCaseStereotypes;
import edu.casetools.dcase.module.i18n.I18nMessageService;
import edu.casetools.dcase.module.impl.DCasePeerModule;
import edu.casetools.dcase.utils.ModelioUtils;
import edu.casetools.dcase.utils.tables.TableUtils;

public class ACLGenerator {

    List<MObject> contextAttributeList;

    public void generateACLTemplates(String folder) {

	List<MObject> contextAttributeList = new ArrayList<>();
	contextAttributeList = TableUtils.getInstance().getAllElementsStereotypedAs(contextAttributeList,
		DCasePeerModule.MODULE_NAME, DCaseStereotypes.STEREOTYPE_ACL_CONTEXT);
	for (MObject contextAttribute : contextAttributeList) {
	    String type = ((ModelElement) contextAttribute).getTagValue(DCasePeerModule.MODULE_NAME,
		    DCaseProperties.PROPERTY_CONTEXT_RESPONSIBILITY);
	    if (type.equals(I18nMessageService.getString("Ui.ACLContext.Property.TagResponsibility.Pull"))) {
		String frequency = ((ModelElement) contextAttribute).getTagValue(DCasePeerModule.MODULE_NAME,
			DCaseProperties.PROPERTY_CONTEXT_FREQUENCY);
		generatePullObserver(contextAttribute, folder, frequency);
	    } else if (type.equals(I18nMessageService.getString("Ui.ACLContext.Property.TagResponsibility.Push"))) {
		generatePushObserver(contextAttribute, folder);
	    }

	}

	if (!contextAttributeList.isEmpty()) {
	    generateReceiver(folder);
	}

    }

    private void generateReceiver(String folder) {
	ClassName contextReceiver = ClassName.get("uk.ac.mdx.cs.ie.acontextlib", "ContextReceiver");

	String projectName = ModelioUtils.getInstance().getProjectName().replaceAll("\\s", "");

	DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
	Date date = new Date();
	File sourcePath = new File(folder);

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

	JavaFile javaFile = JavaFile.builder(projectName, contextClass)
		.addFileComment(
			"/* This code skeleton has been automatically generated \n * as part of the DCase Android Context Library code generator \n * Date: $L, \n */",
			dateFormat.format(date))
		.build();

	try {
	    javaFile.writeTo(sourcePath);

	} catch (IOException e) {
	    e.printStackTrace();
	}

    }

    private void generatePullObserver(MObject contextAttribute, String folder, String frequency) {
	ClassName pullObserver = ClassName.get("uk.ac.mdx.cs.ie.acontextlib", "PullObserver");
	ClassName androidContext = ClassName.get("android.content", "Context");
	String projectName = ModelioUtils.getInstance().getProjectName().replaceAll("\\s", "");
	String mainClassName = contextAttribute.getName().replaceAll("\\s", "");

	DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
	Date date = new Date();
	File sourcePath = new File(folder);

	MethodSpec constructor = MethodSpec.constructorBuilder().addModifiers(Modifier.PUBLIC)
		.addParameter(androidContext, "c").addStatement("super($N,$N,\"$N\")", "c", frequency, mainClassName)
		.addJavadoc(
			"\t//Include your variables here\n\t//This is an example for checking the external storage\n\n    private final static long SIZE_KB = 1024L;\n    private final static long SIZE_MB = SIZE_KB * SIZE_KB;\n    private long mCurrentSpace;\n\n")
		.build();

	MethodSpec checkContext = MethodSpec.methodBuilder("checkContext").addModifiers(Modifier.PUBLIC)
		.addAnnotation(Override.class).returns(void.class).addComment("Object object) {")
		.addComment(
			"\n/*\t// Include your main code to observe context here\n\t// This is an example of checking the external storage of the phone\n\tlong v = Environment.getExternalStorageDirectory().getUsableSpace();\n\tif (mCurrentSpace != v) {\n\t\tsendToContextReceivers(\"sensor.external_storage_remaining\", v / SIZE_MB);\n\t\tmCurrentSpace = v;\n\t}\n*/")
		.build();

	TypeSpec contextClass = TypeSpec.classBuilder(mainClassName + "Context").addModifiers(Modifier.PUBLIC)
		.addJavadoc("import android.os.Environment;\n").superclass(pullObserver).addMethod(constructor)
		.addMethod(checkContext).build();

	JavaFile javaFile = JavaFile.builder(projectName, contextClass)
		.addFileComment(
			"/* This code skeleton has been automatically generated \n * as part of the DCase Android Context Library code generator \n * Date: $L, \n */",
			dateFormat.format(date))
		.build();

	try {
	    javaFile.writeTo(sourcePath);

	} catch (IOException e) {
	    e.printStackTrace();
	}
    }

    private void generatePushObserver(MObject contextAttribute, String folder) {
	String type = ((ModelElement) contextAttribute).getTagValue(DCasePeerModule.MODULE_NAME,
		DCaseProperties.PROPERTY_CONTEXT_LIBTYPE);
	if (type.equals(I18nMessageService.getString("Ui.ACLContext.Property.TagLibType.Sensor"))) {
	    generateSensorObserver(contextAttribute, folder);
	} else if (type.equals(I18nMessageService.getString("Ui.ACLContext.Property.TagLibType.Location"))) {
	    generateLocationObserver(contextAttribute, folder);
	} else if (type.equals(I18nMessageService.getString("Ui.ACLContext.Property.TagLibType.Broadcast"))) {
	    generateBroadcastObserver(contextAttribute, folder);
	} else if (type.equals(I18nMessageService.getString("Ui.ACLContext.Property.TagLibType.Bluetooth"))) {
	    generateBluetoothObserver(contextAttribute, folder);
	} else if (type.equals(I18nMessageService.getString("Ui.ACLContext.Property.TagLibType.None"))) {
	    generatePushObserverClass(contextAttribute, folder);
	}
    }

    private void generateLocationObserver(MObject contextAttribute, String folder) {
	File sourcePath = new File(folder);

	DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
	Date date = new Date();

	ClassName locationContext = ClassName.get("uk.ac.mdx.cs.ie.acontextlib", "LocationContext");
	ClassName androidContext = ClassName.get("android.content", "Context");
	String projectName = ModelioUtils.getInstance().getProjectName().replaceAll("\\s", "");
	String mainClassName = contextAttribute.getName().replaceAll("\\s", "");

	MethodSpec constructor = MethodSpec.constructorBuilder().addModifiers(Modifier.PUBLIC)
		.addParameter(androidContext, "c").addParameter(String.class, "provider")
		.addStatement("super($N,$N,\"$N\")", "c", "provider", mainClassName).build();

	MethodSpec checkContext = MethodSpec.methodBuilder("checkContext").addModifiers(Modifier.PUBLIC)
		.addAnnotation(Override.class).returns(void.class).addComment(" Object object) {")
		.addComment(" sendToContextReceivers(\"device.current_loc\", object);").build();

	TypeSpec contextClass = TypeSpec.classBuilder(mainClassName + "Context").addModifiers(Modifier.PUBLIC)
		.addJavadoc("import android.location.Location;\nimport android.location.LocationManager;\n")
		.superclass(locationContext).addMethod(constructor).addMethod(checkContext).build();

	JavaFile javaFile = JavaFile.builder(projectName, contextClass)
		.addFileComment(
			"/* This code skeleton has been automatically generated \n * as part of the DCase Android Context Library code generator \n * Date: $L, \n */",
			dateFormat.format(date))
		.build();

	try {
	    javaFile.writeTo(sourcePath);

	} catch (IOException e) {
	    e.printStackTrace();
	}
    }

    private void generateSensorObserver(MObject contextAttribute, String folder) {
	File sourcePath = new File(folder);

	DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
	Date date = new Date();

	ClassName sensorContext = ClassName.get("uk.ac.mdx.cs.ie.acontextlib", "SensorContext");
	ClassName androidContext = ClassName.get("android.content", "Context");
	String projectName = ModelioUtils.getInstance().getProjectName().replaceAll("\\s", "");
	String mainClassName = contextAttribute.getName().replaceAll("\\s", "");

	MethodSpec constructor = MethodSpec.constructorBuilder().addModifiers(Modifier.PUBLIC)
		.addParameter(androidContext, "c")
		.addJavadoc(
			"\n//Include your variables here\r\n//This is an example for the light sensor in Android\r\n private long mCurrentValue;\r\n private long mContextDifferenceHigher = 0;\r\n private long mContextDifferenceLower = 0;\r\n\n")
		.addComment(
			"\n/*\r\n\t//The constructor should call the super method. More information on android.hardware.sensor\r\n\t//can be found here: https://developer.android.com/reference/android/hardware/Sensor.html\r\n\r\n\t//This is an example of a super call for the light sensor:\r\n\t super(c, Sensor.TYPE_LIGHT, SensorManager.SENSOR_DELAY_NORMAL, $N));\r\n*/",
			mainClassName)
		.build();

	MethodSpec checkContext = MethodSpec.methodBuilder("checkContext").addModifiers(Modifier.PUBLIC)
		.addAnnotation(Override.class).returns(void.class).addComment("float[] values) {\r\n")
		.addComment(
			"\n/* \r\n\r\n\t\t//Include your code here\r\n\t\t//This is an example for the light sensor in Android\r\n\t\tlong value = Math.round(values['['/]0]);\r\n\r\n        long difference = Math.abs(mCurrentValue - value);\r\n        long threshold;\r\n        if (value > mCurrentValue) {\r\n            threshold = mContextDifferenceHigher;\r\n        } else {\r\n            threshold = mContextDifferenceLower;\r\n        }\r\n\r\n\r\n        if (difference >= threshold) {\r\n            mCurrentValue = value;\r\n            sendToContextReceivers(\"sensor.light_lumens\", mCurrentValue);\r\n            mContextDifferenceHigher = value * 2;\r\n            mContextDifferenceLower = value / 2;\r\n        }\n */")
		.build();

	TypeSpec contextClass = TypeSpec.classBuilder(mainClassName + "Context").addModifiers(Modifier.PUBLIC)
		.addJavadoc("import android.hardware.Sensor;\nimport android.hardware.SensorManager;\n")
		.superclass(sensorContext).addMethod(constructor).addMethod(checkContext).build();

	JavaFile javaFile = JavaFile.builder(projectName, contextClass)
		.addFileComment(
			"/* This code skeleton has been automatically generated \n * as part of the DCase Android Context Library code generator \n * Date: $L, \n */",
			dateFormat.format(date))
		.build();

	try {
	    javaFile.writeTo(sourcePath);

	} catch (IOException e) {
	    e.printStackTrace();
	}
    }

    private void generateBluetoothObserver(MObject contextAttribute, String folder) {
	File sourcePath = new File(folder);

	DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
	Date date = new Date();

	ClassName bluetoothContext = ClassName.get("uk.ac.mdx.cs.ie.acontextlib", "BluetoothLEDevice");
	ClassName androidContext = ClassName.get("android.content", "Context");
	String projectName = ModelioUtils.getInstance().getProjectName().replaceAll("\\s", "");
	String mainClassName = contextAttribute.getName().replaceAll("\\s", "");

	MethodSpec constructor = MethodSpec.constructorBuilder().addModifiers(Modifier.PUBLIC)
		.addParameter(androidContext, "c").addJavadoc(
			"\n//Include your variables here\r\n//This is an example for a bluetooth observer\r\npublic final static String HEART_RATE_MEASUREMENT = \"00002a37-0000-1000-8000-00805f9b34fb\";\r\npublic final static String HEART_RATE_SERVICE = \"0000180d-0000-1000-8000-00805f9b34fb\";\r\n\n")

		.addComment(
			"\n\t/*\r\n\t//The constructor should call the super method. More information on android.bluetooth.bluetoothgattcharacteristic \r\n\t//can be found here: https://developer.android.com/reference/android/bluetooth/BluetoothGattCharacteristic.html\r\n\r\n\t//This is an example of a heart rate measuring bluetooth wristband:\r\n\t super(c, UUID.fromString(HEART_RATE_SERVICE), UUID.fromString(HEART_RATE_MEASUREMENT));\r\n*/",
			mainClassName)
		.build();

	MethodSpec checkContext = MethodSpec.methodBuilder("checkContext").addModifiers(Modifier.PUBLIC)
		.addAnnotation(Override.class).returns(void.class)
		.addComment(
			"\n/* \r\nBluetoothGattCharacteristic characteristic = (BluetoothGattCharacteristic) data;\r\n\r\nint flag = characteristic.getProperties();\r\nint format = -1;\r\n\r\nif ((flag & 0x01) != 0) {\r\n\tformat = BluetoothGattCharacteristic.FORMAT_UINT16;\r\n} else {\r\n\tformat = BluetoothGattCharacteristic.FORMAT_UINT8;\r\n}\r\n\r\nfinal int heartRate = characteristic.getIntValue(format, 1);\r\n\r\nsendToContextReceivers(\"sensor.heartrate\", heartRate);\r\n\n*/")
		.build();

	TypeSpec contextClass = TypeSpec.classBuilder(mainClassName + "Context").addModifiers(Modifier.PUBLIC)
		.addJavadoc(
			"import android.annotation.TargetApi;\nimport android.bluetooth.BluetoothGattCharacteristic;\nimport android.os.Build;\nimport java.util.UUID;\n")
		.superclass(bluetoothContext).addMethod(constructor).addMethod(checkContext).build();

	JavaFile javaFile = JavaFile.builder(projectName, contextClass)
		.addFileComment(
			"/* This code skeleton has been automatically generated \n * as part of the DCase Android Context Library code generator \n * Date: $L, \n */",
			dateFormat.format(date))
		.build();

	try {
	    javaFile.writeTo(sourcePath);

	} catch (IOException e) {
	    e.printStackTrace();
	}
    }

    private void generateBroadcastObserver(MObject contextAttribute, String folder) {
	File sourcePath = new File(folder);

	DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
	Date date = new Date();

	ClassName broadcastContext = ClassName.get("uk.ac.mdx.cs.ie.acontextlib", "BroadcastContext");
	ClassName androidContext = ClassName.get("android.content", "Context");
	String projectName = ModelioUtils.getInstance().getProjectName().replaceAll("\\s", "");
	String mainClassName = contextAttribute.getName().replaceAll("\\s", "");

	MethodSpec constructor = MethodSpec.constructorBuilder().addModifiers(Modifier.PUBLIC)
		.addParameter(androidContext, "c")
		.addComment(
			"\n/*\r\n\t//The constructor should call the super method. More information on android.os.batterymanager\r\n\t//can be found here: https://developer.android.com/reference/android/os/BatteryManager.html\r\n\r\n\t//This is an example of a heart rate measuring bluetooth wristband:\r\n\t super(c, \"Intent.Action_BATTERY_CHANGED\",\"$N\");\r\n*/",
			mainClassName)
		.addJavadoc(
			"//Include your variables here \n//This is an example for checking the battery level \n  private int mBatteryLevel;")
		.build();

	MethodSpec checkContext = MethodSpec.methodBuilder("checkContext").addModifiers(Modifier.PUBLIC)
		.addAnnotation(Override.class).returns(void.class).addComment("//Bundle bundle) {\r\n").addComment(
			"\n/*\r\n\t// Include your variables here\r\n\t//This is an example for checking the battery level \r\n\tint rawlevel = bundle.getInt(BatteryManager.EXTRA_LEVEL, -1);\r\n\tint scale = bundle.getInt(BatteryManager.EXTRA_SCALE, -1);\r\n\r\n\tif (rawlevel >= 0 && scale > 0) {\r\n\t\tmBatteryLevel = (rawlevel * 100) / scale;\r\n\r\n\t\t//Send the receiver the context update\r\n\t\tsendToContextReceivers(\"sensor.battery_level\", mBatteryLevel);\r\n\t}\r\n*/")

		.build();

	TypeSpec contextClass = TypeSpec.classBuilder(mainClassName + "Context").addModifiers(Modifier.PUBLIC)
		.addJavadoc("import android.os.BatteryManager;\nimport android.os.Bundle;\n")
		.superclass(broadcastContext).addMethod(constructor).addMethod(checkContext).build();

	JavaFile javaFile = JavaFile.builder(projectName, contextClass)
		.addFileComment(
			"/* This code skeleton has been automatically generated \n * as part of the DCase Android Context Library code generator \n * Date: $L, \n */",
			dateFormat.format(date))
		.build();

	try {
	    javaFile.writeTo(sourcePath);

	} catch (IOException e) {
	    e.printStackTrace();
	}
    }

    private void generatePushObserverClass(MObject contextAttribute, String folder) {
	File sourcePath = new File(folder);

	DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
	Date date = new Date();

	ClassName pushObserver = ClassName.get("uk.ac.mdx.cs.ie.acontextlib", "PushObserver");
	ClassName androidContext = ClassName.get("android.content", "Context");
	String projectName = ModelioUtils.getInstance().getProjectName().replaceAll("\\s", "");
	String mainClassName = contextAttribute.getName().replaceAll("\\s", "");

	MethodSpec constructor = MethodSpec.constructorBuilder().addModifiers(Modifier.PUBLIC)
		.addJavadoc("// Include your variables here \n").addParameter(androidContext, "c")
		.addCode("super($N);", "c").build();

	MethodSpec checkContext = MethodSpec.methodBuilder("checkContext").addModifiers(Modifier.PUBLIC)
		.addStatement("checkContext(data)").build();

	MethodSpec resume = MethodSpec.methodBuilder("resume").addModifiers(Modifier.PUBLIC).returns(Boolean.class)
		.addAnnotation(Override.class).returns(void.class).addCode("return start();").build();

	MethodSpec stop = MethodSpec.methodBuilder("stop").addModifiers(Modifier.PUBLIC).returns(Boolean.class)
		.addAnnotation(Override.class).returns(void.class).addCode("return stop();").build();

	TypeSpec contextClass = TypeSpec.classBuilder(mainClassName + "Context").addModifiers(Modifier.PUBLIC)
		.superclass(pushObserver).addMethod(constructor).addMethod(checkContext).addMethod(resume)
		.addMethod(stop).build();

	JavaFile javaFile = JavaFile.builder(projectName, contextClass)
		.addFileComment(
			"/* This code skeleton has been automatically generated \n * as part of the DCase Android Context Library code generator \n * Date: $L, \n */",
			dateFormat.format(date))
		.build();

	try {
	    javaFile.writeTo(sourcePath);

	} catch (IOException e) {
	    e.printStackTrace();
	}
    }

}
