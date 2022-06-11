package protocol;

import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.Arrays;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

public class XMLUtils {

	public static String documentToString(Document doc) {
			
	    DOMSource domSource   = new DOMSource(doc);
	    StringWriter writer   = new StringWriter();
	    StreamResult result   = new StreamResult(writer);
	    TransformerFactory tf = TransformerFactory.newInstance();

	    try {
		    Transformer transformer = tf.newTransformer();
			transformer.transform(domSource, result);
		} catch (TransformerException e) {
			e.printStackTrace();
		}
	    return writer.toString();
	}
	
	public static Document stringToDocument(String xmlString) {
		
	    DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance(); // Parser that produces DOM object trees from XML content	       
	    DocumentBuilder builder = null; 									   // API to obtain DOM Document instance

	    try {
	      builder = factory.newDocumentBuilder(); 	                           // Create DocumentBuilder with default configuration
	      return builder.parse(new InputSource(new StringReader(xmlString)));  // Parse the content to Document object
	    } 
	    catch (Exception e) {
	      e.printStackTrace();
	    }
	    return null;
	}
	
	public static boolean validate(String inputXml, String schemaLocation) throws SAXException, IOException {
	
		SchemaFactory factory = SchemaFactory.newInstance("http://www.w3.org/2001/XMLSchema");
		File schemaFile = new File(schemaLocation);
		Schema schema = factory.newSchema(schemaFile);
		Validator validator = schema.newValidator();
		
		// Criar uma Stream a partir da String
		Source source = new StreamSource(new StringReader(inputXml));
		
		// Validação
		boolean isValid = true;
		try {
			validator.validate(source);
		} 
		catch (SAXException e) {
			System.err.println("XML Invalido. -> " + inputXml);
			isValid = false;
		}		
		return isValid;
	}
	
	public static String[][] stringToArray2D(String str) {
		
		String[] rows = str.split("], \\[");
		for (int i = 0; i < rows.length; i++) {
		    rows[i] = rows[i].replace("[[", "").replace("]]", "").replaceAll(" ", "");
		}

		int numberOfColumns = rows[0].split(",").length;
		String[][] matrix = new String[rows.length][numberOfColumns];

		for (int i = 0; i < rows.length; i++) {
		    matrix[i] = rows[i].split(",");
		}

		return matrix;
	}
}
