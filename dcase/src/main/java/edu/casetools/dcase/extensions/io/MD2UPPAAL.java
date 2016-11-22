package edu.casetools.dcase.extensions.io;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import edu.casetools.dcase.extensions.io.elements.DeclarationsWriter;
import edu.casetools.dcase.extensions.io.elements.QueryWriter;
import edu.casetools.dcase.extensions.io.elements.SystemDeclarationWriter;
import edu.casetools.dcase.extensions.io.elements.TemplateWriterHandler;

public class MD2UPPAAL {

    private static final Logger LOGGER = Logger.getLogger(MD2UPPAAL.class.getName());
    private StringBuilder result;
    private MData systemData;

    public MD2UPPAAL() {
	systemData = new MData();
	result = new StringBuilder(systemData.getStringLength());
    }

    public String write() throws IOException {
	writeHeader();
	result.append("<nta>");
	new DeclarationsWriter(result, systemData).writeDeclarations();
	new TemplateWriterHandler(result, systemData).writeTemplates();
	new SystemDeclarationWriter(result, systemData).writeSystemDeclaration();
	new QueryWriter(result).writeQueries();
	result.append("</nta>");
	return result.toString();
    }

    private void writeHeader() throws IOException {
	result.append(
		"<?xml version=\"1.0\" encoding=\"utf-8\"?>\r\n<!DOCTYPE nta PUBLIC '-//Uppaal Team//DTD Flat System 1.1//EN' 'http://www.it.uu.se/research/group/darts/uppaal/flat-1_2.dtd'>");
    }

    public static void main(String[] args) {
	try {
	    new MD2UPPAAL().write();
	} catch (Exception e) {
	    LOGGER.log(Level.SEVERE, e.toString(), e);
	}

    }

    public void writeToFile(File file) {
	FileOutputStream output = null;
	try {
	    if (!file.exists())
		file.createNewFile();
	    output = new FileOutputStream(file);
	    output.write(write().getBytes());
	    output.flush();
	} catch (IOException e) {
	    System.out.println("IOException " + e.toString());
	} finally {
	    close(output);
	}
    }

    private void close(FileOutputStream output) {
	try {
	    output.close();
	} catch (IOException e) {
	    System.out.println("IOException " + e.toString());
	} catch (NullPointerException e) {
	    System.out.println("NullPointerException " + e.toString());
	}
    }

}
