package edu.casetools.dcase.extensions.io;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.modelio.metamodel.factory.ExtensionNotFoundException;
import org.modelio.metamodel.uml.infrastructure.ModelElement;
import org.modelio.metamodel.uml.statik.Package;
import org.modelio.vcore.smkernel.mapi.MObject;

import edu.casetools.dcase.module.api.DCaseProperties;
import edu.casetools.dcase.module.api.DCaseStereotypes;
import edu.casetools.dcase.module.impl.DCasePeerModule;
import edu.casetools.dcase.utils.PropertiesUtils;
import edu.casetools.dcase.utils.tables.TableUtils;

public class MDiagrams2UPPAAL extends AbstractModelWriter {

    private StringBuilder result;
    private List<MObject> states;
    private List<MObject> bops;
    private List<MObject> strs;
    private List<MObject> ntrs;

    public MDiagrams2UPPAAL(Package model) {
	initialiseLists();
	loadElements();
	result = new StringBuilder(states.size() * 50);
    }

    private void initialiseLists() {
	states = new ArrayList<>();
	bops = new ArrayList<>();
	strs = new ArrayList<>();
	ntrs = new ArrayList<>();
    }

    private void loadElements() {
	states = TableUtils.getInstance().getAllElementsStereotypedAs(states, DCasePeerModule.MODULE_NAME,
		DCaseStereotypes.STEREOTYPE_STATE);
	updateIDs(states, DCaseProperties.PROPERTY_STATE_ID);
	strs = TableUtils.getInstance().getAllElementsStereotypedAs(strs, DCasePeerModule.MODULE_NAME,
		DCaseStereotypes.STEREOTYPE_SAME_TIME);

	bops = TableUtils.getInstance().getAllElementsStereotypedAs(bops, DCasePeerModule.MODULE_NAME,
		DCaseStereotypes.STEREOTYPE_PAST_OPERATOR);
	updateIDs(states, DCaseProperties.PROPERTY_PAST_OPERATOR_ID);

    }

    private void updateIDs(List<MObject> list, String property) {
	try {
	    for (int i = 0; i < list.size(); i++) {
		((ModelElement) list.get(i)).putTagValue(DCasePeerModule.MODULE_NAME, property, Integer.toString(i));
	    }
	} catch (ExtensionNotFoundException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}

    }

    @Override
    public void write(Package model) {

    }

    @Override
    public String writeToString() {

	try {
	    writeHeader();
	    result.append("<nta>");
	    writeDeclarations();
	    result.append("</nta>");
	} catch (IOException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}

	// writeTemplates();
	// writeSystemDeclaration();
	// writeQueries();

	return result.toString();
    }

    private void writeHeader() throws IOException {
	result.append(
		"<?xml version=\"1.0\" encoding=\"utf-8\"?>\r\n<!DOCTYPE nta PUBLIC '-//Uppaal Team//DTD Flat System 1.1//EN' 'http://www.it.uu.se/research/group/darts/uppaal/flat-1_2.dtd'>");

    }

    private void writeDeclarations() throws IOException {
	result.append("<declaration>");
	writeVariables();
	writeStates();
	result.append("</declaration>");
    }

    private void writeStates() throws IOException {
	declareStates();
	declareInitialStates();
	declareBoundedStates();
    }

    private void declareBoundedStates() throws IOException {
	result.append("bool s_bop[BOP_NO] := {");
	for (int i = 0; i < bops.size(); i++) {
	    if (i != 0)
		result.append(",");
	    result.append("false");
	    if (i % 10 == 0)
		result.append("\n");
	}
	result.append("};\n");
	result.append("");
    }

    private void declareInitialStates() throws IOException {
	result.append("bool s_init[STATE_NO] := {");
	getInitialStateValues();
	result.append("};\n");
    }

    private void declareStates() throws IOException {
	result.append("bool s[STATE_NO] := {");
	getInitialStateValues();
	result.append("};\n");
    }

    private void getInitialStateValues() {
	for (int i = 0; i < states.size(); i++) {
	    if (i != 0)
		result.append(",");
	    result.append(PropertiesUtils.getInstance().getTaggedValue(DCaseProperties.PROPERTY_STATE_INITIAL_VALUE,
		    (ModelElement) states.get(i)));
	    if (i % 10 == 0)
		result.append("\n");
	}
    }

    private void writeVariables() throws IOException {
	result.append("\r\nint __reach__ = 0;\r\nint __single__ = 0;\r\n\r\nconst int MAX_ITERATION = " + max_iteration
		+ ";\r\nconst int STATE_NO = " + (states.size() + 1) + "; \r\nconst int EVENT_NO = " + event_no
		+ "; \r\nconst int STR_NO   = " + strs.size() + "; \r\nconst int NTR_NO   = " + ntrs.size()
		+ ";   \r\nconst int BOP_NO   = " + bops.size()
		+ ";   \r\n\r\ntypedef int[0,STATE_NO-1]    id_s;\r\ntypedef int[0,STR_NO-1]      id_str;\r\ntypedef int[0,NTR_NO-1]      id_ntr;\r\ntypedef int[0,EVENT_NO-1]    id_e;\r\ntypedef int[0,NTR_NO-1]      id_ei;\r\ntypedef int[0,MAX_ITERATION] iter;\r\ntypedef int[0,BOP_NO-1]      id_bop;\r\n\r\nbroadcast chan c_e   [EVENT_NO+1];\r\nbroadcast chan c_str [STR_NO+1];\r\nbroadcast chan c_ntr [NTR_NO+1];\r\nbroadcast chan c_bop [BOP_NO+1];\r\n\r\nint[0,MAX_ITERATION] iteration:=0;\r\nbool  reset:= true;\n");
    }

}
