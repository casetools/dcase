package edu.casetools.dcase.extensions.io.mnta.templates;

import java.util.ArrayList;
import java.util.List;

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
import edu.casetools.rcase.utils.ModelioUtils;

public class STRGenerator extends AbstractTemplateGenerator {

    private static final String STR_CHANNEL = "c_str[";

    public STRGenerator(MData systemData) {
	super(systemData);
    }

    @Override
    public List<Automaton> generate() {
	List<Automaton> list = new ArrayList<>();
	String rule;
	String invertedRule;
	String consequent;

	for (int i = 0; i < systemData.getStrs().size(); i++) {
	    rule = getRuleString(i);
	    invertedRule = getInvertedRuleString(i);
	    consequent = getConsequentString(i);
	    list.add(generateSTR(i, rule, invertedRule, consequent));
	}

	return list;
    }

    private String getRuleString(int i) {
	String result = "";
	int j = 0;
	for (MObject antecedent : systemData.getAntecedentGroups().get(i).getCompositionChildren()) {
	    if ((antecedent instanceof ModelElement) && isAntecedent(antecedent)) {
		if (j != 0)
		    result = "&&";
		result = result + getStateString(antecedent, "==");
		j++;
	    }
	}

	return "";

    }

    private String getInvertedRuleString(int i) {
	String result = "";
	int j = 0;
	for (MObject antecedent : systemData.getAntecedentGroups().get(i).getCompositionChildren()) {
	    if ((antecedent instanceof ModelElement) && isAntecedent(antecedent)) {
		if (j != 0)
		    result = "||";
		result = result + getStateString(antecedent, "!=");
		j++;
	    }
	}

	return "";

    }

    private boolean isAntecedent(MObject antecedent) {
	return ((ModelElement) antecedent).isStereotyped(DCasePeerModule.MODULE_NAME,
		DCaseStereotypes.STEREOTYPE_ANTECEDENT);
    }

    private String getConsequentString(int i) {

	for (MObject relation : systemData.getAntecedentGroups().get(i).getCompositionChildren()) {
	    if ((relation instanceof ModelElement) && isRule(relation)) {
		for (MObject consequent : relation.getCompositionChildren()) {
		    if ((relation instanceof ModelElement) && ((ModelElement) relation)
			    .isStereotyped(DCasePeerModule.MODULE_NAME, DCaseStereotypes.STEREOTYPE_CONSEQUENT))
			return getStateString(consequent, "==");
		}
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

    private String getStateString(MObject child, String equals) {
	String id = "";
	String value = "";

	id = getStateId(((ModelElement) child).getTagValue(DCasePeerModule.MODULE_NAME,
		DCaseProperties.PROPERTY_ANTECEDENT_STATE_NAME));

	value = ((ModelElement) child).getTagValue(DCasePeerModule.MODULE_NAME,
		DCaseProperties.PROPERTY_CONSEQUENT_STATE_VALUE);

	return "s[" + id + "] " + equals + " " + value;

    }

    private String getStateId(String stateName) {
	return ModelioUtils.getInstance().getElementByName(stateName).getName();
    }

    private Automaton generateSTR(int id, String rule, String invertedRule, String consequent) {
	Automaton m = new Automaton("STR_" + id);
	Location waiting, triggered, not_triggered;
	Transition auxiliarTransition;

	waiting = generateLocation(m, STRLocations.WAITING, STRLocations.WAITING_X, STRLocations.WAITING_Y,
		STRLocations.WAITING_NAME_X, STRLocations.WAITING_NAME_Y, STRLocations.WAITING_EXP_X,
		STRLocations.WAITING_EXP_Y);

	triggered = generateLocation(m, STRLocations.TRIGGERED, STRLocations.TRIGGERED_X, STRLocations.TRIGGERED_Y,
		STRLocations.TRIGGERED_NAME_X, STRLocations.TRIGGERED_NAME_Y, STRLocations.TRIGGERED_EXP_X,
		STRLocations.WAITING_EXP_Y);

	not_triggered = generateLocation(m, STRLocations.NOT_TRIGGERED, STRLocations.NOT_TRIGGERED_X,
		STRLocations.NOT_TRIGGERED_Y, STRLocations.NOT_TRIGGERED_NAME_X, STRLocations.NOT_TRIGGERED_NAME_Y,
		STRLocations.NOT_TRIGGERED_EXP_X, STRLocations.NOT_TRIGGERED_EXP_Y);

	m.setInit(waiting);

	auxiliarTransition = new Transition(m, triggered, waiting);
	auxiliarTransition.setSync(new Synchronization(STR_CHANNEL + id + "+1]", SyncType.INITIATOR, -280, -323));
	auxiliarTransition.addNail(new Nail(-331, -323));

	auxiliarTransition = new Transition(m, not_triggered, waiting);
	auxiliarTransition.setSync(new Synchronization(STR_CHANNEL + id + "+1]", SyncType.INITIATOR, -425, -323));
	auxiliarTransition.addNail(new Nail(-331, -323));

	auxiliarTransition = new Transition(m, waiting, not_triggered);
	auxiliarTransition.setGuard(new Guard(invertedRule, -493, -408));
	auxiliarTransition.setSync(new Synchronization(STR_CHANNEL + id + "]", SyncType.RECEIVER, -467, -425));

	auxiliarTransition = new Transition(m, waiting, triggered);
	auxiliarTransition.setGuard(new Guard(rule, -493, -408));
	auxiliarTransition.setSync(new Synchronization(STR_CHANNEL + id + "]", SyncType.RECEIVER, -467, -425));
	auxiliarTransition.setUpdate(new Update(consequent, -246, -408));

	return m;
    }

}
