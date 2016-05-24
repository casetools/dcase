package edu.casetools.dcase.modelio.diagrams.communication;

import java.util.List;
import java.util.Map;

import org.eclipse.gef.palette.PaletteRoot;
import org.modelio.api.diagram.IDiagramCustomizer;
import org.modelio.api.diagram.IDiagramService;
import org.modelio.api.diagram.tools.PaletteEntry;
import org.modelio.api.modelio.Modelio;
import org.modelio.api.module.IModule;

import edu.casetools.dcase.modelio.diagrams.DiagramCustomizer;
import edu.casetools.dcase.module.api.DCaseTools;
import edu.casetools.dcase.module.i18n.I18nMessageService;

public class CommunicationDiagramCustomizer extends DiagramCustomizer implements IDiagramCustomizer {

    public CommunicationDiagramCustomizer() {
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
	IDiagramService toolRegistry = Modelio.getInstance().getDiagramService();
	paletteRoot.add(createNodesGroup(toolRegistry));
    }

    @Override
    public void initialize(IModule arg0, List<PaletteEntry> arg1, Map<String, String> arg2, boolean arg3) {
	/* The method is empty because is forced by IDiagramCustomizer */

    }

    private org.eclipse.gef.palette.PaletteEntry createNodesGroup(IDiagramService toolRegistry) {
	String groupName = I18nMessageService.getString("ContextPaletteGroup.Bloc");
	String[] toolNames = new String[] { DCaseTools.TOOL_MESSAGE };
	return createGroup(groupName, toolNames, toolRegistry, 0);
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

}
