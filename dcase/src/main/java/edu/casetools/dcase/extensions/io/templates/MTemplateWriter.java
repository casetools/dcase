package edu.casetools.dcase.extensions.io.templates;

import edu.casetools.dcase.extensions.io.LocationManager;
import edu.casetools.dcase.extensions.io.MData;
import edu.casetools.dcase.extensions.io.locations.MLocations;

public class MTemplateWriter extends TemplateWriter {

    public MTemplateWriter(StringBuilder writer, MData systemData) {
	super(writer, systemData);
	registerLocations();
    }

    private void registerLocations() {
	LocationManager.getInstance().registerLocation(MLocations.INITIAL);
	LocationManager.getInstance().registerLocation(MLocations.INITIAL_TO_EVENTS);
	LocationManager.getInstance().registerLocation(MLocations.EVENTS);
	LocationManager.getInstance().registerLocation(MLocations.EVENTS_TO_BOP);
	LocationManager.getInstance().registerLocation(MLocations.BOP);
	LocationManager.getInstance().registerLocation(MLocations.BOP_TO_STR);
	LocationManager.getInstance().registerLocation(MLocations.STR);
	LocationManager.getInstance().registerLocation(MLocations.STR_TO_NTR);
	LocationManager.getInstance().registerLocation(MLocations.NTR);
	LocationManager.getInstance().registerLocation(MLocations.ITERATION);
	LocationManager.getInstance().registerLocation(MLocations.ITERATION_TO_NTR_EFFECT);
	LocationManager.getInstance().registerLocation(MLocations.NTR_EFFECT);
    }

    @Override
    public StringBuilder write() {
	result.append("\t<template>\n");
	result.append("\t\t<name>M</name>\n");
	result.append("\t\t<declaration>");
	writeIncrementFunction();
	writeInitialiseFunction();
	result.append("</declaration>");
	writeLocations();
	result.append(
		"\n\t\t<init ref=\"id" + LocationManager.getInstance().getLocationId(MLocations.INITIAL) + "\"/>");
	writeTransitions();
	result.append("\n\t</template>");

	return result;
    }

    private void writeLocations() {
	writeEventsLocation();
	writeEventToBopLocation();
	writeInitialToEventsLocation();
	writeBOPsToSTRLocation();
	writeIterationToNTREffectLocation();
	writeNTRLocation();
	writeIterationLocation();
	writeNTREffectLocation();
	writeInitialLocation();
	writeSTRtoNTRLocation();
	writeSTRLocation();
	writeBOPLocation();
    }

    private void writeBOPLocation() {
	result.append("\n\t\t<location id=\"id" + LocationManager.getInstance().getLocationId(MLocations.BOP)
		+ "\" x=\"102\" y=\"-110\">");
	result.append("\n\t\t\t<name x=\"128\" y=\"-119\">BOPs</name>");
	result.append("\n\t\t\t<label kind=\"exponentialrate\" x=\"77\" y=\"-101\">1</label>");
	result.append("\n\t\t</location>");
    }

    private void writeSTRLocation() {
	result.append("\n\t\t<location id=\"id" + LocationManager.getInstance().getLocationId(MLocations.STR)
		+ "\" x=\"102\" y=\"161\">");
	result.append("\n\t\t\t<name x=\"119\" y=\"153\">STR</name>");
	result.append("\n\t\t\t<label kind=\"exponentialrate\" x=\"77\" y=\"144\">1</label>");
	result.append("\n\t\t</location>");
    }

    private void writeSTRtoNTRLocation() {
	result.append("\n\t\t<location id=\"id" + LocationManager.getInstance().getLocationId(MLocations.STR_TO_NTR)
		+ "\" x=\"102\" y=\"238\">");
	result.append("\n\t\t\t<name x=\"0\" y=\"212\">STR_TO_NTR</name>");
	result.append("\n\t\t\t<label kind=\"exponentialrate\" x=\"111\" y=\"213\">1</label>");
	result.append("\n\t\t</location>");
    }

    private void writeInitialLocation() {
	result.append("\n\t\t<location id=\"id" + LocationManager.getInstance().getLocationId(MLocations.INITIAL)
		+ "\" x=\"102\" y=\"-603\">");
	result.append("\n\t\t\t<name x=\"127\" y=\"-612\">INITIAL</name>");
	result.append("\n\t\t\t<label kind=\"exponentialrate\" x=\"77\" y=\"-603\">1</label>");
	result.append("\n\t\t</location>");
    }

