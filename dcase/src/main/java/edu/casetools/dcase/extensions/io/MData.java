package edu.casetools.dcase.extensions.io;

import java.util.ArrayList;
import java.util.List;

import org.modelio.api.modelio.model.IModelingSession;
import org.modelio.api.modelio.model.ITransaction;
import org.modelio.metamodel.factory.ExtensionNotFoundException;
import org.modelio.metamodel.uml.infrastructure.ModelElement;
import org.modelio.vcore.smkernel.mapi.MObject;

import edu.casetools.dcase.module.api.DCaseProperties;
import edu.casetools.dcase.module.api.DCaseStereotypes;
import edu.casetools.dcase.module.impl.DCaseModule;
import edu.casetools.dcase.module.impl.DCasePeerModule;
import edu.casetools.dcase.utils.tables.TableUtils;
import uk.ac.mdx.ie.contextmodeller.i18n.I18nMessageService;

public class MData {

    private List<MObject> states;
    private List<MObject> bops;
    private List<MObject> strs;
    private List<MObject> ntrs;

    public MData() {
	initialiseLists();
	loadElements();

    }

    private void initialiseLists() {
	states = new ArrayList<>();
	bops = new ArrayList<>();
	strs = new ArrayList<>();
	ntrs = new ArrayList<>();
    }

    private void loadElements() {
	states = TableUtils.getInstance().getAllElementsStereotypedAs(states, DCasePeerModule.MODULE_NAME,
		DCaseStereotypes.STEREOTYPE_STATE);
	updateIDs(states, DCaseProperties.PROPERTY_STATE_ID);

	bops = TableUtils.getInstance().getAllElementsStereotypedAs(bops, DCasePeerModule.MODULE_NAME,
		DCaseStereotypes.STEREOTYPE_PAST_OPERATOR);
	updateIDs(states, DCaseProperties.PROPERTY_PAST_OPERATOR_ID);

	strs = TableUtils.getInstance().getAllElementsStereotypedAs(strs, DCasePeerModule.MODULE_NAME,
		DCaseStereotypes.STEREOTYPE_SAME_TIME);

	ntrs = TableUtils.getInstance().getAllElementsStereotypedAs(ntrs, DCasePeerModule.MODULE_NAME,
		DCaseStereotypes.STEREOTYPE_NEXT_TIME);

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
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	} finally {
	    transaction.close();
	}

    }

    public List<MObject> getStates() {
	return states;
    }

    public void setStates(List<MObject> states) {
	this.states = states;
    }

    public List<MObject> getBops() {
	return bops;
    }

    public void setBops(List<MObject> bops) {
	this.bops = bops;
    }

    public List<MObject> getStrs() {
	return strs;
    }

    public void setStrs(List<MObject> strs) {
	this.strs = strs;
    }

    public List<MObject> getNtrs() {
	return ntrs;
    }

    public void setNtrs(List<MObject> ntrs) {
	this.ntrs = ntrs;
    }

}
