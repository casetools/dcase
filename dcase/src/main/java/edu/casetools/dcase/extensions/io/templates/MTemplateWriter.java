package edu.casetools.dcase.extensions.io.templates;

import java.util.ArrayList;
import java.util.List;

import edu.casetools.dcase.extensions.io.MData;
import edu.casetools.dcase.extensions.io.juppaal.elements.Automaton;
import edu.casetools.dcase.extensions.io.juppaal.elements.Declaration;
import edu.casetools.dcase.extensions.io.juppaal.elements.Location;
import edu.casetools.dcase.extensions.io.juppaal.elements.Location.LocationType;
import edu.casetools.dcase.extensions.io.juppaal.elements.Nail;
import edu.casetools.dcase.extensions.io.juppaal.elements.Name;
import edu.casetools.dcase.extensions.io.juppaal.elements.Transition;
import edu.casetools.dcase.extensions.io.juppaal.labels.ExponentialRate;
import edu.casetools.dcase.extensions.io.juppaal.labels.Guard;
import edu.casetools.dcase.extensions.io.juppaal.labels.Synchronization;
import edu.casetools.dcase.extensions.io.juppaal.labels.Synchronization.SyncType;
import edu.casetools.dcase.extensions.io.juppaal.labels.Update;
import edu.casetools.dcase.extensions.io.locations.MLocations;

public class MTemplateWriter extends TemplateGenerator {

    public MTemplateWriter(MData systemData) {
	super(systemData);
    }

    @Override
    public List<Automaton> generate() {
	List<Automaton> list = new ArrayList<>();
	Automaton m = new Automaton("M");

	m.setDeclaration(generateMDeclaration());
	m = generateM(m);

	list.add(m);
	return list;
    }

