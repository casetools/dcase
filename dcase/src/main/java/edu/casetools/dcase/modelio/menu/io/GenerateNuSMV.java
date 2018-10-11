package edu.casetools.dcase.modelio.menu.io;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.FileDialog;
import org.modelio.api.module.IModule;
import org.modelio.api.module.command.DefaultModuleCommandHandler;
import org.modelio.metamodel.uml.infrastructure.ModelElement;
import org.modelio.vcore.smkernel.mapi.MObject;

import edu.casetools.dcase.extensions.io.gen.stationary.reasoner.MdData;
import edu.casetools.dcase.extensions.io.gen.stationary.reasoner.MdData.PLATFORM;
import edu.casetools.dcase.m2nusmv.M2NuSMV;
import edu.casetools.dcase.m2nusmv.data.MData;
import edu.casetools.dcase.module.api.DCaseProperties;
import edu.casetools.dcase.module.api.DCaseStereotypes;
import edu.casetools.dcase.module.i18n.I18nMessageService;
import edu.casetools.dcase.module.impl.DCaseModule;
import edu.casetools.dcase.module.impl.DCasePeerModule;
import edu.casetools.dcase.utils.IOUtils;
import edu.casetools.rcase.utils.PropertiesUtils;
import edu.casetools.rcase.utils.tables.TableUtils;

public class GenerateNuSMV extends DefaultModuleCommandHandler {

    private M2NuSMV m2nusmv;
    

    /**
     * Constructor.
     */
    public GenerateNuSMV() {
	super();
    }

    /**
     * @see org.modelio.api.module.commands.DefaultModuleContextualCommand#accept(java.util.List,
     *      org.modelio.api.module.IModule)
     */
    @Override
    public boolean accept(List<MObject> selectedElements, IModule module) {
	// Check that there is only one selected element
	return selectedElements.size() == 1;
    }

    /**
     * @see org.modelio.api.module.commands.DefaultModuleContextualCommand#actionPerformed(java.util.List,
     *      org.modelio.api.module.IModule)
     */

    @Override
    public void actionPerformed(List<MObject> selectedElements, IModule module) {

    	List<MObject> mreasoners = TableUtils.getInstance().getAllElementsStereotypedAs(DCaseModule.getInstance(), DCasePeerModule.MODULE_NAME, new ArrayList<>(), 
    			DCaseStereotypes.STEREOTYPE_M_REASONER);
    	List<MObject> areasoners = TableUtils.getInstance().getAllElementsStereotypedAs(DCaseModule.getInstance(), DCasePeerModule.MODULE_NAME, new ArrayList<>(), 
    			DCaseStereotypes.STEREOTYPE_ANDROID_REASONER);
    	MdData.PLATFORM platform = getPlatform(mreasoners,areasoners);
    	if(platform != null){
    		if(platform.equals(MdData.PLATFORM.BOTH)){
    			generateNuSMVModel(MdData.PLATFORM.STATIONARY);
    			generateNuSMVModel(MdData.PLATFORM.MOBILE);
    		} else
    			generateNuSMVModel(platform);

    	}

    }

    private MdData.PLATFORM getPlatform(List<MObject> mreasoners, List<MObject> areasoners) {
		if(mreasoners.isEmpty() && areasoners.isEmpty()){
			MessageDialog.openInformation(null, "Code generation cancelled",
					"There is no reasoner in the deployment diagram for any platform. "
						+ I18nMessageService.getString("Module.ModuleLabel") + " -> "
						+ I18nMessageService.getString("Module.Export") + " -> "
						+ I18nMessageService.getString("Module.NuSMV"));
			return null;
		} else if(!mreasoners.isEmpty() && areasoners.isEmpty()){
			if(mreasoners.size() != 1) {
				MessageDialog.openInformation(null, "Code generation cancelled",
						"There are more than one M Reasoners. "
							+ I18nMessageService.getString("Module.ModuleLabel") + " -> "
							+ I18nMessageService.getString("Module.Export") + " -> "
							+ I18nMessageService.getString("Module.NuSMV"));
				return null;
			}
			return MdData.PLATFORM.STATIONARY;
		} else if(mreasoners.isEmpty() && !areasoners.isEmpty()){
			if(areasoners.size() != 1) {
				MessageDialog.openInformation(null, "Code generation cancelled",
						"There are more than one Android Reasoners. "
							+ I18nMessageService.getString("Module.ModuleLabel") + " -> "
							+ I18nMessageService.getString("Module.Export") + " -> "
							+ I18nMessageService.getString("Module.NuSMV"));
				return null;
			}
			return MdData.PLATFORM.MOBILE;
		} else if(!mreasoners.isEmpty() && !areasoners.isEmpty()){
			return MdData.PLATFORM.BOTH;
		}
		return null;
		
	}

