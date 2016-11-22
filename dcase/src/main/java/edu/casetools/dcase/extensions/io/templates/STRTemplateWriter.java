package edu.casetools.dcase.extensions.io.templates;

import org.modelio.vcore.smkernel.mapi.MObject;

import edu.casetools.dcase.extensions.io.LocationManager;
import edu.casetools.dcase.extensions.io.MData;
import edu.casetools.dcase.extensions.io.locations.STRLocations;

public class STRTemplateWriter extends TemplateWriter {
    private int idNotTriggered = 37;
    private int idTriggered = 38;
    private int idWaiting = 39;

    public STRTemplateWriter(StringBuilder writer, MData systemData) {
	super(writer, systemData);
	registerLocations();
    }

    private void registerLocations() {
	LocationManager.getInstance().registerLocation(STRLocations.WAITING);
	LocationManager.getInstance().registerLocation(STRLocations.TRIGGERED);
	LocationManager.getInstance().registerLocation(STRLocations.NOT_TRIGGERED);
    }

    @Override
    public StringBuilder write() {
	for (int i = 0; i < systemData.getStrs().size(); i++) {
	    writeSTRTemplate(i);
	}
	return result;
    }

    private void writeSTRTemplate(int i) {
	result.append(getConsequent(i));
	result.append("\n\t<template>");
	result.append("\n\t\t<name>STR_" + i + "</name>");
	writeLocations();
	writeInitialState();
	writeTransitions(i);
	result.append("\n\t</template>");

    }

    private void writeLocations() {
	writeNotTriggeredLocation();
	writeTriggeredLocation();
	writeWaitingLocation();
    }

    private void writeWaitingLocation() {
	result.append("\n\t\t<location id=\"id" + idWaiting + "\" x=\"-331\" y=\"-459\">");
	result.append("\n\t\t\t<name x=\"-314\" y=\"-476\">Waiting</name>");
	result.append("\n\t\t\t<label kind=\"exponentialrate\" x=\"-341\" y=\"-425\">1</label>");
	result.append("\n\t\t</location>");
    }

    private void writeTriggeredLocation() {
	result.append("\n\t\t<location id=\"id" + idTriggered + "\" x=\"-187\" y=\"-323\">");
	result.append("\n\t\t\t<name x=\"-170\" y=\"-332\">Triggered</name>");
	result.append("\n\t\t\t<label kind=\"exponentialrate\" x=\"-170\" y=\"-349\">1</label>");
	result.append("\n\t\t</location>");
    }

    private void writeNotTriggeredLocation() {
	result.append("\n\t\t<location id=\"id" + idNotTriggered + "\" x=\"-467\" y=\"-323\">");
	result.append("\n\t\t\t<name x=\"-569\" y=\"-331\">NotTriggered</name>");
	result.append("\n\t\t\t<label kind=\"exponentialrate\" x=\"-476\" y=\"-357\">1</label>");
	result.append("\n\t\t</location>");
    }

    private void writeInitialState() {
	result.append("\n\t\t<init ref=\"id" + idWaiting + "\"/>");
    }

    private void writeTransitions(int i) {
	writeTriggeredToWaitingTransition(i);
	writeNotTriggeredToWaitingTransition(i);
	writeWaitingToNotTriggeredTransition(i);
	writeWaitingToTriggeredTransition(i);
    }

    private void writeWaitingToTriggeredTransition(int i) {
	result.append("\n\t\t<transition>");
	result.append("\n\t\t\t<source ref=\"id" + idWaiting + "\"/>");
	result.append("\n\t\t\t<target ref=\"id" + idTriggered + "\"/>");
	result.append("\n\t\t\t<label kind=\"guard\" x=\"-246\" y=\"-425\">s[1] == true</label>");
	result.append("\n\t\t\t<label kind=\"synchronisation\" x=\"-246\" y=\"-442\">c_str[" + i + "]?</label>");
	result.append("\n\t\t\t<label kind=\"assignment\" x=\"-246\" y=\"-408\">s[4]:= false</label>");
	result.append("\n\t\t</transition>");
    }

    private void writeWaitingToNotTriggeredTransition(int i) {
	result.append("\n\t\t<transition>");
	result.append("\n\t\t\t<source ref=\"id" + idWaiting + "\"/>");
	result.append("\n\t\t\t<target ref=\"id" + idNotTriggered + "\"/>");
	result.append("\n\t\t\t<label kind=\"guard\" x=\"-493\" y=\"-408\">s[1] == false</label>");
	result.append("\n\t\t\t<label kind=\"synchronisation\" x=\"-467\" y=\"-425\">c_str[" + i + "]?</label>");
	result.append("\n\t\t</transition>");
    }

    private void writeNotTriggeredToWaitingTransition(int i) {
	result.append("\n\t\t<transition>");
	result.append("\n\t\t\t<source ref=\"id" + idNotTriggered + "\"/>");
	result.append("\n\t\t\t<target ref=\"id" + idWaiting + "\"/>");
	result.append("\n\t\t\t<label kind=\"synchronisation\" x=\"-425\" y=\"-323\">c_str[" + i + "+1]!</label>");
	result.append("\n\t\t\t<nail x=\"-331\" y=\"-323\"/>");
	result.append("\n\t\t</transition>");
    }

    private void writeTriggeredToWaitingTransition(int i) {
	result.append("\n\t\t<transition>");
	result.append("\n\t\t\t<source ref=\"id" + idTriggered + "\"/>");
	result.append("\n\t\t\t<target ref=\"id" + idWaiting + "\"/>");
	result.append("\n\t\t\t<label kind=\"synchronisation\" x=\"-280\" y=\"-323\">c_str[" + i + "+1]!</label>");
	result.append("\n\t\t\t<nail x=\"-331\" y=\"-323\"/>");
	result.append("\n\t\t</transition>");
    }

    private String getConsequent(int i) {

	String consequent = "";
	for (MObject child : systemData.getStrs().get(i).getCompositionChildren()) {
	    consequent = consequent + child.getName();
	}
	return consequent;
    }

}