    private Automaton generateM(Automaton m) {
	Location initial, initial_to_events, events, events_to_bop, bops, bops_to_str, str, str_to_ntr, ntr, iteration,
		iteration_to_ntr_effect, ntr_effect;
	Transition auxiliarTransition;

	events = generateLocation(m, MLocations.EVENTS, MLocations.EVENTS_X, MLocations.EVENTS_Y,
		MLocations.EVENTS_NAME_X, MLocations.EVENT_NAME_Y, MLocations.EVENT_EXP_X, MLocations.EVENT_EXP_Y);

	events_to_bop = generateLocation(m, MLocations.EVENTS_TO_BOP, MLocations.EVENTS_TO_BOP_X,
		MLocations.EVENTS_TO_BOP_Y, MLocations.EVENTS_TO_BOP_NAME_X, MLocations.EVENTS_TO_BOP_NAME_Y,
		MLocations.EVENTS_TO_BOP_EXP_X, MLocations.EVENTS_TO_BOP_EXP_Y);

	initial_to_events = generateLocation(m, MLocations.INITIAL_TO_EVENTS, MLocations.INITIAL_TO_EVENTS_X,
		MLocations.INITIAL_TO_EVENTS_Y, MLocations.INITIAL_TO_EVENTS_NAME_X,
		MLocations.INITIAL_TO_EVENTS_NAME_Y, MLocations.INITIAL_TO_EVENTS_EXP_X,
		MLocations.INITIAL_TO_EVENTS_EXP_Y);

	bops_to_str = generateLocation(m, MLocations.BOPS_TO_STR, MLocations.BOPS_TO_STR_X, MLocations.BOPS_TO_STR_Y,
		MLocations.BOPS_TO_STR_NAME_X, MLocations.BOPS_TO_STR_NAME_Y, MLocations.BOPS_TO_STR_EXP_X,
		MLocations.BOPS_TO_STR_EXP_Y);

	iteration_to_ntr_effect = generateLocation(m, MLocations.ITERATION_TO_NTR_EFFECT,
		MLocations.ITERATION_TO_NTR_EFFECT_X, MLocations.ITERATION_TO_NTR_EFFECT_Y,
		MLocations.ITERATION_TO_NTR_EFFECT_NAME_X, MLocations.ITERATION_TO_NTR_EFFECT_NAME_Y,
		MLocations.ITERATION_TO_NTR_EFFECT_EXP_X, MLocations.ITERATION_TO_NTR_EFFECT_EXP_Y);

	ntr = generateLocation(m, MLocations.NTR, MLocations.NTR_X, MLocations.NTR_Y, MLocations.NTR_NAME_X,
		MLocations.NTR_NAME_Y, MLocations.NTR_EXP_X, MLocations.NTR_EXP_Y);

	iteration = generateLocation(m, MLocations.ITERATION, MLocations.ITERATION_X, MLocations.ITERATION_Y,
		MLocations.ITERATION_NAME_X, MLocations.ITERATION_NAME_Y, MLocations.ITERATION_EXP_X,
		MLocations.ITERATION_EXP_Y);

	ntr_effect = generateLocation(m, MLocations.NTR_EFFECT, MLocations.NTR_EFFECT_X, MLocations.NTR_EFFECT_Y,
		MLocations.NTR_EFFECT_NAME_X, MLocations.NTR_EFFECT_NAME_Y, MLocations.NTR_EFFECT_EXP_X,
		MLocations.NTR_EFFECT_EXP_Y);

	initial = generateLocation(m, MLocations.INITIAL, MLocations.INITIAL_X, MLocations.INITIAL_Y,
		MLocations.INITIAL_NAME_X, MLocations.INITIAL_NAME_Y, MLocations.INITIAL_EXP_X,
		MLocations.INITIAL_EXP_Y);

	str_to_ntr = generateLocation(m, MLocations.STR_TO_NTR, MLocations.STR_TO_NTR_X, MLocations.STR_TO_NTR_Y,
		MLocations.STR_TO_NTR_NAME_X, MLocations.STR_TO_NTR_NAME_Y, MLocations.STR_TO_NTR_EXP_X,
		MLocations.STR_TO_NTR_EXPY);

	str = generateLocation(m, MLocations.STR, MLocations.STR_X, MLocations.STR_Y, MLocations.STR_NAME_X,
		MLocations.STR_NAME_Y, MLocations.STR_EXP_X, MLocations.STR_EXP_Y);

	bops = generateLocation(m, MLocations.BOPS, MLocations.BOPS_X, MLocations.BOPS_Y, MLocations.BOPS_NAME_X,
		MLocations.BOPS_NAME_Y, MLocations.BOPS_EXP_X, MLocations.BOPS_EXP_Y);

	m.setInit(initial);

	auxiliarTransition = new Transition(m, initial, initial_to_events);
	auxiliarTransition.setGuard(new Guard("reset == false", 153, -578));
	auxiliarTransition.addNail(new Nail(144, -561));

	auxiliarTransition = new Transition(m, initial_to_events, events_to_bop);
	auxiliarTransition.setGuard(new Guard("EVENT_NO < 1", -93, -408));
	auxiliarTransition.addNail(new Nail(8, -518));
	auxiliarTransition.addNail(new Nail(8, -289));

	auxiliarTransition = new Transition(m, events, events_to_bop);
	auxiliarTransition.setSync(new Synchronization("c_e[EVENT_NO]", SyncType.RECEIVER, 110, -365));

	auxiliarTransition = new Transition(m, events_to_bop, bops);
	auxiliarTransition.setGuard(new Guard("BOP_NO > 0", 110, -246));
	auxiliarTransition.setSync(new Synchronization("c_bop[0]", SyncType.INITIATOR, 110, -229));

	auxiliarTransition = new Transition(m, initial_to_events, events);
	auxiliarTransition.setGuard(new Guard("EVENT_NO > 0", 119, -484));
	auxiliarTransition.setSync(new Synchronization("c_e[0]", SyncType.INITIATOR, 119, -467));

	auxiliarTransition = new Transition(m, events_to_bop, bops_to_str);
	auxiliarTransition.setGuard(new Guard("BOP_NO < 1", 289, -110));
	auxiliarTransition.addNail(new Nail(272, -289));
	auxiliarTransition.addNail(new Nail(272, -34));

	auxiliarTransition = new Transition(m, str_to_ntr, iteration);
	auxiliarTransition.setGuard(new Guard("NTR_NO <= 0", -76, 297));
	auxiliarTransition.addNail(new Nail(18, 238));
	auxiliarTransition.addNail(new Nail(25, 374));

	auxiliarTransition = new Transition(m, bops_to_str, str_to_ntr);
	auxiliarTransition.setGuard(new Guard("STR_NO <= 0", 281, 144));
	auxiliarTransition.addNail(new Nail(247, 85));
	auxiliarTransition.addNail(new Nail(247, 238));

	auxiliarTransition = new Transition(m, initial, initial_to_events);
	auxiliarTransition.setGuard(new Guard("reset == true", -25, -578));
	auxiliarTransition.setUpdate(new Update("initialise()", -8, -561));
	auxiliarTransition.addNail(new Nail(59, -561));

	auxiliarTransition = new Transition(m, bops_to_str, str);
	auxiliarTransition.setGuard(new Guard("STR_NO > 0", 281, 144));
	auxiliarTransition.setSync(new Synchronization("c_str[0]", SyncType.INITIATOR, 119, 110));

	auxiliarTransition = new Transition(m, iteration, iteration_to_ntr_effect);
	auxiliarTransition.setUpdate(new Update("increment()", -76, 391));

	auxiliarTransition = new Transition(m, ntr, iteration);
	auxiliarTransition.setSync(new Synchronization("c_ntr[NTR_NO]", SyncType.RECEIVER, 119, 357));

	auxiliarTransition = new Transition(m, str_to_ntr, ntr);
	auxiliarTransition.setGuard(new Guard("NTR_NO > 0", 119, 246));
	auxiliarTransition.setSync(new Synchronization("c_ntr[0]", SyncType.INITIATOR, 119, 263));

	auxiliarTransition = new Transition(m, ntr_effect, initial);
	auxiliarTransition.setSync(new Synchronization("c_ntr[NTR_NO]", SyncType.RECEIVER, -501, 263));
	auxiliarTransition.addNail(new Nail(-365, -603));

	auxiliarTransition = new Transition(m, iteration_to_ntr_effect, ntr_effect);
	auxiliarTransition.setGuard(new Guard("NTR_NO > 0", -297, 391));
	auxiliarTransition.setSync(new Synchronization("c_ntr[0]", SyncType.INITIATOR, -289, 374));

	auxiliarTransition = new Transition(m, str, str_to_ntr);
	auxiliarTransition.setSync(new Synchronization("c_str[STR_NO]", SyncType.RECEIVER, 119, 178));

	auxiliarTransition = new Transition(m, bops, bops_to_str);
	auxiliarTransition.setSync(new Synchronization("c_bop[BOP_NO]", SyncType.RECEIVER, 110, -51));

	return m;
    }

