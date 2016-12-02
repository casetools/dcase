package edu.casetools.dcase.modelio.diagrams.communication;

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
	IDiagramService toolRegistry = DCaseModule.getInstance().getModuleContext().getModelioServices()
		.getDiagramService();
	paletteRoot.add(createNodesGroup(toolRegistry));
	paletteRoot.add(createLinkGroup(toolRegistry));
	paletteRoot.add(createInformationFlowGroup(toolRegistry));
	paletteRoot.add(createCommonGroup(toolRegistry));
	paletteRoot.add(this.createDefaultFreeDrawingGroup(toolRegistry));

    }

    private PaletteDrawer createNodesGroup(IDiagramService toolRegistry) {
	String groupName = I18nMessageService.getString("ContextCommunicationPaletteGroup.Nodes");
	String[] toolNames = new String[] { "CREATE_COMMUNICATIONNODE", DCaseTools.TOOL_CONTEXT_INFORMATION_MESSAGE,
		"CREATE_COMMUNICATIONMESSAGE" };
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

    /**
     * Create the link group, containing tools to create CommunicationChannels.
     * 
     * @param toolRegistry
     *            The tool registry.
     * @return The group containing all the tool.
     */
    private PaletteDrawer createLinkGroup(IDiagramService toolRegistry) {

	String groupName = I18nMessageService.getString("ContextCommunicationPaletteGroup.Links");
	String[] toolNames = new String[] { "CREATE_COMMUNICATIONCHANNEL" };
	return createGroup(groupName, toolNames, toolRegistry, 0);
    }

    private PaletteDrawer createInformationFlowGroup(final IDiagramService toolRegistry) {
	String groupName = I18nMessageService.getString("ContextCommunicationPaletteGroup.InformationFlow");
	String[] toolNames = new String[] { "CREATE_INFORMATIONFLOW", "CREATE_INFORMATIONFLOWNODE" };
	return createGroup(groupName, toolNames, toolRegistry, 0);
    }

    @Override
    public void initialize(IModule arg0, List<PaletteEntry> arg1, Map<String, String> arg2, boolean arg3) {
	/*
	 * The communication diagram customizer does not need an initialization
	 * method, but is forced by the ancestors to have one
	 */
    }

}
