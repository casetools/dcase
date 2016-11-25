package edu.casetools.dcase.extensions.io.mnta;

import java.io.IOException;

import org.modelio.metamodel.uml.infrastructure.ModelElement;
import org.modelio.vcore.smkernel.mapi.MObject;

import edu.casetools.dcase.extensions.io.mnta.data.FakeData;
import edu.casetools.dcase.extensions.io.mnta.data.MData;
import edu.casetools.dcase.module.api.DCaseStereotypes;
import edu.casetools.dcase.module.impl.DCasePeerModule;

public class SystemDeclarationWriter {

    private static final String STRING_FALSE = "false";
    private static final String STRING_TRUE_FULL = ",true);\n";
    private static final String STRING_FALSE_FULL = "," + STRING_FALSE + ");\n";
    private static final String STRING_EVENT = " = Event(";
    private StringBuilder writer;
    private int eventNumber;
    private MData systemData;

    public SystemDeclarationWriter(MData systemData) {
	this.systemData = systemData;
	writer = new StringBuilder();
    }

    public String write() throws IOException {
	writeDominoEvent();
	writeBOPEvent();
	writeBOPs();
	writeRules();
	initializeSystem();
	return writer.toString();
    }

    private void writeBOPs() throws IOException {
	int srtBound = 5;
	int wrtBound = 3;
	int satLowBound = 2;
	int satUppBound = 4;
	int watLowBound = 2;
	int watUppBound = 4;
	int counter = 0;

	for (int i = 0; i < FakeData.S_STR_NO; i++) {
	    writer.append("srt" + i + " = S_SRT(" + i + "," + srtBound + "," + (FakeData.STATE_NO + counter)
		    + STRING_TRUE_FULL);
	    counter++;
	}
	for (int i = 0; i < FakeData.S_WTR_NO; i++) {
	    writer.append("wrt" + i + " = S_WRT(" + i + "," + wrtBound + "," + (FakeData.STATE_NO + counter)
		    + STRING_TRUE_FULL);
	    counter++;
	}
	for (int i = 0; i < FakeData.S_SAT_NO; i++) {
	    writer.append("sat" + i + " = S_SAT(" + i + "," + satLowBound + "," + satUppBound + ","
		    + (FakeData.STATE_NO + counter) + STRING_TRUE_FULL);
	    counter++;
	}
	for (int i = 0; i < FakeData.S_WAT_NO; i++) {
	    writer.append("wat" + i + " = S_WAT(" + i + "," + watLowBound + "," + watUppBound + ","
		    + (FakeData.STATE_NO + counter) + STRING_TRUE_FULL);
	    counter++;
	}
	writer.append("\n");

    }

    private void writeRules() throws IOException {
	for (int i = 0; i < FakeData.STATE_NO / 2; i++) {
	    writer.append("R_STR_" + i + " = STR_" + i + "();\n");
	}
	for (int i = 0; i < FakeData.STATE_NO - 1; i++) {
	    writer.append("R_NTR_" + i + " = NTR_" + i + "();\n");
	}
	writer.append("\n");
    }

