package edu.casetools.dcase.extensions.io.templates;

import java.util.List;

import edu.casetools.dcase.extensions.io.MData;
import edu.casetools.dcase.extensions.io.juppaal.elements.Automaton;

public abstract class TemplateGenerator {

    protected MData systemData;

    public TemplateGenerator(MData systemData) {
	this.systemData = systemData;
    }

    public abstract List<Automaton> generate();

}
