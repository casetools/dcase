package edu.casetools.dcase.modelio.diagrams.communication;

import java.util.List;
import java.util.Map;

import org.eclipse.gef.palette.PaletteDrawer;
import org.eclipse.gef.palette.PaletteRoot;
import org.modelio.api.modelio.Modelio;
import org.modelio.api.modelio.diagram.IDiagramCustomizer;
import org.modelio.api.modelio.diagram.IDiagramService;
import org.modelio.api.modelio.diagram.tools.PaletteEntry;
import org.modelio.api.module.IModule;

import com.modeliosoft.modelio.javadesigner.annotations.objid;

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
	paletteRoot.add(createLinkGroup(toolRegistry));
	paletteRoot.add(createInformationFlowGroup(toolRegistry));
	paletteRoot.add(createCommonGroup(toolRegistry));
	paletteRoot.add(this.createDefaultFreeDrawingGroup(toolRegistry));

    }

    private PaletteDrawer createNodesGroup(IDiagramService toolRegistry) {
	String groupName = I18nMessageService.getString("ContextCommunicationPaletteGroup.Nodes");
	String[] toolNames = new String[] { "CREATE_COMMUNICATIONNODE", DCaseTools.TOOL_MESSAGE,
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
     * Creates the note and constraint and dependency group.
     * 
     * @param imageService
     *            service used to get metaclasses bitmaps.
     * @return The created group.
     */
    @objid("7a1a5af2-55b6-11e2-877f-002564c97630")
    private PaletteDrawer createCommonGroup(IDiagramService toolRegistry) {
	// common group
	String groupName = I18nMessageService.getString("ContextCommunicationPaletteGroup.Common");
	String[] toolNames = new String[] { "CREATE_NOTE", "CREATE_CONSTRAINT", "CREATE_EXTERNDOCUMENT",
		"CREATE_DEPENDENCY", "CREATE_TRACEABILITY", "CREATE_RELATED_DIAGRAM_LINK" };
	return createGroup(groupName, toolNames, toolRegistry, 0);
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
	// TODO Auto-generated method stub

    }

}
