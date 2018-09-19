package edu.casetools.dcase.extensions.io.gen.stationary.m;

import java.util.ArrayList;
import java.util.List;

import org.modelio.api.modelio.model.IModelingSession;
import org.modelio.api.modelio.model.ITransaction;
import org.modelio.metamodel.mmextensions.infrastructure.ExtensionNotFoundException;
import org.modelio.metamodel.uml.infrastructure.Dependency;
import org.modelio.metamodel.uml.infrastructure.ModelElement;
import org.modelio.vcore.smkernel.mapi.MObject;

import edu.casetools.dcase.m2nusmv.data.MData;
import edu.casetools.dcase.m2nusmv.data.elements.BoundedOperator;
import edu.casetools.dcase.m2nusmv.data.elements.BoundedOperator.BOP_TYPE;
import edu.casetools.dcase.m2nusmv.data.elements.Rule;
import edu.casetools.dcase.m2nusmv.data.elements.RuleElement;
import edu.casetools.dcase.m2nusmv.data.elements.Specification;
import edu.casetools.dcase.m2nusmv.data.elements.Specification.TYPE;
import edu.casetools.dcase.m2nusmv.data.elements.State;
import edu.casetools.dcase.module.api.DCaseProperties;
import edu.casetools.dcase.module.api.DCaseStereotypes;
import edu.casetools.dcase.module.i18n.I18nMessageService;
import edu.casetools.dcase.module.impl.DCaseModule;
import edu.casetools.dcase.module.impl.DCasePeerModule;
import edu.casetools.rcase.utils.ModelioUtils;
import edu.casetools.rcase.utils.tables.TableUtils;

public class MdData {

