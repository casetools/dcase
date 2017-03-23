package edu.casetools.dcase.extensions.io.m;

import java.util.ArrayList;
import java.util.List;

import org.modelio.api.modelio.model.IModelingSession;
import org.modelio.api.modelio.model.ITransaction;
import org.modelio.metamodel.factory.ExtensionNotFoundException;
import org.modelio.metamodel.uml.infrastructure.Dependency;
import org.modelio.metamodel.uml.infrastructure.ModelElement;
import org.modelio.vcore.smkernel.mapi.MObject;

import edu.casetools.dcase.m2nusmv.data.MData;
import edu.casetools.dcase.m2nusmv.data.elements.BoundedOperator;
import edu.casetools.dcase.m2nusmv.data.elements.BoundedOperator.BOP_TYPE;
import edu.casetools.dcase.m2nusmv.data.elements.Rule;
import edu.casetools.dcase.m2nusmv.data.elements.RuleElement;
import edu.casetools.dcase.m2nusmv.data.elements.State;
import edu.casetools.dcase.module.api.DCaseProperties;
import edu.casetools.dcase.module.api.DCaseStereotypes;
import edu.casetools.dcase.module.impl.DCaseModule;
import edu.casetools.dcase.module.impl.DCasePeerModule;
import edu.casetools.dcase.utils.tables.TableUtils;
import uk.ac.mdx.ie.contextmodeller.i18n.I18nMessageService;

public class MdData {

    private List<MObject> states;
    private List<MObject> bops;
    private List<MObject> antecedentGroups;
    private List<MObject> strs;
    private List<MObject> ntrs;
    private List<MObject> events;
    private MData data;

    public MdData() {
	data = new MData();
	initialiseLists();
    }

    private void initialiseLists() {
	states = new ArrayList<>();
	bops = new ArrayList<>();
	antecedentGroups = new ArrayList<>();
	strs = new ArrayList<>();
	ntrs = new ArrayList<>();
	events = new ArrayList<>();
    }

    public void loadDiagramElements() {
	states = TableUtils.getInstance().getAllElementsStereotypedAs(states, DCasePeerModule.MODULE_NAME,
		DCaseStereotypes.STEREOTYPE_STATE);
	updateIDs(states, DCaseProperties.PROPERTY_STATE_ID);

	bops = TableUtils.getInstance().getAllElementsStereotypedAs(bops, DCasePeerModule.MODULE_NAME,
		DCaseStereotypes.STEREOTYPE_PAST_OPERATOR);
	updateIDs(bops, DCaseProperties.PROPERTY_PAST_OPERATOR_ID);

	strs = TableUtils.getInstance().getAllElementsStereotypedAs(strs, DCasePeerModule.MODULE_NAME,
		DCaseStereotypes.STEREOTYPE_SAME_TIME);
	updateIDs(strs, DCaseProperties.PROPERTY_STR_ID);

	ntrs = TableUtils.getInstance().getAllElementsStereotypedAs(ntrs, DCasePeerModule.MODULE_NAME,
		DCaseStereotypes.STEREOTYPE_NEXT_TIME);
	updateIDs(ntrs, DCaseProperties.PROPERTY_NTR_ID);

	events = TableUtils.getInstance().getAllElementsStereotypedAs(events, DCasePeerModule.MODULE_NAME,
		DCaseStereotypes.STEREOTYPE_EVENT);
	updateIDs(events, DCaseProperties.PROPERTY_EVENT_ID);

	antecedentGroups = TableUtils.getInstance().getAllElementsStereotypedAs(antecedentGroups,
		DCasePeerModule.MODULE_NAME, DCaseStereotypes.STEREOTYPE_ANTECEDENT_GROUP);

    }

    private void updateIDs(List<MObject> list, String property) {
	IModelingSession session = DCaseModule.getInstance().getModuleContext().getModelingSession();
	ITransaction transaction = session
		.createTransaction(I18nMessageService.getString("Info.Session.Create", new String[] { "" }));
	try {
	    for (int i = 0; i < list.size(); i++) {
		((ModelElement) list.get(i)).putTagValue(DCasePeerModule.MODULE_NAME, property, Integer.toString(i));
	    }
	    transaction.commit();
	} catch (ExtensionNotFoundException e) {
	    e.printStackTrace();
	} finally {
	    transaction.close();
	}

    }