    private void writeBOPEvent() throws IOException {
	int iteration = (FakeData.STATE_NO / 2) + 1;
	int srtGetsFalseIn = 5;
	int wrtGetsFalseIn = 1;
	int satGetsFalseIn = 2;
	int watGetsFalseIn = 1;
	int eventCounter = 1;
	int stateCounter = 0;
	for (int i = 0; i < FakeData.S_STR_NO; i++) {
	    writer.append("E" + eventCounter + STRING_EVENT + eventCounter + "," + iteration + ","
		    + (FakeData.STATE_NO + stateCounter) + STRING_TRUE_FULL);
	    eventCounter++;
	    writer.append("E" + eventCounter + STRING_EVENT + eventCounter + "," + (iteration + srtGetsFalseIn) + ","
		    + (FakeData.STATE_NO + stateCounter) + STRING_FALSE_FULL);
	    iteration++;
	    eventCounter++;
	    stateCounter++;
	}
	for (int i = 0; i < FakeData.S_WTR_NO; i++) {
	    writer.append("E" + eventCounter + STRING_EVENT + eventCounter + "," + iteration + ","
		    + (FakeData.STATE_NO + stateCounter) + STRING_TRUE_FULL);
	    eventCounter++;
	    writer.append("E" + eventCounter + STRING_EVENT + eventCounter + "," + (iteration + wrtGetsFalseIn) + ","
		    + (FakeData.STATE_NO + stateCounter) + STRING_FALSE_FULL);
	    iteration++;
	    eventCounter++;
	    stateCounter++;
	}
	for (int i = 0; i < FakeData.S_SAT_NO; i++) {
	    writer.append("E" + eventCounter + STRING_EVENT + eventCounter + "," + iteration + ","
		    + (FakeData.STATE_NO + stateCounter) + STRING_TRUE_FULL);
	    eventCounter++;
	    writer.append("E" + eventCounter + STRING_EVENT + eventCounter + "," + (iteration + satGetsFalseIn) + ","
		    + (FakeData.STATE_NO + stateCounter) + STRING_FALSE_FULL);
	    iteration++;
	    eventCounter++;
	    stateCounter++;
	}
	for (int i = 0; i < FakeData.S_WAT_NO; i++) {
	    writer.append("E" + eventCounter + STRING_EVENT + eventCounter + "," + iteration + ","
		    + (FakeData.STATE_NO + stateCounter) + STRING_TRUE_FULL);
	    eventCounter++;
	    writer.append("E" + eventCounter + STRING_EVENT + eventCounter + "," + (iteration + watGetsFalseIn) + ","
		    + (FakeData.STATE_NO + stateCounter) + STRING_FALSE_FULL);
	    iteration++;
	    eventCounter++;
	    stateCounter++;
	}
	eventNumber = eventCounter;
	writer.append("\n");

    }

    private void writeDominoEvent() throws IOException {
	writer.append("E0 = Event(0,1,0,true);\n");
    }

    private void initializeSystem() throws IOException {
	writer.append("system M\n");

	writeEventsSystemInitialization();
	writeBoundedOperatorsSystemInitialization();
	writeStatesSystemDeclaration();
    }

    private void writeBoundedOperatorsSystemInitialization() throws IOException {
	for (int i = 0; i < FakeData.S_STR_NO; i++) {
	    writer.append(",srt" + i);
	    breakLine(i);
	}
	writer.append("\n");
	for (int i = 0; i < FakeData.S_WTR_NO; i++) {
	    writer.append(",wrt" + i);
	    breakLine(i);
	}
	writer.append("\n");
	for (int i = 0; i < FakeData.S_SAT_NO; i++) {
	    writer.append(",sat" + i);
	    breakLine(i);
	}
	writer.append("\n");
	for (int i = 0; i < FakeData.S_WAT_NO; i++) {
	    writer.append(",wat" + i);
	    breakLine(i);
	}
    }

    private void writeStatesSystemDeclaration() throws IOException {
	writer.append("\n");
	for (int i = 0; i < systemData.getAntecedentGroups().size(); i++) {
	    for (MObject child : systemData.getAntecedentGroups().get(i).getCompositionChildren()) {
		if (((ModelElement) child).isStereotyped(DCasePeerModule.MODULE_NAME,
			DCaseStereotypes.STEREOTYPE_SAME_TIME)) {
		    writer.append(",R_STR_" + i);
		}
		if (((ModelElement) child).isStereotyped(DCasePeerModule.MODULE_NAME,
			DCaseStereotypes.STEREOTYPE_NEXT_TIME)) {
		    writer.append(",R_NTR_" + i);
		}
		breakLine(i);
	    }
	}
	writer.append(";\n");
    }

    private void writeEventsSystemInitialization() throws IOException {
	for (int i = 0; i < eventNumber; i++) {
	    writer.append(",E" + i);
	    breakLine(i);
	}
	writer.append("\n");
    }

    private void breakLine(int i) throws IOException {
	if (i % 10 == 0 && i != 0) {
	    writer.append("\n");
	}
    }

}