	private void generateNuSMVModel(MdData.PLATFORM platform) {
	FileDialog dialog = IOUtils.getInstance().getFileDialog(platform.toString()+ "_NuSMV.smv",
		new String[] { "*.txt", "*.smv", "*.*" }, SWT.SAVE);
	String fileLocation = dialog.open();
	int maxExecutionTime = getMaxExecutionTime(platform);
	if ((maxExecutionTime > 0) && (!IOUtils.getInstance().checkIfExists(fileLocation))) {
	    translate(fileLocation, maxExecutionTime, platform);
	}
    }

    private int getMaxExecutionTime(MdData.PLATFORM platform) {
	String maxExecutionTime = "";
	String stereotype = getStereotype(platform);
	String property = getProperty(platform);
	MObject reasoner = TableUtils.getInstance().getAllElementsStereotypedAs(DCaseModule.getInstance(), DCasePeerModule.MODULE_NAME, new ArrayList<>(), 
			stereotype).get(0);
	    maxExecutionTime = PropertiesUtils.getInstance().getTaggedValue(property,
		    (ModelElement) reasoner);

	return handleResult(maxExecutionTime);
    }
    
	

    private String getProperty(PLATFORM platform) {
    	switch(platform){
		case STATIONARY:
			return DCaseProperties.PROPERTY_M_REASONER_MAX_EXECUTION_TIME;
		case MOBILE:
			return DCaseProperties.PROPERTY_ANDROID_MAX_EXECUTION_TIME;
		default:
		return "";
		}
	}

	private String getStereotype(PLATFORM platform) {
		switch(platform){
		case STATIONARY:
			return DCaseStereotypes.STEREOTYPE_M_REASONER;
		case MOBILE:
			return DCaseStereotypes.STEREOTYPE_ANDROID_REASONER;
		default:
		return "";
		}
	}

	private int handleResult(String maxExecutionTime) {
	// @todo Handle this with a proper exception mechanism
	int time = -1;
	if (maxExecutionTime == null) {
	    MessageDialog.openError(null, "Execution Time Exception",
		    "No execution time was provided. Make sure that the diagram is selected when clicking export. Right click on the Reasoning Diagram to be exported. The export of the model has been cancelled.\n");
	} else if (maxExecutionTime.equals("")) {
	    MessageDialog.openError(null, "Execution Time Exception",
		    "No execution time was provided. Make sure that there is a value on the selected M Rule Diagram. The export of the model has been cancelled.\n");
	} else {
	    time = Integer.parseInt(maxExecutionTime); // The value of the
						       // execution time cannot
						       // be
						       // other than an integer
						       // positive number, as
						       // these
						       // exceptions are
						       // controlled
						       // in the properties
						       // page.
						       // So, it will never
						       // reach
						       // this code.
	}
	return time;
    }

    private void translate(String fileLocation, int maxExecutionTime, MdData.PLATFORM platform) {
	try {
	    m2nusmv = new M2NuSMV();
	    m2nusmv.writeModel(getData(fileLocation, maxExecutionTime,platform));
	    MessageDialog.openInformation(null, "Model Exported", "Model exported to NuSMV at:\n" + fileLocation);
	} catch (IOException e) {
	    MessageDialog.openInformation(null, "I/O Exception", "File:" + fileLocation + " \n" + e.getMessage());
	    e.printStackTrace();
	}
    }

    private MData getData(String fileLocation, int maxExecutionTime, MdData.PLATFORM platform) {
	MdData mddata = new MdData();
	mddata.loadDiagramElements();
	mddata.filterPlatform(platform);
	MData mdata = mddata.getMData();
	mdata.setFilePath(fileLocation);
	mdata.setMaxIteration(maxExecutionTime);
	return mdata;
    }

}