    public MData getMData() {

	getStates();
	getRules();
	getBops();

	return data;
    }

    private void getBops() {
	for (MObject operator : bops) {
	    BoundedOperator bop = new BoundedOperator();
	    bop.setId(((ModelElement) operator).getTagValue(DCasePeerModule.MODULE_NAME,
		    DCaseProperties.PROPERTY_PAST_OPERATOR_ID));
	    bop.setStateName(((ModelElement) operator).getTagValue(DCasePeerModule.MODULE_NAME,
		    DCaseProperties.PROPERTY_PAST_OPERATOR_STATE_NAME));
	    bop.setStatus(((ModelElement) operator).getTagValue(DCasePeerModule.MODULE_NAME,
		    DCaseProperties.PROPERTY_PAST_OPERATOR_STATE_VALUE));
	    addBop(operator, bop);

	}

    }

    private void addBop(MObject operator, BoundedOperator bop) {
	if (checkStereotype(operator, DCaseStereotypes.STEREOTYPE_IMMEDIATE_PAST_OPERATOR)) {
	    bop = setImmediatePastOperator(operator, bop);
	} else if (checkStereotype(operator, DCaseStereotypes.STEREOTYPE_ABSOLUTE_PAST_OPERATOR)) {
	    bop = setAbsolutePastOperator(operator, bop);
	}
	data.getBops().add(bop);

    }

    private BoundedOperator setAbsolutePastOperator(MObject operator, BoundedOperator bop) {
	String type = ((ModelElement) operator).getTagValue(DCasePeerModule.MODULE_NAME,
		DCaseProperties.PROPERTY_PAST_OPERATOR_TYPE);
	setAbsolutePastOperatorType(bop, type);
	bop.setLowBound(((ModelElement) operator).getTagValue(DCasePeerModule.MODULE_NAME,
		DCaseProperties.PROPERTY_PAST_OPERATOR_LOWBOUND));
	bop.setLowBound(((ModelElement) operator).getTagValue(DCasePeerModule.MODULE_NAME,
		DCaseProperties.PROPERTY_PAST_OPERATOR_UPPBOUND));
	return bop;
    }

    private void setAbsolutePastOperatorType(BoundedOperator bop, String type) {
	if (type.equals(I18nMessageService.getString("Ui.PastOperator.Property.TagType.Strong"))) {
	    bop.setType(BOP_TYPE.STRONG_ABSOLUTE_PAST);
	} else if (type.equals(I18nMessageService.getString("Ui.PastOperator.Property.TagType.Weak"))) {
	    bop.setType(BOP_TYPE.WEAK_ABSOLUTE_PAST);
	}
    }

    private BoundedOperator setImmediatePastOperator(MObject operator, BoundedOperator bop) {
	String type = ((ModelElement) operator).getTagValue(DCasePeerModule.MODULE_NAME,
		DCaseProperties.PROPERTY_PAST_OPERATOR_TYPE);
	setAbsolutePastOperatorType(bop, type);
	bop.setLowBound(((ModelElement) operator).getTagValue(DCasePeerModule.MODULE_NAME,
		DCaseProperties.PROPERTY_PAST_OPERATOR_BOUND));
	return bop;
    }

    private void getRules() {
	for (MObject antecedentGroup : antecedentGroups) {
	    Rule rule = new Rule();
	    rule.setAntecedents(getAntecedents(antecedentGroup));
	    rule.setConsequent(getConsequent(antecedentGroup));
	    addRule(antecedentGroup, rule);
	}

    }