    private void writeNTREffectLocation() {
	result.append("\n\t\t<location id=\"id"
		+ LocationManager.getInstance()
			.getLocationId(LocationManager.getInstance().getLocationId(MLocations.NTR_EFFECT))
		+ "\" x=\"-365\" y=\"416\">");
	result.append("\n\t\t\t<name x=\"-476\" y=\"425\">NTR_EFFECT</name>");
	result.append("\n\t\t\t<label kind=\"exponentialrate\" x=\"-375\" y=\"450\">1</label>");
	result.append("\n\t\t</location>");
    }

    private void writeIterationLocation() {
	result.append("\n\t\t<location id=\"id" + LocationManager.getInstance().getLocationId(MLocations.ITERATION)
		+ "\" x=\"102\" y=\"416\">");
	result.append("\n\t\t\t<name x=\"119\" y=\"407\">ITERATION</name>");
	result.append("\n\t\t\t<label kind=\"exponentialrate\" x=\"102\" y=\"425\">1</label>");
	result.append("\n\t\t</location>");
    }

    private void writeNTRLocation() {
	result.append("\n\t\t<location id=\"id" + LocationManager.getInstance().getLocationId(MLocations.NTR)
		+ "\" x=\"102\" y=\"323\">");
	result.append("\n\t\t\t<name x=\"128\" y=\"314\">NTR</name>");
	result.append("\n\t\t\t<label kind=\"exponentialrate\" x=\"92\" y=\"357\">1</label>");
	result.append("\n\t\t</location>");
    }

    private void writeIterationToNTREffectLocation() {
	result.append("\n\t\t<location id=\"id"
		+ LocationManager.getInstance().getLocationId(MLocations.ITERATION_TO_NTR_EFFECT)
		+ "\" x=\"-161\" y=\"416\">");
	result.append("\n\t\t\t<name x=\"-255\" y=\"442\">ITERATION_TO_NTR_EFFECT</name>");
	result.append("\n\t\t\t<label kind=\"exponentialrate\" x=\"-178\" y=\"382\">1</label>");
	result.append("\n\t\t</location>");
    }

    private void writeBOPsToSTRLocation() {
	result.append("\n\t\t<location id=\"id" + LocationManager.getInstance().getLocationId(MLocations.BOP_TO_STR)
		+ "\" x=\"102\" y=\"85\">");
	result.append("\n\t\t\t<name x=\"-34\" y=\"68\">BOPS_TO_STR</name>");
	result.append("\n\t\t\t<label kind=\"exponentialrate\" x=\"76\" y=\"68\">1</label>");
	result.append("\n\t\t</location>");
    }

    private void writeInitialToEventsLocation() {
	result.append(
		"\n\t\t<location id=\"id" + LocationManager.getInstance().getLocationId(MLocations.INITIAL_TO_EVENTS)
			+ "\" x=\"102\" y=\"-518\">");
	result.append("\n\t\t\t<name x=\"119\" y=\"-527\">INITIAL_TO_EVENTS</name>");
	result.append("\n\t\t\t<label kind=\"exponentialrate\" x=\"92\" y=\"-484\">1</label>");
	result.append("\n\t\t</location>");
    }

    private void writeEventToBopLocation() {
	result.append("\n\t\t<location id=\"id" + LocationManager.getInstance().getLocationId(MLocations.EVENTS_TO_BOP)
		+ "\" x=\"102\" y=\"-289\">");
	result.append("\n\t\t\t<name x=\"119\" y=\"-314\">EVENT_TO_BOP</name>");
	result.append("\n\t\t\t<label kind=\"exponentialrate\" x=\"92\" y=\"-255\">1</label>");
	result.append("\n\t\t</location>");
    }

    private void writeEventsLocation() {
	result.append("\n\t\t<location id=\"id" + LocationManager.getInstance().getLocationId(MLocations.EVENTS)
		+ "\" x=\"102\" y=\"-408\">");
	result.append("\n\t\t\t<name x=\"127\" y=\"-416\">EVENTS</name>");
	result.append("\n\t\t\t<label kind=\"exponentialrate\" x=\"92\" y=\"-374\">1</label>");
	result.append("\n\t\t</location>");
    }

