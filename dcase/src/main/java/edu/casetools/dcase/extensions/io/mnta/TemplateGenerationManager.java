package edu.casetools.dcase.extensions.io.mnta;

import java.util.ArrayList;
import java.util.List;

import edu.casetools.dcase.extensions.io.juppaal.elements.Automaton;
import edu.casetools.dcase.extensions.io.mnta.data.MData;
import edu.casetools.dcase.extensions.io.mnta.templates.EventGenerator;
import edu.casetools.dcase.extensions.io.mnta.templates.MTemplateGenerator;
import edu.casetools.dcase.extensions.io.mnta.templates.NTRGenerator;
import edu.casetools.dcase.extensions.io.mnta.templates.STRGenerator;

public class TemplateGenerationManager {
    private MData systemData;

    public TemplateGenerationManager(MData systemData) {
	this.systemData = systemData;
    }

    public List<Automaton> generateTemplates() {
	List<Automaton> list = new ArrayList<>();
	list.addAll(new MTemplateGenerator(systemData).generate());
	list.addAll(new EventGenerator(systemData).generate());
	list.addAll(new STRGenerator(systemData).generate());
	list.addAll(new NTRGenerator(systemData).generate());

	// createEvent();
	// createTemporalOperators();
	// writer = new STRTemplateWriter(writer, systemData).write();
	// createNextTimeRules();
	return list;
    }

}
