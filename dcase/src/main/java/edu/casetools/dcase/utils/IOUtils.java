package edu.casetools.dcase.utils;

import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.FileDialog;

public class IOUtils {

    private static IOUtils instance = null;

    /**
     * Gets the single instance of DiagramUtils.
     *
     * @return single instance of DiagramUtils
     */
    public static IOUtils getInstance() {
	if (instance == null) {
	    instance = new IOUtils();
	}
	return instance;
    }

    public FileDialog getFileDialog(String filename, String[] filetypes, int type) {
	FileDialog dialog = new FileDialog(Display.getCurrent().getActiveShell(), type);
	dialog.setFilterNames(new String[] { "Text Files", "All Files (*.*)" });
	dialog.setFilterExtensions(filetypes); // Windows
	// wild
	// cards
	dialog.setFilterPath(System.getProperty("user.home") + "/Desktop"); // Windows
									    // path
	dialog.setFileName(filename);
	return dialog;
    }

}
