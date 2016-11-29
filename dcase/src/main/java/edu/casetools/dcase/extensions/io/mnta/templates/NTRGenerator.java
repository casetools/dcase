package edu.casetools.dcase.extensions.io.mnta.templates;

import java.util.ArrayList;
import java.util.List;

import org.modelio.metamodel.uml.infrastructure.Dependency;
import org.modelio.metamodel.uml.infrastructure.ModelElement;
import org.modelio.vcore.smkernel.mapi.MObject;

import edu.casetools.dcase.extensions.io.juppaal.elements.Automaton;
import edu.casetools.dcase.extensions.io.juppaal.elements.Declaration;
import edu.casetools.dcase.extensions.io.juppaal.elements.Location;
import edu.casetools.dcase.extensions.io.juppaal.elements.Nail;
import edu.casetools.dcase.extensions.io.juppaal.elements.Transition;
import edu.casetools.dcase.extensions.io.juppaal.labels.Guard;
import edu.casetools.dcase.extensions.io.juppaal.labels.Synchronization;
import edu.casetools.dcase.extensions.io.juppaal.labels.Synchronization.SyncType;
import edu.casetools.dcase.extensions.io.juppaal.labels.Update;
import edu.casetools.dcase.extensions.io.mnta.data.MData;
import edu.casetools.dcase.extensions.io.mnta.locations.NTRLocations;
import edu.casetools.dcase.module.api.DCaseProperties;
import edu.casetools.dcase.module.api.DCaseStereotypes;
import edu.casetools.dcase.module.impl.DCasePeerModule;
import edu.casetools.dcase.utils.ModelioUtils;

public class NTRGenerator extends AbstractTemplateGenerator {

    private static final String NTR_CHANNEL = "c_ntr[";
    private List<MObject> antecedentsGroups;

    public NTRGenerator(MData systemData) {
	super(systemData);
	antecedentsGroups = getNextTimeRuleAntecedentsGroups();
    }

    private List<MObject> getNextTimeRuleAntecedentsGroups() {
	List<MObject> nextTimeRuleAntecedents = new ArrayList<>();
	for (MObject antecedentGroup : systemData.getAntecedentGroups()) {
	    if (isNextTime(antecedentGroup))
		nextTimeRuleAntecedents.add(antecedentGroup);
	}
	return nextTimeRuleAntecedents;
    }

    private boolean isNextTime(MObject antecedentGroup) {
	for (MObject element : antecedentGroup.getCompositionChildren()) {
	    if ((element instanceof ModelElement) && isNextTimeRule(element)) {
		return true;
	    }
	}
	return false;
    }

    @Override
    public List<Automaton> generate() {
	List<Automaton> list = new ArrayList<>();
	String rule;
	String invertedRule;
	String consequent;

	for (int i = 0; i < antecedentsGroups.size(); i++) {
	    rule = getRuleString(i);
	    invertedRule = getInvertedRuleString(i);
	    consequent = getConsequentString(antecedentsGroups.get(i));
	    list.add(generateNTR(i, rule, invertedRule, consequent));
	}

	return list;
    }

    private String getRuleString(int i) {
	String result = "";
	int j = 0;
	for (MObject antecedent : antecedentsGroups.get(i).getCompositionChildren()) {

	    if ((antecedent instanceof ModelElement) && isAntecedent(antecedent)) {
		if (j != 0)
		    result = result + " && ";
		result = result + getStateString(antecedent, "==", DCaseProperties.PROPERTY_ANTECEDENT_STATE_NAME,
			DCaseProperties.PROPERTY_ANTECEDENT_STATE_VALUE);
		j++;
	    }
	}

	return result;

    }

    private String getInvertedRuleString(int i) {
	String result = "";
	int j = 0;
	for (MObject antecedent : antecedentsGroups.get(i).getCompositionChildren()) {
	    if ((antecedent instanceof ModelElement) && isAntecedent(antecedent)) {
		if (j != 0)
		    result = result + " || ";
		result = result + getStateString(antecedent, "!=", DCaseProperties.PROPERTY_ANTECEDENT_STATE_NAME,
			DCaseProperties.PROPERTY_ANTECEDENT_STATE_VALUE);
		j++;
	    }
	}

	return result;

    }

