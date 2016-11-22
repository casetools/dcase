package edu.casetools.dcase.extensions.io.templates;

import edu.casetools.dcase.extensions.io.MData;

public abstract class TemplateWriter {

    protected StringBuilder result;
    protected MData systemData;

    public TemplateWriter(StringBuilder writer, MData systemData) {
	this.result = writer;
	this.systemData = systemData;
    }

    public abstract StringBuilder write();

}