    private Location generateLocation(Automaton m, String name, int x, int y, int xName, int yName, int xExp,
	    int yExp) {
	Location location = new Location(m, new Name(name, xName, yName), LocationType.NORMAL, x, y);
	location.setExponentialRate(new ExponentialRate("1", xExp, yExp));
	return location;
    }

    private Declaration generateMDeclaration() {
	Declaration mDeclaration = new Declaration();
	mDeclaration.add(writeMDeclarations());
	return mDeclaration;
    }

    private String writeMDeclarations() {
	StringBuilder writer = new StringBuilder();
	writer = writeInitialiseFunction(writer);
	writer = writeIncrementFunction(writer);
	return writer.toString();
    }

    private StringBuilder writeInitialiseFunction(StringBuilder writer) {
	writer.append("void initialise(){\n    int i;\n");
	if (!systemData.getStates().isEmpty())
	    writer.append("   for (i =0; i < STATE_NO; i++)\n    {\n        s[i] := s_init[i];\n    }\n ");
	if (!systemData.getBops().isEmpty())
	    writer.append("	for (i =0; i < BOP_NO; i++)\n    {\n        s_bop[i] := false;\n    }\n ");
	writer.append("		reset:= false;\n");
	writer.append("}\n");
	return writer;
    }

    private StringBuilder writeIncrementFunction(StringBuilder writer) {
	writer.append("void increment(){\n ");
	writer.append("		 if (iteration < MAX_ITERATION) {\n");
	writer.append("			 iteration++;\n");
	writer.append("		 } else {\n ");
	writer.append("			 iteration := 0;\n ");
	writer.append("			 reset := true;\n ");
	writer.append("		}\n\n");
	writer.append("}\n");
	writer.append("\n");

	return writer;
    }

}