    private RuleElement getConsequent(MObject antecedentGroup) {
	MObject consequent;
	for (MObject relation : antecedentGroup.getCompositionChildren()) {
	    if ((relation instanceof ModelElement) && isRule(relation)) {
		consequent = ((Dependency) relation).getDependsOn();
		if ((consequent instanceof ModelElement) && isConsequent(consequent))
		    return getRuleElement(consequent, DCaseProperties.PROPERTY_CONSEQUENT_STATE_NAME,
			    DCaseProperties.PROPERTY_CONSEQUENT_STATE_VALUE);
	    }
	}
	return null;
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

    private List<RuleElement> getAntecedents(MObject antecedentGroup) {
	List<RuleElement> list = new ArrayList<>();
	for (MObject antecedent : antecedentGroup.getCompositionChildren()) {
	    if ((antecedent instanceof ModelElement) && isAntecedent(antecedent)) {
		list.add(getRuleElement(antecedent, DCaseProperties.PROPERTY_ANTECEDENT_STATE_NAME,
			DCaseProperties.PROPERTY_ANTECEDENT_STATE_VALUE));
	    }
	}
	return list;
    }

    private boolean isAntecedent(MObject antecedent) {
	return checkStereotype(antecedent, DCaseStereotypes.STEREOTYPE_ANTECEDENT);
    }

    private RuleElement getRuleElement(MObject element, String name, String value) {
	RuleElement ruleElement = new RuleElement();

	ruleElement.setName(((ModelElement) element).getTagValue(DCasePeerModule.MODULE_NAME, name));
	ruleElement.setStatus(((ModelElement) element).getTagValue(DCasePeerModule.MODULE_NAME, value));

	return ruleElement;

    }

    private void addRule(MObject antecedentGroup, Rule rule) {
	if (isRule(antecedentGroup, DCaseStereotypes.STEREOTYPE_SAME_TIME)) {
	    rule.setId(getRuleId(antecedentGroup));
	    data.getStrs().add(rule);
	} else if (isRule(antecedentGroup, DCaseStereotypes.STEREOTYPE_NEXT_TIME)) {
	    rule.setId(getRuleId(antecedentGroup));
	    data.getNtrs().add(rule);
	}
    }

    private String getRuleId(MObject antecedentGroup) {
	for (MObject element : antecedentGroup.getCompositionChildren()) {
	    if ((element instanceof ModelElement) && checkStereotype(element, DCaseStereotypes.STEREOTYPE_SAME_TIME)) {
		return ((ModelElement) element).getTagValue(DCasePeerModule.MODULE_NAME,
			DCaseProperties.PROPERTY_STR_ID);
	    }
	    if ((element instanceof ModelElement) && checkStereotype(element, DCaseStereotypes.STEREOTYPE_NEXT_TIME)) {
		return ((ModelElement) element).getTagValue(DCasePeerModule.MODULE_NAME,
			DCaseProperties.PROPERTY_NTR_ID);
	    }
	}
	return "-1";
    }

    private boolean isRule(MObject antecedentGroup, String stereotype) {
	for (MObject element : antecedentGroup.getCompositionChildren()) {
	    if ((element instanceof ModelElement) && checkStereotype(element, stereotype)) {
		return true;
	    }
	}
	return false;
    }

    private boolean checkStereotype(MObject element, String stereotype) {
	return ((ModelElement) element).isStereotyped(DCasePeerModule.MODULE_NAME, stereotype);
    }

    private void getStates() {

	for (MObject state : states) {
	    String independence = ((ModelElement) state).getTagValue(DCasePeerModule.MODULE_NAME,
		    DCaseProperties.PROPERTY_STATE_INDEPENDENT);
	    State auxState = createState(true, state);
	    data.getStates().add(auxState);
	    if ((independence != null) && (independence.equalsIgnoreCase("TRUE"))) {
		data.getIndependentStates().add(auxState);
	    }

	}

    }

    private State createState(boolean independence, MObject state) {
	State s = new State();
	s.setId(((ModelElement) state).getTagValue(DCasePeerModule.MODULE_NAME, DCaseProperties.PROPERTY_STATE_ID));
	s.setInitialValue(((ModelElement) state).getTagValue(DCasePeerModule.MODULE_NAME,
		DCaseProperties.PROPERTY_STATE_INITIAL_VALUE));
	s.setIndepedence(independence);
	s.setName(state.getName());
	return s;
    }

}
