package edu.casetools.dcase.modelio.diagrams.interaction;

import java.util.List;
import java.util.Map;

import org.eclipse.gef.palette.PaletteRoot;
import org.modelio.api.modelio.diagram.IDiagramCustomizer;
import org.modelio.api.modelio.diagram.tools.PaletteEntry;
import org.modelio.api.module.IModule;

import edu.casetools.dcase.modelio.diagrams.DiagramCustomizer;

public class InteractionDiagramCustomizer extends DiagramCustomizer implements IDiagramCustomizer {

    public InteractionDiagramCustomizer() {
	super();
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.modelio.api.diagram.IDiagramCustomizer#fillPalette(org.eclipse.gef
     * .palette.PaletteRoot)
     */
    @Override
    public void fillPalette(PaletteRoot paletteRoot) {
	// TODO Implement the palette method or remove the class
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.modelio.api.diagram.IDiagramCustomizer#keepBasePalette()
     */
    @Override
    public boolean keepBasePalette() {
	return true;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.modelio.api.diagram.IDiagramCustomizer#getParameters()
     */
    @Override
    public Map<String, String> getParameters() {
	return null;
    }

    @Override
    public void initialize(IModule arg0, List<PaletteEntry> arg1, Map<String, String> arg2, boolean arg3) {
	// TODO Auto-generated method stub

    }

}