package edu.casetools.dcase.extensions.io.m;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

import edu.casetools.dcase.m2nusmv.data.MData;
import edu.casetools.dcase.m2nusmv.data.elements.BoundedOperator;
import edu.casetools.dcase.m2nusmv.data.elements.Rule;
import edu.casetools.dcase.m2nusmv.data.elements.State;
import edu.casetools.dcase.utils.ModelioUtils;

public class MGenerator {

    private FileWriter filestream;
    private BufferedWriter writer;
    private MData data;

    public void generateMCode() {

    }

    private void initialiseWriter(String file) {
	try {
	    filestream = new FileWriter(file);
	    writer = new BufferedWriter(filestream);
	    MdData dData = new MdData();
	    dData.loadDiagramElements();
	    this.data = dData.getMData();
	} catch (IOException e) {
	    e.printStackTrace();
	}
    }

    public void generate(String file) throws IOException {
	initialiseWriter(file);
	generateCommentHeader();
	generateStateHeader();
	writer.append("\n");
	generateIndependentStateDeclarations();
	writer.append("\n");
	generateStateInitialisations();
	writer.append("\n");
	for (Rule str : data.getStrs())
	    generateStr(str);
	for (Rule ntr : data.getNtrs())
	    generateNtr(ntr);
	writer.close();
    }

    private void generateStr(Rule str) throws IOException {
	writer.append("\tssr( (");
	generateRuleBody(str);

    }

    private void generateNtr(Rule ntr) throws IOException {
	writer.append("\tsEr( (");
	generateRuleBody(ntr);
    }

    private void generateRuleBody(Rule str) throws IOException {
	generateAntecedents(str);
	generateBOPs(str);
	generateConsequent(str);
	writer.append("\n");
    }

    private void generateConsequent(Rule str) throws IOException {
	writer.append(" ) ->");
	writer.append(this.getStatus(str.getConsequent().getStatus()) + str.getConsequent().getName());
	writer.append(");");
    }

    private void generateBOPs(Rule rule) throws IOException {
	for (int i = 0; i < rule.getBops().size(); i++) {
	    generateBOP(rule.getBops().get(i));
	    if ((i != (rule.getBops().size() - 1)))
		writer.append(" ^ ");
	}
    }

    private void generateBOP(BoundedOperator bop) throws IOException {
	switch (bop.getType()) {
	case STRONG_IMMEDIATE_PAST:
	    generateSpecificOperator(bop.getOperatorName(), "[-]", bop.getStateName(), bop.getLowBound(),
		    getStatus(bop.getStatus()));
	    break;
	case WEAK_IMMEDIATE_PAST:
	    generateSpecificOperator(bop.getOperatorName(), "<->", bop.getStateName(), bop.getLowBound(),
		    getStatus(bop.getStatus()));
	    break;
	case STRONG_ABSOLUTE_PAST:
	    generateSpecificOperator(bop.getOperatorName(), "[-]", bop.getStateName(),
		    bop.getLowBound() + "-" + bop.getUppBound(), getStatus(bop.getStatus()));
	    break;
	case WEAK_ABSOLUTE_PAST:
	    generateSpecificOperator(bop.getOperatorName(), "<->", bop.getStateName(),
		    bop.getLowBound() + "-" + bop.getUppBound(), getStatus(bop.getStatus()));
	    break;
	default:
	    break;
	}

    }

    private void generateSpecificOperator(String operatorName, String operatorType, String stateName, String bound,
	    String status) throws IOException {
	writer.append(operatorType + "[" + bound + "]" + status + stateName);
    }

    private void generateAntecedents(Rule rule) throws IOException {
	for (int i = 0; i < rule.getAntecedents().size(); i++) {
	    writer.append(
		    this.getStatus(rule.getAntecedents().get(i).getStatus()) + rule.getAntecedents().get(i).getName());
	    if ((i != rule.getAntecedents().size() - 1))
		writer.append(" ^ ");
	    else if (!rule.getBops().isEmpty())
		writer.append(" ^ ");
	}
    }

    private void generateIndependentStateDeclarations() throws IOException {
	for (State state : data.getIndependentStates()) {
	    generateIndependentStateDeclaration(state.getName());
	}
    }

    private void generateStateInitialisations() throws IOException {
	for (State state : data.getStates()) {
	    if (!state.isIndependent()) {
		generateStateInitialisation(state.getName(), getStatus(state.getInitialValue()));
	    }
	}
    }

    private String getStatus(String status) {
	if (status.equalsIgnoreCase("TRUE"))
	    return "";
	else if (status.equalsIgnoreCase("FALSE"))
	    return "#";
	else
	    return null;
    }

    private void generateStateHeader() throws IOException {
	writer.append(" states(");
	for (int i = 0; i < data.getStates().size(); i++) {
	    writer.append(data.getStates().get(i).getName());
	    if (i != data.getStates().size() - 1)
		writer.append(", ");
	}
	writer.append(");\n\n");
    }

    private void generateStateInitialisation(String stateName, String status) throws IOException {
	writer.append(" holdsAt(");
	writer.append(status + stateName);
	writer.append(",0);\n");
    }

    private void generateIndependentStateDeclaration(String stateName) throws IOException {
	writer.append(" is(");
	writer.append(stateName);
	writer.append(");\n");
	writer.append(" is(");
	writer.append("#" + stateName);
	writer.append(");\n");
    }

    private void generateCommentHeader() throws IOException {
	String projectName = ModelioUtils.getInstance().getProjectName();
	writer.append(
		"/*************************************************************\r\n*\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t *\r\n* This file has been automatically generated as part of the  *\r\n* DCASE module for the project "
			+ projectName
			+ ". \r\n*\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t *\r\n*************************************************************/\n\n");
    }
}
