package edu.casetools.dcase.extensions.io.elements;

import java.io.IOException;

import org.modelio.metamodel.uml.infrastructure.ModelElement;

import edu.casetools.dcase.extensions.io.FakeData;
import edu.casetools.dcase.extensions.io.MData;
import edu.casetools.dcase.module.api.DCaseProperties;
import edu.casetools.dcase.module.impl.DCasePeerModule;

public class DeclarationsWriter {
    private static final String STRING_FALSE = "false";
    private StringBuilder result;
    private MData systemData;

    public DeclarationsWriter(StringBuilder writer, MData systemData) {
	this.result = writer;
	this.systemData = systemData;
    }

    public void writeDeclarations() throws IOException {
	result.append("<declaration>");
	writeVariables();
	writeStates();
	result.append("</declaration>");
    }

    private void writeVariables() throws IOException {
	result.append("\r\nint __reach__ = 0;\r\nint __single__ = 0;\r\n\r\n");
	result.append("const int MAX_ITERATION = " + FakeData.MAX_ITERATION + ";\r\n");
	result.append("const int INTEGER_OVERFLOW = 32767;");
	result.append("\r\n");
	writeElementNo();
	result.append("\r\n");
	writeIDs();
	result.append("\r\n");
	writeChannels();
	result.append("\r\n");
	result.append("iter iteration:=0;\r\n");
	result.append("bool reset:= true;\n");
    }

    private void writeChannels() {
	result.append("broadcast chan c_e   [EVENT_NO+1];\r\n");
	result.append("broadcast chan c_str [STR_NO+1];\r\n");
	result.append("broadcast chan c_ntr [NTR_NO+1];\r\n");
	result.append("broadcast chan c_bop [BOP_NO+1];\r\n");
    }

    private void writeIDs() {
	result.append("typedef int[0,MAX_ITERATION] iter;\r\n");
	if (!systemData.getStates().isEmpty())
	    result.append("typedef int[0,STATE_NO-1]    id_s;\r\n");
	if (!systemData.getStrs().isEmpty())
	    result.append("typedef int[0,STR_NO-1]      id_str;\r\n");
	if (!systemData.getNtrs().isEmpty())
	    result.append("typedef int[0,NTR_NO-1]      id_ntr;\r\n");
	if (!(FakeData.EVENT_NO <= 0))
	    result.append("typedef int[0,EVENT_NO-1]    id_e;\r\n");
	if (!systemData.getNtrs().isEmpty())
	    result.append("typedef int[0,NTR_NO-1]      id_ei;\r\n");
	if (!systemData.getBops().isEmpty())
	    result.append("typedef int[0,BOP_NO-1]      id_bop;\r\n");
    }

    private void writeElementNo() {
	result.append("const int STATE_NO = " + systemData.getStates().size() + ";\r\n");
	result.append("const int EVENT_NO = " + FakeData.EVENT_NO + ";\r\n");
	result.append("const int STR_NO   = " + systemData.getStrs().size() + ";\r\n");
	result.append("const int NTR_NO   = " + systemData.getNtrs().size() + ";\r\n");
	result.append("const int BOP_NO   = " + systemData.getBops().size() + ";\r\n");
    }

    private void writeStates() throws IOException {
	if (!systemData.getStates().isEmpty()) {
	    declareStates();
	    declareInitialStates();
	}
	if (!systemData.getBops().isEmpty())
	    declareBoundedStates();
    }

    private void breakLine(int i) throws IOException {
	if (i % 10 == 0 && i != 0) {
	    result.append("\n");
	}
    }

    private void declareStates() throws IOException {
	result.append("bool s[STATE_NO] := {");
	getInitialStateValues();
	result.append("};\n");
    }

    private void declareInitialStates() throws IOException {
	result.append("bool s_init[STATE_NO] := {");
	getInitialStateValues();
	result.append("};\n\n");
    }

    private void getInitialStateValues() throws IOException {
	for (int i = 0; i < systemData.getStates().size(); i++) {
	    if (i != 0) {
		result.append(",");
	    }
	    result.append(((ModelElement) systemData.getStates().get(i)).getTagValue(DCasePeerModule.MODULE_NAME,
		    DCaseProperties.PROPERTY_STATE_INITIAL_VALUE));
	    breakLine(i);
	}
    }

    private void declareBoundedStates() throws IOException {
	result.append("bool s_bop[BOP_NO] := {");
	for (int i = 0; i < systemData.getBops().size(); i++) {
	    if (i != 0) {
		result.append(",");
	    }
	    result.append(STRING_FALSE);
	    breakLine(i);
	}
	result.append("};\n");
	result.append("");
    }

}