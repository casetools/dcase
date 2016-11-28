package edu.casetools.dcase.extensions.io.mdiag2uppaal.mnta.templates;

import java.util.List;

import edu.casetools.dcase.extensions.io.juppaal.elements.Automaton;
import edu.casetools.dcase.extensions.io.juppaal.elements.Location;
import edu.casetools.dcase.extensions.io.juppaal.elements.Location.LocationType;
import edu.casetools.dcase.extensions.io.juppaal.elements.Name;
import edu.casetools.dcase.extensions.io.juppaal.labels.ExponentialRate;
import edu.casetools.dcase.extensions.io.mdiag2uppaal.mnta.data.MData;

public abstract class AbstractTemplateGenerator {

    protected MData systemData;

    public AbstractTemplateGenerator(MData systemData) {
	this.systemData = systemData;
    }

    public abstract List<Automaton> generate();

    protected Location generateLocation(final Automaton m, final String name, final int x, final int y, final int xName,
	    final int yName, final int xExp, final int yExp) {
	Location location = new Location(m, new Name(name, xName, yName), LocationType.NORMAL, x, y);
	location.setExponentialRate(new ExponentialRate("1", xExp, yExp));
	return location;
    }

}