    private List<MObject> states;
    private List<MObject> bops;
    private List<MObject> antecedentGroups;
    private List<MObject> strs;
    private List<MObject> ntrs;
    private List<MObject> events;
    private List<MObject> specifications;
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
	specifications = new ArrayList<>();
    }

    public void loadDiagramElements() {

	states = TableUtils.getInstance().getAllElementsStereotypedAs(DCaseModule.getInstance(), DCasePeerModule.MODULE_NAME, states, 
		DCaseStereotypes.STEREOTYPE_STATE);
	updateIDs(states, DCaseProperties.PROPERTY_CONTEXT_STATE_ID);

	bops = TableUtils.getInstance().getAllElementsStereotypedAs(DCaseModule.getInstance(), DCasePeerModule.MODULE_NAME, bops, 
		DCaseStereotypes.STEREOTYPE_PAST_OPERATOR);
	updateIDs(bops, DCaseProperties.PROPERTY_PAST_OPERATOR_ID);

	strs = TableUtils.getInstance().getAllElementsStereotypedAs(DCaseModule.getInstance(), DCasePeerModule.MODULE_NAME, strs, 
		DCaseStereotypes.STEREOTYPE_SAME_TIME);
	updateIDs(strs, DCaseProperties.PROPERTY_STR_ID);

	ntrs = TableUtils.getInstance().getAllElementsStereotypedAs(DCaseModule.getInstance(), DCasePeerModule.MODULE_NAME, ntrs, 
		DCaseStereotypes.STEREOTYPE_NEXT_TIME);
	updateIDs(ntrs, DCaseProperties.PROPERTY_NTR_ID);

	events = TableUtils.getInstance().getAllElementsStereotypedAs(DCaseModule.getInstance(), DCasePeerModule.MODULE_NAME, events, 
		DCaseStereotypes.STEREOTYPE_EVENT);
	updateIDs(events, DCaseProperties.PROPERTY_EVENT_ID);

	antecedentGroups = TableUtils.getInstance().getAllElementsStereotypedAs(DCaseModule.getInstance(), DCasePeerModule.MODULE_NAME, 
			antecedentGroups, DCaseStereotypes.STEREOTYPE_ANTECEDENT_GROUP);

    }

    public void loadSpecifications() {
	List<MObject> diagramElements = new ArrayList<>();
	diagramElements = ModelioUtils.getInstance().getAllElements(DCaseModule.getInstance());

	for (MObject diagramElement : diagramElements) {

	    if ((diagramElement instanceof ModelElement) && ((ModelElement) diagramElement)
		    .isStereotyped(DCasePeerModule.MODULE_NAME, DCaseStereotypes.STEREOTYPE_SPECIFICATION)) {
		specifications.add(diagramElement);
	    }
	}
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
	getSpecifications();

	return data;
    }

    private void getSpecifications() {
	int index = 0;
	for (MObject operator : specifications) {
	    Specification specification = new Specification();
	    specification.setId(Integer.toString(index));
	    specification.setSpec(((ModelElement) operator).getTagValue(DCasePeerModule.MODULE_NAME,
		    DCaseProperties.PROPERTY_SPECIFICATION));
	    specification.setType(getSpecType(((ModelElement) operator).getTagValue(DCasePeerModule.MODULE_NAME,
		    DCaseProperties.PROPERTY_SPECIFICATION_TYPE)));
	    index++;
	    data.getSpecifications().add(specification);

	}

    }

    private TYPE getSpecType(String tagValue) {
	TYPE type = null;
	if (tagValue.equals(I18nMessageService.getString("Ui.Specification.Property.TagSpecificationType.CTL"))) {
	    type = TYPE.CTL;
	} else if (tagValue
		.equals(I18nMessageService.getString("Ui.Specification.Property.TagSpecificationType.LTL"))) {
	    type = TYPE.LTL;
	} else if (tagValue
		.equals(I18nMessageService.getString("Ui.Specification.Property.TagSpecificationType.PSL"))) {
	    type = TYPE.PSL;
	} else if (tagValue
		.equals(I18nMessageService.getString("Ui.Specification.Property.TagSpecificationType.Invariant"))) {
	    type = TYPE.INVARIANT;
	} else if (tagValue
		.equals(I18nMessageService.getString("Ui.Specification.Property.TagSpecificationType.Compute"))) {
	    type = TYPE.COMPUTE;
	}
	return type;
    }

    private void getBops() {
	for (MObject operator : bops) {

	    BoundedOperator bop = new BoundedOperator();
	    bop.setId(((ModelElement) operator).getTagValue(DCasePeerModule.MODULE_NAME,
		    DCaseProperties.PROPERTY_PAST_OPERATOR_ID));
	    bop.setOperatorName("bop" + ((ModelElement) operator).getTagValue(DCasePeerModule.MODULE_NAME,
		    DCaseProperties.PROPERTY_PAST_OPERATOR_ID));
	    bop.setStateName(((ModelElement) operator).getTagValue(DCasePeerModule.MODULE_NAME,
		    DCaseProperties.PROPERTY_PAST_OPERATOR_STATE_NAME).replaceAll("\\s+","_"));
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
	bop.setOperatorName("bop" + ((ModelElement) operator).getTagValue(DCasePeerModule.MODULE_NAME,
		DCaseProperties.PROPERTY_PAST_OPERATOR_ID));
	bop = setAbsolutePastOperatorType(bop, type);
	bop.setLowBound(((ModelElement) operator).getTagValue(DCasePeerModule.MODULE_NAME,
		DCaseProperties.PROPERTY_PAST_OPERATOR_LOWBOUND));
	bop.setUppBound(((ModelElement) operator).getTagValue(DCasePeerModule.MODULE_NAME,
		DCaseProperties.PROPERTY_PAST_OPERATOR_UPPBOUND));
	bop.setStateName(((ModelElement) operator).getTagValue(DCasePeerModule.MODULE_NAME,
		DCaseProperties.PROPERTY_PAST_OPERATOR_STATE_NAME).replaceAll("\\s+","_"));
	bop.setStatus(((ModelElement) operator).getTagValue(DCasePeerModule.MODULE_NAME,
		DCaseProperties.PROPERTY_PAST_OPERATOR_STATE_VALUE));
	return bop;
    }

    private BoundedOperator setAbsolutePastOperatorType(BoundedOperator bop, String type) {
	if (type.equals(I18nMessageService.getString("Ui.PastOperator.Property.TagType.Strong"))) {
	    bop.setType(BOP_TYPE.STRONG_ABSOLUTE_PAST);
	} else if (type.equals(I18nMessageService.getString("Ui.PastOperator.Property.TagType.Weak"))) {
	    bop.setType(BOP_TYPE.WEAK_ABSOLUTE_PAST);
	}
	return bop;
    }

    private BoundedOperator setImmediatePastOperatorType(BoundedOperator bop, String type) {
	if (type.equals(I18nMessageService.getString("Ui.PastOperator.Property.TagType.Strong"))) {
	    bop.setType(BOP_TYPE.STRONG_IMMEDIATE_PAST);
	} else if (type.equals(I18nMessageService.getString("Ui.PastOperator.Property.TagType.Weak"))) {
	    bop.setType(BOP_TYPE.WEAK_IMMEDIATE_PAST);
	}
	return bop;
    }

    private BoundedOperator setImmediatePastOperator(MObject operator, BoundedOperator bop) {
	String type = ((ModelElement) operator).getTagValue(DCasePeerModule.MODULE_NAME,
		DCaseProperties.PROPERTY_PAST_OPERATOR_TYPE);
	bop.setOperatorName("bop" + ((ModelElement) operator).getTagValue(DCasePeerModule.MODULE_NAME,
		DCaseProperties.PROPERTY_PAST_OPERATOR_ID));
	bop = setImmediatePastOperatorType(bop, type);
	bop.setLowBound(((ModelElement) operator).getTagValue(DCasePeerModule.MODULE_NAME,
		DCaseProperties.PROPERTY_PAST_OPERATOR_BOUND));
	bop.setStateName(((ModelElement) operator).getTagValue(DCasePeerModule.MODULE_NAME,
		DCaseProperties.PROPERTY_PAST_OPERATOR_STATE_NAME).replaceAll("\\s+","_"));
	bop.setStatus(((ModelElement) operator).getTagValue(DCasePeerModule.MODULE_NAME,
		DCaseProperties.PROPERTY_PAST_OPERATOR_STATE_VALUE));
	return bop;
    }

    private void getRules() {
	for (MObject antecedentGroup : antecedentGroups) {
	    Rule rule = new Rule();
	    rule.setAntecedents(getAntecedents(antecedentGroup));
	    rule.setBops(getRuleBops(antecedentGroup));
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

    private List<BoundedOperator> getRuleBops(MObject antecedentGroup) {
	List<BoundedOperator> list = new ArrayList<>();
	for (MObject antecedent : antecedentGroup.getCompositionChildren()) {
	    if ((antecedent instanceof ModelElement) && isBop(antecedent)) {
		list.add(getRuleBop(antecedent));
	    }
	}
	return list;
    }

    private BoundedOperator getRuleBop(MObject operator) {
	BoundedOperator bop = new BoundedOperator();
	if (checkStereotype(operator, DCaseStereotypes.STEREOTYPE_IMMEDIATE_PAST_OPERATOR)) {
	    bop = setImmediatePastOperator(operator, bop);
	} else if (checkStereotype(operator, DCaseStereotypes.STEREOTYPE_ABSOLUTE_PAST_OPERATOR)) {
	    bop = setAbsolutePastOperator(operator, bop);
	}
	return bop;
    }

    private boolean isAntecedent(MObject antecedent) {
	return checkStereotype(antecedent, DCaseStereotypes.STEREOTYPE_ANTECEDENT);
    }

    private boolean isBop(MObject antecedent) {
	return checkStereotype(antecedent, DCaseStereotypes.STEREOTYPE_PAST_OPERATOR);
    }

    private RuleElement getRuleElement(MObject element, String name, String value) {
	RuleElement ruleElement = new RuleElement();

	ruleElement.setName(((ModelElement) element).getTagValue(DCasePeerModule.MODULE_NAME, name).replaceAll("\\s+","_"));
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
	    State auxState = createState(false, state);

	    if ((independence != null) && (independence.equalsIgnoreCase("TRUE"))) {
		auxState = createState(true, state);
		data.getIndependentStates().add(auxState);
	    }
	    data.getStates().add(auxState);
	}

    }

    private State createState(boolean independence, MObject state) {
	State s = new State();
	s.setId(((ModelElement) state).getTagValue(DCasePeerModule.MODULE_NAME, DCaseProperties.PROPERTY_CONTEXT_STATE_ID));
	s.setInitialValue(((ModelElement) state).getTagValue(DCasePeerModule.MODULE_NAME,
		DCaseProperties.PROPERTY_STATE_INITIAL_VALUE));
	s.setIndepedence(independence);
	s.setName(state.getName().replaceAll("\\s+","_"));
	return s;
    }

}
