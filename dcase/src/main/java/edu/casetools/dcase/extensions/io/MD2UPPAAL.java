package edu.casetools.dcase.extensions.io;

import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Logger;

import edu.casetools.dcase.extensions.io.juppaal.elements.Automaton;
import edu.casetools.dcase.extensions.io.juppaal.elements.Declaration;
import edu.casetools.dcase.extensions.io.juppaal.elements.NTA;
import edu.casetools.dcase.extensions.io.old.DeclarationsWriter;
import edu.casetools.dcase.extensions.io.old.TemplateGeneratorFactory;

public class MD2UPPAAL {

    private static final Logger LOGGER = Logger.getLogger(MD2UPPAAL.class.getName());
    private MData systemData;

    public MD2UPPAAL() {
	systemData = new MData();
    }

    public void translate(String filePath) throws IOException {

	try {
	    Declaration declaration = new Declaration();
	    declaration.add(new DeclarationsWriter(systemData).write());
	    NTA nta = new NTA();
	    nta.setDeclarations(declaration);
	    nta.setTemplates((ArrayList<Automaton>) new TemplateGeneratorFactory(systemData).generateTemplates());
	    // nta.setSystemDeclaration();

	    // Document doc = docBuilder.newDocument();
	    // Element nta = doc.createElement("nta");
	    // doc.appendChild(nta);

	    // StringBuilder sb = new StringBuilder();
	    // addHeader(sb);
	    // nta.appendChild(new DeclarationsWriter(doc,
	    // systemData).getDeclaration());

	    // nta = new TemplateWriterHandler(doc, nta,
	    // systemData).addTemplates();
	    //
	    // generateTemplates(doc, nta);
	    //
	    // Element sys = doc.createElement("system");
	    // sys.setTextContent(generateSystemDeclaration(null));
	    // nta.appendChild(sys);

	    // transformer.transform(new DOMSource(doc), new
	    // javax.xml.transform.stream.StreamResult(writer));

	    nta.writeModelToFile(filePath);

	} catch (Exception e) {
	    // TODO Auto-generated catch block
	    System.out.println(e.getMessage());
	}

    }
}
