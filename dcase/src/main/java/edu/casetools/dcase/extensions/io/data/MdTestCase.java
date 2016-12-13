package edu.casetools.dcase.extensions.io.data;

import java.util.ArrayList;
import java.util.List;

import org.modelio.metamodel.uml.infrastructure.ModelElement;
import org.modelio.vcore.smkernel.mapi.MObject;

import edu.casetools.dcase.module.api.DCaseStereotypes;
import edu.casetools.dcase.module.impl.DCasePeerModule;
import edu.casetools.dcase.utils.ModelioUtils;

public class MdTestCase {

    private MObject testCase;
    private List<MObject> events;

    public MdTestCase(MObject testCase) {
	this.testCase = testCase;
	events = new ArrayList<>();
	loadTestCaseEvents();
    }

    private void loadTestCaseEvents() {
	List<MObject> children = new ArrayList<>();
	children = ModelioUtils.getInstance().getElementsFromMObject((ArrayList<MObject>) children, testCase);
	for (MObject child : children) {
	    if (child instanceof ModelElement) {
		if (((ModelElement) child).isStereotyped(DCasePeerModule.MODULE_NAME, DCaseStereotypes.EVENT)) {
		    events.add(child);
		}
	    }
	}

    }

    public MObject getTestCase() {
	return testCase;
    }

    public void setTestCase(MObject testCase) {
	this.testCase = testCase;
    }

    public List<MObject> getEvents() {
	return events;
    }

    public void setEvents(List<MObject> events) {
	this.events = events;
    }

}
