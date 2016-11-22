package edu.casetools.dcase.extensions.io.elements;

import java.io.IOException;

import edu.casetools.dcase.extensions.io.FakeData;

public class QueryWriter {

    private StringBuilder result;
    private int[] states;

    public QueryWriter(StringBuilder writer) {
	this.result = writer;
	initialise();

    }

    private void initialise() {
	states = new int[FakeData.STATE_NO];
	for (int i = 0; i < FakeData.STATE_NO; i++) {
	    if (i < (FakeData.STATE_NO / 2)) {
		states[i] = 0;
	    } else
		states[i] = 1;
	}
    }

    public void writeQueries() throws IOException {
	result.append("<queries>");
	for (int i = 0; i < FakeData.STATE_NO; i++) {
	    if (i > 0) {
		shiftStates(i);
	    }
	    result.append("<query>\n\t<formula>(iteration ==" + i + " &amp;&amp; M.Iteration) --&gt; (");
	    for (int j = 0; j < FakeData.STATE_NO; j++) {
		writeStateInQuery(j);
	    }
	    for (int j = FakeData.STATE_NO; j < FakeData.BOP_NO + FakeData.STATE_NO; j++) {
		result.append("&amp;&amp; s[" + j + "] == false");
	    }

	    result.append(")\n\t</formula>\n\t<comment>\n\t</comment>\n</query>\n");
	}
	result.append("</queries>");
    }

    private void writeStateInQuery(int j) throws IOException {
	if (j != 0) {
	    result.append(" &amp;&amp; ");
	}
	result.append("s[" + j + "] == " + states[j]);
    }

    private void shiftStates(int i) {
	if ((i - 1) >= 0) {
	    states[i - 1] = 1;
	}
	if ((i - 2) >= 0) {
	    states[i - 2] = 0;
	}
	if (((FakeData.STATE_NO / 2) + i - 1) < FakeData.STATE_NO) {
	    states[(FakeData.STATE_NO / 2) + i - 1] = 0;
	}
    }

}
