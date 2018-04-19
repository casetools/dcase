package edu.casetools.dcase.extensions.tables.contextmodel.model;

import java.util.ArrayList;

import org.modelio.metamodel.uml.infrastructure.ModelElement;
import org.modelio.vcore.smkernel.mapi.MObject;

import edu.casetools.dcase.module.api.DCaseProperties;
import edu.casetools.dcase.module.impl.DCasePeerModule;
import edu.casetools.rcase.utils.PropertiesUtils;

public class ContextModelTableRow {
    ModelElement element;
    ArrayList<Object> values;

    public ContextModelTableRow(MObject element) {
	this.element = (ModelElement) element;
	this.update();
    }

    private void update() {
	values = new ArrayList<>();
	values.add(element.getName());
	// TagId
	values.add(PropertiesUtils.getInstance().getTaggedValue(DCaseProperties.PROPERTY_MESSAGE_ID, element));

	// TagResponsibility
	values.add(getTag(DCaseProperties.PROPERTY_MESSAGE_RESPONSIBILITY));

	// TagRegularity
	values.add(getTag(DCaseProperties.PROPERTY_MESSAGE_REGULARITY));

	// TagFrequency
	values.add(PropertiesUtils.getInstance().getTaggedValue(DCaseProperties.PROPERTY_MESSAGE_FREQUENCY, element));

	// TagSynchronicity
	values.add(getTag(DCaseProperties.PROPERTY_MESSAGE_SYNCHRONICITY));

    }

    private String getTag(String propertyName) {
	String status = element.getTagValue(DCasePeerModule.MODULE_NAME, propertyName);
	if (status != null)
	    return status;
	return "";
    }

    public void set(int column, Object value) {
	values.set(column, value);

    }

    public Object get(int column) {
	return values.get(column);
    }

    public int size() {
	return values.size();
    }

    public ModelElement getContextAttribute() {
	return element;
    }
}
