package edu.casetools.dcase.extensions.io.mnta.templates;

import java.util.ArrayList;
import java.util.List;

import org.modelio.metamodel.uml.infrastructure.Dependency;
import org.modelio.metamodel.uml.infrastructure.ModelElement;
import org.modelio.vcore.smkernel.mapi.MObject;

import edu.casetools.dcase.extensions.io.juppaal.elements.Automaton;
import edu.casetools.dcase.extensions.io.juppaal.elements.Location;
import edu.casetools.dcase.extensions.io.juppaal.elements.Nail;
import edu.casetools.dcase.extensions.io.juppaal.elements.Transition;
import edu.casetools.dcase.extensions.io.juppaal.labels.Guard;
import edu.casetools.dcase.extensions.io.juppaal.labels.Synchronization;
import edu.casetools.dcase.extensions.io.juppaal.labels.Synchronization.SyncType;
import edu.casetools.dcase.extensions.io.juppaal.labels.Update;
import edu.casetools.dcase.extensions.io.mnta.data.MData;
import edu.casetools.dcase.extensions.io.mnta.locations.STRLocations;
import edu.casetools.dcase.module.api.DCaseProperties;
import edu.casetools.dcase.module.api.DCaseStereotypes;
import edu.casetools.dcase.module.impl.DCasePeerModule;
import edu.casetools.dcase.utils.ModelioUtils;

public class STRGenerator extends AbstractTemplateGenerator {

    private static final String STR_CHANNEL = "c_str[";
    private List<MObject> antecedentsGroups;

    public STRGenerator(MData systemData) {
	super(systemData);
	antecedentsGroups = getSameTimeRuleAntecedentsGroups();
    }

    private List<MObject> getSameTimeRuleAntecedentsGroups() {
	List<MObject> sameTimeRuleAntecedents = new ArrayList<>();
	for (MObject antecedentGroup : systemData.getAntecedentGroups()) {
	    if (isSameTime(antecedentGroup))
		sameTimeRuleAntecedents.add(antecedentGroup);
	}
	return sameTimeRuleAntecedents;
    }

    private boolean isSameTime(MObject antecedentGroup) {
	for (MObject element : antecedentGroup.getCompositionChildren()) {
	    if ((element instanceof ModelElement) && isSameTimeRule(element)) {
		return true;
	    }
	}
	return false;
    }

    private boolean isSameTimeRule(MObject antecedent) {
	return ((ModelElement) antecedent).isStereotyped(DCasePeerModule.MODULE_NAME,
		DCaseStereotypes.STEREOTYPE_SAME_TIME);
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
	    list.add(generateSTR(i, rule, invertedRule, consequent));
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

    private String getConsequentString(MObject antecedentGroup) {
	MObject consequent;
	for (MObject relation : antecedentGroup.getCompositionChildren()) {
	    if ((relation instanceof ModelElement) && isRule(relation)) {
		consequent = ((Dependency) relation).getDependsOn();
		if ((consequent instanceof ModelElement) && ((ModelElement) consequent)
			.isStereotyped(DCasePeerModule.MODULE_NAME, DCaseStereotypes.STEREOTYPE_CONSEQUENT))
		    return getStateString(consequent, ":=", DCaseProperties.PROPERTY_CONSEQUENT_STATE_NAME,
			    DCaseProperties.PROPERTY_CONSEQUENT_STATE_VALUE);

		break;
	    }
	}
	return "";
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

    private Automaton generateSTR(int id, String rule, String invertedRule, String consequent) {
	Automaton m = new Automaton("STR_" + id);
	Location waiting;
	Location triggered;
	Location notTriggered;

	waiting = generateLocation(m, STRLocations.WAITING, STRLocations.WAITING_X, STRLocations.WAITING_Y,
		STRLocations.WAITING_NAME_X, STRLocations.WAITING_NAME_Y, STRLocations.WAITING_EXP_X,
		STRLocations.WAITING_EXP_Y);

	triggered = generateLocation(m, STRLocations.TRIGGERED, STRLocations.TRIGGERED_X, STRLocations.TRIGGERED_Y,
		STRLocations.TRIGGERED_NAME_X, STRLocations.TRIGGERED_NAME_Y, STRLocations.TRIGGERED_EXP_X,
		STRLocations.WAITING_EXP_Y);

	notTriggered = generateLocation(m, STRLocations.NOT_TRIGGERED, STRLocations.NOT_TRIGGERED_X,
		STRLocations.NOT_TRIGGERED_Y, STRLocations.NOT_TRIGGERED_NAME_X, STRLocations.NOT_TRIGGERED_NAME_Y,
		STRLocations.NOT_TRIGGERED_EXP_X, STRLocations.NOT_TRIGGERED_EXP_Y);

	m.setInit(waiting);

	generateTransitions(id, rule, invertedRule, consequent, m, waiting, triggered, notTriggered);

	return m;
    }

    private void generateTransitions(int id, String rule, String invertedRule, String consequent, Automaton m,
	    Location waiting, Location triggered, Location notTriggered) {
	Transition auxiliarTransition;

	auxiliarTransition = new Transition(m, triggered, waiting);
	auxiliarTransition.setSync(new Synchronization(STR_CHANNEL + id + "+1]", SyncType.INITIATOR, -280, -323));
	auxiliarTransition.addNail(new Nail(-331, -323));

	auxiliarTransition = new Transition(m, notTriggered, waiting);
	auxiliarTransition.setSync(new Synchronization(STR_CHANNEL + id + "+1]", SyncType.INITIATOR, -425, -323));
	auxiliarTransition.addNail(new Nail(-331, -323));

	auxiliarTransition = new Transition(m, waiting, notTriggered);
	auxiliarTransition.setGuard(new Guard(invertedRule, -493, -408));
	auxiliarTransition.setSync(new Synchronization(STR_CHANNEL + id + "]", SyncType.RECEIVER, -467, -425));

	auxiliarTransition = new Transition(m, waiting, triggered);
	auxiliarTransition.setGuard(new Guard(rule, -246, -425));
	auxiliarTransition.setSync(new Synchronization(STR_CHANNEL + id + "]", SyncType.RECEIVER, -246, -408));
	auxiliarTransition.setUpdate(new Update(consequent, -246, -408));
    }

}
