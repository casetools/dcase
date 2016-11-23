package edu.casetools.dcase.extensions.io.old;

import java.io.IOException;

import edu.casetools.dcase.extensions.io.FakeData;
import edu.casetools.dcase.extensions.io.MData;

public class SystemDeclarationWriter {

    private static final String STRING_FALSE = "false";
    private static final String STRING_TRUE_FULL = ",true);\n";
    private static final String STRING_FALSE_FULL = "," + STRING_FALSE + ");\n";
    private static final String STRING_EVENT = " = Event(";
    private StringBuilder result;
    private int eventNumber;
    private MData systemData;

    public SystemDeclarationWriter(StringBuilder writer, MData systemData) {
	this.result = writer;
	this.systemData = systemData;
    }

    public void writeSystemDeclaration() throws IOException {
	result.append("<system>");
	writeDominoEvent();
	writeBOPEvent();
	writeBOPs();
	writeRules();
	initializeSystem();
	result.append("</system>");
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
	    result.append("srt" + i + " = S_SRT(" + i + "," + srtBound + "," + (FakeData.STATE_NO + counter)
		    + STRING_TRUE_FULL);
	    counter++;
	}
	for (int i = 0; i < FakeData.S_WTR_NO; i++) {
	    result.append("wrt" + i + " = S_WRT(" + i + "," + wrtBound + "," + (FakeData.STATE_NO + counter)
		    + STRING_TRUE_FULL);
	    counter++;
	}
	for (int i = 0; i < FakeData.S_SAT_NO; i++) {
	    result.append("sat" + i + " = S_SAT(" + i + "," + satLowBound + "," + satUppBound + ","
		    + (FakeData.STATE_NO + counter) + STRING_TRUE_FULL);
	    counter++;
	}
	for (int i = 0; i < FakeData.S_WAT_NO; i++) {
	    result.append("wat" + i + " = S_WAT(" + i + "," + watLowBound + "," + watUppBound + ","
		    + (FakeData.STATE_NO + counter) + STRING_TRUE_FULL);
	    counter++;
	}
	result.append("\n");

    }

    private void writeRules() throws IOException {
	for (int i = 0; i < FakeData.STATE_NO / 2; i++) {
	    result.append("R_STR_" + i + " = STR_" + i + "();\n");
	}
	for (int i = 0; i < FakeData.STATE_NO - 1; i++) {
	    result.append("R_NTR_" + i + " = NTR_" + i + "();\n");
	}
	result.append("\n");
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
	    result.append("E" + eventCounter + STRING_EVENT + eventCounter + "," + iteration + ","
		    + (FakeData.STATE_NO + stateCounter) + STRING_TRUE_FULL);
	    eventCounter++;
	    result.append("E" + eventCounter + STRING_EVENT + eventCounter + "," + (iteration + srtGetsFalseIn) + ","
		    + (FakeData.STATE_NO + stateCounter) + STRING_FALSE_FULL);
	    iteration++;
	    eventCounter++;
	    stateCounter++;
	}
	for (int i = 0; i < FakeData.S_WTR_NO; i++) {
	    result.append("E" + eventCounter + STRING_EVENT + eventCounter + "," + iteration + ","
		    + (FakeData.STATE_NO + stateCounter) + STRING_TRUE_FULL);
	    eventCounter++;
	    result.append("E" + eventCounter + STRING_EVENT + eventCounter + "," + (iteration + wrtGetsFalseIn) + ","
		    + (FakeData.STATE_NO + stateCounter) + STRING_FALSE_FULL);
	    iteration++;
	    eventCounter++;
	    stateCounter++;
	}
	for (int i = 0; i < FakeData.S_SAT_NO; i++) {
	    result.append("E" + eventCounter + STRING_EVENT + eventCounter + "," + iteration + ","
		    + (FakeData.STATE_NO + stateCounter) + STRING_TRUE_FULL);
	    eventCounter++;
	    result.append("E" + eventCounter + STRING_EVENT + eventCounter + "," + (iteration + satGetsFalseIn) + ","
		    + (FakeData.STATE_NO + stateCounter) + STRING_FALSE_FULL);
	    iteration++;
	    eventCounter++;
	    stateCounter++;
	}
	for (int i = 0; i < FakeData.S_WAT_NO; i++) {
	    result.append("E" + eventCounter + STRING_EVENT + eventCounter + "," + iteration + ","
		    + (FakeData.STATE_NO + stateCounter) + STRING_TRUE_FULL);
	    eventCounter++;
	    result.append("E" + eventCounter + STRING_EVENT + eventCounter + "," + (iteration + watGetsFalseIn) + ","
		    + (FakeData.STATE_NO + stateCounter) + STRING_FALSE_FULL);
	    iteration++;
	    eventCounter++;
	    stateCounter++;
	}
	eventNumber = eventCounter;
	result.append("\n");

    }

    private void writeDominoEvent() throws IOException {
	result.append("E0 = Event(0,1,0,true);\n");
    }

    private void initializeSystem() throws IOException {
	result.append("system M\n");

	writeEventsSystemInitialization();
	writeBoundedOperatorsSystemInitialization();
	writeStatesSystemDeclaration();
    }

    private void writeBoundedOperatorsSystemInitialization() throws IOException {
	for (int i = 0; i < FakeData.S_STR_NO; i++) {
	    result.append(",srt" + i);
	    breakLine(i);
	}
	result.append("\n");
	for (int i = 0; i < FakeData.S_WTR_NO; i++) {
	    result.append(",wrt" + i);
	    breakLine(i);
	}
	result.append("\n");
	for (int i = 0; i < FakeData.S_SAT_NO; i++) {
	    result.append(",sat" + i);
	    breakLine(i);
	}
	result.append("\n");
	for (int i = 0; i < FakeData.S_WAT_NO; i++) {
	    result.append(",wat" + i);
	    breakLine(i);
	}
    }

    private void writeStatesSystemDeclaration() throws IOException {
	result.append("\n");
	for (int i = 0; i < systemData.getStrs().size(); i++) {
	    result.append(",R_STR_" + i);
	    breakLine(i);
	}
	result.append("\n");
	for (int i = 0; i < systemData.getNtrs().size(); i++) {
	    result.append(",R_NTR_" + i);
	    breakLine(i);
	}
	result.append(";\n");
    }

    private void writeEventsSystemInitialization() throws IOException {
	for (int i = 0; i < eventNumber; i++) {
	    result.append(",E" + i);
	    breakLine(i);
	}
	result.append("\n");
    }

    private void breakLine(int i) throws IOException {
	if (i % 10 == 0 && i != 0) {
	    result.append("\n");
	}
    }

}