    private void writeTransitions() {
	result.append("\n\t\t<transition>");
	result.append(
		"\n\t\t\t<source ref=\"id" + LocationManager.getInstance().getLocationId(MLocations.INITIAL) + "\"/>");
	result.append("\n\t\t\t<target ref=\"id"
		+ LocationManager.getInstance().getLocationId(MLocations.INITIAL_TO_EVENTS) + "\"/>");
	result.append("\n\t\t\t<label kind=\"guard\" x=\"153\" y=\"-578\">reset == false</label>");
	result.append("\n\t\t\t<nail x=\"144\" y=\"-561\"/>");
	result.append("\n\t\t</transition>");
	result.append("\n\t\t<transition>");
	result.append("\n\t\t\t<source ref=\"id"
		+ LocationManager.getInstance().getLocationId(MLocations.INITIAL_TO_EVENTS) + "\"/>");
	result.append("\n\t\t\t<target ref=\"id" + LocationManager.getInstance().getLocationId(MLocations.EVENTS_TO_BOP)
		+ "\"/>");
	result.append("\n\t\t\t<label kind=\"guard\" x=\"-93\" y=\"-408\">EVENT_NO &lt; 1</label>");
	result.append("\n\t\t\t<nail x=\"8\" y=\"-518\"/>");
	result.append("\n\t\t\t<nail x=\"8\" y=\"-289\"/>");
	result.append("\n\t\t</transition>");
	result.append("\n\t\t<transition>");
	result.append(
		"\n\t\t\t<source ref=\"id" + LocationManager.getInstance().getLocationId(MLocations.EVENTS) + "\"/>");
	result.append("\n\t\t\t<target ref=\"id" + LocationManager.getInstance().getLocationId(MLocations.EVENTS_TO_BOP)
		+ "\"/>");
	result.append("\n\t\t\t<label kind=\"synchronisation\" x=\"110\" y=\"-365\">c_e[EVENT_NO]?</label>");
	result.append("\n\t\t</transition>");
	result.append("\n\t\t<transition>");
	result.append("\n\t\t\t<source ref=\"id" + LocationManager.getInstance().getLocationId(MLocations.EVENTS_TO_BOP)
		+ "\"/>");
	result.append(
		"\n\t\t\t<target ref=\"id" + LocationManager.getInstance().getLocationId(MLocations.BOP) + "\"/>");
	result.append("\n\t\t\t<label kind=\"guard\" x=\"110\" y=\"-246\">BOP_NO &gt; 0</label>");
	result.append("\n\t\t\t<label kind=\"synchronisation\" x=\"110\" y=\"-229\">c_bop[0]!</label>");
	result.append("\n\t\t</transition>");
	result.append("\n\t\t<transition>");
	result.append("\n\t\t\t<source ref=\"id"
		+ LocationManager.getInstance().getLocationId(MLocations.INITIAL_TO_EVENTS) + "\"/>");
	result.append(
		"\n\t\t\t<target ref=\"id" + LocationManager.getInstance().getLocationId(MLocations.EVENTS) + "\"/>");
	result.append("\n\t\t\t<label kind=\"guard\" x=\"119\" y=\"-484\">EVENT_NO &gt; 0</label>");
	result.append("\n\t\t\t<label kind=\"synchronisation\" x=\"119\" y=\"-467\">c_e[0]!</label>");
	result.append("\n\t\t</transition>");
	result.append("\n\t\t<transition>");
	result.append("\n\t\t\t<source ref=\"id" + LocationManager.getInstance().getLocationId(MLocations.EVENTS_TO_BOP)
		+ "\"/>");
	result.append("\n\t\t\t<target ref=\"id" + LocationManager.getInstance().getLocationId(MLocations.BOP_TO_STR)
		+ "\"/>");
	result.append("\n\t\t\t<label kind=\"guard\" x=\"289\" y=\"-110\">BOP_NO &lt; 1</label>");
	result.append("\n\t\t\t<nail x=\"272\" y=\"-289\"/>");
	result.append("\n\t\t\t<nail x=\"272\" y=\"34\"/>");
	result.append("\n\t\t</transition>");
	result.append("\n\t\t<transition>");
	result.append("\n\t\t\t<source ref=\"id"
		+ LocationManager.getInstance().getLocationId(MLocations.ITERATION_TO_NTR_EFFECT) + "\"/>");
	result.append(
		"\n\t\t\t<target ref=\"id" + LocationManager.getInstance().getLocationId(MLocations.INITIAL) + "\"/>");
	result.append("\n\t\t\t<label kind=\"guard\" x=\"-255\" y=\"255\">NTR_NO &lt;= 0</label>");
	result.append("\n\t\t\t<nail x=\"-161\" y=\"110\"/>");
	result.append("\n\t\t\t<nail x=\"-161\" y=\"-552\"/>");
	result.append("\n\t\t</transition>");
	result.append("\n\t\t<transition>");
	result.append("\n\t\t\t<source ref=\"id" + LocationManager.getInstance().getLocationId(MLocations.STR_TO_NTR)
		+ "\"/>");
	result.append("\n\t\t\t<target ref=\"id" + LocationManager.getInstance().getLocationId(MLocations.ITERATION)
		+ "\"/>");
	result.append("\n\t\t\t<label kind=\"guard\" x=\"-76\" y=\"297\">NTR_NO &lt;= 0</label>");
	result.append("\n\t\t\t<nail x=\"18\" y=\"238\"/>");
	result.append("\n\t\t\t<nail x=\"25\" y=\"374\"/>");
	result.append("\n\t\t</transition>");
	result.append("\n\t\t<transition>");
	result.append("\n\t\t\t<source ref=\"id" + LocationManager.getInstance().getLocationId(MLocations.BOP_TO_STR)
		+ "\"/>");
	result.append("\n\t\t\t<target ref=\"id" + LocationManager.getInstance().getLocationId(MLocations.STR_TO_NTR)
		+ "\"/>");
	result.append("\n\t\t\t<label kind=\"guard\" x=\"281\" y=\"144\">STR_NO &lt;= 0</label>");
	result.append("\n\t\t\t<nail x=\"247\" y=\"85\"/>");
	result.append("\n\t\t\t<nail x=\"247\" y=\"238\"/>");
	result.append("\n\t\t</transition>");
	result.append("\n\t\t<transition>");
	result.append(
		"\n\t\t\t<source ref=\"id" + LocationManager.getInstance().getLocationId(MLocations.INITIAL) + "\"/>");
	result.append("\n\t\t\t<target ref=\"id"
		+ LocationManager.getInstance().getLocationId(MLocations.INITIAL_TO_EVENTS) + "\"/>");
	result.append("\n\t\t\t<label kind=\"guard\" x=\"-25\" y=\"-578\">reset == true</label>");
	result.append("\n\t\t\t<label kind=\"assignment\" x=\"-8\" y=\"-561\">initialise()</label>");
	result.append("\n\t\t\t<nail x=\"59\" y=\"-561\"/>");
	result.append("\n\t\t</transition>");
	result.append("\n\t\t<transition>");
	result.append("\n\t\t\t<source ref=\"id" + LocationManager.getInstance().getLocationId(MLocations.BOP_TO_STR)
		+ "\"/>");
	result.append(
		"\n\t\t\t<target ref=\"id" + LocationManager.getInstance().getLocationId(MLocations.STR) + "\"/>");
	result.append("\n\t\t\t<label kind=\"guard\" x=\"119\" y=\"93\">STR_NO &gt; 0</label>");
	result.append("\n\t\t\t<label kind=\"synchronisation\" x=\"119\" y=\"110\">c_str[0]!</label>");
	result.append("\n\t\t</transition>");
	result.append("\n\t\t<transition>");
	result.append("\n\t\t\t<source ref=\"id" + LocationManager.getInstance().getLocationId(MLocations.ITERATION)
		+ "\"/>");
	result.append("\n\t\t\t<target ref=\"id"
		+ LocationManager.getInstance().getLocationId(MLocations.ITERATION_TO_NTR_EFFECT) + "\"/>");
	result.append("\n\t\t\t<label kind=\"assignment\" x=\"-76\" y=\"391\">increment()</label>");
	result.append("\n\t\t</transition>");
	result.append("\n\t\t<transition>");
	result.append(
		"\n\t\t\t<source ref=\"id" + LocationManager.getInstance().getLocationId(MLocations.NTR) + "\"/>");
	result.append("\n\t\t\t<target ref=\"id" + LocationManager.getInstance().getLocationId(MLocations.ITERATION)
		+ "\"/>");
	result.append("\n\t\t\t<label kind=\"synchronisation\" x=\"119\" y=\"357\">c_ntr[NTR_NO]?</label>");
	result.append("\n\t\t</transition>");
	result.append("\n\t\t<transition>");
	result.append("\n\t\t\t<source ref=\"id" + LocationManager.getInstance().getLocationId(MLocations.STR_TO_NTR)
		+ "\"/>");
	result.append(
		"\n\t\t\t<target ref=\"id" + LocationManager.getInstance().getLocationId(MLocations.NTR) + "\"/>");
	result.append("\n\t\t\t<label kind=\"guard\" x=\"119\" y=\"246\">NTR_NO &gt; 0</label>");
	result.append("\n\t\t\t<label kind=\"synchronisation\" x=\"119\" y=\"263\">c_ntr[0]!</label>");
	result.append("\n\t\t</transition>");
	result.append("\n\t\t<transition>");
	result.append("\n\t\t\t<source ref=\"id" + LocationManager.getInstance().getLocationId(MLocations.NTR_EFFECT)
		+ "\"/>");
	result.append(
		"\n\t\t\t<target ref=\"id" + LocationManager.getInstance().getLocationId(MLocations.INITIAL) + "\"/>");
	result.append("\n\t\t\t<label kind=\"synchronisation\" x=\"-501\" y=\"263\">c_ntr[NTR_NO]?</label>");
	result.append("\n\t\t\t<nail x=\"-365\" y=\"-603\"/>");
	result.append("\n\t\t</transition>");
	result.append("\n\t\t<transition>");
	result.append("\n\t\t\t<source ref=\"id"
		+ LocationManager.getInstance().getLocationId(MLocations.ITERATION_TO_NTR_EFFECT) + "\"/>");
	result.append("\n\t\t\t<target ref=\"id" + LocationManager.getInstance().getLocationId(MLocations.NTR_EFFECT)
		+ "\"/>");
	result.append("\n\t\t\t<label kind=\"guard\" x=\"-297\" y=\"391\">NTR_NO &gt; 0</label>");
	result.append("\n\t\t\t<label kind=\"synchronisation\" x=\"-289\" y=\"374\">c_ntr[0]!</label>");
	result.append("\n\t\t</transition>");
	result.append("\n\t\t<transition>");
	result.append(
		"\n\t\t\t<source ref=\"id" + LocationManager.getInstance().getLocationId(MLocations.STR) + "\"/>");
	result.append("\n\t\t\t<target ref=\"id" + LocationManager.getInstance().getLocationId(MLocations.STR_TO_NTR)
		+ "\"/>");
	result.append("\n\t\t\t<label kind=\"synchronisation\" x=\"119\" y=\"178\">c_str[STR_NO]?</label>");
	result.append("\n\t\t</transition>");
	result.append("\n\t\t<transition>");
	result.append(
		"\n\t\t\t<source ref=\"id" + LocationManager.getInstance().getLocationId(MLocations.BOP) + "\"/>");
	result.append("\n\t\t\t<target ref=\"id" + LocationManager.getInstance().getLocationId(MLocations.BOP_TO_STR)
		+ "\"/>");
	result.append("\n\t\t\t<label kind=\"synchronisation\" x=\"110\" y=\"-51\">c_bop[BOP_NO]?</label>");
	result.append("\n\t\t</transition>");
    }

    private void writeInitialiseFunction() {
	result.append("void initialise(){\n    int i;\n");
	if (!systemData.getStates().isEmpty())
	    result.append("   for (i =0; i &lt; STATE_NO; i++)\n    {\n        s[i] := s_init[i];\n    }\n ");
	if (!systemData.getBops().isEmpty())
	    result.append("for (i =0; i &lt; BOP_NO; i++)\n    {\n        s_bop[i] := false;\n    }\n ");
	result.append("reset:= false;\n");
	result.append("}\n");
    }

    private void writeIncrementFunction() {
	result.append("void increment(){\n\n ");
	result.append("if (iteration &lt; MAX_ITERATION) {\n");
	result.append("iteration++;\n");
	result.append("} else {\n ");
	result.append("iteration := 0;\n ");
	result.append("reset := true;\n ");
	result.append("}\n");
	result.append("}\n");
	result.append("\n");
    }

}
