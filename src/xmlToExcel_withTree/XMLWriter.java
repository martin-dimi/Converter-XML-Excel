package xmlToExcel_withTree;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;

import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

public class XMLWriter {
	
	private int tabs = 0;
	
	XMLWriter(){
		
	}

	public void write(Entity tree, File output) {

		try {
			StringWriter stringWriter = new StringWriter();
			XMLOutputFactory factory = XMLOutputFactory.newInstance();
			XMLStreamWriter writer = factory.createXMLStreamWriter(stringWriter);
			//XMLStreamWriter writer = new IndentingXMLStreamWriter(xmlof.createXMLStreamWriter(stringWriter));

			writer.writeStartDocument();

			writer.writeCharacters("\n");
			writer.writeStartElement(tree.getName());
			
			process(tree, writer);
			
			writer.writeCharacters("\n");
			writer.writeEndElement();
			
			writer.flush();
			writer.close();

			String xmlString = stringWriter.getBuffer().toString();
			stringWriter.close();
			
			File file = new File(output + "/" + tree.getName() + ".xml");
			FileWriter fileWriter = new FileWriter(file);
			fileWriter.write(prettyFormat(xmlString, 4));
			fileWriter.close();

		} catch (XMLStreamException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}catch (NullPointerException e) {
			System.out.println("Tree is not set");
		}
	}
	
	private void process(Entity parent, XMLStreamWriter writer) throws XMLStreamException {
		
		for(Entity child : parent.getChildren()) {
			writer.writeStartElement(child.getName());
			writer.writeCharacters(child.getContext());
			if(child.hasChildren())
				process(child, writer);
			writer.writeEndElement();
		}
	}
	
	public static String prettyFormat(String input, int indent) {
	    try {
	        Source xmlInput = new StreamSource(new StringReader(input));
	        StringWriter stringWriter = new StringWriter();
	        StreamResult xmlOutput = new StreamResult(stringWriter);
	        TransformerFactory transformerFactory = TransformerFactory.newInstance();
	        transformerFactory.setAttribute("indent-number", indent);
	        Transformer transformer = transformerFactory.newTransformer(); 
	        transformer.setOutputProperty(OutputKeys.INDENT, "yes");	// indent is the space before the indent
	        transformer.setOutputProperty(OutputKeys.DOCTYPE_PUBLIC, "yes"); // for the root
	        transformer.transform(xmlInput, xmlOutput);
	        return xmlOutput.getWriter().toString();
	    } catch (Exception e) {
	        throw new RuntimeException(e); // simple exception handling, please review it
	    }
	}
	
}
