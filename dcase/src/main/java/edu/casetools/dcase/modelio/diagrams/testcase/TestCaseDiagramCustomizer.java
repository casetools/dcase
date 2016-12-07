package edu.casetools.dcase.modelio.diagrams.testcase;

import java.util.List;
import java.util.Map;

import org.eclipse.gef.palette.PaletteDrawer;
import org.eclipse.gef.palette.PaletteRoot;
import org.modelio.api.modelio.diagram.IDiagramCustomizer;
import org.modelio.api.modelio.diagram.IDiagramService;
import org.modelio.api.modelio.diagram.tools.PaletteEntry;
import org.modelio.api.module.IModule;

import edu.casetools.dcase.modelio.diagrams.DiagramCustomizer;
import edu.casetools.dcase.module.api.DCaseTools;
import edu.casetools.dcase.module.i18n.I18nMessageService;
import edu.casetools.dcase.module.impl.DCaseModule;

public class TestCaseDiagramCustomizer extends DiagramCustomizer implements IDiagramCustomizer {

    public TestCaseDiagramCustomizer() {
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
	IDiagramService toolRegistry = DCaseModule.getInstance().getModuleContext().getModelioServices()
		.getDiagramService();
	PaletteDrawer commonGroup = createBasics();

	paletteRoot.add(commonGroup);
	paletteRoot.add(createNodesGroup(toolRegistry));
	paletteRoot.add(createMessagesGroup(toolRegistry));
	paletteRoot.add(createDefaultNotesGroup(toolRegistry));
	paletteRoot.add(createDefaultFreeDrawingGroup(toolRegistry));
    }

    private org.eclipse.gef.palette.PaletteEntry createMessagesGroup(IDiagramService toolRegistry) {
	String groupName = I18nMessageService.getString("ScopePaletteGroup.Messages");
	String[] toolNames = new String[] { DCaseTools.EVENT_MESSAGE };
	return createGroup(groupName, toolNames, toolRegistry, 0);
    }

    private org.eclipse.gef.palette.PaletteEntry createNodesGroup(IDiagramService toolRegistry) {
	String groupName = I18nMessageService.getString("ScopePaletteGroup.Nodes");
	String[] toolNames = new String[] { DCaseTools.EVENTS_SIMULATOR, DCaseTools.MSYSTEM };
	return createGroup(groupName, toolNames, toolRegistry, 0);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.modelio.api.diagram.IDiagramCustomizer#keepBasePalette()
     */
    @Override
    public boolean keepBasePalette() {
	return false;
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
	/*
	 * The interaction diagram customizer does not need an initialization
	 * method, but is forced by the ancestors to have one
	 */
    }

}