    private boolean isAntecedent(MObject antecedent) {
	return ((ModelElement) antecedent).isStereotyped(DCasePeerModule.MODULE_NAME,
		DCaseStereotypes.STEREOTYPE_ANTECEDENT);
    }

    private boolean isNextTimeRule(MObject antecedent) {
	return ((ModelElement) antecedent).isStereotyped(DCasePeerModule.MODULE_NAME,
		DCaseStereotypes.STEREOTYPE_NEXT_TIME);
    }

    private String getConsequentString(MObject antecedentGroup) {
	MObject consequent;
	for (MObject relation : antecedentGroup.getCompositionChildren()) {
	    if ((relation instanceof ModelElement) && isRule(relation)) {
		consequent = ((Dependency) relation).getDependsOn();
		if ((consequent instanceof ModelElement) && isConsequent(consequent))
		    return getStateString(consequent, ":=", DCaseProperties.PROPERTY_CONSEQUENT_STATE_NAME,
			    DCaseProperties.PROPERTY_CONSEQUENT_STATE_VALUE);
		break;
	    }
	}
	return "";
    }

    private boolean isConsequent(MObject relation) {
	return ((ModelElement) relation).isStereotyped(DCasePeerModule.MODULE_NAME,
		DCaseStereotypes.STEREOTYPE_CONSEQUENT);
    }

    private boolean isRule(MObject relation) {
	return ((ModelElement) relation).isStereotyped(DCasePeerModule.MODULE_NAME,
		DCaseStereotypes.STEREOTYPE_SAME_TIME)
		|| ((ModelElement) relation).isStereotyped(DCasePeerModule.MODULE_NAME,
			DCaseStereotypes.STEREOTYPE_NEXT_TIME);
    }

    private String getStateString(MObject child, String equals, String propertyName, String propertyValue) {
	String name = ((ModelElement) child).getTagValue(DCasePeerModule.MODULE_NAME, propertyName);
	String id = getStateId(name);
	String value = ((ModelElement) child).getTagValue(DCasePeerModule.MODULE_NAME, propertyValue);

	return "s[" + id + "] " + equals + " " + value;

    }

    private String getStateId(String stateName) {
	ModelElement element = (ModelElement) ModelioUtils.getInstance().getElementByName(stateName);
	if (element != null && element.isStereotyped(DCasePeerModule.MODULE_NAME, DCaseStereotypes.STEREOTYPE_STATE))
	    return element.getTagValue(DCasePeerModule.MODULE_NAME, DCaseProperties.PROPERTY_STATE_ID);
	else
	    return stateName;
    }

    private Automaton generateNTR(int id, String rule, String invertedRule, String consequent) {
	Automaton m = new Automaton("NTR_" + id);
	Location waiting;
	Location triggered;
	Location notTriggered;
	Location effect;
	Location next;
	Location initial;

	m.setDeclaration(new Declaration("bool nextTime:= false;\n"));

	effect = generateLocation(m, NTRLocations.EFFECT, NTRLocations.EFFECT_X, NTRLocations.EFFECT_Y,
		NTRLocations.EFFECT_NAME_X, NTRLocations.EFFECT_NAME_Y, NTRLocations.EFFECT_EXP_X,
		NTRLocations.EFFECT_EXP_Y);

	next = generateLocation(m, NTRLocations.NEXT, NTRLocations.NEXT_X, NTRLocations.NEXT_Y,
		NTRLocations.NEXT_NAME_X, NTRLocations.NEXT_NAME_Y, NTRLocations.NEXT_EXP_X, NTRLocations.NEXT_EXP_Y);

	waiting = generateLocation(m, NTRLocations.WAITING, NTRLocations.WAITING_X, NTRLocations.WAITING_Y,
		NTRLocations.WAITING_NAME_X, NTRLocations.WAITING_NAME_Y, NTRLocations.WAITING_EXP_X,
		NTRLocations.WAITING_EXP_Y);

	triggered = generateLocation(m, NTRLocations.TRIGGERED, NTRLocations.TRIGGERED_X, NTRLocations.TRIGGERED_Y,
		NTRLocations.TRIGGERED_NAME_X, NTRLocations.TRIGGERED_NAME_Y, NTRLocations.TRIGGERED_EXP_X,
		NTRLocations.TRIGGERED_EXP_Y);

	notTriggered = generateLocation(m, NTRLocations.NOT_TRIGGERED, NTRLocations.NOT_TRIGGERED_X,
		NTRLocations.NOT_TRIGGERED_Y, NTRLocations.NOT_TRIGGERED_NAME_X, NTRLocations.NOT_TRIGGERED_NAME_Y,
		NTRLocations.NOT_TRIGGERED_EXP_X, NTRLocations.NOT_TRIGGERED_EXP_Y);

	initial = generateLocation(m, NTRLocations.INITIAL, NTRLocations.INITIAL_X, NTRLocations.INITIAL_Y,
		NTRLocations.INITIAL_NAME_X, NTRLocations.INITIAL_NAME_Y, NTRLocations.INITIAL_EXP_X,
		NTRLocations.INITIAL_EXP_Y);

	m.setInit(initial);

	generateTransitions(id, rule, invertedRule, consequent, m, waiting, triggered, notTriggered, effect, next,
		initial);

	return m;
    }

    private void generateTransitions(int id, String rule, String invertedRule, String consequent, Automaton m,
	    Location waiting, Location triggered, Location notTriggered, Location effect, Location next,
	    Location initial) {
	Transition auxiliarTransition;

	auxiliarTransition = new Transition(m, effect, initial);
	auxiliarTransition.setGuard(new Guard("reset == true", 221, -85));
	auxiliarTransition.setSync(new Synchronization(NTR_CHANNEL + id + "+1]", SyncType.INITIATOR, 221, -68));
	auxiliarTransition.addNail(new Nail(212, 59));
	auxiliarTransition.addNail(new Nail(212, -212));

	auxiliarTransition = new Transition(m, effect, next);
	auxiliarTransition.setGuard(new Guard("nextTime == false && reset == false", -348, 85));
	auxiliarTransition.addNail(new Nail(-118, 93));

	auxiliarTransition = new Transition(m, effect, next);
	auxiliarTransition.setGuard(new Guard("nextTime == true && reset == false", 34, 85));
	auxiliarTransition.setUpdate(new Update(consequent, 34, 102));
	auxiliarTransition.addNail(new Nail(17, 93));

	auxiliarTransition = new Transition(m, waiting, effect);
	auxiliarTransition.setSync(new Synchronization(NTR_CHANNEL + id + "]", SyncType.RECEIVER, -34, 8));
	auxiliarTransition.addNail(new Nail(-51, 17));

	auxiliarTransition = new Transition(m, next, initial);
	auxiliarTransition.setSync(new Synchronization(NTR_CHANNEL + id + "]", SyncType.INITIATOR, -450, 25));
	auxiliarTransition.addNail(new Nail(-374, 127));
	auxiliarTransition.addNail(new Nail(-374, -213));

	auxiliarTransition = new Transition(m, notTriggered, waiting);
	auxiliarTransition.setSync(new Synchronization(NTR_CHANNEL + id + "]", SyncType.INITIATOR, -170, -68));

	auxiliarTransition = new Transition(m, triggered, waiting);
	auxiliarTransition.setSync(new Synchronization(NTR_CHANNEL + id + "]", SyncType.INITIATOR, 0, -68));

	auxiliarTransition = new Transition(m, initial, notTriggered);
	auxiliarTransition.setGuard(new Guard(invertedRule, -280, -179));
	auxiliarTransition.setSync(new Synchronization(NTR_CHANNEL + id + "]", SyncType.RECEIVER, -280, -195));
	auxiliarTransition.setUpdate(new Update("nextTime := false", -280, -161));

	auxiliarTransition = new Transition(m, initial, triggered);
	auxiliarTransition.setGuard(new Guard(rule, 34, -187));
	auxiliarTransition.setSync(new Synchronization(NTR_CHANNEL + id + "]", SyncType.RECEIVER, 34, -204));
	auxiliarTransition.setUpdate(new Update("nextTime := true", 34, -170));

    }

}
